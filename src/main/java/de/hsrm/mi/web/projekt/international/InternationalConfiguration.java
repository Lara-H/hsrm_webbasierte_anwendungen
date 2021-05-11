package de.hsrm.mi.web.projekt.international;

import org.hibernate.validator.spi.messageinterpolation.LocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


// @Configuration
// public class InternationalConfiguration implements WebMvcConfigurer {
//     @Bean
//     public LocaleResolver localeResolver() {
//         SessionLocaleResolver resolver = new SessionLocaleResolver();
//         // Optional: Browser-Präferenz übersteuern
//         // resolver.setDefaultLocale(Locale.GERMANY);
//         return (LocaleResolver) resolver;
//     }

//     @Bean
//     public LocaleChangeInterceptor localeChangeInterceptor() {
//         LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
//         interceptor.setParamName("sprache");
//         return interceptor;
//     }

//     @Override
//     public void addInterceptors(InterceptorRegistry registry) {
//         registry.addInterceptor(localeChangeInterceptor());
//     }
// }