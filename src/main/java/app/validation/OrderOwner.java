package app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {OrderOwnerValidator.class})
public @interface OrderOwner {

    String message() default "You are not allowed to access this order!";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
