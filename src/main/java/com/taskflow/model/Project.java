package com.taskflow.model;

import java.util.UUID;

/**
 * Entidad que representa un proyecto o microproyecto.
 * Las tareas se agrupan dentro de proyectos.
 */
public class Project {
    private String id;
    private String name;
    private String description;

    /**
     * Constructor para crear un nuevo proyecto.
     */
    public Project(String name, String description) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor vacío para deserialización JSON.
     */
    public Project() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
