package com.jam2in.arcus.admin.tool.bean;

import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class UserDetailsService
    implements org.springframework.security.core.userdetails.UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(StringUtils.EMPTY));

    UserDto userDto = UserDto.of(userEntity);

    return new UserPrincipal(userDto, userEntity.getPassword());
  }

}