package de.hsrm.mi.web.projekt.sichtung;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import de.hsrm.mi.web.projekt.validierung.Siebzehnhaft;

public class Sichtung {

    @Size(min=3,max=80,message="{form.nameSize}")
    @NotBlank
    private String name;

    @Size(min=3,max=80,message="{form.ortSize}")
    @NotBlank
    private String ort;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message="{form.datumNotNull}")
    @PastOrPresent(message = "{form.datumPastOrPresent}")
    private LocalDate datum;

    @Siebzehnhaft(message="{siebzehnhaft.fehler}")
    @Size(min=3,max=80,message="{form.beschreibungSize}")
    @NotBlank
    private String beschreibung;

    public Sichtung() {
    }

    // Erste Einträge zum Testen
    public Sichtung(String name, String ort, String beschreibung) {
        this.name = name;
        this.ort = ort;
        this.beschreibung = beschreibung;
        setDatum(LocalDate.now());
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