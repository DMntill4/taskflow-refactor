package com.taskflow.service;

import com.taskflow.model.User;
import com.taskflow.repository.JsonRepository;

import java.util.List;

/**
 * Servicio que maneja la lógica de negocio de los usuarios.
 */
public class UserService {
    private final JsonRepository repository;

    public UserService(JsonRepository repository) {
        this.repository = repository;
    }

    /**
     * Crea un nuevo usuario.
     */
    public User createUser(String name, String email) {
        User user = new User(name, email);
        repository.addUser(user);
        return user;
    }

    /**
     * Obtiene todos los usuarios.
     */
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    /**
     * Obtiene un usuario por su ID.
     */
    public User getUserById(String id) {
        return repository.getUserById(id);
    }

    /**
     * Elimina un usuario por su ID.
     */
    public void deleteUser(String id) {
        repository.deleteUser(id);
    }

    /**
     * Busca un usuario por nombre (case-insensitive).
     */
    public User findByName(String name) {
        return repository.getAllUsers().stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
