/**
 * Controller für Sichtungen
 */

package de.hsrm.mi.web.projekt.sichtung;

import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@SessionAttributes(names = { "meinesichtungen" })
public class SichtungController {
    ArrayList<Sichtung> listSichtungen = new ArrayList<Sichtung>();
    int idCount = 1;

    @GetMapping("/sichtung/meine")
    public String showSichtungen(Model m) {
        //initListe(m);
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
        sichtung.setId(idCount);
        listSichtungen.add(sichtung);
        m.addAttribute("meinesichtungform", listSichtungen);
        idCount++;
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/sichtung/meine");
        return redirectView;   
    }

    // public void initListe(Model m) {
    //     Sichtung ersteSichtung = new Sichtung("Name1", "Wiesbaden", "Beschreibung1");
    //     Sichtung zweiteSichtung = new Sichtung("Name2", "Mainz", "Beschreibung2");
    //     Sichtung dritteSichtung = new Sichtung("Name3", "Berlin", "Beschreibung3");
    //     listSichtungen.add(ersteSichtung);
    //     listSichtungen.add(zweiteSichtung);
    //     listSichtungen.add(dritteSichtung); 
    // }



}