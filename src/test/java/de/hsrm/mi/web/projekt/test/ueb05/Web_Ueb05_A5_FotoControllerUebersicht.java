package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

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
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Web_Ueb05_A5_FotoControllerUebersicht {
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
    @DisplayName("Übersichtsseite per HTTP GET /foto")
    @Transactional
    public void foto_uebersichtsseite() throws Exception {
        List<Foto> fotoliste = initDB();
        assertThat(fotoliste.size()).isGreaterThanOrEqualTo(4);

        var result = mockmvc.perform(
            get("/foto")
        ).andExpect(status().isOk())
         .andExpect(content().string(containsString(fotoliste.get(0).getDateiname())))
         .andExpect(content().string(containsString(fotoliste.get(1).getDateiname())))
         .andExpect(content().string(containsString(fotoliste.get(2).getDateiname())))
         .andExpect(content().string(containsString(fotoliste.get(3).getDateiname())))
         .andExpect(content().string(containsString(fotoliste.get(0).getOrt())))
         .andExpect(content().string(containsString(fotoliste.get(1).getOrt())))
         .andExpect(content().string(containsString(fotoliste.get(2).getOrt())))
         .andExpect(content().string(containsString(fotoliste.get(3).getOrt())))
         .andReturn();

         var mv = result.getModelAndView();
         @SuppressWarnings("unchecked")
         List<Foto> modelfotoliste = (List<Foto>)mv.getModel().get("fotos");
         
         assertThat(modelfotoliste.size()).isEqualTo(fotoliste.size());
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
