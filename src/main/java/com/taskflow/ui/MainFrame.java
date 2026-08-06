package com.taskflow.ui;

import com.taskflow.model.TaskStatus;
import com.taskflow.service.ProjectService;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Ventana principal de TaskFlow con diseño compacto estilo Linear/GitHub.
 * Incluye barra superior delgada (48px), búsqueda rápida, selector de tema (Claro/Oscuro),
 * barra lateral de navegación con indicador de estado activo de 3px y tablero Kanban.
 */
public class MainFrame extends JFrame {
    private final TaskService taskService;
    private final UserService userService;
    private final ProjectService projectService;

    private Map<TaskStatus, KanbanColumn> kanbanColumns;
    private UserPanel userPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    private JButton boardNavBtn;
    private JButton usersNavBtn;
    private JButton themeToggleBtn;
    private JPanel topHeaderPanel;
    private JPanel sidebarPanel;
    private JPanel kanbanBoardPanel;

    public MainFrame(TaskService taskService, UserService userService, ProjectService projectService) {
        this.taskService = taskService;
        this.userService = userService;
        this.projectService = projectService;

        setTitle("TaskFlow — Issue & Task Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 780);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.getBgPrimary());
        setLayout(new BorderLayout());

        buildUI();
        refreshAll();
    }

    private void buildUI() {
        // --- 1. Top Header (~48px thin bar) ---
        topHeaderPanel = new JPanel(new BorderLayout(12, 0));
        topHeaderPanel.setBackground(ThemeManager.getBgPrimary());
        topHeaderPanel.setPreferredSize(new Dimension(getWidth(), 48));
        topHeaderPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.getBorderColor()),
            BorderFactory.createEmptyBorder(0, 16, 0, 16)
        ));

        // Left: Logo & Breadcrumb
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        leftHeader.setOpaque(false);

        JLabel logoLabel = ThemeManager.createLabel("TaskFlow", ThemeManager.FONT_HEADING, ThemeManager.getTextPrimary());
        JLabel breadcrumbLabel = ThemeManager.createLabel("/ Workspace", ThemeManager.FONT_REGULAR, ThemeManager.getTextMuted());

        leftHeader.add(logoLabel);
        leftHeader.add(breadcrumbLabel);
        topHeaderPanel.add(leftHeader, BorderLayout.WEST);

        // Center: Search input Command Palette style
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField("Buscar o presionar '/' para comandos...");
        searchField.setPreferredSize(new Dimension(280, 28));
        ThemeManager.styleTextField(searchField);
        searchField.setFont(ThemeManager.FONT_SMALL);
        searchField.setForeground(ThemeManager.getTextMuted());
        searchPanel.add(searchField);
        topHeaderPanel.add(searchPanel, BorderLayout.CENTER);

        // Right: Theme switch + New Task action button
        JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        headerActions.setOpaque(false);

        themeToggleBtn = ThemeManager.createSecondaryButton(ThemeManager.isDarkMode() ? "Modo Claro" : "Modo Oscuro");
        themeToggleBtn.addActionListener(e -> toggleTheme());

        JButton newTaskBtn = ThemeManager.createAccentButton("+ Nueva Tarea");
        newTaskBtn.addActionListener(e -> showNewTaskDialog());

        headerActions.add(themeToggleBtn);
        headerActions.add(newTaskBtn);
        topHeaderPanel.add(headerActions, BorderLayout.EAST);

        add(topHeaderPanel, BorderLayout.NORTH);

        // --- 2. Fixed Left Sidebar (~220px) ---
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(ThemeManager.getBgSecondary());
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, ThemeManager.getBorderColor()),
            BorderFactory.createEmptyBorder(16, 8, 16, 8)
        ));

        // Nav Label
        JLabel navLabel = ThemeManager.createLabel("VISTAS", ThemeManager.FONT_SMALL, ThemeManager.getTextMuted());
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(navLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Nav Buttons with 3px Left Accent bar for active state
        boardNavBtn = createSidebarNavButton("Tablero Kanban", true);
        usersNavBtn = createSidebarNavButton("Vista por Usuario", false);

        boardNavBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "BOARD");
            setActiveNavButton(boardNavBtn, usersNavBtn);
        });

        usersNavBtn.addActionListener(e -> {
            cardLayout.show(mainContentPanel, "USERS");
            setActiveNavButton(usersNavBtn, boardNavBtn);
            userPanel.refreshUserCombo();
        });

        sidebarPanel.add(boardNavBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebarPanel.add(usersNavBtn);

        sidebarPanel.add(Box.createVerticalStrut(24));
        sidebarPanel.add(ThemeManager.createSeparator());
        sidebarPanel.add(Box.createVerticalStrut(16));

        // Admin section
        JLabel adminLabel = ThemeManager.createLabel("ADMINISTRACIÓN", ThemeManager.FONT_SMALL, ThemeManager.getTextMuted());
        adminLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(adminLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JButton newUserBtn = createSidebarNavButton("+ Nuevo Usuario", false);
        newUserBtn.addActionListener(e -> showNewUserDialog());

        JButton newProjectBtn = createSidebarNavButton("+ Nuevo Proyecto", false);
        newProjectBtn.addActionListener(e -> showNewProjectDialog());

        sidebarPanel.add(newUserBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebarPanel.add(newProjectBtn);

        add(sidebarPanel, BorderLayout.WEST);

        // --- 3. Main Content Area ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(ThemeManager.getBgPrimary());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Kanban Board View
        kanbanBoardPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        kanbanBoardPanel.setBackground(ThemeManager.getBgPrimary());
        kanbanColumns = new EnumMap<>(TaskStatus.class);

        for (TaskStatus status : TaskStatus.values()) {
            KanbanColumn column = new KanbanColumn(status, taskService, userService, this::refreshAll);
            kanbanColumns.put(status, column);
            kanbanBoardPanel.add(column);
        }

        // User View
        userPanel = new UserPanel(taskService, userService, this::refreshAll);

        mainContentPanel.add(kanbanBoardPanel, "BOARD");
        mainContentPanel.add(userPanel, "USERS");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JButton createSidebarNavButton(String text, boolean active) {
        JButton btn = new JButton(text) {
            private boolean isActive = active;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean currentActive = Boolean.TRUE.equals(getClientProperty("active"));

                if (currentActive) {
                    g2.setColor(ThemeManager.getBgCardHover());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                    // 3px Left Accent Bar
                    g2.setColor(ThemeManager.getAccent());
                    g2.fillRect(0, 4, 3, getHeight() - 8);
                } else if (getModel().isRollover()) {
                    g2.setColor(ThemeManager.getBgCardHover());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                }

                g2.setColor(currentActive ? ThemeManager.getTextPrimary() : ThemeManager.getTextSecondary());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = 14;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        btn.setFont(ThemeManager.FONT_MEDIUM);
        btn.putClientProperty("active", active);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(204, 34));
        btn.setPreferredSize(new Dimension(204, 34));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        return btn;
    }

    private void setActiveNavButton(JButton activeBtn, JButton inactiveBtn) {
        activeBtn.putClientProperty("active", true);
        inactiveBtn.putClientProperty("active", false);
        activeBtn.repaint();
        inactiveBtn.repaint();
    }

    private void toggleTheme() {
        boolean isDark = ThemeManager.isDarkMode();
        ThemeManager.setDarkMode(!isDark);
        themeToggleBtn.setText(ThemeManager.isDarkMode() ? "Modo Claro" : "Modo Oscuro");

        // Update colors dynamically across the UI
        getContentPane().setBackground(ThemeManager.getBgPrimary());
        topHeaderPanel.setBackground(ThemeManager.getBgPrimary());
        sidebarPanel.setBackground(ThemeManager.getBgSecondary());
        mainContentPanel.setBackground(ThemeManager.getBgPrimary());
        kanbanBoardPanel.setBackground(ThemeManager.getBgPrimary());

        refreshAll();
        SwingUtilities.updateComponentTreeUI(this);
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
