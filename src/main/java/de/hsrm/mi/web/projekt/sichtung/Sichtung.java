package de.hsrm.mi.web.projekt.sichtung;
import java.time.LocalDate;

public class Sichtung {
    String name;
    String ort;
    LocalDate datum = LocalDate.now();
    String beschreibung;

    public Sichtung(String name, String ort, String beschreibung) {
        this.name = name;
        this.ort = ort;
        this.beschreibung = beschreibung;
    }

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

    public String toString() {
        return "Name: " + this.getName() + ", Ort: " + this.getOrt() + ", Datum: " + this.getDatum() + ", Beschreibung: " + this.getBeschreibung();
    }


    
}