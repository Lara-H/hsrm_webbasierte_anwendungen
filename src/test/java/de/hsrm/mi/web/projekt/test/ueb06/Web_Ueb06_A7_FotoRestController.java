package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoRepository;
import de.hsrm.mi.web.projekt.foto.FotoService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Web_Ueb06_A7_FotoRestController {
    RestTemplate resttemplate = new RestTemplate();

    final String TESTMIMETYPE = "image/jpg";
    final String TESTORT = "Testhausen";
    final LocalDateTime TESTZEITSTEMPEL = LocalDateTime.now();
    final double TESTGEOLAENGE = 17f;
    final double TESTGEOBREITE = 4f;

    @Autowired
    private FotoRepository fotorepo;

    @Autowired
    private FotoService fotoservice;

    @Autowired
    private MockMvc mockmvc;

    @Test
    public void vorabcheck() {
        assertThat(FotoRepository.class).isInterface();
        assertThat(fotorepo).isNotNull();
    }

    private List<Foto> initDB() throws IOException {
        fotorepo.deleteAll();

        // Fotos in DB bereitstellen
        List<Foto> fotolist = new ArrayList<>();
        for (var dateiname : List.of("testbild-1-20110828.jpg", "testbild-3-20170904.jpg", "testbild-2-20050508.jpg",
                "testbild-4-20040710.jpg")) {

            byte[] fotobytes = null;

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

    final String SCHOEN = " ist nett";
    final String DOOF = " ist öde";
    final String ANREDE1 = "Meisterin ";
    final String ANREDE2 = "Meister ";

    private void allefotoskommentieren(List<Foto> fotolist) {
        for (var foto : fotolist) {
            fotoservice.fotoKommentieren(foto.getId(), ANREDE1 + foto.getId(), foto.getDateiname() + SCHOEN);
            fotoservice.fotoKommentieren(foto.getId(), ANREDE2 + foto.getId(), foto.getDateiname() + DOOF);
        }
    }


    @Test
    @DisplayName("FotoRestController - GET /api/foto")
    @Transactional
    public void get_api_foto() throws Exception {
        var fotoliste = initDB();
        allefotoskommentieren(fotoliste);

        mockmvc.perform(
            get("/api/foto")
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful())
         .andExpect(jsonPath("$", hasSize(fotoliste.size())))
         .andExpect(jsonPath("$[0].id", is(fotoliste.get(0).getId()), Long.class))
         .andExpect(jsonPath("$[0].dateiname", is(fotoliste.get(0).getDateiname())))
         .andExpect(jsonPath("$[0].ort", is(fotoliste.get(0).getOrt())))
         .andExpect(jsonPath("$[0].zeitstempel", is(fotoliste.get(0).getZeitstempel().toString())))
         .andExpect(jsonPath("$[0].geolaenge", is(fotoliste.get(0).getGeolaenge())))
         .andExpect(jsonPath("$[0].geobreite", is(fotoliste.get(0).getGeobreite())))
         .andExpect(jsonPath("$[0].fotodaten").doesNotExist())

         .andExpect(jsonPath("$[2].id", is(fotoliste.get(2).getId()), Long.class))
         .andExpect(jsonPath("$[2].dateiname", is(fotoliste.get(2).getDateiname())))
         .andExpect(jsonPath("$[2].ort", is(fotoliste.get(2).getOrt())))
         .andExpect(jsonPath("$[2].zeitstempel", is(fotoliste.get(2).getZeitstempel().toString())))
         .andExpect(jsonPath("$[2].geolaenge", is(fotoliste.get(2).getGeolaenge())))
         .andExpect(jsonPath("$[2].geobreite", is(fotoliste.get(2).getGeobreite())))
         .andExpect(jsonPath("$[2].fotodaten").doesNotExist())

         .andExpect(jsonPath("$[1].kommentare[0]", is(fotoliste.get(1).getKommentare().get(0).getId()), Long.class))
         .andExpect(jsonPath("$[3].kommentare[1]", is(fotoliste.get(3).getKommentare().get(1).getId()), Long.class));

    }


    @Test
    @DisplayName("FotoRestController - GET /api/foto/{id}/kommentar")
    @Transactional
    public void get_api_foto_kommentar() throws Exception {
        var fotoliste = initDB();
        allefotoskommentieren(fotoliste);

        int testidx = 2;
        long testid = fotoliste.get(testidx).getId();

        mockmvc.perform(
            get("/api/foto/"+testid+"/kommentar")
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful())
         .andExpect(jsonPath("$[0].id", is(fotoliste.get(testidx).getKommentare().get(0).getId()), Long.class))
         .andExpect(jsonPath("$[0].autor", is(fotoliste.get(testidx).getKommentare().get(0).getAutor())))
         .andExpect(jsonPath("$[0].text", is(fotoliste.get(testidx).getKommentare().get(0).getText())))

         .andExpect(jsonPath("$[1].id", is(fotoliste.get(testidx).getKommentare().get(1).getId()), Long.class))
         .andExpect(jsonPath("$[1].autor", is(fotoliste.get(testidx).getKommentare().get(1).getAutor())))
         .andExpect(jsonPath("$[1].text", is(fotoliste.get(testidx).getKommentare().get(1).getText())));

    }    

    @Test
    @DisplayName("FotoRestController - DELETE /api/foto/{id}")
    @Transactional
    public void delete_api_foto() throws Exception {
        var fotoliste = initDB();
        allefotoskommentieren(fotoliste);

        int testidx = 2;
        long testid = fotoliste.get(testidx).getId();

        long n = fotorepo.count();

        // Foto <testid> löschen lassen
        mockmvc.perform(
            delete("/api/foto/"+testid)
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful());

        // Ist es weg?
        assertThat(fotorepo.count()).isEqualTo(n - 1);
        assertThat(fotorepo.existsById(testid)).isFalse();

    }        


    @Test
    @DisplayName("FotoRestController - DELETE /api/foto/{id}/kommentar/{kid}")
    @Transactional
    public void delete_api_foto_kommentar() throws Exception {
        var fotoliste = initDB();
        allefotoskommentieren(fotoliste);

        int testidx = 2;
        Foto testfoto = fotoliste.get(testidx);
        long testid = testfoto.getId();

        int n = testfoto.getKommentare().size();
        long testkid = testfoto.getKommentare().get(1).getId();

        // Foto <testid> löschen lassen
        mockmvc.perform(
            delete("/api/foto/"+testid+"/kommentar/"+testkid)
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful());

        // Ist der Kommentar weg?
        assertThat(testfoto.getKommentare().size()).isEqualTo(n - 1);
        assertThat(testfoto.getKommentare().stream().anyMatch(e -> e.getId() == testid)).isFalse();

    }        
}