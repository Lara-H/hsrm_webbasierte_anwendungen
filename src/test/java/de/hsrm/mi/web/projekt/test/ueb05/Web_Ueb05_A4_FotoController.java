package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Web_Ueb05_A4_FotoController {
    final String TESTMIMETYPE = "image/jpg";
    final String TESTDATEINAME = "testbild-4-20040710.jpg";
    final String TESTORT = "irgendwo";
    final LocalDateTime TESTZEITSTEMPEL = LocalDateTime.now();
    final double TESTGEOLAENGE = 17f;
    final double TESTGEOBREITE = 4f;

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private FotoRepository fotorepo;

    private byte[] readTestFotoBytes(String dateiname) throws IOException {
        var testbildfile = new File("src/test/resources/testbilder/" + dateiname);
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

    @Test
    @DisplayName("Foto-Upload per HTTP POST")
    @Transactional
    public void foto_upload() throws Exception {

        fotorepo.deleteAll();

        // Foto per HTTP POST hochladen
        MockMultipartFile bilddatei = new MockMultipartFile(
            "datei", TESTDATEINAME, TESTMIMETYPE, readTestFotoBytes(TESTDATEINAME));
            
        mockmvc.perform(
            multipart("/foto")
            .file(bilddatei)
        ).andExpect(status().isOk());

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(fotorepo.count()).isEqualTo(1);

        // Foto frisch aus DB auslesen und checken
        Foto neugelesen = fotorepo.findAll().get(0);
        assertThat(neugelesen.getDateiname()).isEqualTo(TESTDATEINAME);
        assertThat(neugelesen.getMimetype()).isEqualTo(TESTMIMETYPE);
        byte[] dbfotobytes = neugelesen.getFotodaten();
        assertThat(dbfotobytes).isEqualTo(readTestFotoBytes(TESTDATEINAME));
    }


    @Test
    @DisplayName("Foto-Upload nur bei mindestens 17 Bytes Bild-Daten")
    @Transactional
    public void foto_upload_zu_wenig_daten() throws Exception {
        fotorepo.deleteAll();

        byte[] zuwenigdaten = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};

        // "Foto" per HTTP POST hochladen
        MockMultipartFile bilddatei = new MockMultipartFile(
            "datei", TESTDATEINAME, TESTMIMETYPE, zuwenigdaten);

        mockmvc.perform(
            multipart("/foto")
            .file(bilddatei)
        ).andExpect(status().isOk());

        // Repository darf keinen Eintrag dazu bekommen haben
        assertThat(fotorepo.count()).isEqualTo(0);
    }


    @Test
    @DisplayName("Foto-Download per HTTP GET")
    @Transactional
    public void foto_download() throws Exception {
        fotorepo.deleteAll();

        // Foto zusammenbasteln
        byte[] fotobytes = readTestFotoBytes(TESTDATEINAME);

        final Foto unmanagedfoto = new Foto();
        unmanagedfoto.setMimetype(TESTMIMETYPE);
        unmanagedfoto.setDateiname(TESTDATEINAME);
        unmanagedfoto.setOrt(TESTORT);
        unmanagedfoto.setFotodaten(fotobytes);
        unmanagedfoto.setZeitstempel(TESTZEITSTEMPEL);

        // Foto in DB speichern
        Foto managedfoto = fotorepo.save(unmanagedfoto);
        long dbfotoid = managedfoto.getId();

        // Versuch, Foto wieder per HTTP downzuloaden
        mockmvc.perform(
            get("/foto/" + dbfotoid)
        ).andExpect(status().isOk())
         .andExpect(header().string("Content-Type", TESTMIMETYPE))
         .andExpect(content().bytes(fotobytes));
    }

    @Test
    @DisplayName("Foto löschen HTTP GET auf .../del")
    @Transactional
    public void foto_loeschen() throws Exception {
        fotorepo.deleteAll();

        // Fotos in DB bereitstellen
        List<Long> idlist = new ArrayList<>();
        for (var dateiname : List.of("testbild-1-20110828.jpg", "testbild-2-20050508.jpg", "testbild-3-20170904.jpg")) {

            byte[] fotobytes = readTestFotoBytes(dateiname);

            final Foto unmanagedfoto = new Foto();
            unmanagedfoto.setMimetype(TESTMIMETYPE);
            unmanagedfoto.setDateiname(dateiname);
            unmanagedfoto.setOrt(TESTORT);
            unmanagedfoto.setFotodaten(fotobytes);
            unmanagedfoto.setZeitstempel(TESTZEITSTEMPEL);

            // Foto in DB speichern
            Foto managedfoto = fotorepo.save(unmanagedfoto);
            
            // vergebene ID des gespeicherten Fotos merken
            long dbfotoid = managedfoto.getId();
            idlist.add(dbfotoid);
        }

        // Fotos nach IDs nacheinander per HTTP-GET löschen
        for (var id: idlist) {
            String urlpfad = String.format("/foto/%s/del", id);
            mockmvc.perform(
                get(urlpfad)
            ).andExpect(status().is3xxRedirection())
             .andExpect(header().string("Location", "/foto"));
            
             // Foto mit gelöschter id darf nicht mehr im Repository sein
            assertThat(fotorepo.findById(id).isPresent()).isFalse();
        }
        assertThat(fotorepo.count()).isZero();
    }

}
