package de.hsrm.mi.web.projekt.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.foto.Foto;

/*
 * Hilfsmethoden zur Bildauswertung und -bearbeitung
 * verwendet https://www.javaxt.com/
 * 
 * In build.gradle Repository und Dependency ergänzen:
 * 
 * ...
 * repositories {
 *	 ...
 *	 maven { url "http://www.javaxt.com/maven" }
 * }
 * ...
 * dependencies {
 *   ...
 *	 implementation 'javaxt:javaxt-core:1.10.4'
 * }  
 * 
 */

@Service
public class FotoBearbeitungServiceImpl implements FotoBearbeitungService {
    Logger logger = LoggerFactory.getLogger(FotoBearbeitungServiceImpl.class);

    @Override
    public void aktualisiereMetadaten(Foto foto) {
        final int EXIFTAGDATE = 0x0132;

        ByteArrayInputStream bis = new ByteArrayInputStream(foto.getFotodaten());
        javaxt.io.Image img = new javaxt.io.Image(bis);

        double[] coord = img.getGPSCoordinate();
        logger.info("extractMetadata: Coord for {} isch {}", foto, coord);
        if (coord != null) {
            foto.setGeolaenge(coord[0]);
            foto.setGeobreite(coord[1]);
            foto.setOrt(String.format("Geo (%.4f, %.4f)", foto.getGeobreite(), foto.getGeolaenge()));
        }

        Map<Integer, Object> exiftags = img.getExifTags();
        String exifdate = (String) exiftags.get(EXIFTAGDATE);
        if (exifdate != null) {
            var formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
            foto.setZeitstempel(LocalDateTime.parse(exifdate, formatter));
        }
        logger.info("addMetadata Ergebnis: {}", foto);
    }

    @Override
    public void orientiereFoto(Foto foto) {
        ByteArrayInputStream bis = new ByteArrayInputStream(foto.getFotodaten());
        javaxt.io.Image img = new javaxt.io.Image(bis);

        // nach Möglichkeit gemäß EXIF-Orientierung korrekt drehen
        img.rotate();
    }

    @Override
    public void skaliereFoto(Foto foto, int breite, int hoehe) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(foto.getFotodaten());
        javaxt.io.Image img = new javaxt.io.Image(bis);

        int origbreite = img.getWidth();
        int orighoehe = img.getHeight();

        double faktor_breite = (double) breite / origbreite;
        double faktor_hoehe = (double) hoehe / orighoehe;
        double faktor = faktor_breite < faktor_hoehe ? faktor_breite : faktor_hoehe;

        int br = (int) (origbreite * faktor);
        int ho = (int) (orighoehe * faktor);

        img.resize(br, ho);
        foto.setFotodaten(img.getByteArray());

        logger.info("scaleFoto Ergebnis: {}",foto);
    }
}
