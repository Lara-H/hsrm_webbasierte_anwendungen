/**
 * Einzelne Sichtung
 */

package de.hsrm.mi.web.projekt.sichtung;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class Sichtung {
    private String name;
    private String ort;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datum;
    private String beschreibung;
    private int id;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrt() {
        return this.ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public java.time.LocalDate getDatum() {
        return this.datum;
    }

    public void setDatum(java.time.LocalDate datum) {
        this.datum = datum;
    }

    public String getBeschreibung() {
        return this.beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "Name: " + this.getName() + ", Ort: " + this.getOrt() + ", Datum: " + this.getDatum() + ", Beschreibung: " + this.getBeschreibung();
    }
}