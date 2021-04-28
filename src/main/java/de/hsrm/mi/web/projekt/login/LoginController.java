package de.hsrm.mi.web.projekt.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(names = { "loggedinusername" }) // Liste mit Benutzernamen
public class LoginController {

    /**
     * Bei Seitenaufruf Formular laden
     */
    @GetMapping("/login")
    public String showForm(Model model) {
        User user = new User(); // Neuen Benutzer anlegen
        model.addAttribute("user", user);
        return "login";
    }

    /**
     * Nach Absenden des Formulares sucess-Template laden
     */
    @PostMapping("/login")
    public String submitForm(@ModelAttribute("user") User user, Model m) {
        System.out.println(user);
        if (user.getUsername() != "") { // Wenn Username nicht leer
            String correctPassword = user.getUsername() + user.getUsername().length(); // Korrektes Passwort
            if (user.getPassword().equals(correctPassword)) { // Wenn Passwort richtig
                m.addAttribute("loggedinusername", user.getUsername()); // In SessionAttribut-Liste speichern
                return "login_success"; // Folgeseite
            } else {
                m.addAttribute("loggedinusername", ""); // Leeren String in SessionAttribut-Liste speichern
                m.addAttribute("hilfe", "Hinweis: Das korrekte Passwort für " + user.getUsername() + " ist " + correctPassword + ".");
    
            }
        } 
        return "login";
    }
}