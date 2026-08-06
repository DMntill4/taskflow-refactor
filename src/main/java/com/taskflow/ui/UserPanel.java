package com.taskflow.ui;

import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.model.User;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vista de usuario en formato de tabla densa inspirada en GitHub/Linear.
 * Muestra filas con divisores de 1px, estado, identificadores monoespaciados,
 * indicador de prioridad compacto y efecto hover sutil.
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
        setBackground(ThemeManager.getBgSecondary());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        buildPanel();
    }

    private void buildPanel() {
        // --- Header: Title + User Selector ---
        JPanel headerPanel = new JPanel(new BorderLayout(8, 0));
        headerPanel.setOpaque(false);

        JLabel titleLabel = ThemeManager.createLabel(
            "Vista de Tareas por Usuario",
            ThemeManager.FONT_SECTION_HEADER,
            ThemeManager.getTextPrimary()
        );
        headerPanel.add(titleLabel, BorderLayout.WEST);

        userCombo = new JComboBox<>();
        refreshUserCombo();
        ThemeManager.styleComboBox(userCombo);
        userCombo.setPreferredSize(new Dimension(220, 32));
        userCombo.addActionListener(e -> refreshUserTasks());
        headerPanel.add(userCombo, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Content Area ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ThemeManager.getBgSecondary());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ThemeManager.getBgSecondary());
        scrollPane.getViewport().setBackground(ThemeManager.getBgSecondary());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        refreshUserTasks();
    }

    public void refreshUserCombo() {
        userCombo.removeAllItems();
        userCombo.addItem(new UserItem(null, "-- Seleccionar usuario --"));
        for (User user : userService.getAllUsers()) {
            userCombo.addItem(new UserItem(user.getId(), user.getName()));
        }
    }

    public void refreshUserTasks() {
        contentPanel.removeAll();

        UserItem selectedUser = (UserItem) userCombo.getSelectedItem();
        if (selectedUser == null || selectedUser.getId() == null) {
            JLabel emptyLabel = ThemeManager.createLabel(
                "Selecciona un usuario para desplegar su tablero de tareas.",
                ThemeManager.FONT_REGULAR,
                ThemeManager.getTextMuted()
            );
            emptyLabel.setAlignmentX(LEFT_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            contentPanel.add(emptyLabel);
        } else {
            List<Task> userTasks = taskService.getTasksByUser(selectedUser.getId());

            if (userTasks.isEmpty()) {
                JLabel emptyLabel = ThemeManager.createLabel(
                    "Este usuario no posee tareas asignadas actualmente.",
                    ThemeManager.FONT_REGULAR,
                    ThemeManager.getTextMuted()
                );
                emptyLabel.setAlignmentX(LEFT_ALIGNMENT);
                emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                contentPanel.add(emptyLabel);
            } else {
                // Summary Stats
                contentPanel.add(createStatsPanel(userTasks));
                contentPanel.add(Box.createRigidArea(new Dimension(0, 16)));

                // Group by Priority
                for (Priority priority : Priority.values()) {
                    List<Task> priorityTasks = userTasks.stream()
                            .filter(t -> t.getPriority() == priority)
                            .collect(Collectors.toList());

                    if (!priorityTasks.isEmpty()) {
                        contentPanel.add(createPriorityGroup(priority, priorityTasks));
                        contentPanel.add(Box.createRigidArea(new Dimension(0, 16)));
                    }
                }
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatsPanel(List<Task> tasks) {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(LEFT_ALIGNMENT);

        long todo = tasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgress = tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();

        statsPanel.add(createStatBadge(String.valueOf(todo), "Por Hacer", ThemeManager.STATUS_TODO_FG));
        statsPanel.add(createStatBadge(String.valueOf(inProgress), "En Progreso", ThemeManager.STATUS_IN_PROGRESS_FG));
        statsPanel.add(createStatBadge(String.valueOf(done), "Finalizadas", ThemeManager.STATUS_DONE_FG));

        return statsPanel;
    }

    private JPanel createStatBadge(String value, String label, Color color) {
        JPanel badge = new JPanel();
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setBackground(ThemeManager.getBgCard());
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, false),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));

        JLabel valueLabel = ThemeManager.createLabel(value, ThemeManager.FONT_MONO_BOLD, color);
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);
        badge.add(valueLabel);

        JLabel nameLabel = ThemeManager.createLabel(label, ThemeManager.FONT_SMALL, ThemeManager.getTextSecondary());
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        badge.add(nameLabel);

        return badge;
    }

    private JPanel createPriorityGroup(Priority priority, List<Task> tasks) {
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.setOpaque(false);
        groupPanel.setAlignmentX(LEFT_ALIGNMENT);

        // Group Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(priority.getColor());
                g2.fillOval(0, 4, 8, 8);
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(10, 16));
        headerPanel.add(dot);

        JLabel groupLabel = ThemeManager.createLabel(
            "PRIORIDAD " + priority.getLabel().toUpperCase() + " (" + tasks.size() + ")",
            ThemeManager.FONT_SECTION_HEADER,
            ThemeManager.getTextPrimary()
        );
        headerPanel.add(groupLabel);

        groupPanel.add(headerPanel);
        groupPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        // Dense Rows
        for (Task task : tasks) {
            groupPanel.add(createTaskRow(task));
        }

        return groupPanel;
    }

    private JPanel createTaskRow(Task task) {
        JPanel row = new JPanel(new BorderLayout(12, 0)) {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        setBackground(ThemeManager.getBgCardHover());
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        setBackground(ThemeManager.getBgCard());
                        repaint();
                    }
                });
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(ThemeManager.getBorderColor());
                // Hairline bottom row divider 1px
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };

        row.setBackground(ThemeManager.getBgCard());
        row.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        row.setPreferredSize(new Dimension(Integer.MAX_VALUE, 38));
        row.setAlignmentX(LEFT_ALIGNMENT);

        // Left: Monospace ID + Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftPanel.setOpaque(false);

        String formattedId = "TSK-" + (task.getId() != null ? task.getId().toUpperCase() : "000");
        JLabel idLabel = ThemeManager.createLabel(formattedId, ThemeManager.FONT_MONO, ThemeManager.getTextSecondary());
        JLabel titleLabel = ThemeManager.createLabel(task.getTitle(), ThemeManager.FONT_REGULAR, ThemeManager.getTextPrimary());

        leftPanel.add(idLabel);
        leftPanel.add(titleLabel);
        row.add(leftPanel, BorderLayout.WEST);

        // Right: Status Chip
        JLabel statusBadge = ThemeManager.createBadge(
            task.getStatus().getLabel(),
            task.getStatus().getColor(),
            ThemeManager.getBgCardHover()
        );
        row.add(statusBadge, BorderLayout.EAST);

        return row;
    }

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
