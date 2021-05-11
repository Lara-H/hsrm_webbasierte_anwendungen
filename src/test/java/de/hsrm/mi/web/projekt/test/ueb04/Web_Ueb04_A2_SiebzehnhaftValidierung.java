package de.hsrm.mi.web.projekt.test.ueb04;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.sichtung.Sichtung;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Web_Ueb04_A2_SiebzehnhaftValidierung {


    private static ValidatorFactory validatorFactory;
    
    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void closeValidatorFactory() {
        validatorFactory.close();
    }


    /*
     * Beschreibung muss '17' oder 'siebzehn' (groß/klein) enthalten
     */

    @Test
    @DisplayName("@Siebzehnhaft: 17 drin und alles ist gut")
    public void validate17drinOk() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", "Es gibt mehr als 17 Gründe, öfters zu hüpfen.");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("@Siebzehnhaft: 'siebzehn' drin und alles ist gut")
    public void validatesiebzehndrinOk() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", "Es gibt siebzehn Ecken");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("@Siebzehnhaft: 'Siebzehn' drin und alles ist gut")
    public void validateSiebzehndrinOk() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", "Die 42 ist out. Siebzehn aber nicht.");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("@Siebzehnhaft: 17-fach ok")
    public void validate17fachOk() {
        String beschr = "Der Einsatz hat sich 17-fach gelohnt.";

        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", beschr);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("@Siebzehnhaft: Ausdrücke wie 3*17+4 ok")
    public void validate17ausdruckOk() {
        String beschr = "Die Lösung ist 3*17+4.";

        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", beschr);
        assertTrue(violations.isEmpty());
    }


    @Test
    @DisplayName("@Siebzehnhaft: Siebzehnmangel wäre falsch")
    public void validateMangelFalsch() {
        String beschr = "Ich habe im Sieb zehn Löcher gezählt.";

        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", beschr);
        assertFalse(violations.isEmpty());
    }

}
