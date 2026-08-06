package com.taskflow.model;

import java.util.UUID;

/**
 * Clase modelo para representar a un Usuario.
 */
public class User {
    private String id;
    private String name;

    public User(String name) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
    }

    public User() {}

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

    @Override
    public String toString() {
        return name;
    }
}
