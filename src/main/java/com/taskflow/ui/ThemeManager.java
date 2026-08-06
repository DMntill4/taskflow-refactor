package com.taskflow.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Gestor de temas y estilos para la interfaz gráfica de TaskFlow.
 * Inspirado en la estética limpia, plana y densa de Linear, GitHub y Jira.
 * Soporta modo oscuro y modo claro con tokens de color disciplinados.
 */
public class ThemeManager {

    private static boolean darkMode = true; // Por defecto modo oscuro (Developer tool dark)

    // ==================== PALETAS DE COLOR ====================

    // Dark Mode Colors
    private static final Color DARK_BG_PRIMARY = new Color(13, 17, 23);       // #0D1117
    private static final Color DARK_BG_SECONDARY = new Color(22, 27, 34);     // #161B22
    private static final Color DARK_BG_CARD = new Color(22, 27, 34);          // #161B22
    private static final Color DARK_BG_CARD_HOVER = new Color(33, 38, 45);    // #21262D
    private static final Color DARK_BG_INPUT = new Color(13, 17, 23);         // #0D1117
    private static final Color DARK_BORDER = new Color(48, 54, 61);           // #30363D
    private static final Color DARK_TEXT_PRIMARY = new Color(230, 237, 243);  // #E6EDF3
    private static final Color DARK_TEXT_SECONDARY = new Color(139, 148, 158);// #8B949E
    private static final Color DARK_TEXT_MUTED = new Color(110, 119, 129);    // #6E7781
    private static final Color DARK_ACCENT = new Color(59, 130, 246);         // #3B82F6
    private static final Color DARK_ACCENT_HOVER = new Color(37, 99, 235);   // #2563EB

    // Light Mode Colors
    private static final Color LIGHT_BG_PRIMARY = new Color(255, 255, 255);    // #FFFFFF
    private static final Color LIGHT_BG_SECONDARY = new Color(247, 248, 250);  // #F7F8FA
    private static final Color LIGHT_BG_CARD = new Color(255, 255, 255);       // #FFFFFF
    private static final Color LIGHT_BG_CARD_HOVER = new Color(247, 248, 250); // #F7F8FA
    private static final Color LIGHT_BG_INPUT = new Color(255, 255, 255);      // #FFFFFF
    private static final Color LIGHT_BORDER = new Color(225, 228, 232);        // #E1E4E8
    private static final Color LIGHT_TEXT_PRIMARY = new Color(31, 35, 40);     // #1F2328
    private static final Color LIGHT_TEXT_SECONDARY = new Color(110, 119, 129); // #6E7781
    private static final Color LIGHT_TEXT_MUTED = new Color(139, 148, 158);   // #8B949E
    private static final Color LIGHT_ACCENT = new Color(37, 99, 235);        // #2563EB
    private static final Color LIGHT_ACCENT_HOVER = new Color(29, 78, 216);   // #1D4ED8

    // Status Pill Colors (Desaturated, neutral dev tools)
    public static final Color STATUS_TODO_FG = new Color(110, 119, 129);      // #6E7781
    public static final Color STATUS_TODO_BG_DARK = new Color(33, 38, 45);   // #21262D
    public static final Color STATUS_TODO_BG_LIGHT = new Color(241, 242, 244);// #F1F2F4

    public static final Color STATUS_IN_PROGRESS_FG = new Color(37, 99, 235); // #2563EB
    public static final Color STATUS_IN_PROGRESS_BG_DARK = new Color(28, 45, 66); // #1C2D42
    public static final Color STATUS_IN_PROGRESS_BG_LIGHT = new Color(239, 244, 255); // #EFF4FF

    public static final Color STATUS_DONE_FG = new Color(26, 127, 55);       // #1A7F37
    public static final Color STATUS_DONE_BG_DARK = new Color(18, 44, 26);   // #122C1A
    public static final Color STATUS_DONE_BG_LIGHT = new Color(233, 247, 239);// #E9F7EF

    // Priority Indicator Colors (Dots)
    public static final Color PRIORITY_URGENT = new Color(207, 34, 46);   // Red #CF222E
    public static final Color PRIORITY_HIGH = new Color(217, 119, 6);     // Orange #D97706
    public static final Color PRIORITY_MEDIUM = new Color(212, 167, 44);   // Yellow #D4A72C
    public static final Color PRIORITY_LOW = new Color(139, 148, 158);    // Gray #8B949E

    // Dynamic Getters according to current theme
    public static Color getBgPrimary() { return darkMode ? DARK_BG_PRIMARY : LIGHT_BG_PRIMARY; }
    public static Color getBgSecondary() { return darkMode ? DARK_BG_SECONDARY : LIGHT_BG_SECONDARY; }
    public static Color getBgCard() { return darkMode ? DARK_BG_CARD : LIGHT_BG_CARD; }
    public static Color getBgCardHover() { return darkMode ? DARK_BG_CARD_HOVER : LIGHT_BG_CARD_HOVER; }
    public static Color getBgInput() { return darkMode ? DARK_BG_INPUT : LIGHT_BG_INPUT; }
    public static Color getBorderColor() { return darkMode ? DARK_BORDER : LIGHT_BORDER; }
    public static Color getTextPrimary() { return darkMode ? DARK_TEXT_PRIMARY : LIGHT_TEXT_PRIMARY; }
    public static Color getTextSecondary() { return darkMode ? DARK_TEXT_SECONDARY : LIGHT_TEXT_SECONDARY; }
    public static Color getTextMuted() { return darkMode ? DARK_TEXT_MUTED : LIGHT_TEXT_MUTED; }
    public static Color getAccent() { return darkMode ? DARK_ACCENT : LIGHT_ACCENT; }
    public static Color getAccentHover() { return darkMode ? DARK_ACCENT_HOVER : LIGHT_ACCENT_HOVER; }

