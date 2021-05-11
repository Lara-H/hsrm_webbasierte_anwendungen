package de.hsrm.mi.web.projekt.sichtung.validierung;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SiebzehnValidator.class)
@Documented
public @interface Siebzehnhaft {
    String message() default "Wert muss eine '17' oder das Wort 'Siebzehn' enthalten";
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
}