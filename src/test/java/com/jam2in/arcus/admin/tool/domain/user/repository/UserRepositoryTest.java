package com.jam2in.arcus.admin.tool.domain.user.repository;

import com.jam2in.arcus.admin.tool.domain.user.entity.RoleEntity;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private UserEntity.UserEntityBuilder userEntityBuilder;

  @Before
  public void before() {
    userEntityBuilder =
        UserEntity.builder()
          .username("foo")
          .email("foo@bar.com")
          .password("foobar")
          .roles(List.of(RoleEntity.ROLE_ADMIN, RoleEntity.ROLE_USER));
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void save_usernameEmpty() {
    userEntityBuilder.username(null);

    userRepository.save(userEntityBuilder.build());
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void save_emailEmpty() {
    userEntityBuilder.email(null);

    userRepository.save(userEntityBuilder.build());
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void save_passwordEmpty() {
    userEntityBuilder.password(null);

    userRepository.save(userEntityBuilder.build());
  }

  @Test
  public void existsByUsername() {
    // given
    UserEntity userEntity = userEntityBuilder.build();
    userRepository.save(userEntity);

    // when
    boolean exists = userRepository.existsByUsername(userEntity.getUsername());

    // then
    assertThat(exists, is(true));
  }

  @Test
  public void existsByUsername_noUsername() {
    // given
    UserEntity userEntity = userEntityBuilder.build();

    // when
    boolean exists = userRepository.existsByUsername(userEntity.getUsername());

    // then
    assertThat(exists, is(false));
  }

  @Test
  public void existsByEmail() {
    // given
    UserEntity userEntity = userEntityBuilder.build();
    userRepository.save(userEntity);

    // when
    boolean exists = userRepository.existsByEmail(userEntity.getEmail());

    // then
    assertThat(exists, is(true));
  }

  @Test
  public void existsByEmail_noEmail() {
    // given
    UserEntity userEntity = userEntityBuilder.build();

    // when
    boolean exists = userRepository.existsByEmail(userEntity.getEmail());

    // then
    assertThat(exists, is(false));
  }


  @Test
  public void findByUsername() {
    // given
    UserEntity userEntity = userEntityBuilder.build();

    // when
    userRepository.save(userEntity);
    Optional<UserEntity> optionalUserEntity =
        userRepository.findByUsername(userEntity.getUsername());

    // then
    assertThat(optionalUserEntity.isPresent(), is(true));
  }

  @Test
  public void findByUsername_noUsername() {
    // given
    UserEntity userEntity = userEntityBuilder.build();

    // when
    Optional<UserEntity> optionalUserEntity =
        userRepository.findByUsername(userEntity.getUsername());

    // then
    assertThat(optionalUserEntity.isPresent(), is(false));
  }

}
