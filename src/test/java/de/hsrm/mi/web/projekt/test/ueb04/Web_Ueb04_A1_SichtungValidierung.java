package de.hsrm.mi.web.projekt.test.ueb04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
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
public class Web_Ueb04_A1_SichtungValidierung {


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
     * Name darf nicht leer sein und muss mindestens 3 Zeichen lang sein
     */

    @Test
    @DisplayName("Sichtung-Validierung: Name nicht leer")
    public void validateNameNichtLeer() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "name", "");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Sichtung-Validierung: Name nicht null")
    public void validateNameNichtNull() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "name", null);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Sichtung-Validierung: Name ok")
    public void validateNameOk() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "name", "Yok");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Sichtung-Validierung: Name nicht zu kurz")
    public void validateNameZuKurz() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "name", "XY");
        assertFalse(violations.isEmpty());
    }


    /*
     * Ort nicht leer
     */

    @Test
    @DisplayName("Sichtung-Validierung: Ort nicht leer")
    public void validateOrtNichtLeer() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "ort", "");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Sichtung-Validierung: Ort nicht null")
    public void validateOrtNichtNull() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "ort", null);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Sichtung-Validierung: Ort ok")
    public void validateOrtOk() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "ort", "Aha");
        assertTrue(violations.isEmpty());
    }


    /*
     * Beschreibung darf nicht leer und nicht laenger als 80 Zeichen sein
     */

    @Test
    @DisplayName("Sichtung-Validierung: Beschreibung nicht leer")
    public void validateBeschreibungNichtLeer() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", "");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Sichtung-Validierung: Beschreibung ok")
    public void validateBeschreibungOk() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", "Im Garten sind siebzehn Kängurus herumgehüpft");
        assertTrue(violations.isEmpty());
    }


    private static final String BESCHREIBUNG80 = "Im Garten sind siebzehn Kängurus herumgehüpft, daneben aber auch neun Nasenbären";

    @Test
    @DisplayName("Sichtung-Validierung: Beschreibung 80 Zeichen ok")
    public void validateBeschreibung80ZeichenOk() {
        String b = BESCHREIBUNG80;
        assertEquals(80, b.length());
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", b);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Sichtung-Validierung: Beschreibung nicht >80 Zeichen")
    public void validateBeschreibungZuLang() {
        String beschrEinsZuLang = BESCHREIBUNG80 + "!";
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "beschreibung", beschrEinsZuLang);
        assertFalse(violations.isEmpty());
    }





    /*
     * Datum darf nicht leer sein
     */

    @Test
    @DisplayName("Sichtung-Validierung: Datum darf nicht leer sein")
    public void validateHaltbarkeitAbgelaufen() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "datum", null);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Sichtung-Validierung: Datum 'heute' ok'")
    public void validateHaltbarkeitHeuteOk() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "datum", LocalDate.now());
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Sichtung-Validierung: Datum 'Vergangenheit' ok")
    public void validateHaltbarkeitZukunftOk() {
        Set<ConstraintViolation<Sichtung>> violations = validator.validateValue(
            Sichtung.class, "datum", LocalDate.now().minusDays(17));
        assertTrue(violations.isEmpty());
    }

}
