package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;

import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoRepository;
import de.hsrm.mi.web.projekt.foto.FotoServiceImpl;
import de.hsrm.mi.web.projekt.foto.Kommentar;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Web_Ueb06_A4_FotoServiceKommentierungsmeth {
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

    final String SCHOEN = " ist schön";
    final String DOOF = " ist doof";
    final String ANREDE1 = "Frau ";
    final String ANREDE2 = "Herr ";

    private void allefotoskommentieren(List<Foto> fotolist) {
        for (var foto: fotolist) {
            fotoservice.fotoKommentieren(foto.getId(), ANREDE1+foto.getId(), foto.getDateiname()+SCHOEN);
            fotoservice.fotoKommentieren(foto.getId(), ANREDE2+foto.getId(), foto.getDateiname()+DOOF);
        }
    }


    @Test
    @DisplayName("FotoService: fotoKommentieren()")
    @Transactional
    public void foto_kommentieren() throws Exception {
        List<Foto> fotolist = initDB();

        allefotoskommentieren(fotolist);

        for (var foto: fotolist) {
            var klst = foto.getKommentare();
            assertThat(klst.get(0).getAutor()).isEqualTo(ANREDE1+foto.getId());
            assertThat(klst.get(1).getAutor()).isEqualTo(ANREDE2+foto.getId());
            assertThat(klst.get(0).getText()).isEqualTo(foto.getDateiname()+SCHOEN);
            assertThat(klst.get(1).getText()).isEqualTo(foto.getDateiname()+DOOF);
        }
        assertThat(fotorepo.count()).isEqualTo(fotolist.size());
    }


    @Test
    @DisplayName("FotoService: fotoKommentieren() mit falscher Foto-ID")
    @Transactional
    public void foto_kommentieren_falsche_id() throws Exception {
        List<Foto> fotolist = initDB();

        allefotoskommentieren(fotolist);

        assertThrows(NoSuchElementException.class, () -> {
            fotoservice.fotoKommentieren(12345, "willi", "kein kommentar");
        });

    }


    @Test
    @DisplayName("FotoService: alleKommentareFuerFoto()")
    @Transactional
    public void alle_kommentare_fuer_foto() throws Exception {
        List<Foto> fotolist = initDB();
        allefotoskommentieren(fotolist);

        var foto = fotorepo.findAll().get(1);

        List<Kommentar> repofklist = foto.getKommentare();
        List<Kommentar> akfflist = fotoservice.alleKommentareFuerFoto(foto.getId());
        
        // Liefert Service gleiche Kommentare wie frisch aus der DB?
        assertThat(akfflist.size()).isEqualTo(repofklist.size());
        assertThat(akfflist.get(0).getId()).isEqualTo(repofklist.get(0).getId());
        assertThat(akfflist.get(0).getAutor()).isEqualTo(repofklist.get(0).getAutor());
        assertThat(akfflist.get(0).getText()).isEqualTo(repofklist.get(0).getText());
        assertThat(akfflist.get(0).getZeitpunkt()).isEqualTo(repofklist.get(0).getZeitpunkt());

        assertThat(akfflist.get(1).getId()).isEqualTo(repofklist.get(1).getId());
        assertThat(akfflist.get(1).getAutor()).isEqualTo(repofklist.get(1).getAutor());
        assertThat(akfflist.get(1).getText()).isEqualTo(repofklist.get(1).getText());
        assertThat(akfflist.get(1).getZeitpunkt()).isEqualTo(repofklist.get(1).getZeitpunkt());
    }


    @Test
    @DisplayName("FotoService: fotoKommentarLoeschen()")
    @Transactional
    public void foto_kommentar_loeschen() throws Exception {
        List<Foto> fotolist = initDB();
        allefotoskommentieren(fotolist);

        var foto = fotorepo.findAll().get(2);

        List<Kommentar> repofklist = foto.getKommentare();
        List<Kommentar> akfflist = fotoservice.alleKommentareFuerFoto(foto.getId());

        int repo_n = repofklist.size();
        long kid0 = foto.getKommentare().get(0).getId();
        long kid1 = foto.getKommentare().get(1).getId();
        assertThat(akfflist.size()).isEqualTo(repo_n);

        fotoservice.fotoKommentarLoeschen(foto.getId(), kid0);

        assertThat(akfflist.size()).isEqualTo(repo_n - 1);
        assertThat(foto.getKommentare().size()).isEqualTo(repo_n - 1);  
        // früheres zweites Kommentarlistenele ist nun vorne in der Liste
        assertThat(foto.getKommentare().get(0).getId()).isEqualTo(kid1);
    }


    @Test
    @DisplayName("FotoService: fotoKommentarLoeschen() mit falscher Foto-ID")
    @Transactional
    public void foto_kommentar_loeschen_falsche_fotoid() throws Exception {
        List<Foto> fotolist = initDB();
        allefotoskommentieren(fotolist);

        
        assertThrows(NoSuchElementException.class, () -> {
            fotoservice.fotoKommentarLoeschen(12345, 1);
        });

    }

    @Test
    @DisplayName("FotoService: fotoKommentarLoeschen() mit richtiger Foto und falscher Kommentar-ID")
    @Transactional
    public void foto_kommentar_loeschen_falsche_kommentarid() throws Exception {
        List<Foto> fotolist = initDB();
        allefotoskommentieren(fotolist);

        Foto f1 = fotolist.get(1);
        
        assertThrows(NoSuchElementException.class, () -> {
            fotoservice.fotoKommentarLoeschen(f1.getId(), 12345617);
        });

    }


}