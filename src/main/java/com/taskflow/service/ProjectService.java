package com.taskflow.service;

import com.taskflow.model.Project;
import com.taskflow.repository.JsonRepository;

import java.util.List;

/**
 * Servicio que maneja la lógica de negocio de los proyectos.
 */
public class ProjectService {
    private final JsonRepository repository;

    public ProjectService(JsonRepository repository) {
        this.repository = repository;
    }

    /**
     * Crea un nuevo proyecto.
     */
    public Project createProject(String name, String description) {
        Project project = new Project(name, description);
        repository.addProject(project);
        return project;
    }

    /**
     * Obtiene todos los proyectos.
     */
    public List<Project> getAllProjects() {
        return repository.getAllProjects();
    }

    /**
     * Obtiene un proyecto por su ID.
     */
    public Project getProjectById(String id) {
        return repository.getProjectById(id);
    }

    /**
     * Elimina un proyecto por su ID.
     */
    public void deleteProject(String id) {
        repository.deleteProject(id);
    }
}
