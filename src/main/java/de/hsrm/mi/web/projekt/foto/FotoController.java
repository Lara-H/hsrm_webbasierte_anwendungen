// package de.hsrm.mi.web.projekt.foto;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.ui.Model;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// @Controller
// public class FotoController {
//     Logger logger = LoggerFactory.getLogger(FotoController.class);

//     @PostMapping("/foto")
//     public String postFoto(Model m) {
//         return "foto/list";
//     }

//     @GetMapping("/foto")
//     public String getFoto(Model m) {
//         return "foto/list";
//     }

//     @GetMapping("/foto/{id}")
//     public String getFotoId(Model m) {
//         return null;
//     }

//     @GetMapping("/foto/{id}/del")
//     public String deleteFoto(Model m) {
//         return "foto/list";
//     }
    
// }