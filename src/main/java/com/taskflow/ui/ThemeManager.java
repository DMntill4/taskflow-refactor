package com.taskflow.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Gestor de temas y estilos para la interfaz gráfica.
 * Define colores, fuentes y utilidades de estilo para un tema oscuro
 * inspirado en Notion/Trello.
 */
public class ThemeManager {

    // ==================== COLORES DEL TEMA OSCURO ====================

    /** Fondo principal de la aplicación */
    public static final Color BG_PRIMARY = new Color(25, 25, 35);
    /** Fondo secundario (paneles laterales) */
    public static final Color BG_SECONDARY = new Color(35, 35, 50);
    /** Fondo de las tarjetas */
    public static final Color BG_CARD = new Color(45, 45, 65);
    /** Fondo de las tarjetas al hacer hover */
    public static final Color BG_CARD_HOVER = new Color(55, 55, 80);
    /** Fondo de las columnas Kanban */
    public static final Color BG_COLUMN = new Color(30, 30, 45);
    /** Fondo del header */
    public static final Color BG_HEADER = new Color(20, 20, 30);
    /** Fondo de inputs */
    public static final Color BG_INPUT = new Color(40, 40, 60);

    /** Color del texto principal */
    public static final Color TEXT_PRIMARY = new Color(230, 230, 245);
    /** Color del texto secundario */
    public static final Color TEXT_SECONDARY = new Color(160, 160, 185);
    /** Color del texto terciario (muted) */
    public static final Color TEXT_MUTED = new Color(120, 120, 150);

    /** Color de acento principal (azul/púrpura) */
    public static final Color ACCENT = new Color(99, 102, 241);
    /** Color de acento hover */
    public static final Color ACCENT_HOVER = new Color(129, 132, 255);
    /** Color de acento sutil (para bordes) */
    public static final Color ACCENT_SUBTLE = new Color(99, 102, 241, 50);

    /** Color de éxito */
    public static final Color SUCCESS = new Color(34, 197, 94);
    /** Color de advertencia */
    public static final Color WARNING = new Color(245, 158, 11);
    /** Color de peligro */
    public static final Color DANGER = new Color(239, 68, 68);

    /** Color de bordes */
    public static final Color BORDER = new Color(60, 60, 85);
    /** Color de bordes sutiles */
    public static final Color BORDER_SUBTLE = new Color(50, 50, 70);

    // ==================== FUENTES ====================

    /** Fuente principal */
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    /** Fuente en negrita */
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    /** Fuente pequeña */
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    /** Fuente de título */
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    /** Fuente de título grande */
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
    /** Fuente de encabezado de columna */
    public static final Font FONT_COLUMN_HEADER = new Font("Segoe UI", Font.BOLD, 14);

    // ==================== DIMENSIONES ====================

    /** Radio de bordes redondeados */
    public static final int BORDER_RADIUS = 12;
    /** Padding estándar */
    public static final int PADDING = 12;
    /** Padding pequeño */
    public static final int PADDING_SMALL = 6;
    /** Gap entre elementos */
    public static final int GAP = 8;

    // ==================== UTILIDADES ====================

    /**
     * Crea un panel con fondo transparente.
     */
    public static JPanel createTransparentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Crea un botón estilizado con el tema oscuro.
     */
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.setColor(TEXT_PRIMARY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        button.setFont(FONT_BOLD);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 24, 36));
        return button;
    }

    /**
     * Crea un botón de acento (azul).
     */
    public static JButton createAccentButton(String text) {
        return createStyledButton(text, ACCENT);
    }

    /**
     * Crea un botón secundario (gris).
     */
    public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, BG_CARD);
    }

    /**
     * Crea un botón de peligro (rojo).
     */
    public static JButton createDangerButton(String text) {
        return createStyledButton(text, DANGER);
    }

    /**
     * Crea un borde redondeado.
     */
    public static Border createRoundedBorder(Color color, int radius) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1, true),
            BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)
        );
    }

    /**
     * Aplica el estilo de input al campo de texto.
     */
    public static void styleTextField(JTextField field) {
        field.setBackground(BG_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(FONT_REGULAR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    /**
     * Aplica estilo a un JTextArea.
     */
    public static void styleTextArea(JTextArea area) {
        area.setBackground(BG_INPUT);
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(TEXT_PRIMARY);
        area.setFont(FONT_REGULAR);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }

    /**
     * Aplica estilo a un JComboBox.
     */
    public static void styleComboBox(JComboBox<?> combo) {
        combo.setBackground(BG_INPUT);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(FONT_REGULAR);
        combo.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
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
     * Crea un separador horizontal estilizado.
     */
    public static JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_SUBTLE);
        sep.setBackground(BG_PRIMARY);
        return sep;
    }
}
