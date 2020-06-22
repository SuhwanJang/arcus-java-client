package com.jam2in.arcus.admin.tool.error;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public enum ApiErrorCode {

  COMMON_INTERNAL_SERVER_ERROR(
      "COMMON001", "internal server error",
      HttpStatus.INTERNAL_SERVER_ERROR),
  COMMON_METHOD_NOT_ALLOWED(
      "COMMON002", "method not allowed",
      HttpStatus.METHOD_NOT_ALLOWED),
  COMMON_INVALID_CONTENT(
      "COMMON003", "invalid content",
      HttpStatus.BAD_REQUEST),
  COMMON_ACCESS_DENIED(
      "COMMON004", "access denied",
      HttpStatus.FORBIDDEN),
  COMMON_INVALID_USERNAME_OR_PASSWORD(
      "COMMON005", "invalid username or password",
      HttpStatus.UNAUTHORIZED),
  COMMON_INVALID_PARAMETER(
      "COMMON006", "invalid parameter",
      HttpStatus.BAD_REQUEST),
  COMMON_CONFLICT(
      "COMMON007", "conflict",
      HttpStatus.CONFLICT),

  COMMON_INVALID_TYPE(
      "must %s type"),

  USER_NOT_FOUND(
      "USER001", "user is not found",
      HttpStatus.NOT_FOUND),
  USER_USERNAME_NOT_FOUND(
      "USER002", "username is not found",
      HttpStatus.NOT_FOUND),
  USER_EMAIL_NOT_FOUND(
      "USER003", "email is not found",
      HttpStatus.NOT_FOUND),
  USER_USERNAME_DUPLICATED(
      "USER004", "username is duplicated",
      HttpStatus.CONFLICT),
  USER_EMAIL_DUPLICATED(
      "USER005", "email is duplicated",
      HttpStatus.CONFLICT),
  USER_PASSWORD_MISMATCH(
      "USER006", "password is mismatch",
      HttpStatus.FORBIDDEN),
  NO_USER(
      "USER007", "no user",
      HttpStatus.NO_CONTENT),

  ENSEMBLE_NOT_FOUND(
      "ENSEMBLE001", "ensemble is not found",
      HttpStatus.NOT_FOUND),
  ENSEMBLE_NAME_DUPLICATED(
      "ENSEMBLE002", "name is duplicated",
      HttpStatus.CONFLICT),

  ZOOKEEPER_NOT_FOUND(
      "ZOOKEEPER001", "zookeeper is not found",
      HttpStatus.NOT_FOUND),
  ZOOKEEPER_ADDRESS_DUPLICATED(
      "ZOOKEEPER002", "address is duplicated",
      HttpStatus.CONFLICT),
  ZOOKEEPER_TASK_TIMEOUT(
      "ZOOKEEPER100", "task timeout",
      HttpStatus.INTERNAL_SERVER_ERROR),
  ZOOKEEPER_TASK_REJECTED(
      "ZOOKEEPER101", "task rejected",
      HttpStatus.INTERNAL_SERVER_ERROR),
  ZOOKEEPER_CONNECTION_FAILED(
      "ZOOKEEPER102", "connection failed",
      HttpStatus.INTERNAL_SERVER_ERROR),
  ZOOKEEPER_UNKNOWN(
      "ZOOKEEPER199", "unknown",
      HttpStatus.INTERNAL_SERVER_ERROR),
  ;

  private final String code;
  // TODO: use message source
  private final String message;
  private final HttpStatus status;

  ApiErrorCode(String message) {
    this(StringUtils.EMPTY, message, null);
  }

  ApiErrorCode(String code, String message, HttpStatus status) {
    this.code = code;
    this.message = message;
    this.status = status;
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
