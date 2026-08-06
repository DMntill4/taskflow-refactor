package com.taskflow.model;

import java.awt.Color;

/**
 * Enum simple para las prioridades de una tarea.
 */
public enum Priority {
    HIGH("Alta", new Color(220, 38, 38)),     // Rojo
    MEDIUM("Media", new Color(217, 119, 6)),  // Naranja / Amarillo
    LOW("Baja", new Color(75, 85, 99));       // Gris

    private final String label;
    private final Color color;

    Priority(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return label;
    }
}
