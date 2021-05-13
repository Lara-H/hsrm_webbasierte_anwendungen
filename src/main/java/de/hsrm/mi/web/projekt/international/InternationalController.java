package de.hsrm.mi.web.projekt.international;

import java.text.NumberFormat;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class InternationalController {
    Logger logger = LoggerFactory.getLogger(InternationalController.class);

    // Änderung der Sprache
    @GetMapping("/international")
    public String getInternational(Locale locale, Model m) { // "de" "en"
        m.addAttribute("sprache", locale.getDisplayLanguage()); // "Deutsch" "Englisch"
        var numformat = NumberFormat.getInstance(locale);
        var zahl = numformat.format(1234567891); // "1.234.567.891" "1,234,567,891"
        m.addAttribute("zahl", zahl);
        return "international/international";
    }

}