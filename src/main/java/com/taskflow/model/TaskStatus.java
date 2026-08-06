package com.taskflow.model;

import java.awt.Color;

/**
 * Enum simple para los estados de una tarea.
 */
public enum TaskStatus {
    TODO("Por Hacer", new Color(107, 114, 128)),
    IN_PROGRESS("En Proceso", new Color(37, 99, 235)),
    DONE("Finalizado", new Color(16, 185, 129));

    private final String label;
    private final Color color;

    TaskStatus(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    public TaskStatus next() {
        if (this == TODO) {
            return IN_PROGRESS;
        } else if (this == IN_PROGRESS) {
            return DONE;
        } else {
            return DONE;
        }
    }

    @Override
    public String toString() {
        return label;
    }
}
