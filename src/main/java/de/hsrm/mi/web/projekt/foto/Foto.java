package de.hsrm.mi.web.projekt.foto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
//import javax.persistence.PreRemove;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Foto {
    @Id @GeneratedValue long 
    id;

    @Version long 
    version;

    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(cascade = CascadeType.ALL)
    private List<Kommentar> kommentare = new ArrayList<>();

    @NotBlank 
    private String mimetype = "";

    @Size(min=3,message="Mindenstens {min} Zeichen notwendig.") @NotBlank 
    private String dateiname = "";

    private String ort = "";

    @PastOrPresent(message = "Darf nicht in der Zukunft liegen.") 
    private LocalDateTime zeitstempel = LocalDateTime.MIN;

    private double geolaenge = 0;
    
    private double geobreite = 0;

    @JsonIgnore
    @Lob 
    private byte[] fotodaten;

    public Foto(String dateiname, byte[] fotodaten, String mimetype) {
        this.dateiname = dateiname;
        this.fotodaten = fotodaten;
        this.mimetype = mimetype;
    }

    public Foto() {
    }

    // @PreRemove
    // public void kommentareLoeschen(Foto foto) {
    //     kommentare.removeAll(kommentare);
    // }

    public void addKommentar(Kommentar kommentar) {
        kommentare.add(kommentar);
    }

    public void removeKommentar(Kommentar kommentar) {
        kommentare.remove(kommentar);
    }

    public Kommentar findKommentarById(long id) {
        for (Kommentar kommentar : kommentare) {
            if (kommentar.getId() == id) {
                return kommentar;
            }
        }
        return null;
    }

    public List<Kommentar> getKommentare() {
        return this.kommentare;
    }

    public void setKommentare(List<Kommentar> kommentare) {
        this.kommentare = kommentare;
    }

    public long getId() {
		return this.id;
	}

    public void setId(long id) {
		this.id = id;
	}

    public long getVersion() {
        return this.version;
    }

    public void setVersion(long version) {
        this.version = version;
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

    public void setOrt(String optional) {
        this.ort = optional;
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