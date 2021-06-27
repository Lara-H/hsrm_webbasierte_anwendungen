package de.hsrm.mi.web.projekt.messaging;

public class FotoMessage {
    public static final String FOTO_GESPEICHERT = "fotoGespeichert";
    public static final String FOTO_GELOESCHT = "fotoGeloescht";
    private String operation;
    private long id;
    public FotoMessage() {}
    public FotoMessage(String op, long id) {
    this.operation = op;
    this.id = id;
}

public String getOperation() {
    return this.operation;
}

public void setOperation(String operation) {
    this.operation = operation;
}

public long getId() {
    return this.id;
}

public void setId(long id) {
    this.id = id;
}

}