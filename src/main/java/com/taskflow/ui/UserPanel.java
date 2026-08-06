package com.taskflow.ui;

import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.model.User;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel que muestra las tareas asignadas a un usuario específico,
 * agrupadas por prioridad (Alta → Media → Baja).
 * Cada grupo muestra un indicador visual con el color de prioridad.
 */
public class UserPanel extends JPanel {
    private final TaskService taskService;
    private final UserService userService;
    private final Runnable onRefresh;
    private JPanel contentPanel;
    private JComboBox<UserItem> userCombo;

    public UserPanel(TaskService taskService, UserService userService, Runnable onRefresh) {
        this.taskService = taskService;
        this.userService = userService;
        this.onRefresh = onRefresh;

        setLayout(new BorderLayout(0, 12));
        setBackground(ThemeManager.BG_SECONDARY);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        buildPanel();
    }

    private void buildPanel() {
        // --- Header: Title + User selector ---
        JPanel headerPanel = new JPanel(new BorderLayout(8, 0));
        headerPanel.setOpaque(false);

        JLabel titleLabel = ThemeManager.createLabel(
            "👤 Vista de Usuario",
            ThemeManager.FONT_TITLE,
            ThemeManager.TEXT_PRIMARY
        );
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // User selector
        userCombo = new JComboBox<>();
        refreshUserCombo();
        ThemeManager.styleComboBox(userCombo);
        userCombo.setPreferredSize(new Dimension(200, 32));
        userCombo.addActionListener(e -> refreshUserTasks());
        headerPanel.add(userCombo, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Content: Task groups by priority ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ThemeManager.BG_SECONDARY);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ThemeManager.BG_SECONDARY);
        scrollPane.getViewport().setBackground(ThemeManager.BG_SECONDARY);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        refreshUserTasks();
    }

    /**
     * Actualiza el combo de usuarios.
     */
    public void refreshUserCombo() {
        userCombo.removeAllItems();
        userCombo.addItem(new UserItem(null, "-- Seleccionar usuario --"));
        for (User user : userService.getAllUsers()) {
            userCombo.addItem(new UserItem(user.getId(), user.getName()));
        }
    }

    /**
     * Refresca las tareas mostradas según el usuario seleccionado.
     */
    public void refreshUserTasks() {
        contentPanel.removeAll();

        UserItem selectedUser = (UserItem) userCombo.getSelectedItem();
        if (selectedUser == null || selectedUser.getId() == null) {
            JLabel emptyLabel = ThemeManager.createLabel(
                "Selecciona un usuario para ver sus tareas asignadas",
                ThemeManager.FONT_REGULAR,
                ThemeManager.TEXT_MUTED
            );
            emptyLabel.setAlignmentX(LEFT_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            contentPanel.add(emptyLabel);
        } else {
            List<Task> userTasks = taskService.getTasksByUser(selectedUser.getId());

            if (userTasks.isEmpty()) {
                JLabel emptyLabel = ThemeManager.createLabel(
                    "Este usuario no tiene tareas asignadas",
                    ThemeManager.FONT_REGULAR,
                    ThemeManager.TEXT_MUTED
                );
                emptyLabel.setAlignmentX(LEFT_ALIGNMENT);
                emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                contentPanel.add(emptyLabel);
            } else {
                // Summary stats
                contentPanel.add(createStatsPanel(userTasks));
                contentPanel.add(Box.createRigidArea(new Dimension(0, 16)));

                // Group by priority
                for (Priority priority : Priority.values()) {
                    List<Task> priorityTasks = userTasks.stream()
                            .filter(t -> t.getPriority() == priority)
                            .collect(Collectors.toList());

                    if (!priorityTasks.isEmpty()) {
                        contentPanel.add(createPriorityGroup(priority, priorityTasks));
                        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
                    }
                }
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatsPanel(List<Task> tasks) {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(LEFT_ALIGNMENT);

        long todo = tasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgress = tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();

        statsPanel.add(createStatBadge("📋 " + todo, "Por Hacer", ThemeManager.TEXT_MUTED));
        statsPanel.add(createStatBadge("🔄 " + inProgress, "En Progreso", new Color(59, 130, 246)));
        statsPanel.add(createStatBadge("✅ " + done, "Finalizado", ThemeManager.SUCCESS));

        return statsPanel;
    }

    private JPanel createStatBadge(String value, String label, Color color) {
        JPanel badge = new JPanel();
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setBackground(ThemeManager.BG_CARD);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.BORDER_SUBTLE, 1, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        JLabel valueLabel = ThemeManager.createLabel(value, ThemeManager.FONT_TITLE, color);
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);
        badge.add(valueLabel);

        JLabel nameLabel = ThemeManager.createLabel(label, ThemeManager.FONT_SMALL, ThemeManager.TEXT_MUTED);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        badge.add(nameLabel);

        return badge;
    }

    private JPanel createPriorityGroup(Priority priority, List<Task> tasks) {
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.setOpaque(false);
        groupPanel.setAlignmentX(LEFT_ALIGNMENT);

        // Group header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(LEFT_ALIGNMENT);

        // Priority dot
        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(priority.getColor());
                g2.fillOval(0, 2, 12, 12);
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(12, 16));
        headerPanel.add(dot);

        JLabel groupLabel = ThemeManager.createLabel(
            "Prioridad " + priority.getLabel() + " (" + tasks.size() + ")",
            ThemeManager.FONT_BOLD,
            priority.getColor()
        );
        headerPanel.add(groupLabel);

        groupPanel.add(headerPanel);
        groupPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        // Task items
        for (Task task : tasks) {
            groupPanel.add(createTaskRow(task));
            groupPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        }

        return groupPanel;
    }

    private JPanel createTaskRow(Task task) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(ThemeManager.BG_CARD);
        row.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setAlignmentX(LEFT_ALIGNMENT);

        // Status icon + Title
        JLabel titleLabel = ThemeManager.createLabel(
            task.getStatus().getIcon() + " " + task.getTitle(),
            ThemeManager.FONT_REGULAR,
            ThemeManager.TEXT_PRIMARY
        );
        row.add(titleLabel, BorderLayout.CENTER);

        // Status badge
        JLabel statusLabel = new JLabel(task.getStatus().getLabel());
        statusLabel.setFont(ThemeManager.FONT_SMALL);
        statusLabel.setForeground(task.getStatus().getColor());
        row.add(statusLabel, BorderLayout.EAST);

        return row;
    }

    // --- Helper class ---
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
}
