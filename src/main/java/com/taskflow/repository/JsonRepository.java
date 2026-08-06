package com.taskflow.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio que maneja la persistencia de datos en archivos JSON.
 * Utiliza Gson para serialización/deserialización.
 * Los archivos se guardan en la carpeta data/ del proyecto.
 */
public class JsonRepository {
    private static final String DATA_DIR = "data";
    private static final String TASKS_FILE = DATA_DIR + "/tasks.json";
    private static final String USERS_FILE = DATA_DIR + "/users.json";
    private static final String PROJECTS_FILE = DATA_DIR + "/projects.json";

    private final Gson gson;

    private List<Task> tasks;
    private List<User> users;
    private List<Project> projects;

    public JsonRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        ensureDataDirectory();
        loadAll();
    }

    /**
     * Asegura que el directorio data/ exista.
     */
    private void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Carga todos los datos desde los archivos JSON.
     */
    private void loadAll() {
        tasks = loadFromFile(TASKS_FILE, new TypeToken<List<Task>>(){}.getType());
        users = loadFromFile(USERS_FILE, new TypeToken<List<User>>(){}.getType());
        projects = loadFromFile(PROJECTS_FILE, new TypeToken<List<Project>>(){}.getType());
    }

    /**
     * Carga una lista desde un archivo JSON.
     * Si el archivo no existe, retorna una lista vacía.
     */
    private <T> List<T> loadFromFile(String filePath, Type type) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(file)) {
            List<T> result = gson.fromJson(reader, type);
            return result != null ? result : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al cargar " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Guarda una lista en un archivo JSON.
     */
    private <T> void saveToFile(String filePath, List<T> data) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar " + filePath + ": " + e.getMessage());
        }
    }

    // ==================== TASKS ====================

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Task getTaskById(String id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void updateTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(task.getId())) {
                tasks.set(i, task);
                break;
            }
        }
        saveTasks();
    }

    public void deleteTask(String id) {
        tasks.removeIf(t -> t.getId().equals(id));
        saveTasks();
    }

    public void saveTasks() {
        saveToFile(TASKS_FILE, tasks);
    }

    // ==================== USERS ====================

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public void deleteUser(String id) {
        users.removeIf(u -> u.getId().equals(id));
        saveUsers();
    }

    public void saveUsers() {
        saveToFile(USERS_FILE, users);
    }

    // ==================== PROJECTS ====================

    public List<Project> getAllProjects() {
        return new ArrayList<>(projects);
    }

    public Project getProjectById(String id) {
        return projects.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addProject(Project project) {
        projects.add(project);
        saveProjects();
    }

    public void deleteProject(String id) {
        projects.removeIf(p -> p.getId().equals(id));
        saveProjects();
    }

    public void saveProjects() {
        saveToFile(PROJECTS_FILE, projects);
    }
}
