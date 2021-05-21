package de.hsrm.mi.web.projekt.foto;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.PastOrPresent;

@Entity
public class Kommentar {
    @Id @GeneratedValue long 
    id;

    @Version long 
    version;

    private String autor = "";

    @PastOrPresent(message = "Darf nicht in der Zukunft liegen.") 
    private LocalDateTime zeitpunkt = LocalDateTime.MIN;
    
    private String text = "";

    public Kommentar(String autor, String text) {
        this.autor = autor;
        this.text = text;
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

    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LocalDateTime getZeitpunkt() {
        return this.zeitpunkt;
    }

    public void setZeitpunkt(LocalDateTime zeitpunkt) {
        this.zeitpunkt = zeitpunkt;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    






}