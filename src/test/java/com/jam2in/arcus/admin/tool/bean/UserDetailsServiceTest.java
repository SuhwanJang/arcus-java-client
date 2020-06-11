package com.jam2in.arcus.admin.tool.bean;

import com.jam2in.arcus.admin.tool.domain.user.type.Access;
import com.jam2in.arcus.admin.tool.domain.user.type.Role;
import com.jam2in.arcus.admin.tool.domain.user.entity.UserEntity;
import com.jam2in.arcus.admin.tool.domain.user.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceTest {

  @InjectMocks
  private UserDetailsService userDetailsService;

  @Mock
  private UserRepository userRepository;

  @Test
  public void loadUserByUsername() {
    // given
    UserEntity userEntity =
        UserEntity.builder()
          .username("foo")
          .password("bar")
          .role(Role.ROLE_ADMIN)
          .accesses(List.of(
              Access.ACCESS_ZOOKEEPER_CLUSTER_MANAGEMENT,
              Access.ACCESS_CACHE_CLUSTER_MANAGEMENT))
          .build();


    User user = new User(
        userEntity.getUsername(),
        userEntity.getPassword(),
        Stream.concat(Stream.of(userEntity.getRole()), userEntity.getAccesses().stream())
            .collect(Collectors.toList()));

    given(userRepository.findByUsername(user.getUsername()))
        .willReturn(Optional.of(userEntity));

    // when
    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

    // then
    verify(userRepository, atMostOnce()).findByUsername(user.getUsername());
    assertThat(userDetails.getUsername(), is(user.getUsername()));
    assertThat(userDetails.getPassword(), is(user.getPassword()));
    assertThat(userDetails.getAuthorities(), is(user.getAuthorities()));
  }

  @Test(expected = UsernameNotFoundException.class)
  public void getUserDetails_notFound() {
    // given
    String username = "foo";

    given(userRepository.findByUsername(username)).willReturn(Optional.empty());

    // when
    userDetailsService.loadUserByUsername(username);
  }

}
