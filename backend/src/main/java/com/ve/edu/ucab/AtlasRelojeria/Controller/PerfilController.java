package com.ve.edu.ucab.AtlasRelojeria.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ve.edu.ucab.AtlasRelojeria.Model.Perfil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/perfiles")
@CrossOrigin(origins = "http://localhost:8080")
public class PerfilController {

    private static final Path RUTA_JSON = Paths.get("src/main/resources/perfiles.json");
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<List<Perfil>> obtenerPerfiles() throws IOException {
        List<Perfil> perfiles = mapper.readValue(Files.readString(RUTA_JSON), new TypeReference<List<Perfil>>() {});
        return ResponseEntity.ok(perfiles);
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearPerfil(@RequestBody Perfil nuevo) throws IOException {
        List<Perfil> perfiles = mapper.readValue(Files.readString(RUTA_JSON), new TypeReference<List<Perfil>>() {});
        perfiles.add(nuevo);
        Files.writeString(RUTA_JSON, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(perfiles));
        return ResponseEntity.ok("Perfil creado exitosamente");
    }

    @DeleteMapping("/eliminar/{nombre}")
    public ResponseEntity<String> eliminarPerfil(@PathVariable String nombre) throws IOException {
        List<Perfil> perfiles = mapper.readValue(Files.readString(RUTA_JSON), new TypeReference<List<Perfil>>() {});
        List<Perfil> actualizados = perfiles.stream()
            .filter(p -> !p.getNombre().equalsIgnoreCase(nombre))
            .toList();
        Files.writeString(RUTA_JSON, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualizados));
        return ResponseEntity.ok("Perfil eliminado exitosamente");
    }

    @PostMapping("/login")
public ResponseEntity<Map<String, Object>> login(@RequestBody Perfil datos) throws IOException {

    List<Perfil> perfiles = mapper.readValue(Files.readString(RUTA_JSON), new TypeReference<List<Perfil>>() {});

    Optional<Perfil> coincidencia = perfiles.stream()
        .filter(p -> p.getId().equalsIgnoreCase(datos.getId()) &&
                     p.getContrasena().equals(datos.getContrasena()))
        .findFirst();

    if (coincidencia.isPresent()) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Login correcto");
        respuesta.put("esAdministrador", coincidencia.get().isEsAdministrador());
        respuesta.put("usuario", coincidencia.get());
        return ResponseEntity.ok(respuesta);
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("mensaje", "Credenciales inválidas"));
}


} 