package com.example.demo.controllers;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api")
public class OpenApiController {

    @GetMapping("/docs")
    public String showApiDocs() {
        // Здесь вы можете вернуть любое представление (например, HTML) с ссылкой на документацию OpenAPI
        // или просто перенаправить на адрес, где отображается документация OpenAPI
        return "redirect:/swagger-ui.html";
    }
}
