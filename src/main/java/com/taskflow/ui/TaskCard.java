package com.taskflow.ui;

import com.taskflow.model.Task;
import com.taskflow.model.User;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Componente visual que representa una tarjeta de tarea en el tablero Kanban.
 * Muestra el título, prioridad (con indicador de color), usuario asignado,
 * y botones de acción para cambiar estado o editar.
 */
public class TaskCard extends JPanel {
    private final Task task;
    private final TaskService taskService;
    private final UserService userService;
    private final Runnable onRefresh;
    private boolean hovered = false;

    public TaskCard(Task task, TaskService taskService, UserService userService, Runnable onRefresh) {
        this.task = task;
        this.taskService = taskService;
        this.userService = userService;
        this.onRefresh = onRefresh;

        setLayout(new BorderLayout(0, 6));
        setBackground(ThemeManager.BG_CARD);
        setBorder(BorderFactory.createEmptyBorder(
            ThemeManager.PADDING, ThemeManager.PADDING,
            ThemeManager.PADDING, ThemeManager.PADDING
        ));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        buildCard();
        addHoverEffect();
    }

    private void buildCard() {
        // --- Top: Priority indicator + Title ---
        JPanel topPanel = new JPanel(new BorderLayout(8, 0));
        topPanel.setOpaque(false);

        // Priority dot
        JPanel priorityDot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(task.getPriority().getColor());
                g2.fillOval(2, 2, 10, 10);
                g2.dispose();
            }
        };
        priorityDot.setOpaque(false);
        priorityDot.setPreferredSize(new Dimension(14, 14));
        priorityDot.setToolTipText("Prioridad: " + task.getPriority().getLabel());

        JLabel titleLabel = ThemeManager.createLabel(task.getTitle(), ThemeManager.FONT_BOLD, ThemeManager.TEXT_PRIMARY);
        titleLabel.setToolTipText(task.getTitle());

        topPanel.add(priorityDot, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // --- Center: Description (truncated) ---
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            String desc = task.getDescription();
            if (desc.length() > 60) {
                desc = desc.substring(0, 57) + "...";
            }
            JLabel descLabel = ThemeManager.createLabel(desc, ThemeManager.FONT_SMALL, ThemeManager.TEXT_SECONDARY);
            add(descLabel, BorderLayout.CENTER);
        }

        // --- Bottom: User + Priority badge + Actions ---
        JPanel bottomPanel = new JPanel(new BorderLayout(4, 0));
        bottomPanel.setOpaque(false);

        // Left: User info + Priority badge
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        infoPanel.setOpaque(false);

        // Priority badge
        JLabel priorityBadge = new JLabel(task.getPriority().getLabel()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(
                    task.getPriority().getColor().getRed(),
                    task.getPriority().getColor().getGreen(),
                    task.getPriority().getColor().getBlue(),
                    40
                ));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        priorityBadge.setFont(ThemeManager.FONT_SMALL);
        priorityBadge.setForeground(task.getPriority().getColor());
        priorityBadge.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        priorityBadge.setOpaque(false);
        infoPanel.add(priorityBadge);

        // User badge
        if (task.getAssignedUserId() != null) {
            User user = userService.getUserById(task.getAssignedUserId());
            if (user != null) {
                JLabel userLabel = new JLabel("👤 " + user.getName());
                userLabel.setFont(ThemeManager.FONT_SMALL);
                userLabel.setForeground(ThemeManager.TEXT_MUTED);
                infoPanel.add(userLabel);
            }
        }

        bottomPanel.add(infoPanel, BorderLayout.CENTER);

        // Right: Action buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        actionsPanel.setOpaque(false);

        // Advance status button
        if (task.getStatus() != task.getStatus().next() || task.getStatus() != com.taskflow.model.TaskStatus.DONE) {
            JButton advanceBtn = createSmallButton("▶");
            advanceBtn.setToolTipText("Avanzar a: " + task.getStatus().next().getLabel());
            advanceBtn.addActionListener(e -> {
                taskService.advanceTaskStatus(task.getId());
                onRefresh.run();
            });
            if (task.getStatus() != com.taskflow.model.TaskStatus.DONE) {
                actionsPanel.add(advanceBtn);
            }
        }

        // Delete button
        JButton deleteBtn = createSmallButton("✕");
        deleteBtn.setToolTipText("Eliminar tarea");
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, "¿Eliminar la tarea \"" + task.getTitle() + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                taskService.deleteTask(task.getId());
                onRefresh.run();
            }
        });
        actionsPanel.add(deleteBtn);

        bottomPanel.add(actionsPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createSmallButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setForeground(ThemeManager.TEXT_MUTED);
        btn.setBackground(ThemeManager.BG_CARD);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(28, 24));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(ThemeManager.ACCENT);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(ThemeManager.TEXT_MUTED);
            }
        });
        return btn;
    }

    private void addHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                setBackground(ThemeManager.BG_CARD_HOVER);
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                setBackground(ThemeManager.BG_CARD);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ThemeManager.BORDER_RADIUS, ThemeManager.BORDER_RADIUS);

        // Left border with priority color
        g2.setColor(task.getPriority().getColor());
        g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(hovered ? ThemeManager.ACCENT_SUBTLE : ThemeManager.BORDER_SUBTLE);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
            ThemeManager.BORDER_RADIUS, ThemeManager.BORDER_RADIUS);
        g2.dispose();
    }

    public Task getTask() {
        return task;
    }
}
