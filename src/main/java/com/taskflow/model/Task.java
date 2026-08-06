package com.taskflow.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Entidad que representa una tarea en el sistema.
 * Contiene toda la información necesaria: título, descripción,
 * prioridad, estado, usuario asignado, proyecto y fechas.
 */
public class Task {
    private String id;
    private String title;
    private String description;
    private Priority priority;
    private TaskStatus status;
    private String assignedUserId;
    private String projectId;
    private String createdAt;
    private String dueDate;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Constructor completo para crear una nueva tarea.
     */
    public Task(String title, String description, Priority priority, String projectId) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = TaskStatus.TODO;
        this.projectId = projectId;
        this.createdAt = LocalDateTime.now().format(FORMATTER);
        this.assignedUserId = null;
        this.dueDate = null;
    }

    /**
     * Constructor vacío para deserialización JSON.
     */
    public Task() {}

    // --- Getters y Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Retorna una representación en texto de la tarea.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s — %s | %s", 
            priority.getLabel(), title, status.getLabel(),
            assignedUserId != null ? "Asignado" : "Sin asignar");
    }
}
