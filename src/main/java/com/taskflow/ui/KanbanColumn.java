package com.taskflow.ui;

import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Columna del tablero Kanban que muestra las tareas de un estado específico.
 * Cada columna tiene un header con el nombre del estado, un contador de tareas,
 * y un área scrollable con las tarjetas de tarea.
 */
public class KanbanColumn extends JPanel {
    private final TaskStatus status;
    private final TaskService taskService;
    private final UserService userService;
    private final Runnable onRefresh;
    private JPanel cardsContainer;
    private JLabel countLabel;

    public KanbanColumn(TaskStatus status, TaskService taskService, UserService userService, Runnable onRefresh) {
        this.status = status;
        this.taskService = taskService;
        this.userService = userService;
        this.onRefresh = onRefresh;

        setLayout(new BorderLayout(0, 8));
        setBackground(ThemeManager.BG_COLUMN);
        setBorder(BorderFactory.createEmptyBorder(
            ThemeManager.PADDING, ThemeManager.PADDING,
            ThemeManager.PADDING, ThemeManager.PADDING
        ));

        buildColumn();
    }

    private void buildColumn() {
        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout(8, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // Status indicator + title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        titlePanel.setOpaque(false);

        // Status color dot
        JPanel statusDot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(status.getColor());
                g2.fillOval(0, 2, 12, 12);
                g2.dispose();
            }
        };
        statusDot.setOpaque(false);
        statusDot.setPreferredSize(new Dimension(12, 16));

        JLabel titleLabel = ThemeManager.createLabel(
            status.getIcon() + " " + status.getLabel(),
            ThemeManager.FONT_COLUMN_HEADER,
            ThemeManager.TEXT_PRIMARY
        );

        countLabel = new JLabel("0");
        countLabel.setFont(ThemeManager.FONT_SMALL);
        countLabel.setForeground(ThemeManager.TEXT_MUTED);
        countLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        titlePanel.add(statusDot);
        titlePanel.add(titleLabel);
        titlePanel.add(countLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // --- Cards container with scroll ---
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(ThemeManager.BG_COLUMN);

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBackground(ThemeManager.BG_COLUMN);
        scrollPane.getViewport().setBackground(ThemeManager.BG_COLUMN);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Style the scrollbar
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        scrollPane.getVerticalScrollBar().setBackground(ThemeManager.BG_COLUMN);

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

        countLabel.setText(String.valueOf(filteredTasks.size()));

        for (Task task : filteredTasks) {
            TaskCard card = new TaskCard(task, taskService, userService, onRefresh);
            cardsContainer.add(card);
            cardsContainer.add(Box.createRigidArea(new Dimension(0, ThemeManager.GAP)));
        }

        // Add empty state if no tasks
        if (filteredTasks.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setOpaque(false);
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

            JLabel emptyLabel = ThemeManager.createLabel(
                "Sin tareas", ThemeManager.FONT_SMALL, ThemeManager.TEXT_MUTED
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

    public TaskStatus getStatus() {
        return status;
    }
}
