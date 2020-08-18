package com.jam2in.arcus.admin.tool.domain.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { AddressValidator.class })
public @interface Address {

  String message() default "invalid address.";

  Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
