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
  COMMON_INVALID_PARAMETER(
      "COMMON004", "invalid parameter",
      HttpStatus.BAD_REQUEST),
  COMMON_ACCESS_DENIED(
      "COMMON005", "access denied",
      HttpStatus.FORBIDDEN),
  COMMON_CONFLICT(
      "COMMON006", "conflict",
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
  USER_INVALID_USERNAME_OR_PASSWORD(
      "USER008", "invalid username or password",
      HttpStatus.UNAUTHORIZED),

  KUBERNETES_NOT_FOUND(
      "KUBERNETES001", "kubernetes is not found",
      HttpStatus.NOT_FOUND),
  KUBERNETES_NAME_DUPLICATED(
      "KUBERNETES002", "name is duplicated",
      HttpStatus.CONFLICT),
  KUBERNETES_ADDRESS_DUPLICATED(
      "KUBERNETES003", "address is duplicated",
      HttpStatus.CONFLICT),

  AGENT_NOT_FOUND(
      "AGENT001", "agent is not found",
      HttpStatus.NOT_FOUND),
  AGENT_CACHE_OPTION_NOT_FOUND(
      "AGENT002", "cache options are not found",
      HttpStatus.NOT_FOUND),
  AGENT_TASK_TIMEOUT(
      "AGENT100", "task timeout",
      HttpStatus.INTERNAL_SERVER_ERROR),
  AGENT_TASK_REJECTED(
      "AGENT101", "task rejected",
      HttpStatus.INTERNAL_SERVER_ERROR),
  AGENT_CONNECTION_FAILED(
      "AGENT102", "connection failed",
      HttpStatus.BAD_GATEWAY),
  AGENT_UNKNOWN(
      "AGENT199", "unknown",
      HttpStatus.INTERNAL_SERVER_ERROR),

  ENSEMBLE_NOT_FOUND(
      "ZKENSEMBLE001", "ensemble is not found",
      HttpStatus.NOT_FOUND),
  ENSEMBLE_DUPLICATED_NAME(
      "ZKENSEMBLE002", "name is duplicated",
      HttpStatus.CONFLICT),
  ENSEMBLE_DUPLICATED_PORT(
      "ZKENSEMBLE003", "port is duplicated",
      HttpStatus.CONFLICT),
  ENSEMBLE_INITIALIZATION_FAILURE(
      "ZKENSEMBLE004", "ensemble initialization failure",
      HttpStatus.INTERNAL_SERVER_ERROR),

  ZOOKEEPER_NOT_FOUND(
      "ZKSERVER001", "zookeeper is not found",
      HttpStatus.NOT_FOUND),
  ZOOKEEPER_ADDRESS_DUPLICATED(
      "ZKSERVER002", "address is duplicated",
      HttpStatus.CONFLICT),
  ZOOKEEPER_TASK_TIMEOUT(
      "ZKSERVER100", "task timeout",
      HttpStatus.INTERNAL_SERVER_ERROR),
  ZOOKEEPER_TASK_REJECTED(
      "ZKSERVER101", "task rejected",
      HttpStatus.INTERNAL_SERVER_ERROR),
  ZOOKEEPER_CONNECTION_FAILED(
      "ZKSERVER102", "connection failed",
      HttpStatus.BAD_GATEWAY),
  ZOOKEEPER_UNKNOWN(
      "ZKSERVER199", "unknown",
      HttpStatus.INTERNAL_SERVER_ERROR),

  MEMCACHED_TASK_TIMEOUT(
      "MEMCACHED100", "task timeout",
      HttpStatus.INTERNAL_SERVER_ERROR),
  MEMCACHED_TASK_REJECTED(
      "MEMCACHED101", "task rejected",
      HttpStatus.INTERNAL_SERVER_ERROR),
  MEMCACHED_CONNECTION_FAILED(
      "MEMCACHED102", "connection failed",
      HttpStatus.BAD_GATEWAY),
  MEMCACHED_UNKNOWN(
      "MEMCACHED199", "unknown",
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
