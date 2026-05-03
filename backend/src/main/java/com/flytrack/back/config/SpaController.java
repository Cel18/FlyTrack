package com.flytrack.back.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * SPA Fallback Controller — FlyTrack
 *
 * Redirige cualquier ruta que NO sea una ruta de la API (/api/**)
 * al index.html de Angular, permitiendo que el enrutamiento
 * del lado del cliente funcione correctamente.
 */
@RestController
public class SpaController {

    private static final String INDEX_HTML = "static/index.html";

    /**
     * Captura todas las rutas que no corresponden a la API,
     * a Swagger ni a recursos estáticos con extensión conocida,
     * y devuelve el index.html de Angular.
     */
    @GetMapping(value = {
            "/",
            "/{path:[^\\.]*}",
            "/{path:^(?!api|swagger-ui|v3).*$}/**/{subPath:[^\\.]*}"
    })
    public ResponseEntity<Resource> forward(
            @org.springframework.web.bind.annotation.PathVariable(required = false) String path,
            @org.springframework.web.bind.annotation.PathVariable(required = false) String subPath,
            HttpServletRequest request) throws IOException {
        Resource resource = new ClassPathResource(INDEX_HTML);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }
}
