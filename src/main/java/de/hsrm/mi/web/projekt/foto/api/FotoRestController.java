package de.hsrm.mi.web.projekt.foto.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.hsrm.mi.web.projekt.foto.Foto;
import de.hsrm.mi.web.projekt.foto.FotoService;
import de.hsrm.mi.web.projekt.foto.Kommentar;
import javaxt.json.JSONException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/foto")
public class FotoRestController {
    Logger logger = LoggerFactory.getLogger(FotoRestController.class);

    @Autowired
    FotoService fotoservice;

    @GetMapping(value="",produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Foto> getFoto() throws JSONException {
        return fotoservice.alleFotosNachZeitstempelSortiert();
    }

    @DeleteMapping("/{id}")
    public void deleteFoto(@PathVariable long id) throws JSONException {
        fotoservice.loescheFoto(id);
    }

    @GetMapping(value="/{id}/kommentar", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Kommentar> getKommentar(@PathVariable long id) throws JSONException {
        return fotoservice.alleKommentareFuerFoto(id);
    }

    @DeleteMapping("/{id}/kommentar/{kid}")
    public void deleteKommentar(@PathVariable long id, @PathVariable long kid) throws JSONException {
        fotoservice.fotoKommentarLoeschen(id, kid);
    }

    @GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Foto getFotoID(@PathVariable long id) throws JSONException{
        return fotoservice.fotoAbfragenNachId(id).get();
    }

}