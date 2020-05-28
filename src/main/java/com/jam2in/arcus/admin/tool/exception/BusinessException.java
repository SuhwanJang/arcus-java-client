package com.jam2in.arcus.admin.tool.exception;

public class BusinessException extends BaseException {

  public BusinessException(ApiErrorCode apiErrorCode) {
    super(apiErrorCode);
  }

}
