package com.jam2in.arcus.admin.tool.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jam2in.arcus.admin.tool.bean.JwtTokenProvider;
import com.jam2in.arcus.admin.tool.bean.UserPrincipal;
import com.jam2in.arcus.admin.tool.error.ApiError;
import com.jam2in.arcus.admin.tool.domain.user.dto.UserDto;
import com.jam2in.arcus.admin.tool.error.ApiErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider,
                                 ObjectMapper objectMapper) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response)
      throws AuthenticationException {
    UserDto userDto;

    try {
      userDto = objectMapper.readValue(request.getInputStream(), UserDto.class);
    } catch (IOException e) {
      throw new InternalAuthenticationServiceException(e.getMessage(), e);
    }

    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            userDto.getUsername(), userDto.getPassword(), Collections.emptyList()));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult) {
    jwtTokenProvider.applyHeader(response, (UserDetails) authResult.getPrincipal());

    try {
      response.getWriter().write(
          objectMapper.writeValueAsString(
              ((UserPrincipal) authResult.getPrincipal()).getUserDto()));
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException failed) throws IOException {
    ApiError.response(response,
        ApiError.of(ApiErrorCode.COMMON_INVALID_USERNAME_OR_PASSWORD),
        objectMapper);
  }

}