    public static boolean isDarkMode() { return darkMode; }
    public static void setDarkMode(boolean dark) { darkMode = dark; }

    // Legacy public constants for components referencing static fields
    public static final Color BG_PRIMARY = DARK_BG_PRIMARY;
    public static final Color BG_SECONDARY = DARK_BG_SECONDARY;
    public static final Color BG_CARD = DARK_BG_CARD;
    public static final Color BG_CARD_HOVER = DARK_BG_CARD_HOVER;
    public static final Color BG_COLUMN = DARK_BG_SECONDARY;
    public static final Color BG_HEADER = DARK_BG_PRIMARY;
    public static final Color BG_INPUT = DARK_BG_INPUT;

    public static final Color TEXT_PRIMARY = DARK_TEXT_PRIMARY;
    public static final Color TEXT_SECONDARY = DARK_TEXT_SECONDARY;
    public static final Color TEXT_MUTED = DARK_TEXT_MUTED;

    public static final Color ACCENT = DARK_ACCENT;
    public static final Color ACCENT_HOVER = DARK_ACCENT_HOVER;
    public static final Color ACCENT_SUBTLE = new Color(59, 130, 246, 40);

    public static final Color SUCCESS = STATUS_DONE_FG;
    public static final Color WARNING = PRIORITY_MEDIUM;
    public static final Color DANGER = PRIORITY_URGENT;

    public static final Color BORDER = DARK_BORDER;
    public static final Color BORDER_SUBTLE = DARK_BORDER;

    // ==================== TIPOGRAFÍA ====================
    // Neutral system font stack + JetBrains Mono / SF Mono / Consolas for IDs

    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_MEDIUM = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SECTION_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_COLUMN_HEADER = new Font("Segoe UI", Font.BOLD, 13);

    // Monospace font for Issue IDs (e.g. TSK-104)
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_MONO_BOLD = new Font("Consolas", Font.BOLD, 12);

    // ==================== DIMENSIONES & BORDES ====================

    /** Radio de bordes máximo de 6px (estilo plano de herramientas de dev) */
    public static final int BORDER_RADIUS = 6;
    public static final int PADDING = 12;
    public static final int PADDING_SMALL = 6;
    public static final int GAP = 8;

    // ==================== COMPONENTES & UTILIDADES ====================

    public static JPanel createTransparentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Crea un botón primario con relleno plano (32px de alto, 6px de radio).
     */
    public static JButton createAccentButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bg = getModel().isRollover() ? getAccentHover() : getAccent();
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        button.setFont(FONT_MEDIUM);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 24, 32));
        return button;
    }

    /**
     * Crea un botón secundario con borde sutil de 1px (estilo ghost/border-only).
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2.setColor(getBgCardHover());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
                }
                
                // Border hairline 1px
                g2.setColor(getBorderColor());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
                
                g2.setColor(getTextPrimary());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        button.setFont(FONT_REGULAR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 20, 32));
        return button;
    }

    /**
     * Crea un botón de acción de peligro (rojo plano).
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bg = getModel().isRollover() ? DANGER.darker() : DANGER;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        button.setFont(FONT_MEDIUM);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 20, 32));
        return button;
    }

    /**
     * Aplica el estilo de campo de texto con borde sutil de 1px.
     */
    public static void styleTextField(JTextField field) {
        field.setBackground(getBgInput());
        field.setForeground(getTextPrimary());
        field.setCaretColor(getTextPrimary());
        field.setFont(FONT_REGULAR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getBorderColor(), 1, false),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    /**
     * Aplica el estilo a un JTextArea con borde sutil de 1px.
     */
    public static void styleTextArea(JTextArea area) {
        area.setBackground(getBgInput());
        area.setForeground(getTextPrimary());
        area.setCaretColor(getTextPrimary());
        area.setFont(FONT_REGULAR);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getBorderColor(), 1, false),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }

    /**
     * Aplica el estilo a un JComboBox.
     */
    public static void styleComboBox(JComboBox<?> combo) {
        combo.setBackground(getBgInput());
        combo.setForeground(getTextPrimary());
        combo.setFont(FONT_REGULAR);
        combo.setBorder(BorderFactory.createLineBorder(getBorderColor(), 1, false));
    }

    /**
     * Crea un JLabel estilizado.
     */
    public static JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    /**
     * Crea un badge compacto (chip) con 4px de radio.
     */
    public static JLabel createBadge(String text, Color fgColor, Color bgColor) {
        JLabel badge = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        badge.setFont(FONT_SMALL);
        badge.setForeground(fgColor);
        badge.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        badge.setOpaque(false);
        return badge;
    }

    /**
     * Crea un avatar circular compacto con las iniciales del usuario.
     */
    public static JPanel createAvatar(String name, int size) {
        String initials = "";
        if (name != null && !name.trim().isEmpty()) {
            String[] parts = name.trim().split("\\s+");
            if (parts.length >= 2) {
                initials = ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
            } else if (parts[0].length() >= 2) {
                initials = parts[0].substring(0, 2).toUpperCase();
            } else {
                initials = parts[0].toUpperCase();
            }
        } else {
            initials = "?";
        }

        final String avatarInitials = initials;
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBgCardHover());
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                g2.setColor(getBorderColor());
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                
                g2.setColor(getTextSecondary());
                g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(10, size / 2 - 1)));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(avatarInitials)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(avatarInitials, x, y);
                
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(size, size));
        avatar.setToolTipText(name);
        return avatar;
    }

    /**
     * Crea un separador horizontal sutil de 1px.
     */
    public static JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(getBorderColor());
        sep.setBackground(getBgPrimary());
        return sep;
    }
}
