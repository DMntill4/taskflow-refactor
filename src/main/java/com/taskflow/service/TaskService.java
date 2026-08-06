package com.taskflow.service;

import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.JsonRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que maneja la lógica de negocio de las tareas.
 * Proporciona métodos para CRUD, filtrado y asignación.
 */
public class TaskService {
    private final JsonRepository repository;

    public TaskService(JsonRepository repository) {
        this.repository = repository;
    }

    /**
     * Crea una nueva tarea y la persiste.
     */
    public Task createTask(String title, String description, Priority priority, String projectId) {
        Task task = new Task(title, description, priority, projectId);
        repository.addTask(task);
        return task;
    }

    /**
     * Obtiene todas las tareas.
     */
    public List<Task> getAllTasks() {
        return repository.getAllTasks();
    }

    /**
     * Obtiene una tarea por su ID.
     */
    public Task getTaskById(String id) {
        return repository.getTaskById(id);
    }

    /**
     * Actualiza el estado de una tarea.
     */
    public void updateTaskStatus(String taskId, TaskStatus newStatus) {
        Task task = repository.getTaskById(taskId);
        if (task != null) {
            task.setStatus(newStatus);
            repository.updateTask(task);
        }
    }

    /**
     * Avanza el estado de una tarea al siguiente estado.
     * TODO -> IN_PROGRESS -> DONE
     */
    public void advanceTaskStatus(String taskId) {
        Task task = repository.getTaskById(taskId);
        if (task != null) {
            task.setStatus(task.getStatus().next());
            repository.updateTask(task);
        }
    }

    /**
     * Asigna una tarea a un usuario.
     */
    public void assignTask(String taskId, String userId) {
        Task task = repository.getTaskById(taskId);
        if (task != null) {
            task.setAssignedUserId(userId);
            repository.updateTask(task);
        }
    }

    /**
     * Obtiene las tareas asignadas a un usuario específico.
     */
    public List<Task> getTasksByUser(String userId) {
        return repository.getAllTasks().stream()
                .filter(t -> userId.equals(t.getAssignedUserId()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra tareas por prioridad.
     */
    public List<Task> getTasksByPriority(Priority priority) {
        return repository.getAllTasks().stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    /**
     * Filtra tareas por estado.
     */
    public List<Task> getTasksByStatus(TaskStatus status) {
        return repository.getAllTasks().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las tareas de un proyecto específico.
     */
    public List<Task> getTasksByProject(String projectId) {
        return repository.getAllTasks().stream()
                .filter(t -> projectId.equals(t.getProjectId()))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una tarea existente.
     */
    public void updateTask(Task task) {
        repository.updateTask(task);
    }

    /**
     * Elimina una tarea por su ID.
     */
    public void deleteTask(String taskId) {
        repository.deleteTask(taskId);
    }

    /**
     * Cuenta tareas por estado para un proyecto.
     */
    public long countByStatus(String projectId, TaskStatus status) {
        return repository.getAllTasks().stream()
                .filter(t -> (projectId == null || projectId.equals(t.getProjectId())))
                .filter(t -> t.getStatus() == status)
                .count();
    }
}
