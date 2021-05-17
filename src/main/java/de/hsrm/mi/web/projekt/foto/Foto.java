package de.hsrm.mi.web.projekt.foto;

import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

public class Foto {
    @Id @GeneratedValue long id;
    @GeneratedValue long version;
    @NotBlank private String mimetype = "";
    @Size(min=3,message="Mindenstens {min} Zeichen notwendig.") @NotBlank private String dateiname = "";
    private String ort = "";
    @PastOrPresent(message = "Darf nicht in der Zukunft liegen.") private LocalDateTime zeitstempel = LocalDateTime.MIN;
    private double geolaenge = 0;
    private double geobreite = 0;
    @Lob private byte[] fotodaten;

    public Foto(String mimetype, String dateiname, String ort, LocalDateTime zeitstempel, double geolaenge, double geobreite) {
        this.mimetype = mimetype;
        this.dateiname = dateiname;
        this.ort = ort;
        this.zeitstempel = zeitstempel;
        this.geolaenge = geolaenge;
        this.geobreite = geobreite;
    }

    public String getMimetype() {
        return this.mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDateiname() {
        return this.dateiname;
    }

    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

    public String getOrt() {
        return this.ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public LocalDateTime getZeitstempel() {
        return this.zeitstempel;
    }

    public void setZeitstempel(LocalDateTime zeitstempel) {
        this.zeitstempel = zeitstempel;
    }

    public double getGeolaenge() {
        return this.geolaenge;
    }

    public void setGeolaenge(double geolaenge) {
        this.geolaenge = geolaenge;
    }

    public double getGeobreite() {
        return this.geobreite;
    }

    public void setGeobreite(double geobreite) {
        this.geobreite = geobreite;
    }

    public byte[] getFotodaten() {
        return this.fotodaten;
    }

    public void setFotodaten(byte[] fotodaten) {
        this.fotodaten = fotodaten;
    }
}