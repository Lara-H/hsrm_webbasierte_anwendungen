package de.hsrm.mi.web.projekt.sichtung;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(names = { "meinesichtungen" }) // Liste mit Sichtungen
public class SichtungController {

    /**
     * Bei Seitenaufruf Formular laden
     */
    @GetMapping("/sichtung/meine")
    public String showSichtungen(Model m) {
        ArrayList<Sichtung> listSichtungen = new ArrayList<Sichtung>();
        Sichtung ersteSichtung = new Sichtung("Name1", "Wiesbaden", "Beschreibung1");
        Sichtung zweiteSichtung = new Sichtung("Name2", "Mainz", "Beschreibung2");
        Sichtung dritteSichtung = new Sichtung("Name3", "Berlin", "Beschreibung3");
        listSichtungen.add(ersteSichtung);
        listSichtungen.add(zweiteSichtung);
        listSichtungen.add(dritteSichtung);
        m.addAttribute("listSichtungen", listSichtungen);
        return "liste";
    }

    /**
     * Liste mit Sichtungen
     */
    public void initListe() {
        
    }
    
}