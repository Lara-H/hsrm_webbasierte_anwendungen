package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoRepository;
import de.hsrm.mi.web.projekt.foto.FotoServiceImpl;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class Web_Ueb05_A3_FotoService {
    final String TESTMIMETYPE = "image/jpg";
    final String TESTDATEINAME = "testbild-2-20050508.jpg";
    final String TESTORT = "Testhausen";
    final LocalDateTime TESTZEITSTEMPEL = LocalDateTime.now();
    final double TESTGEOLAENGE = 17f;
    final double TESTGEOBREITE = 4f;

    @Autowired
    private FotoRepository fotorepo;

    @Autowired
    private FotoServiceImpl fotoservice;

    private byte[] readTestFotoBytes(String dateiname) throws IOException {
        var testbildfile = new File("src/test/resources/testbilder/"+dateiname);
        byte[] testfotodaten;
        try (var fis = new FileInputStream(testbildfile)) {
            testfotodaten = fis.readAllBytes();
        }
        assert testfotodaten != null;
        return testfotodaten;
    }

    @Test
    public void vorabcheck() {
        assertThat(FotoRepository.class).isInterface();
        assertThat(fotorepo).isNotNull();
    }


    private List<Foto> initDB() throws IOException {
        fotorepo.deleteAll();

        // Fotos in DB bereitstellen
        List<Foto> fotolist = new ArrayList<>();
        for (var dateiname : List.of("testbild-1-20110828.jpg", "testbild-3-20170904.jpg", "testbild-2-20050508.jpg", "testbild-4-20040710.jpg")) {

            byte[] fotobytes = readTestFotoBytes(dateiname);

            final Foto unmanagedfoto = new Foto();
            unmanagedfoto.setMimetype(TESTMIMETYPE);
            unmanagedfoto.setDateiname(dateiname);
            unmanagedfoto.setOrt(TESTORT);
            unmanagedfoto.setFotodaten(fotobytes);
            unmanagedfoto.setZeitstempel(TESTZEITSTEMPEL);

            
            // Foto in DB speichern
            Foto managedfoto = fotorepo.save(unmanagedfoto);
            fotolist.add(managedfoto);
        }
        return fotolist;
    }


    @Test
    @Transactional
    @DisplayName("FotoService: fotoAbspeichern")
    public void foto_persist() throws IOException {
        final Foto unmanagedfoto = new Foto();
        unmanagedfoto.setMimetype(TESTMIMETYPE);
        unmanagedfoto.setDateiname(TESTDATEINAME);
        unmanagedfoto.setOrt(TESTORT);
        unmanagedfoto.setFotodaten(readTestFotoBytes(TESTDATEINAME));
        unmanagedfoto.setZeitstempel(TESTZEITSTEMPEL);

        // Repository leeren
        fotorepo.deleteAll();
        assertThat(fotorepo.count()).isEqualTo(0);

        // Foto in Repository speichern
        Foto managedfoto = fotoservice.fotoAbspeichern(unmanagedfoto);
        long gespeichertId = managedfoto.getId();

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(fotorepo.count()).isEqualTo(1);

        // und wieder frisch aus DB auslesen
        Foto neugelesen = fotorepo.getOne(gespeichertId);
        assertThat(neugelesen.getDateiname()).isEqualTo(TESTDATEINAME);
        assertThat(neugelesen.getMimetype()).isEqualTo(TESTMIMETYPE);
        assertThat(neugelesen.getFotodaten()).isEqualTo(readTestFotoBytes(TESTDATEINAME));
        // getOrt() und getZeitstempel() nicht einbezogen, da die bei Metadatenareicherung überschrieben werden
    }


    @Test
    @DisplayName("FotoService: fotoAbfragenNachId")
    @Transactional
    public void foto_abfragen_nach_id() throws Exception {
        List<Foto> fotolist = initDB();

        for (var foto: fotolist) {
            // alle eingespeicherten Fotos müssen per Service auffindbar sein
            Optional<Foto> fo = fotoservice.fotoAbfragenNachId(foto.getId());
            assertThat(fo.isPresent()).isTrue();

            Foto f = fo.get();
            assertThat(f.getDateiname()).isEqualTo(foto.getDateiname());
            assertThat(f.getOrt()).isEqualTo(foto.getOrt());
            assertThat(f.getZeitstempel()).isEqualTo(foto.getZeitstempel());
            assertThat(f.getFotodaten()).isEqualTo(foto.getFotodaten());
        }
        assertThat(fotorepo.count()).isEqualTo(fotolist.size());
    }


    @Test
    @DisplayName("FotoService: alleFotosNachZeitstempelSortiert")
    @Transactional
    public void foto_abfragen_sortiert() throws Exception {
        List<Foto> fotolist = initDB();

        // Fotoliste sortieren
        fotolist.sort((f1, f2) -> f1.getZeitstempel().compareTo(f2.getZeitstempel()));

        // gucken, ob Fotoservice Fotos in gleicher Reihenfolge liefert
        Iterator<Foto> fiter = fotolist.iterator();
        for (var f: fotoservice.alleFotosNachZeitstempelSortiert()) {
            Foto foto = fiter.next();
            assertThat(f.getDateiname()).isEqualTo(foto.getDateiname());
            assertThat(f.getOrt()).isEqualTo(foto.getOrt());
            assertThat(f.getZeitstempel()).isEqualTo(foto.getZeitstempel());
            assertThat(f.getFotodaten()).isEqualTo(foto.getFotodaten());
        }
    }


    @Test
    @DisplayName("FotoService: loescheFoto")
    @Transactional
    public void foto_loeschen() throws Exception {
        List<Foto> fotolist = initDB();

        // Fotos nach IDs nacheinander löschen
        for (var foto: fotolist) {
            fotoservice.loescheFoto(foto.getId());
            // Foto mit gelöschter id darf nicht mehr im Repository sein
            assertThat(fotorepo.findById(foto.getId()).isPresent()).isFalse();
        }
        // Zum Schluss darf kein Foto übrig sein
        assertThat(fotorepo.count()).isZero();
    }

}