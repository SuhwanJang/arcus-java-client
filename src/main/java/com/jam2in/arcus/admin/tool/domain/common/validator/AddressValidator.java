package com.jam2in.arcus.admin.tool.domain.common.validator;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<Address, String> {

  public static final int SIZE_MIN_PORT = 1;
  public static final int SIZE_MAX_PORT = 65535;

  @Override
  public void initialize(Address constraintAnnotation) {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    String[] splitted = value.split(":", 2);

    return splitted.length >= 2
        && !StringUtils.isEmpty(splitted[0])
        && !StringUtils.isEmpty(splitted[1])
        && NumberUtils.isDigits(splitted[1])
        && Integer.parseInt(splitted[1]) >= SIZE_MIN_PORT
        && Integer.parseInt(splitted[1]) <= SIZE_MAX_PORT;
  }

}
