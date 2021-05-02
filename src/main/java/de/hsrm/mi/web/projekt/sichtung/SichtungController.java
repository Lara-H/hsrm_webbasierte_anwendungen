/**
 * Controller für Sichtungen
 */

package de.hsrm.mi.web.projekt.sichtung;

import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@SessionAttributes(names = { "meinesichtungen" })
public class SichtungController {
    Logger logger = LoggerFactory.getLogger(SichtungController.class);
    ArrayList<Sichtung> listSichtungen = new ArrayList<Sichtung>();
    int idCount = 1;

    @GetMapping("/sichtung/meine")
    public String showSichtungen(Model m) {
        // initListe(m);
        m.addAttribute("meinesichtungen", listSichtungen);
        return "liste";
    }

    @GetMapping("/sichtung/meine/neu")
    public String newSichtung(Model m) {
        Sichtung sichtung = new Sichtung();
        m.addAttribute("meinesichtungform", sichtung);
        return "bearbeiten";
    }

    @PostMapping("/sichtung/meine/neu")
    public RedirectView submitForm(@ModelAttribute("minesichtungen") Sichtung sichtung, Model m) {
        RedirectView redirectView = new RedirectView();
        logger.info("Name = {}", sichtung.getName());
        if (sichtung.getName() != "") {
            sichtung.setId(idCount);
            listSichtungen.add(sichtung);
            m.addAttribute("meinesichtungform", listSichtungen);
            idCount++;
            redirectView.setUrl("/sichtung/meine");
        } else {
            m.addAttribute("emptyEntry", "Bitte geben Sie einen Namen ein.");
            redirectView.setUrl("/sichtung/meine/neu");
        }
        return redirectView;
    }

    @GetMapping("/sichtung/meine/{id}/del")
    public String delete(Model m, @PathVariable int id) {
        logger.info("Element gelöscht");
        for (Sichtung sichtung : listSichtungen) {
            if (sichtung.getId() == id) {
                listSichtungen.remove(sichtung);
                break;
            }
        }
        m.addAttribute("meinesichtungform", listSichtungen);
        return "delete";
    }

    @PostMapping("/sichtung/meine/{id}/del")
    public void delete() {
        logger.info("Element hoffentlich gelöscht");
    }

    @GetMapping("/sichtung/meine/{id}")
    public String edit() {
        return "edit";
    }

    // public void initListe(Model m) {
    // Sichtung ersteSichtung = new Sichtung("Name1", "Wiesbaden", "Beschreibung1");
    // Sichtung zweiteSichtung = new Sichtung("Name2", "Mainz", "Beschreibung2");
    // Sichtung dritteSichtung = new Sichtung("Name3", "Berlin", "Beschreibung3");
    // listSichtungen.add(ersteSichtung);
    // listSichtungen.add(zweiteSichtung);
    // listSichtungen.add(dritteSichtung);
    // }

}