package com.taskflow.ui;

import com.taskflow.model.*;
import com.taskflow.service.ProjectService;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Diálogo modal para crear o editar una tarea.
 * Incluye campos para título, descripción, prioridad, usuario asignado,
 * proyecto y fecha límite.
 */
public class TaskDialog extends JDialog {
    private final TaskService taskService;
    private final UserService userService;
    private final ProjectService projectService;
    private final Runnable onSave;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<Priority> priorityCombo;
    private JComboBox<UserItem> userCombo;
    private JComboBox<ProjectItem> projectCombo;
    private JTextField dueDateField;

    private Task existingTask; // null si es nueva tarea

    /**
     * Constructor para crear una nueva tarea.
     */
    public TaskDialog(JFrame parent, TaskService taskService, UserService userService,
                      ProjectService projectService, Runnable onSave) {
        this(parent, taskService, userService, projectService, onSave, null);
    }

    /**
     * Constructor para editar una tarea existente.
     */
    public TaskDialog(JFrame parent, TaskService taskService, UserService userService,
                      ProjectService projectService, Runnable onSave, Task existingTask) {
        super(parent, existingTask == null ? "Nueva Tarea" : "Editar Tarea", true);
        this.taskService = taskService;
        this.userService = userService;
        this.projectService = projectService;
        this.onSave = onSave;
        this.existingTask = existingTask;

        buildDialog();
        populateFields();

        setSize(480, 520);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(ThemeManager.BG_SECONDARY);
    }

    private void buildDialog() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ThemeManager.BG_SECONDARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Title
        mainPanel.add(createFieldPanel("Título *", titleField = new JTextField()));
        ThemeManager.styleTextField(titleField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Description
        JLabel descLabel = ThemeManager.createLabel("Descripción", ThemeManager.FONT_BOLD, ThemeManager.TEXT_SECONDARY);
        descLabel.setAlignmentX(LEFT_ALIGNMENT);
        mainPanel.add(descLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        descriptionArea = new JTextArea(3, 30);
        ThemeManager.styleTextArea(descriptionArea);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(null);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        mainPanel.add(scrollPane);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Priority
        priorityCombo = new JComboBox<>(Priority.values());
        ThemeManager.styleComboBox(priorityCombo);
        mainPanel.add(createComboPanel("Prioridad", priorityCombo));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // User assignment
        userCombo = new JComboBox<>();
        userCombo.addItem(new UserItem(null, "-- Sin asignar --"));
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            userCombo.addItem(new UserItem(user.getId(), user.getName()));
        }
        ThemeManager.styleComboBox(userCombo);
        mainPanel.add(createComboPanel("Asignar a", userCombo));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Project
        projectCombo = new JComboBox<>();
        projectCombo.addItem(new ProjectItem(null, "-- Sin proyecto --"));
        List<Project> projects = projectService.getAllProjects();
        for (Project project : projects) {
            projectCombo.addItem(new ProjectItem(project.getId(), project.getName()));
        }
        ThemeManager.styleComboBox(projectCombo);
        mainPanel.add(createComboPanel("Proyecto", projectCombo));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Due date
        mainPanel.add(createFieldPanel("Fecha límite (YYYY-MM-DD)", dueDateField = new JTextField()));
        ThemeManager.styleTextField(dueDateField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);

        JButton cancelBtn = ThemeManager.createSecondaryButton("Cancelar");
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = ThemeManager.createAccentButton(existingTask == null ? "Crear Tarea" : "Guardar");
        saveBtn.addActionListener(e -> saveTask());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        mainPanel.add(buttonPanel);

        setContentPane(mainPanel);
    }

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel label = ThemeManager.createLabel(labelText, ThemeManager.FONT_BOLD, ThemeManager.TEXT_SECONDARY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));

        field.setAlignmentX(LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        panel.add(field);

        return panel;
    }

    private JPanel createComboPanel(String labelText, JComboBox<?> combo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel label = ThemeManager.createLabel(labelText, ThemeManager.FONT_BOLD, ThemeManager.TEXT_SECONDARY);
        label.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));

        combo.setAlignmentX(LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        panel.add(combo);

        return panel;
    }

    private void populateFields() {
        if (existingTask != null) {
            titleField.setText(existingTask.getTitle());
            descriptionArea.setText(existingTask.getDescription());
            priorityCombo.setSelectedItem(existingTask.getPriority());
            if (existingTask.getDueDate() != null) {
                dueDateField.setText(existingTask.getDueDate());
            }

            // Select user
            if (existingTask.getAssignedUserId() != null) {
                for (int i = 0; i < userCombo.getItemCount(); i++) {
                    UserItem item = userCombo.getItemAt(i);
                    if (existingTask.getAssignedUserId().equals(item.getId())) {
                        userCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }

            // Select project
            if (existingTask.getProjectId() != null) {
                for (int i = 0; i < projectCombo.getItemCount(); i++) {
                    ProjectItem item = projectCombo.getItemAt(i);
                    if (existingTask.getProjectId().equals(item.getId())) {
                        projectCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    private void saveTask() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El título es obligatorio.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String description = descriptionArea.getText().trim();
        Priority priority = (Priority) priorityCombo.getSelectedItem();
        UserItem selectedUser = (UserItem) userCombo.getSelectedItem();
        ProjectItem selectedProject = (ProjectItem) projectCombo.getSelectedItem();
        String dueDate = dueDateField.getText().trim();

        if (existingTask == null) {
            // Crear nueva tarea
            Task task = taskService.createTask(title, description, priority,
                selectedProject != null ? selectedProject.getId() : null);
            if (selectedUser != null && selectedUser.getId() != null) {
                taskService.assignTask(task.getId(), selectedUser.getId());
            }
            if (!dueDate.isEmpty()) {
                task.setDueDate(dueDate);
                taskService.updateTask(task);
            }
        } else {
            // Editar tarea existente
            existingTask.setTitle(title);
            existingTask.setDescription(description);
            existingTask.setPriority(priority);
            existingTask.setAssignedUserId(selectedUser != null ? selectedUser.getId() : null);
            existingTask.setProjectId(selectedProject != null ? selectedProject.getId() : null);
            existingTask.setDueDate(dueDate.isEmpty() ? null : dueDate);
            taskService.updateTask(existingTask);
        }

        onSave.run();
        dispose();
    }

    // --- Helper classes for combo items ---

    private static class UserItem {
        private final String id;
        private final String name;

        UserItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }

        @Override
        public String toString() { return name; }
    }

    private static class ProjectItem {
        private final String id;
        private final String name;

        ProjectItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }

        @Override
        public String toString() { return name; }
    }
}
