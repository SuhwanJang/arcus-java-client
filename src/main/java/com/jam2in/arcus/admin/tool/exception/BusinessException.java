package com.jam2in.arcus.admin.tool.exception;

import com.jam2in.arcus.admin.tool.error.ApiError;

public class BusinessException extends BaseException {

  public BusinessException(ApiError apiError) {
    super(apiError);
  }

}
