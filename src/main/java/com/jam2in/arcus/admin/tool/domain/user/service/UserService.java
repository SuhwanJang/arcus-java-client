package com.jam2in.arcus.admin.tool.domain.user.service;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.exception.ApiErrorCode;
import com.jam2in.arcus.admin.tool.exception.BusinessException;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public UserDto create(UserDto userDto) throws BusinessException {
    checkDuplicateUsername(userDto.getUsername());
    checkDuplicateEmail(userDto.getEmail());

    UserEntity userEntity = UserEntity.of(userDto);
    userEntity.encodePassword(passwordEncoder.encode(userDto.getPassword()));
    userEntity.applyAdminRole();  // TODO: 어드민은 최대 하나만 존재해야 함

    userRepository.save(userEntity);

    return UserDto.of(userEntity);
  }

  @Transactional
  public UserDto update(long id, UserDto userDto)
      throws BusinessException {
    // FIXME: userRepository.save 호출하여 수정? 아니면 영속성 컨텍스트에서 entity field 수정?
    UserEntity userEntity = getEntity(id);

    if (!passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())) {
      throw new BusinessException(ApiErrorCode.USER_PASSWORD_MISMATCH);
    }

    if (!StringUtils.equals(userDto.getUsername(), userEntity.getUsername())) {
      checkDuplicateUsername(userDto.getUsername());
      userEntity.updateUsername(userDto.getUsername());
    }

    if (!StringUtils.equals(userDto.getEmail(), userEntity.getEmail())) {
      checkDuplicateEmail(userDto.getEmail());
      userEntity.updateEmail(userDto.getEmail());
    }

    if (!passwordEncoder.matches(userDto.getNewPassword(), userEntity.getPassword())) {
      userEntity.encodePassword(passwordEncoder.encode(userDto.getNewPassword()));
    }

    // TODO: roles 업데이트 필요
    return UserDto.of(userEntity);
  }

  public UserDto get(long id) throws BusinessException {
    return UserDto.of(getEntity(id));
  }

  public UserDto getByUsername(String username) throws BusinessException {
    return UserDto.of(getEntityByUsername(username));
  }

  @Transactional
  public void delete(long id) throws BusinessException {
    if (!userRepository.existsById(id)) {
      throw new BusinessException(ApiErrorCode.USER_NOT_FOUND);
    }

    userRepository.deleteById(id);
  }

  public org.springframework.security.core.userdetails.User getUserDetails(String username)
      throws BusinessException {
    UserEntity userEntity = getEntityByUsername(username);

    return new org.springframework.security.core.userdetails.User(
        userEntity.getUsername(),
        userEntity.getPassword(),
        userEntity.getRoles());
  }

  private UserEntity getEntity(long id) throws BusinessException {
    return userRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.USER_NOT_FOUND));
  }

  private UserEntity getEntityByUsername(String username) throws BusinessException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new BusinessException(ApiErrorCode.USER_USERNAME_NOT_FOUND));
  }

  public boolean isExistsByUsername(String username) {
    return userRepository.exists(Example.of(
        UserEntity.builder().username(username).build(),
        ExampleMatcher.matching().withIgnoreCase()));
  }

  private boolean isExistsByEmail(String username) {
    return userRepository.exists(Example.of(
        UserEntity.builder().username(username).build(),
        ExampleMatcher.matching().withIgnoreCase()));
  }

  private void checkDuplicateUsername(String username) throws BusinessException {
    if (isExistsByUsername(username)) {
      throw new BusinessException(ApiErrorCode.USER_USERNAME_DUPLICATED);
    }
  }

  private void checkDuplicateEmail(String email) throws BusinessException {
    if (isExistsByEmail(email)) {
      throw new BusinessException(ApiErrorCode.USER_EMAIL_DUPLICATED);
    }
  }

}
