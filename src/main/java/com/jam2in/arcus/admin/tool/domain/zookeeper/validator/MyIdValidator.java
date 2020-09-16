package com.jam2in.arcus.admin.tool.domain.zookeeper.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyIdValidator implements ConstraintValidator<MyId, Integer> {

  @Override
  public void initialize(MyId constraintAnnotation) {
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    return value == null || value >= 1;
  }

}
