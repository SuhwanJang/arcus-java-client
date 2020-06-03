package com.jam2in.arcus.admin.tool.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public enum ApiErrorCode {

  // FIXME: message source 사용

  COMMON_INTERNAL_SERVER_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "COMMON001", "internal server error"),
  COMMON_METHOD_NOT_ALLOWED(
      HttpStatus.METHOD_NOT_ALLOWED,
      "COMMON002", "method not allowed"),
  COMMON_INVALID_BODY(
      HttpStatus.BAD_REQUEST,
      "COMMON003", "invalid body"),
  COMMON_ACCESS_DENIED(
    HttpStatus.FORBIDDEN,
      "COMMON004", "access denied"),
  COMMON_INVALID_USERNAME_OR_PASSWORD(
      HttpStatus.UNAUTHORIZED,
      "COMMON005", "invalid username or password"),
  COMMON_INVALID_PARAMETER(
      HttpStatus.BAD_REQUEST,
      "COMMON006", "invalid parameter"),

  COMMON_INVALID_TYPE(
      "must %s type"),

  USER_NOT_FOUND(
      HttpStatus.NOT_FOUND,
      "USER001", "user is not found"),
  USER_USERNAME_NOT_FOUND(
      HttpStatus.NOT_FOUND,
      "USER002", "username is not found"),
  USER_EMAIL_NOT_FOUND(
      HttpStatus.NOT_FOUND,
      "USER003", "email is not found"),
  USER_USERNAME_DUPLICATED(
      HttpStatus.CONFLICT,
      "USER004", "username is duplicated"),
  USER_EMAIL_DUPLICATED(
      HttpStatus.CONFLICT,
      "USER005", "email is duplicated"),
  USER_PASSWORD_MISMATCH(
      HttpStatus.FORBIDDEN,
      "USER006", "password is mismatch"),
  NO_USER(
      HttpStatus.NO_CONTENT,
      "USER007", "no user");

  private final HttpStatus status;
  private final String code;
  private final String message;

  ApiErrorCode(String message) {
    this(null, StringUtils.EMPTY, message);
  }

  ApiErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  public HttpStatus status() {
    return status;
  }

  public String code() {
    return code;
  }

  public String message() {
    return message;
  }

}
