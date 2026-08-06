package com.taskflow;

import com.taskflow.model.Priority;
import com.taskflow.model.Project;
import com.taskflow.model.User;
import com.taskflow.repository.JsonRepository;
import com.taskflow.service.ProjectService;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;
import com.taskflow.ui.MainFrame;
import com.taskflow.ui.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Punto de entrada principal para la aplicación TaskFlow.
 * Inicializa los servicios, carga datos de prueba si es necesario,
 * y lanza la interfaz gráfica.
 */
public class Main {
    public static void main(String[] args) {
        // Establecer un LookAndFeel moderno si es posible
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer SystemLookAndFeel: " + e.getMessage());
        }
        
        // Ajustes globales para fuentes y ToolTips
        setUIFont (new javax.swing.plaf.FontUIResource(ThemeManager.FONT_REGULAR));
        UIManager.put("ToolTip.background", ThemeManager.BG_CARD_HOVER);
        UIManager.put("ToolTip.foreground", ThemeManager.TEXT_PRIMARY);
        UIManager.put("ToolTip.font", ThemeManager.FONT_SMALL);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(ThemeManager.BORDER));

        // Inicializar capas
        JsonRepository repository = new JsonRepository();
        TaskService taskService = new TaskService(repository);
        UserService userService = new UserService(repository);
        ProjectService projectService = new ProjectService(repository);

        // Inicializar datos de prueba si los repositorios están vacíos (para la primera ejecución o demo)
        initializeDemoDataIfEmpty(taskService, userService, projectService);

        // Iniciar GUI en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(taskService, userService, projectService);
            frame.setVisible(true);
        });
    }

    /**
     * Crea datos iniciales para demostración si el sistema está vacío.
     */
    private static void initializeDemoDataIfEmpty(TaskService taskService, UserService userService, ProjectService projectService) {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Inicializando datos de prueba para la demo...");
            
            // 1. Crear usuarios
            User alice = userService.createUser("Alice (Dev)", "alice@taskflow.com");
            User bob = userService.createUser("Bob (QA)", "bob@taskflow.com");
            
            // 2. Crear proyectos
            Project backend = projectService.createProject("Backend API", "API REST para el Gestor de Tareas");
            Project frontend = projectService.createProject("Frontend UI", "Interfaz Gráfica en Swing");
            
            // 3. Crear tareas
            com.taskflow.model.Task task1 = taskService.createTask(
                "Diseñar esquema JSON", 
                "Definir cómo se guardarán las tareas, usuarios y proyectos en los archivos JSON.", 
                Priority.HIGH, 
                backend.getId()
            );
            taskService.assignTask(task1.getId(), alice.getId());
            taskService.updateTaskStatus(task1.getId(), com.taskflow.model.TaskStatus.DONE);
            
            com.taskflow.model.Task task2 = taskService.createTask(
                "Implementar UI Kanban", 
                "Crear las 3 columnas y las tarjetas drag & drop.", 
                Priority.HIGH, 
                frontend.getId()
            );
            taskService.assignTask(task2.getId(), bob.getId());
            taskService.updateTaskStatus(task2.getId(), com.taskflow.model.TaskStatus.IN_PROGRESS);
            
            com.taskflow.model.Task task3 = taskService.createTask(
                "Presentación del Lunes", 
                "Preparar diapositivas y demo para la presentación del proyecto.", 
                Priority.MEDIUM, 
                null
            );
            
            com.taskflow.model.Task task4 = taskService.createTask(
                "Ajustar colores del tema oscuro", 
                "El contraste en los botones no es suficiente.", 
                Priority.LOW, 
                frontend.getId()
            );
            
            System.out.println("Datos de prueba creados exitosamente.");
        }
    }
    
    /**
     * Aplica una fuente global a todos los componentes Swing.
     */
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
}
