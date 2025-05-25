package com.ve.edu.ucab.AtlasRelojeria.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ve.edu.ucab.AtlasRelojeria.Model.Producto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:8080")
public class ProductoController {

    private static final Path RUTA_JSON = Paths.get("src/main/resources/productos.json");

    /** Vista para administrador: muestra todos los atributos */
    @GetMapping("/inventario")
    public ResponseEntity<List<Producto>> obtenerInventario() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Producto> productos = mapper.readValue(Files.readString(RUTA_JSON), new TypeReference<List<Producto>>() {});
        return ResponseEntity.ok(productos);
    }

    /** Vista para cliente: solo nombre, precio, descripcion e imagen */
    @GetMapping("/vista-cliente")
    public ResponseEntity<List<Map<String, Object>>> obtenerVistaCliente() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> lista = new ArrayList<>();

        List<Map<String, Object>> productos = mapper.readValue(Files.readString(RUTA_JSON), new TypeReference<List<Map<String, Object>>>() {});
        for (Map<String, Object> p : productos) {
                Map<String, Object> vista = new HashMap<>();
                vista.put("nombre", p.get("nombre"));
                vista.put("descripcion", p.get("descripcion"));
                vista.put("precio", p.get("precio"));
                vista.put("imagen", p.get("imagen"));
                vista.put("id", p.get("id"));
                vista.put("cantidad", p.get("cantidad"));
                lista.add(vista);
        }

        return ResponseEntity.ok(lista);
    }

}
