package com.taskflow.model;

import java.util.UUID;

/**
 * Entidad que representa un usuario del sistema.
 * Un usuario puede tener tareas asignadas.
 */
public class User {
    private String id;
    private String name;
    private String email;

    /**
     * Constructor para crear un nuevo usuario.
     */
    public User(String name, String email) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.email = email;
    }

    /**
     * Constructor vacío para deserialización JSON.
     */
    public User() {}

    // --- Getters y Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Representación detallada del usuario.
     */
    public String toDetailString() {
        return String.format("%s (%s)", name, email);
    }
}
