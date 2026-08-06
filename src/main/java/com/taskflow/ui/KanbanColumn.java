package com.taskflow.ui;

import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Columna Kanban densa y limpia estilo Linear/GitHub.
 * Muestra el estado, el contador de tareas en un badge monoespaciado y las tarjetas alineadas.
 */
public class KanbanColumn extends JPanel {
    private final TaskStatus status;
    private final TaskService taskService;
    private final UserService userService;
    private final Runnable onRefresh;
    private JPanel cardsContainer;
    private JLabel countBadge;

    public KanbanColumn(TaskStatus status, TaskService taskService, UserService userService, Runnable onRefresh) {
        this.status = status;
        this.taskService = taskService;
        this.userService = userService;
        this.onRefresh = onRefresh;

        setLayout(new BorderLayout(0, 8));
        setBackground(ThemeManager.getBgSecondary());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        buildColumn();
    }

    private void buildColumn() {
        // --- Header: Status title + Counter Badge ---
        JPanel headerPanel = new JPanel(new BorderLayout(8, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.getBorderColor()),
            BorderFactory.createEmptyBorder(0, 0, 8, 0)
        ));

        // Left: Status dot + Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        titlePanel.setOpaque(false);

        JPanel statusDot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(status.getColor());
                g2.fillOval(0, 4, 8, 8);
                g2.dispose();
            }
        };
        statusDot.setOpaque(false);
        statusDot.setPreferredSize(new Dimension(10, 16));

        JLabel titleLabel = ThemeManager.createLabel(
            status.getLabel().toUpperCase(),
            ThemeManager.FONT_SECTION_HEADER,
            ThemeManager.getTextPrimary()
        );

        titlePanel.add(statusDot);
        titlePanel.add(titleLabel);

        // Right: Counter Badge (Monospace)
        countBadge = ThemeManager.createBadge("0", ThemeManager.getTextSecondary(), ThemeManager.getBgCardHover());
        countBadge.setFont(ThemeManager.FONT_MONO_BOLD);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(countBadge, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Cards container with scroll ---
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(ThemeManager.getBgSecondary());

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBackground(ThemeManager.getBgSecondary());
        scrollPane.getViewport().setBackground(ThemeManager.getBgSecondary());
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        scrollPane.getVerticalScrollBar().setBackground(ThemeManager.getBgSecondary());

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Actualiza las tarjetas de la columna con las tareas proporcionadas.
     */
    public void refreshTasks(List<Task> tasks) {
        cardsContainer.removeAll();

        List<Task> filteredTasks = tasks.stream()
                .filter(t -> t.getStatus() == status)
                .collect(java.util.stream.Collectors.toList());

        countBadge.setText(String.valueOf(filteredTasks.size()));

        for (Task task : filteredTasks) {
            TaskCard card = new TaskCard(task, taskService, userService, onRefresh);
            cardsContainer.add(card);
            cardsContainer.add(Box.createRigidArea(new Dimension(0, ThemeManager.GAP)));
        }

        // Empty state: Minimalist, no heavy illustration
        if (filteredTasks.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setOpaque(false);
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));

            JLabel emptyLabel = ThemeManager.createLabel(
                "No hay tareas en esta columna", ThemeManager.FONT_SMALL, ThemeManager.getTextMuted()
            );
            emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
            emptyPanel.add(emptyLabel);
            cardsContainer.add(emptyPanel);
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ThemeManager.BORDER_RADIUS, ThemeManager.BORDER_RADIUS);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(ThemeManager.getBorderColor());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ThemeManager.BORDER_RADIUS, ThemeManager.BORDER_RADIUS);
        g2.dispose();
    }

    public TaskStatus getStatus() {
        return status;
    }
}
