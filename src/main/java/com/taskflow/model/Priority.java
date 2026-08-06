package com.taskflow.model;

import java.awt.Color;

/**
 * Enum que representa los niveles de prioridad de una tarea.
 * Cada prioridad tiene un label en español y un color asociado para la UI.
 */
public enum Priority {
    HIGH("Alta", new Color(239, 68, 68), new Color(254, 226, 226)),      // Rojo
    MEDIUM("Media", new Color(245, 158, 11), new Color(254, 243, 199)),  // Amarillo/Naranja
    LOW("Baja", new Color(34, 197, 94), new Color(220, 252, 231));       // Verde

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
