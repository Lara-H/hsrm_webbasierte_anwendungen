package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;

import de.hsrm.mi.web.projekt.foto.Kommentar;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Web_Ueb06_A3_KommentarEntity {

    @Test
    @DisplayName("Kommentar-Entity instanziierbar und initialisiert")
    public void kommentar_eigenschaften_dahanne() {
        // Kommentar-Entität existiert und initialisiert sich ordentlich
        Kommentar k = new Kommentar();
        assertThat(Kommentar.class).hasAnnotation(Entity.class);
        assertThat(k.getAutor()).isEqualTo("");
        assertThat(k.getText()).isEqualTo("");
        assertThat(k.getZeitpunkt().isAfter(LocalDateTime.now().minusSeconds(17))).isTrue();
    }

    @Test
    @DisplayName("Kommentar-Entity Gleichheit")
    public void kommentar_gleichheit() {
        Kommentar k1 = new Kommentar();
        k1.setAutor("Jöndhard");
        k1.setText("Biffel");
        k1.setZeitpunkt(LocalDateTime.now());
        k1.setId(17);

        Kommentar k2 = new Kommentar();
        k2.setAutor("Friedfert");
        k2.setText("von Senkel");
        k2.setZeitpunkt(LocalDateTime.now().plusHours(1));
        k2.setId(17);

        assertThat(k1).isEqualTo(k2);
        assertThat(k1.hashCode()).isEqualTo(k2.hashCode());
    }

    @Test
    @DisplayName("Kommentar-Entity Ungleichheit")
    public void kommentar_ungleichheit() {
        LocalDateTime jetzt = LocalDateTime.now();

        Kommentar k1 = new Kommentar();
        k1.setAutor("Jöndhard");
        k1.setText("Biffel");
        k1.setZeitpunkt(jetzt);
        k1.setId(17);

        Kommentar k2 = new Kommentar();
        k2.setAutor("Jöndhard");
        k2.setText("Biffel");
        k2.setZeitpunkt(jetzt);
        k2.setId(42);

        assertThat(k1).isNotEqualTo(k2);
    }
}