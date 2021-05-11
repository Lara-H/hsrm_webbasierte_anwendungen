package de.hsrm.mi.web.projekt.validierung;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SiebzehnValidator implements ConstraintValidator<Siebzehnhaft, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value.toUpperCase().contains("17") | value.toUpperCase().contains("{siebzehnhaft.siebzehn}")) {
            return true;
        }
        return false;
    }

}
