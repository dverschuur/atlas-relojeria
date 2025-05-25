package com.ve.edu.ucab.AtlasRelojeria.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class Perfil {
    String nombre;
    String id;
    int edad;
    private String contrasena;
    private boolean esAdministrador;
    
    /** Constructores */
    public Perfil(String nombre, String id, int edad) {
        this.nombre = nombre;
        this.id = id;
        this.edad = edad;
        this.contrasena = contrasena;
        this.esAdministrador = esAdministrador;
    }

    public Perfil() {}

    /** Getters and Setters */

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getContrasena() {
    return contrasena;
}

public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
}

public boolean isEsAdministrador() {
    return esAdministrador;
}

public void setEsAdministrador(boolean esAdministrador) {
    this.esAdministrador = esAdministrador;
}

    @Override
    public String toString() {
        return nombre + ";" + id +";" + edad;
    }

    private static final String ARCHIVO = "perfiles.txt";

    public static boolean autenticar(String nombre, String id) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO));
        for (String linea : lineas) {
            String[] partes = linea.split(",");
            if (partes.length == 3 && partes[0].equals(nombre) && partes[1].equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static void crearPerfil(String nombre, String id, int edad) throws IOException {
        String nuevaLinea = nombre + "," + id + "," + edad + System.lineSeparator();
        Files.write(Paths.get(ARCHIVO), nuevaLinea.getBytes(), StandardOpenOption.APPEND);
    }

    public static void eliminarPerfil(String nombre) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO));
        List<String> nuevas = lineas.stream()
            .filter(l -> !l.startsWith(nombre + ","))
            .collect(Collectors.toList());
        Files.write(Paths.get(ARCHIVO), nuevas);
    }

}
