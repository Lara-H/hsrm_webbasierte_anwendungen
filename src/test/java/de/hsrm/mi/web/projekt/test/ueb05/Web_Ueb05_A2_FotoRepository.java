package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoRepository;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class Web_Ueb05_A2_FotoRepository {
    final String TESTMIMETYPE = "image/jpg";
    final String TESTDATEINAME = "testbild-1-20110828.jpg";
    final String TESTORT = "irgendwo";
    final LocalDateTime TESTZEITSTEMPEL = LocalDateTime.now();
    final double TESTGEOLAENGE = 17f;
    final double TESTGEOBREITE = 4f;

    @Autowired
    private FotoRepository fotorepo;

    public byte[] readTestFotoBytes(String dateiname) throws IOException {
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

    @Test
    @Transactional
    @DisplayName("Foto-Entity im Repository persistieren")
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
        final Foto managedfoto = fotorepo.save(unmanagedfoto);
        long gespeichertId = managedfoto.getId();

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(fotorepo.count()).isEqualTo(1);

        // und wieder frisch aus DB auslesen
        Foto neugelesen = fotorepo.getOne(gespeichertId);
        assertThat(neugelesen.getDateiname()).isEqualTo(TESTDATEINAME);
        assertThat(neugelesen.getMimetype()).isEqualTo(TESTMIMETYPE);
        assertThat(neugelesen.getOrt()).isEqualTo(TESTORT);
        assertThat(neugelesen.getZeitstempel()).isEqualTo(TESTZEITSTEMPEL);
        assertThat(neugelesen.getFotodaten()).isEqualTo(readTestFotoBytes(TESTDATEINAME));
    }

}