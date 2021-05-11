package de.hsrm.mi.web.projekt.international;

import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.hibernate.validator.spi.messageinterpolation.LocaleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class InternationalController {
    Logger logger = LoggerFactory.getLogger(InternationalController.class);

    // Sagt Templates, wo Übersetzungsdateien liegen
    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("static/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    // Was tut das?
    @GetMapping("/international")
    public String getInternational(Locale locale, Model m) { // "de" "en"
        m.addAttribute("sprache", locale.getDisplayLanguage()); // "Deutsch" "Englisch"
        var numformat = NumberFormat.getInstance(locale);
        var zahl = numformat.format(1234567891); // "1.234.567.891" "1,234,567,891"
        m.addAttribute("zahl", zahl);
        return "international/international";
    }

    // Sprache erkennen
    @GetMapping("/")
    public String viewHomePage(Model m, HttpServletRequest request) {
        m.addAttribute("pageTitle", "Test");
        Locale currentLocale = request.getLocale();
        String countryCode = currentLocale.getCountry();
        String countryName = currentLocale.getDisplayCountry();
        String langCode = currentLocale.getLanguage();
        String langName = currentLocale.getDisplayLanguage();
        logger.info("Land = {}", countryCode + ": " + countryName);
        logger.info("Sprache = {}", langCode + ": " + langName);
        return "index";
    }

}