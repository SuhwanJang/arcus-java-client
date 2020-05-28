package com.jam2in.arcus.admin.tool.exception;

import lombok.Getter;

@Getter
public class BaseException extends Exception {

  private final ApiError apiError;

  public BaseException(ApiError apiError) {
    super(apiError.getMessage());
    this.apiError = apiError;
  }

  public BaseException(ApiErrorCode apiErrorCode) {
    this(ApiError.of(apiErrorCode));
  }

}
