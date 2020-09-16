package com.jam2in.arcus.admin.tool.domain.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { PortValidator.class })
public @interface Port {

  String message() default "invalid port.";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

}
