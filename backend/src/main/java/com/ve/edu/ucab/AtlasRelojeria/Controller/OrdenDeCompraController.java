package com.ve.edu.ucab.AtlasRelojeria.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ve.edu.ucab.AtlasRelojeria.Model.OrdenDeCompra;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "http://localhost:8080")
public class OrdenDeCompraController {

    private static final Path RUTA_ORDENES = Paths.get("src/main/resources/ordenes.json");
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @PostMapping
    public ResponseEntity<String> registrarOrden(@RequestBody OrdenDeCompra nuevaOrden) throws IOException {
        List<OrdenDeCompra> ordenes = Files.exists(RUTA_ORDENES)
                ? mapper.readValue(Files.readString(RUTA_ORDENES), new TypeReference<List<OrdenDeCompra>>() {})
                : new ArrayList<>();

        if (nuevaOrden.getFechaCompra() == null) {
            nuevaOrden.setFechaCompra(LocalDateTime.now());
        }

        ordenes.add(nuevaOrden);
        Files.writeString(RUTA_ORDENES, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ordenes));

        return ResponseEntity.ok("Orden de compra registrada exitosamente");
    }
} 