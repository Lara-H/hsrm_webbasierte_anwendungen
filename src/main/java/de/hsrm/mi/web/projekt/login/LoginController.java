package de.hsrm.mi.web.projekt.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@SessionAttributes(names = { "loggedinusername" }) // Liste mit Benutzernamen
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String showForm(Model m) {
        User user = new User(); // Neuen User anlegen
        m.addAttribute("user", user);
        return "login";
    }

    @PostMapping("/login")
    public String submitForm(@ModelAttribute("user") User user, Model m) {
        if (user.getUsername() != "") { // Wenn Username nicht leer
            String correctPassword = user.getUsername() + user.getUsername().length(); // Korrektes Passwort
            logger.info("Richtiges Passwort = {}", correctPassword);
            if (user.getPassword().equals(correctPassword)) { // Wenn Passwort richtig
                m.addAttribute("loggedinusername", user.getUsername()); // In SessionAttribut-Liste speichern
                return "redirect:/sichtung/meine";
            } else {
                m.addAttribute("loggedinusername", ""); // Leeren String in SessionAttribut-Liste speichern
                m.addAttribute("wrongPassword", "{login.wrongPassword} " + user.getUsername() + ": " + correctPassword + ".");
            }
        } 
        return "login"; 
    }
}