package com.ve.edu.ucab.AtlasRelojeria.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ve.edu.ucab.AtlasRelojeria.Model.OrdenDeCompra;
import com.ve.edu.ucab.AtlasRelojeria.Model.Producto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
public class OrdenDeCompraController {

    private static final Path RUTA_ORDENES = Paths.get("src/main/resources/compras.json");
    private static final Path RUTA_PRODUCTOS = Paths.get("src/main/resources/productos.json");
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @GetMapping
    public ResponseEntity<List<OrdenDeCompra>> obtenerOrdenes() throws IOException {
        List<OrdenDeCompra> ordenes = Files.exists(RUTA_ORDENES)
                ? mapper.readValue(Files.readString(RUTA_ORDENES), new TypeReference<List<OrdenDeCompra>>() {})
                : new ArrayList<>();
        return ResponseEntity.ok(ordenes);
    }

    @PostMapping
    public ResponseEntity<?> registrarOrden(@RequestBody OrdenDeCompra nuevaOrden) throws IOException {
        try {
            // Leer productos existentes
            List<Producto> productos = mapper.readValue(Files.readString(RUTA_PRODUCTOS), new TypeReference<List<Producto>>() {});
            
            // Buscar el producto
            Optional<Producto> productoOpt = productos.stream()
                    .filter(p -> p.getId().equals(nuevaOrden.getProducto()))
                    .findFirst();
            
            if (productoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("El producto no existe");
            }

            Producto producto = productoOpt.get();
            
            // Leer órdenes existentes
            List<OrdenDeCompra> ordenes = Files.exists(RUTA_ORDENES)
                    ? mapper.readValue(Files.readString(RUTA_ORDENES), new TypeReference<List<OrdenDeCompra>>() {})
                    : new ArrayList<>();

            // Generar ID de orden
            nuevaOrden.setNumeroDeCompra(String.format("C%d", ordenes.size() + 1));
            
            // Establecer fecha actual
            nuevaOrden.setFechaCompra(LocalDate.now());
            
            // Calcular monto total
            double montoTotal = producto.getPrecio() * Integer.parseInt(nuevaOrden.getCantidadDeProducto());
            nuevaOrden.setMonto(String.valueOf(montoTotal));

            // Actualizar cantidad en producto
            producto.setCantidad(producto.getCantidad() + Integer.parseInt(nuevaOrden.getCantidadDeProducto()));
            
            // Guardar cambios en productos
            Files.writeString(RUTA_PRODUCTOS, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(productos));

            // Guardar la orden
            ordenes.add(nuevaOrden);
            Files.writeString(RUTA_ORDENES, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ordenes));

            return ResponseEntity.ok("Orden de compra registrada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar la orden: " + e.getMessage());
        }
    }
} 