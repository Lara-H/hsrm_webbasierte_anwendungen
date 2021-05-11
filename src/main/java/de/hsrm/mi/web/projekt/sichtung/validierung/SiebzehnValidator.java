package de.hsrm.mi.web.projekt.sichtung.validierung;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SiebzehnValidator implements ConstraintValidator<Siebzehnhaft, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value.toUpperCase().contains("17") | value.toUpperCase().contains("SIEBZEHN")) {
            return true;
        }
        return false;
    }

}
