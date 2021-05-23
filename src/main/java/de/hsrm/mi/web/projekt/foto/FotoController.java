package de.hsrm.mi.web.projekt.foto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@SessionAttributes(names = { "loggedinusername" }) // Liste mit Benutzernamen

public class FotoController {
    @Autowired
    FotoService fotoservice;
    Logger logger = LoggerFactory.getLogger(FotoController.class);

    @ModelAttribute("fotos")
    public void initFotoListe(Model m){
        List<Foto> listFotos = new ArrayList<Foto>();
        m.addAttribute("fotos", listFotos);
    }
    @ModelAttribute("kommentare")
    public void initKommentarListe(Model m){
        List<Kommentar> listKommentare = new ArrayList<Kommentar>();
        m.addAttribute("kommentare", listKommentare);
    }
    
    @PostMapping("/foto")
    public String postFoto(@RequestParam("datei") MultipartFile datei, Model m, @ModelAttribute("fotos") List<Foto> fotoListe) {
        try {
            Foto foto = new Foto(datei.getOriginalFilename(), datei.getBytes(), datei.getContentType());
            if (datei.getSize() > 16) {
                fotoservice.fotoAbspeichern(foto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		m.addAttribute("fotos", fotoservice.alleFotosNachZeitstempelSortiert());
        return "foto/liste";
    }

    @GetMapping("/foto")
    public String getFoto(Model m, @ModelAttribute("fotos") List<Foto> fotoListe) {
        m.addAttribute("fotos", fotoservice.alleFotosNachZeitstempelSortiert());
        
        return "foto/liste";
    }

    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> getFotoId(Model m, @PathVariable Long id) {
        Foto foto = fotoservice.fotoAbfragenNachId(id).get();
        return ResponseEntity.ok()
            .header("Content-Type", foto.getMimetype())
            .body(foto.getFotodaten());
    }

    @GetMapping("/foto/{id}/del")
    public String deleteFoto(Model m, @PathVariable Long id) {
        fotoservice.loescheFoto(id);
        return "redirect:/foto";
    }

    @GetMapping("/foto/{id}/kommentar")
    public String getKommentare(Model m, @PathVariable Long id, @ModelAttribute("kommentare") List<Kommentar> kommentare) {
        m.addAttribute("foto", fotoservice.fotoAbfragenNachId(id).get());
        m.addAttribute("kommentare", fotoservice.fotoAbfragenNachId(id).get().getKommentare());
        if (id != null) {
            return "foto/kommentare";
        }
        return "foto/liste";
    }

    @PostMapping("/foto/{id}/kommentar")
    public String postKommentare(Model m, @PathVariable Long id, @ModelAttribute("loggedinusername") String loggedinusername, @ModelAttribute("kommentare") List<Kommentar> kommentare, @RequestParam("kommentarText") String kommentarText) {
        if (!kommentarText.isEmpty()) {
            fotoservice.fotoKommentieren(id, loggedinusername, kommentarText);
        }
        return "redirect:/foto/{id}/kommentar";
    }
    
}