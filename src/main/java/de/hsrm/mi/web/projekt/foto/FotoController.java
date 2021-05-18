package de.hsrm.mi.web.projekt.foto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class FotoController {
    @Autowired
    FotoService fotoservice;
    Logger logger = LoggerFactory.getLogger(FotoController.class);

    @ModelAttribute("fotos")
    public void initListe(Model m){
        m.addAttribute("fotos", new ArrayList<Foto>());
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
    
}