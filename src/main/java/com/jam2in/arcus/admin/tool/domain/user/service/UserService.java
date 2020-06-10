package com.jam2in.arcus.admin.tool.domain.user.service;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.entity.AdminEntity;
import com.jam2in.arcus.admin.tool.domain.user.entity.RoleEntity;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.domain.user.repository.AdminRepository;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  private final AdminRepository adminRepository;

  private final EmailService emailService;

  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
                     AdminRepository adminRepository,
                     EmailService emailService,
                     PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.adminRepository = adminRepository;
    this.emailService = emailService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public UserDto create(UserDto userDto) {
    checkDuplicateUsername(userDto.getUsername());
    checkDuplicateEmail(userDto.getEmail());

    UserEntity userEntity = UserEntity.of(userDto);
    userEntity.updatePassword(passwordEncoder.encode(userDto.getPassword()));

    if (userRepository.count() == 0) {
      userEntity.applyAdminRole();
    } else {
      userEntity.applyUserRole();
    }

    userRepository.save(userEntity);

    if (isAdmin(userEntity)) {
      adminRepository.save(AdminEntity.builder().userEntity(userEntity).build());
    }

    return UserDto.of(userEntity);
  }

  @Transactional
  public UserDto update(long id, UserDto userDto) {
    // FIXME: userRepository.save 호출하여 수정? 아니면 영속성 컨텍스트에서 entity field 수정?
    UserEntity userEntity = getEntity(id);

    if (!passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())) {
      throw new BusinessException(ApiErrorCode.USER_PASSWORD_MISMATCH);
    }

    checkDuplicateEmail(userDto.getEmail());
    userEntity.updateEmail(userDto.getEmail());

    if (StringUtils.length(userDto.getNewPassword()) > 0) {
      userEntity.updatePassword(passwordEncoder.encode(userDto.getNewPassword()));
    }

    // TODO: roles 업데이트 필요
    return UserDto.of(userEntity);
  }

  public UserDto get(long id) {
    return UserDto.of(getEntity(id));
  }

  public List<UserDto> getAll() {
    return UserDto.of(getAllEntity());
  }

  public UserDto getByUsername(String username) {
    return UserDto.of(getEntityByUsername(username));
  }

  @Transactional
  public void delete(long id) {
    if (!userRepository.existsById(id)) {
      throw new BusinessException(ApiErrorCode.USER_NOT_FOUND);
    }

    userRepository.deleteById(id);
  }

  @Transactional
  public void resetPassword(String username) {
    UserEntity userEntity = getEntityByUsername(username);
    String newPassword = UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY)
        .substring(0, 10);  // FIXME: 비밀번호 최대 사이즈 사용 (UserDto.SIZE_MAX_PASSWORD)
    userEntity.updatePassword(passwordEncoder.encode(newPassword));

    String subject = "Your password has changed";
    String to = userEntity.getEmail();
    String text = "To " + username + ".\n\n"
        + "your password has changed with " + newPassword + ".\n\n"
        + "Try to login and recommend to change your password.\n\n";
    emailService.send(subject, to, text);
  }

  private UserEntity getEntity(long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.USER_NOT_FOUND));
  }

  private UserEntity getEntityByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.USER_USERNAME_NOT_FOUND));
  }

  private List<UserEntity> getAllEntity() {
    List<UserEntity> users = new ArrayList<>(userRepository.findAll());
    if (users.isEmpty()) {
      throw new BusinessException(ApiErrorCode.NO_USER);
    }
    return users;
  }

  private void checkDuplicateUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new BusinessException(ApiErrorCode.USER_USERNAME_DUPLICATED);
    }
  }

  private void checkDuplicateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new BusinessException(ApiErrorCode.USER_EMAIL_DUPLICATED);
    }
  }

  private boolean isAdmin(UserEntity userEntity) {
    return userEntity.getRoles()
        .stream().anyMatch(r ->
            r == RoleEntity.ROLE_ADMIN);
  }

}
