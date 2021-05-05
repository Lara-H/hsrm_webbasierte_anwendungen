package de.hsrm.mi.web.projekt.test.ueb03;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.hsrm.mi.web.projekt.sichtung.Sichtung;

public class Web_Ueb03_A3_Sichtung {
    private final String TESTNAME = "Jöndhard";
    private final String TESTORT = "In der Ecke 9, 99441 Vollradisroda";
    private final LocalDate TESTDATUM = LocalDate.now();
    private final String TESTBESCHREIBUNG = "Ich habe einen Bus Linie 17 gesehen!";
    
    @Test
    @DisplayName("Sichtung-Instanz anlegen und setter/toString() testen")
    public void sichtung_vorhanden() {
        Sichtung sichtung = new Sichtung();
        sichtung.setName(TESTNAME);
        sichtung.setOrt(TESTORT);
        sichtung.setDatum(TESTDATUM);
        sichtung.setBeschreibung(TESTBESCHREIBUNG);

        String tostr = sichtung.toString();
        assertTrue(tostr.contains(TESTNAME));
        assertTrue(tostr.contains(TESTORT));
        assertTrue(tostr.contains(TESTBESCHREIBUNG));
    }
    
}