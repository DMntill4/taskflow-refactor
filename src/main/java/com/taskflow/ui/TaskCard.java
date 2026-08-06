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
 * Tarjeta Kanban densa e inspirada en Linear/GitHub.
 * Muestra el identificador monoespaciado (ej: TSK-A1B2), título, punto de prioridad,
 * avatar con iniciales del usuario asignado y botones de acción sutiles.
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
        setBackground(ThemeManager.getBgCard());
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));

        buildCard();
        addHoverEffect();
    }

    private void buildCard() {
        // --- Top Header: Monospace Issue ID + Priority Dot ---
        JPanel topPanel = new JPanel(new BorderLayout(6, 0));
        topPanel.setOpaque(false);

        // Monospace Task ID (e.g. TSK-A1B2C3D4)
        String formattedId = "TSK-" + (task.getId() != null ? task.getId().toUpperCase() : "000");
        JLabel idLabel = ThemeManager.createLabel(formattedId, ThemeManager.FONT_MONO, ThemeManager.getTextSecondary());

        // Small 8px Priority Indicator Dot
        JPanel priorityDot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(task.getPriority().getColor());
                g2.fillOval(2, 4, 8, 8);
                g2.dispose();
            }
        };
        priorityDot.setOpaque(false);
        priorityDot.setPreferredSize(new Dimension(12, 16));
        priorityDot.setToolTipText("Prioridad: " + task.getPriority().getLabel());

        topPanel.add(idLabel, BorderLayout.WEST);
        topPanel.add(priorityDot, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- Center: Title + Description ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JLabel titleLabel = ThemeManager.createLabel(task.getTitle(), ThemeManager.FONT_MEDIUM, ThemeManager.getTextPrimary());
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        centerPanel.add(titleLabel);

        if (task.getDescription() != null && !task.getDescription().trim().isEmpty()) {
            String desc = task.getDescription().trim();
            if (desc.length() > 55) {
                desc = desc.substring(0, 52) + "...";
            }
            JLabel descLabel = ThemeManager.createLabel(desc, ThemeManager.FONT_SMALL, ThemeManager.getTextMuted());
            descLabel.setAlignmentX(LEFT_ALIGNMENT);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            centerPanel.add(descLabel);
        }

        add(centerPanel, BorderLayout.CENTER);

        // --- Bottom: User Avatar + Action Buttons ---
        JPanel bottomPanel = new JPanel(new BorderLayout(4, 0));
        bottomPanel.setOpaque(false);

        // User Avatar & Name
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        userPanel.setOpaque(false);

        if (task.getAssignedUserId() != null) {
            User user = userService.getUserById(task.getAssignedUserId());
            if (user != null) {
                userPanel.add(ThemeManager.createAvatar(user.getName(), 22));
                JLabel userNameLabel = ThemeManager.createLabel(user.getName(), ThemeManager.FONT_SMALL, ThemeManager.getTextSecondary());
                userPanel.add(userNameLabel);
            }
        } else {
            JLabel unassignedLabel = ThemeManager.createLabel("Sin asignar", ThemeManager.FONT_SMALL, ThemeManager.getTextMuted());
            userPanel.add(unassignedLabel);
        }

        bottomPanel.add(userPanel, BorderLayout.WEST);

        // Action Buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        actionsPanel.setOpaque(false);

        if (task.getStatus() != com.taskflow.model.TaskStatus.DONE) {
            JButton advanceBtn = createGhostButton("▶");
            advanceBtn.setToolTipText("Mover a: " + task.getStatus().next().getLabel());
            advanceBtn.addActionListener(e -> {
                taskService.advanceTaskStatus(task.getId());
                onRefresh.run();
            });
            actionsPanel.add(advanceBtn);
        }

        JButton deleteBtn = createGhostButton("✕");
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

    private JButton createGhostButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(ThemeManager.FONT_SMALL);
        btn.setForeground(ThemeManager.getTextMuted());
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(24, 20));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(ThemeManager.getAccent());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(ThemeManager.getTextMuted());
            }
        });
        return btn;
    }

    private void addHoverEffect() {
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
        
        // Hairline 1px border. Darkens on hover
        g2.setColor(hovered ? ThemeManager.getAccent() : ThemeManager.getBorderColor());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ThemeManager.BORDER_RADIUS, ThemeManager.BORDER_RADIUS);
        
        g2.dispose();
    }

    public Task getTask() {
        return task;
    }
}
