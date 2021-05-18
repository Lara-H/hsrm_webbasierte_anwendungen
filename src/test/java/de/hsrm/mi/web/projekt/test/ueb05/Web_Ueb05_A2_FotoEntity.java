package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.foto.Foto;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class Web_Ueb05_A2_FotoEntity {
    @Autowired
    Validator validator;

    @Test
    @DisplayName("Foto-Entity instanziierbar und initialisiert")
    public void foto_ok() {
        // Foto-Entität existiert und initialisiert sich ordentlich
        Foto foto = new Foto();
        assertThat(Foto.class).hasAnnotation(Entity.class);
        assertThat(foto.getMimetype()).isEqualTo("");
        assertThat(foto.getDateiname()).isEqualTo("");
        assertThat(foto.getOrt()).isEqualTo("");
        assertThat(foto.getZeitstempel()).isEqualTo(LocalDateTime.MIN);
        assertThat(foto.getGeolaenge()).isEqualTo(0f);
        assertThat(foto.getGeobreite()).isEqualTo(0f);
        assertThat(foto.getFotodaten()).isNull();
    }
}