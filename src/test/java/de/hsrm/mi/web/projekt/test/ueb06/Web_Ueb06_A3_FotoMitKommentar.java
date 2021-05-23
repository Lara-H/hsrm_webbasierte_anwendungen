package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;

import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoRepository;
import de.hsrm.mi.web.projekt.foto.Kommentar;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Web_Ueb06_A3_FotoMitKommentar {
    final String TESTMIMETYPE = "image/jpg";
    final String TESTDATEINAME = "testbild-1-20110828.jpg";
    final String TESTORT = "irgendwo";
    final LocalDateTime TESTZEITSTEMPEL = LocalDateTime.now();
    final double TESTGEOLAENGE = 17f;
    final double TESTGEOBREITE = 4f;

    @Autowired
    private FotoRepository fotorepo;

    @Test
    public void vorabcheck() {
        assertThat(FotoRepository.class).isInterface();
        assertThat(fotorepo).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("Foto-Entity mit Kommentar im Repository persistieren")
    public void foto_mit_kommentar_save() throws IOException {
        List<Kommentar> kchecklist = new ArrayList<>();
        
        final Foto unmanagedfoto = new Foto();
        unmanagedfoto.setMimetype(TESTMIMETYPE);
        unmanagedfoto.setDateiname(TESTDATEINAME);
        unmanagedfoto.setOrt(TESTORT);
        unmanagedfoto.setZeitstempel(TESTZEITSTEMPEL);

        // Kommentare an Foto hängen
        for (var s: List.of("ding", "dong", "dudel")) {
            Kommentar k = new Kommentar();
            k.setAutor(s+"bert");
            k.setText("Ich finde den "+s+" interessant");
            k.setZeitpunkt(LocalDateTime.now());
            kchecklist.add(k);
            unmanagedfoto.getKommentare().add(k);
        }

        // Repository leeren
        fotorepo.deleteAll();
        assertThat(fotorepo.count()).isEqualTo(0);

        // Foto in Repository speichern
        final Foto managedfoto = fotorepo.save(unmanagedfoto);
        long gespeichertId = managedfoto.getId();

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(fotorepo.count()).isEqualTo(1);

        // und wieder frisch aus DB auslesen
        final Foto neugelesen = fotorepo.getOne(gespeichertId);
        assertThat(neugelesen.getDateiname()).isEqualTo(TESTDATEINAME);
        assertThat(neugelesen.getMimetype()).isEqualTo(TESTMIMETYPE);
        assertThat(neugelesen.getOrt()).isEqualTo(TESTORT);
        assertThat(neugelesen.getZeitstempel()).isEqualTo(TESTZEITSTEMPEL);

        // Kommentarliste sollte mit persistiert und daher (ggf. lazy) abrufbar sein
        assertThat(neugelesen.getKommentare().size()).isEqualTo(kchecklist.size());
        var nklist = neugelesen.getKommentare();
        for (int i=0; i < nklist.size(); i++) {
            var kn = nklist.get(i);
            var kc = kchecklist.get(i);
            assertThat(kn.getAutor()).isEqualTo(kc.getAutor());
            assertThat(kn.getText()).isEqualTo(kc.getText());
            assertThat(kn.getZeitpunkt()).isEqualTo(kc.getZeitpunkt());
            assertThat(kn.getId()).isNotEqualTo(0);
        }
    }

}