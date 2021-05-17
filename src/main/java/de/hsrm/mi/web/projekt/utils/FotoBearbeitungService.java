package de.hsrm.mi.web.projekt.utils;

import java.io.IOException;

import de.hsrm.mi.web.projekt.foto.Foto;

public interface FotoBearbeitungService {
    public void aktualisiereMetadaten(Foto foto);
    public void orientiereFoto(Foto foto);
    public void skaliereFoto(Foto foto, int breite, int hoehe) throws IOException;
}
