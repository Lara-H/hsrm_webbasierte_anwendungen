package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.foto.AdressService;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class Web_Ueb06_A2_AdressService {
    @Autowired
    private AdressService adressService;

    @Test
    @DisplayName("AdressService: findeAdresse() für UdE")
    public void finde_adresse_ude() throws IOException {
        Optional<String> adropt = adressService.findeAdresse(50.09729, 8.21704);
        assertThat(adropt.isPresent()).isTrue();
        assertThat(adropt.get()).containsIgnoringCase("Hochschule RheinMain, Unter den Eichen");
    }


    @Test
    @DisplayName("AdressService: findeAdresse() für Schloss")
    public void finde_adresse_koninklijk_paleis() throws IOException {
        Optional<String> adropt = adressService.findeAdresse(52.37313, 4.89135);
        assertThat(adropt.isPresent()).isTrue();
        assertThat(adropt.get()).containsIgnoringCase("Koninklijk Paleis, 147, Nieuwezijds Voorburgwal, Centrum, Amsterdam");
    }


    @Test
    @DisplayName("AdressService: findeAdresse() für Vollradisroda")
    public void finde_adresse_vollradisroda() throws IOException {
        Optional<String> adropt = adressService.findeAdresse(50.9154670, 11.4968531);
        assertThat(adropt.isPresent()).isTrue();
        assertThat(adropt.get()).containsIgnoringCase("Vollradisroda, Döbritschen");
    }


    @Test
    @DisplayName("AdressService: findeAdresse() für mitten in der Nordsee sollte fehlschlagen")
    public void finde_adresse_nordsee() throws IOException {
        Optional<String> adropt = adressService.findeAdresse(56.2456, 3.1970);
        assertThat(adropt.isPresent()).isFalse();
    }

    @Test
    @DisplayName("AdressService: findeAdresse() für mitten im Südatlantik sollte fehlschlagen")
    public void finde_adresse_suedsalzwasser() throws IOException {
        Optional<String> adropt = adressService.findeAdresse(-52.7, -9.32);
        assertThat(adropt.isPresent()).isFalse();
    }
}