package de.hsrm.mi.web.projekt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.hsrm.mi.web.projekt.jwt.JwtAuthorizationFilter;

/**
 * Zweigeteilte Security-Konfiguration für
 * 
 * - Prio 1: (REST-)API, explizit "stateless", um Konflikte mit Session-Cookies 
 *   aus eventuell im Browser parallel laufender WebMVC-Session zu vermeiden, 
 *   und ohne (bei REST-APIs störende) CSRF-Sicherung
 * 
 * - Prio 2: WebMVC-UI, Standard-Konfiguration mit Sessions und CSRF-Schutz
 */
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Prio 1: Konfiguration für REST-API, Endpunkte unter /api/**, stateless
     */
    @Configuration
    @Order(1)
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        JwtAuthorizationFilter jwtAuthorizationFilter;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // http.antMatcher() (Matcher, nicht Matcher*s*) beschränkt diese Konfiguration 
            // ausschließlich auf Pfade unterhalb /api
            http.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/foto/*").permitAll() // wenn man das per Token schützt, geht <img src="/api/foto/{id}"> im Vue-Template nicht mehr - dann wird's komplizert, Image müsste per JS-fetch mit Token geholt und eingebaut werden
                .antMatchers(HttpMethod.POST, "/api/foto").hasRole("PHOTOGRAPH")
                .antMatchers(HttpMethod.DELETE, "/api/foto/*").hasRole("PHOTOGRAPH")
                .antMatchers("/api/**").hasAnyRole("GUCKER", "PHOTOGRAPH")
                .anyRequest().denyAll()
            .and()
                // keine Security-Sessions für zustandsloses REST-APIs (anders als bei z.B. WebMVC)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                // eigenen jwtAuthorizationFilter (s.o.) in Spring-Filterkette 
                // (z.B.) vor Standardfilter UsernamePasswordAuthenticationFilter einfügen
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                // CSRF-Schutz-Tokens stören für REST-APIs
                .csrf().disable()
            ;
        }

    }

    /**
     * Prio 2: Konfiguration für übrige Aspekte und insb. WebMVC-Anwendung
     */
    @Order(2)
    @Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private FotoUserDetailsService fotoUserDetailsService;

        @Bean
        PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }    

        @Override
        protected void configure(AuthenticationManagerBuilder authmanagerbuilder) throws Exception {
            var pwenc = passwordEncoder();

            authmanagerbuilder.inMemoryAuthentication()
                .withUser("friedfert").password(pwenc.encode("dingdong")).roles("GUCKER")
            .and()
                .withUser("joghurta").password(pwenc.encode("geheim123")).roles("PHOTOGRAPH");

            authmanagerbuilder.userDetailsService(fotoUserDetailsService).passwordEncoder(pwenc);
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests()
                    // Error-Seite geht immer
                    .antMatchers("/error").permitAll()

                    // der Messagebroker-Endpunkt gehört eigentlich oben zu den
                    // Frontend/API-Endpunkten, da er aber (leider) nicht unter
                    // /api aufgehängt ist (hätte ich so machen sollen...),
                    // wird er von antMatcher("/api/**") oben nicht erfasst.
                    // Also kommt er hier rein - bevor alle ihre FE-Pfade ändern müssen
                    .antMatchers("/messagebroker").permitAll()

                    // Nur Rolle PHOTOGRAPH darf Fotos Posten und löschen
                    .antMatchers("/foto/*/del").hasRole("PHOTOGRAPH")
                    .antMatchers(HttpMethod.POST, "/foto").hasRole("PHOTOGRAPH")
                    
                    // zum Entwickeln
                    .antMatchers("/h2-console/**").permitAll()

                    // alle oben nicht explizit behandelte Anfragen müssen authentifiziert sein.
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    //.loginPage("/login") // falls eigenes Login-Formular
                    .defaultSuccessUrl("/foto")
                    .permitAll()
                .and()
                    .csrf()
                    // CSRF nur selektiv ausschalten, z.B. für H2-Konsole
                    .ignoringAntMatchers("/h2-console/**")
                ;
                http.headers().frameOptions().disable();
        }

        /*
         * "AuthenticationManager" über Factory-Methode @Autowired'bar machen,
         * wird z.B. in JwtLoginController verwendet;
         * also in Oberklasse abrufbaren Auth.Manager als injizierbare 
         * @Bean auch anderen Komponenten verfügbar machen.
         */
        @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    }
}





// package de.hsrm.mi.web.projekt.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.crypto.factory.PasswordEncoderFactories;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter {
//     @Autowired UserDetailService UserDetailService;
//     @Bean PasswordEncoder passwordEncoder() { // @Bean -> Encoder woanders per @Autowired abrufbar
//         return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//     }
//     protected void configure(AuthenticationManagerBuilder authmanagerbuilder) throws Exception {
//         PasswordEncoder pwenc = passwordEncoder(); // Injection "in sich selbst" geht leider nicht
//         authmanagerbuilder.inMemoryAuthentication() // "in memory"-Benutzerdatenbank anlegen
//         .withUser("friedfert")
//         .password(pwenc.encode("dingdong")) // Passwörter nicht im Klartext speichern
//         .roles("GUCKER")
//         .and() // nächster Eintrag
//         .withUser("joghurta")
//         .password(pwenc.encode("geheim123"))
//         .roles("PHOTOGRAPH");
//         authmanagerbuilder.userDetailsService(UserDetailService)
//         .passwordEncoder(passwordEncoder());
//     } 

//     protected void configure(HttpSecurity http) throws Exception {
//         http.authorizeRequests()
//             .antMatchers("/api/**").permitAll()
//             .antMatchers("/messageboker").permitAll()
//             .antMatchers("/h2-console/**").permitAll()
//             .antMatchers("/css/*").permitAll()
//             .antMatchers("/register", "/logout").permitAll()
//             .antMatchers(HttpMethod.GET, "/foto/{id}/del").hasRole("PHOTOGRAPH")
//             .antMatchers(HttpMethod.DELETE).hasRole("PHOTOGRAPH")
//             .antMatchers(HttpMethod.POST, "/foto").hasRole("PHOTOGRAPH")
//             .antMatchers("/user*", "/user/*").authenticated()
//             .anyRequest().hasAnyRole("GUCKER", "PHOTOGRAPH")
//         .and()
//             .formLogin()
//             //.loginPage("/login") // falls eigenes Login-Formular
//             .defaultSuccessUrl("/foto")
//             .permitAll();
//             http.csrf()
//             .ignoringAntMatchers("/h2-console/**")
//             .ignoringAntMatchers("/api/**");
//             http.headers().frameOptions().disable();
//     }
// }



