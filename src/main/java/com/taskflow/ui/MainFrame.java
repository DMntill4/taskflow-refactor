package com.taskflow.ui;

import com.taskflow.model.Priority;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.ProjectService;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Ventana principal de la aplicación TaskFlow.
 * Contiene el tablero Kanban con las 3 columnas y la barra lateral de navegación.
 */
public class MainFrame extends JFrame {
    private final TaskService taskService;
    private final UserService userService;
    private final ProjectService projectService;

    private Map<TaskStatus, KanbanColumn> kanbanColumns;
    private UserPanel userPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public MainFrame(TaskService taskService, UserService userService, ProjectService projectService) {
        this.taskService = taskService;
        this.userService = userService;
        this.projectService = projectService;

        setTitle("TaskFlow - Gestor de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.BG_PRIMARY);
        setLayout(new BorderLayout());

        buildUI();
        refreshAll();
    }

    private void buildUI() {
        // Top Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.BG_HEADER);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = ThemeManager.createLabel("TaskFlow", ThemeManager.FONT_HEADING, ThemeManager.TEXT_PRIMARY);
        titleLabel.setIconTextGap(10);
        // Podríamos añadir un ícono aquí si tuviéramos uno
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Header Actions
        JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        headerActions.setOpaque(false);

        JButton filterBtn = ThemeManager.createSecondaryButton("Filtros");
        filterBtn.addActionListener(e -> showFilterDialog());
        
        JButton newTaskBtn = ThemeManager.createAccentButton("+ Nueva Tarea");
        newTaskBtn.addActionListener(e -> showNewTaskDialog());

        headerActions.add(filterBtn);
        headerActions.add(newTaskBtn);
        headerPanel.add(headerActions, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(ThemeManager.BG_SECONDARY);
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Navigation buttons in sidebar
        JButton boardNavBtn = createNavButton("📋 Tablero Kanban", true);
        JButton usersNavBtn = createNavButton("👥 Vista de Usuarios", false);

        boardNavBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "BOARD");
            boardNavBtn.setForeground(ThemeManager.ACCENT);
            usersNavBtn.setForeground(ThemeManager.TEXT_SECONDARY);
        });

        usersNavBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "USERS");
            usersNavBtn.setForeground(ThemeManager.ACCENT);
            boardNavBtn.setForeground(ThemeManager.TEXT_SECONDARY);
            userPanel.refreshUserCombo();
        });

        sidebarPanel.add(boardNavBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(usersNavBtn);
        
        sidebarPanel.add(Box.createVerticalStrut(30));
        sidebarPanel.add(ThemeManager.createSeparator());
        sidebarPanel.add(Box.createVerticalStrut(20));

        // Admin actions
        JLabel adminLabel = ThemeManager.createLabel("ADMINISTRACIÓN", ThemeManager.FONT_SMALL, ThemeManager.TEXT_MUTED);
        adminLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(adminLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton newUserBtn = createNavButton("+ Nuevo Usuario", false);
        newUserBtn.addActionListener(e -> showNewUserDialog());
        
        JButton newProjectBtn = createNavButton("+ Nuevo Proyecto", false);
        newProjectBtn.addActionListener(e -> showNewProjectDialog());

        sidebarPanel.add(newUserBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(newProjectBtn);

        add(sidebarPanel, BorderLayout.WEST);

        // Main Content (CardLayout for switching between views)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(ThemeManager.BG_PRIMARY);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Kanban Board View
        JPanel kanbanBoard = new JPanel(new GridLayout(1, 3, 20, 0));
        kanbanBoard.setBackground(ThemeManager.BG_PRIMARY);
        kanbanColumns = new EnumMap<>(TaskStatus.class);

        for (TaskStatus status : TaskStatus.values()) {
            KanbanColumn column = new KanbanColumn(status, taskService, userService, this::refreshAll);
            kanbanColumns.put(status, column);
            kanbanBoard.add(column);
        }

        // 2. User View
        userPanel = new UserPanel(taskService, userService, this::refreshAll);

        mainContentPanel.add(kanbanBoard, "BOARD");
        mainContentPanel.add(userPanel, "USERS");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(ThemeManager.FONT_REGULAR);
        btn.setForeground(active ? ThemeManager.ACCENT : ThemeManager.TEXT_SECONDARY);
        btn.setBackground(ThemeManager.BG_SECONDARY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(200, 35));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getForeground() != ThemeManager.ACCENT) {
                    btn.setForeground(ThemeManager.TEXT_PRIMARY);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.getForeground() != ThemeManager.ACCENT) {
                    btn.setForeground(ThemeManager.TEXT_SECONDARY);
                }
            }
        });
        
        return btn;
    }

    private void showNewTaskDialog() {
        TaskDialog dialog = new TaskDialog(this, taskService, userService, projectService, this::refreshAll);
        dialog.setVisible(true);
    }

    private void showNewUserDialog() {
        String name = JOptionPane.showInputDialog(this, "Nombre del usuario:", "Nuevo Usuario", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            String email = JOptionPane.showInputDialog(this, "Correo electrónico (opcional):", "Nuevo Usuario", JOptionPane.PLAIN_MESSAGE);
            userService.createUser(name.trim(), email != null ? email.trim() : "");
            refreshAll();
            JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
        }
    }

    private void showNewProjectDialog() {
        String name = JOptionPane.showInputDialog(this, "Nombre del proyecto:", "Nuevo Proyecto", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            String desc = JOptionPane.showInputDialog(this, "Descripción (opcional):", "Nuevo Proyecto", JOptionPane.PLAIN_MESSAGE);
            projectService.createProject(name.trim(), desc != null ? desc.trim() : "");
            refreshAll();
            JOptionPane.showMessageDialog(this, "Proyecto creado exitosamente.");
        }
    }
    
    private void showFilterDialog() {
        // En una implementación completa, esto abriría un diálogo para filtrar
        // por proyecto, usuario o prioridad específica, y actualizaría el tablero
        JOptionPane.showMessageDialog(this, 
            "La función de filtros detallados está en desarrollo.\n" +
            "Actualmente puedes usar la vista 'Vista de Usuarios' para filtrar por persona.", 
            "Filtros", JOptionPane.INFORMATION_MESSAGE);
    }

    public void refreshAll() {
        // Refresh Kanban columns
        java.util.List<com.taskflow.model.Task> allTasks = taskService.getAllTasks();
        for (KanbanColumn column : kanbanColumns.values()) {
            column.refreshTasks(allTasks);
        }
        
        // Refresh User view if active
        if (userPanel != null) {
            userPanel.refreshUserTasks();
        }
    }
}
