package com.jam2in.arcus.admin.tool.domain.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PortValidator implements ConstraintValidator<Port, Integer> {

  public static final int SIZE_MIN_PORT = 1;
  public static final int SIZE_MAX_PORT = 65535;

  @Override
  public void initialize(Port constraintAnnotation) {
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    return value == null ||
        value >= PortValidator.SIZE_MIN_PORT
        && value <= PortValidator.SIZE_MAX_PORT;
  }

}
