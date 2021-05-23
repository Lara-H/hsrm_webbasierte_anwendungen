package de.hsrm.mi.web.projekt.foto.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoService;
import de.hsrm.mi.web.projekt.foto.Kommentar;
import javaxt.sql.Model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/rest")
public class FotoRestController {
    @Autowired
    FotoService fotoservice;
    Logger logger = LoggerFactory.getLogger(FotoRestController.class);

    @GetMapping("/api/foto")
    public List<Foto> getFoto(Model m) {
        return fotoservice.alleFotosNachZeitstempelSortiert();
    }

    @DeleteMapping("/api/foto/{id}")
    public void deleteFoto(Model m) {
    }

    @GetMapping("/api/foto/{id}/kommentar")
    public List<Kommentar> getKommentar(Model m) {
        return null;
    }

    @DeleteMapping("/api/foto/{id}/kommentar/{kid}")
    public void deleteKommentar(Model m) {
    }

}