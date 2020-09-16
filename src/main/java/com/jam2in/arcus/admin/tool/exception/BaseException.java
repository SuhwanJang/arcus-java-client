package com.jam2in.arcus.admin.tool.exception;

import com.jam2in.arcus.admin.tool.error.ApiError;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

  private final ApiError apiError;

  public BaseException(ApiError apiError) {
    super(apiError.getMessage());
    this.apiError = apiError;
  }

}
