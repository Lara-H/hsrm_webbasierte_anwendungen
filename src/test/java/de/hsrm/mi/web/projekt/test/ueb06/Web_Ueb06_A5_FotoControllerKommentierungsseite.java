package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoRepository;
import de.hsrm.mi.web.projekt.foto.FotoServiceImpl;
import de.hsrm.mi.web.projekt.foto.Kommentar;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Web_Ueb06_A5_FotoControllerKommentierungsseite {
    final String TESTMIMETYPE = "image/jpg";
    final String TESTDATEINAME = "testbild-2-20050508.jpg";
    final String TESTORT = "Testhausen";
    final LocalDateTime TESTZEITSTEMPEL = LocalDateTime.now();
    final double TESTGEOLAENGE = 17f;
    final double TESTGEOBREITE = 4f;


    @Autowired
    private MockMvc mockmvc;

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

    final String INTERESSANT = " ist sehr interessant";
    final String LANGWEILIG = " ist wirklich langweilig";
    final String ANREDE1 = "Fr. ";
    final String ANREDE2 = "Hr. ";

    private void allefotoskommentieren(List<Foto> fotolist) {
        for (var foto: fotolist) {
            fotoservice.fotoKommentieren(foto.getId(), ANREDE1+foto.getId(), foto.getDateiname()+INTERESSANT);
            fotoservice.fotoKommentieren(foto.getId(), ANREDE2+foto.getId(), foto.getDateiname()+LANGWEILIG);
        }
    }



    @Test
    @DisplayName("FotoController: /foto/{id}/kommentar mit eingeloggtem Benutzer liefert Kommentarseite für Foto id mit Kommentar-Formular")
    @Transactional
    public void get_foto_id_kommentar() throws Exception {
        // Sessionattribut vorbereiten
        final String kommentaruser = "jbiff017";
        Map<String, Object> sessiondata = new HashMap<>();
        sessiondata.put("loggedinusername", kommentaruser);
        
        List<Foto> fotoliste = initDB();
        allefotoskommentieren(fotoliste);

        Foto foto1 = fotoliste.get(1);
        long id1 = foto1.getId();
        var klist1 = foto1.getKommentare();

        mockmvc.perform(
            get("/foto/"+id1+"/kommentar")
            .sessionAttrs(sessiondata)
        ).andExpect(status().isOk())
         .andExpect(content().string(containsString(foto1.getDateiname())))
         .andExpect(content().string(containsString(foto1.getOrt())))

         .andExpect(content().string(containsString(klist1.get(0).getAutor())))
         .andExpect(content().string(containsString(klist1.get(0).getText())))
         .andExpect(content().string(containsString(klist1.get(1).getAutor())))
         .andExpect(content().string(containsString(klist1.get(1).getText())))

         .andExpect(content().string(containsStringIgnoringCase("<form")))
         .andExpect(content().string(containsStringIgnoringCase("<input")));
    }


    @Test
    @DisplayName("FotoController: /foto/{id}/kommentar ohne eingeloggten User zeigt Formular-Seite ohne Kommentar-Formular")
    @Transactional
    public void get_foto_id_kommentar_kein_loggedinuser() throws Exception {
        List<Foto> fotoliste = initDB();
        allefotoskommentieren(fotoliste);

        Foto foto1 = fotoliste.get(1);
        long id1 = foto1.getId();
        var klist1 = foto1.getKommentare();

        mockmvc.perform(
            get("/foto/"+id1+"/kommentar")
        ).andExpect(status().isOk())
         .andExpect(content().string(containsString(foto1.getDateiname())))
         .andExpect(content().string(containsString(foto1.getOrt())))

         .andExpect(content().string(containsString(klist1.get(0).getAutor())))
         .andExpect(content().string(containsString(klist1.get(0).getText())))
         .andExpect(content().string(containsString(klist1.get(1).getAutor())))
         .andExpect(content().string(containsString(klist1.get(1).getText())))

         .andExpect(content().string(not(containsStringIgnoringCase("<form"))))
         .andExpect(content().string(not(containsStringIgnoringCase("<input"))));

    }
    

    @Test
	@DisplayName("POST /foto/{id}/kommentar legt für eingeloggten Benutzer Kommentar für Foto id an")
    @Transactional
	void post_foto_id_kommentar() throws Exception {
		// Sessionattribut vorbereiten
        final String kommentaruser = "jbiff017";
		Map<String, Object> sessiondata = new HashMap<>();
		sessiondata.put("loggedinusername", kommentaruser);
        
        List<Foto> fotoliste = initDB();
        allefotoskommentieren(fotoliste);

        final int fotonr = 2;
        Foto foto2 = fotoliste.get(fotonr);
        long fotoid = foto2.getId();
        int nKommentare = foto2.getKommentare().size();
        String kommentar = "Ich erkenne viele Dinge auf "+foto2.getDateiname();
        final String urlpfad = "/foto/"+fotoid+"/kommentar";
        
		// Eintrag anlegen, sollte abschließend wieder auf /foto/../kommentar redirecten
		mockmvc.perform(
            post(urlpfad)
			.param("kommentar", kommentar)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.sessionAttrs(sessiondata)
            ).andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", urlpfad));
            
        // Foto in DB sollte nun einen Kommentar mehr haben
        Foto f = fotorepo.getOne(fotoid);
        assertThat(f.getKommentare().size()).isEqualTo(nKommentare + 1);

        Kommentar k = f.getKommentare().get(nKommentare);
        assertThat(k.getAutor()).isEqualTo(kommentaruser);
        assertThat(k.getText()).isEqualTo(kommentar);

		 
        // und gucken, ob Eintrag in der HTML-Seite auftaucht
		mockmvc.perform(
			get(urlpfad)
			.sessionAttrs(sessiondata)
		)
		.andExpect(content().string(containsString(kommentaruser)))
		.andExpect(content().string(containsString(kommentar)));
	}


    @Test
	@DisplayName("POST /foto/{id}/kommentar ohne eingeloggten Benutzer tut nichts")
    @Transactional
	void post_foto_id_kommentar_nicht_eingeloggt() throws Exception {
        List<Foto> fotoliste = initDB();
        allefotoskommentieren(fotoliste);

        final int fotonr = 2;
        Foto foto2 = fotoliste.get(fotonr);
        long fotoid = foto2.getId();
        long nKommentare = foto2.getKommentare().size();
        String kommentar = "Ich erhüpfe manche Sachen auf "+foto2.getDateiname();
        final String urlpfad = "/foto/"+fotoid+"/kommentar";
        
		// Eintrag anlegen, sollte abschließend wieder auf /foto/../kommentar redirecten
		mockmvc.perform(
            post(urlpfad)
			.param("kommentar", kommentar)
			.contentType(MediaType.MULTIPART_FORM_DATA)
            ).andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", urlpfad));
            
        // Foto sollte nun *keinen* Kommentar mehr haben
        assertThat(fotorepo.getOne(fotoid).getKommentare().size()).isEqualTo(nKommentare);
		 
        // und gucken, ob Eintrag in der HTML-Seite auftaucht (darf er nicht)
		mockmvc.perform(
			get(urlpfad)
		)
		.andExpect(content().string(not(containsString(kommentar))));
	}


}