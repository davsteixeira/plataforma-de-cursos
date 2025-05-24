package br.com.PlataformaDeCursos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String home() {
        return """
                <h1>Plataforma de Cursos</h1>
                <p>Bem-vinda à plataforma de cursos!</p>
            """;
    }
}
