package com.taskflow.model;

import java.awt.Color;

/**
 * Enum que representa los niveles de prioridad de una tarea.
 * Estilo desaturado para herramientas de desarrollo (Linear/Jira/GitHub).
 */
public enum Priority {
    HIGH("Alta", new Color(207, 34, 46), new Color(60, 22, 24)),      // Red #CF222E
    MEDIUM("Media", new Color(212, 167, 44), new Color(54, 37, 18)),  // Yellow #D4A72C
    LOW("Baja", new Color(139, 148, 158), new Color(33, 38, 45));     // Gray #8B949E

    private final String label;
    private final Color color;
    private final Color backgroundColor;

    Priority(String label, Color color, Color backgroundColor) {
        this.label = label;
        this.color = color;
        this.backgroundColor = backgroundColor;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public String toString() {
        return label;
    }
}

