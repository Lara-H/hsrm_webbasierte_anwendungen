package de.hsrm.mi.web.projekt.login;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}