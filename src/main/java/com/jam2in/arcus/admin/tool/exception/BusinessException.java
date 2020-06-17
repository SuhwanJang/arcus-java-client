package com.jam2in.arcus.admin.tool.exception;

import com.jam2in.arcus.admin.tool.error.ApiErrorCode;

public class BusinessException extends BaseException {

  public BusinessException(ApiErrorCode apiErrorCode) {
    super(apiErrorCode);
  }

}
