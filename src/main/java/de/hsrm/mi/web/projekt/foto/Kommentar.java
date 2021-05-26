package de.hsrm.mi.web.projekt.foto;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.PastOrPresent;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
generator = ObjectIdGenerators.PropertyGenerator.class, 
property = "id")
@Entity
public class Kommentar {
    @Id @GeneratedValue long 
    id;

    @Version long 
    version;

    private String autor;

    @PastOrPresent(message = "Darf nicht in der Zukunft liegen.") 
    private LocalDateTime zeitpunkt;
    
    private String text;

    public Kommentar(String autor, String text) {
        this.autor = autor;
        this.text = text;
        this.zeitpunkt = LocalDateTime.now();
    }

    public Kommentar() {
        this.autor = "";
        this.text = "";
        this.zeitpunkt = LocalDateTime.now();
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

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Kommentar other = (Kommentar) obj;
        if (id != other.id)
            return false;
        return true;
    }

}