package de.hsrm.mi.web.projekt.sichtung;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@SessionAttributes(names = { "meinesichtungen" })
public class SichtungController {
    Logger logger = LoggerFactory.getLogger(SichtungController.class);

    // Wird automatisch aufgerufen zum Anlegen der Liste
    @ModelAttribute("meinesichtungen")
    public void initListe(Model m) {
        List<Sichtung> listSichtungen = new ArrayList<Sichtung>();
        listSichtungen.add(new Sichtung("Erste Sichtung", "Wiesbaden", "Erste Beschreibung"));
        listSichtungen.add(new Sichtung("Zweite Sichtung", "Mainz", "Zweite Beschreibung"));
        listSichtungen.add(new Sichtung("Dritte Sichtung", "Frankfurt", "Dritte Beschreibung"));
        m.addAttribute("meinesichtungen", listSichtungen);
    }

    @GetMapping("/sichtung/meine")
    public String showSichtungen(Model m) {
        return "sichtung/liste";
    }

    @GetMapping("/sichtung/meine/neu")
    public String newSichtung(Model m) {
        m.addAttribute("meinesichtungform", new Sichtung());
        return "sichtung/meine/bearbeiten";
    }

    @PostMapping("/sichtung/meine/neu")
    public String submitForm(Model m, @Valid @ModelAttribute("meinesichtungform") Sichtung sichtung, BindingResult sichtungErrors,
    @ModelAttribute("meinesichtungen") List<Sichtung> listSichtungen) {
        if (sichtungErrors.hasErrors()) {
            return "sichtung/meine/bearbeiten";
        }
        listSichtungen.add(sichtung);
        m.addAttribute("meinesichtungform", listSichtungen);
        return "redirect:/sichtung/meine";
    }

    @GetMapping("/sichtung/meine/{nr}/del")
    public String delete(Model m, @PathVariable int nr,
            @ModelAttribute("meinesichtungen") List<Sichtung> listSichtungen) {
        logger.info("Element gel??scht");
        listSichtungen.remove(nr);
        return "redirect:/sichtung/meine";
    }

    @GetMapping("/sichtung/meine/{nr}")
    public String edit(Model m, @PathVariable int nr,
            @ModelAttribute("meinesichtungen") List<Sichtung> listSichtungen) {
        m.addAttribute("meinesichtungform", listSichtungen.get(nr));
        listSichtungen.remove(nr);
        return "sichtung/meine/bearbeiten";
    }

}