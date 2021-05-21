package de.hsrm.mi.web.projekt.foto;

public class NoSuchElementException extends Exception {

    public NoSuchElementException() {
        super("Keine Kommentare zu diesem Foto gefunden");
    }
    
}