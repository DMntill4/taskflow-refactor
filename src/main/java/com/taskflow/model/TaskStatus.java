package com.taskflow.model;

import java.awt.Color;

/**
 * Enum que representa los estados posibles de una tarea.
 * Estilo de herramientas de desarrollo (Linear/Jira/GitHub) sin emojis.
 */
public enum TaskStatus {
    TODO("Por Hacer", new Color(110, 119, 129)),       // Gray #6E7781
    IN_PROGRESS("En Progreso", new Color(37, 99, 235)), // Blue #2563EB
    DONE("Finalizado", new Color(26, 127, 55));         // Green #1A7F37

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

    /**
     * Retorna el siguiente estado en el flujo de trabajo.
     * TODO -> IN_PROGRESS -> DONE -> DONE
     */
    public TaskStatus next() {
        switch (this) {
            case TODO: return IN_PROGRESS;
            case IN_PROGRESS: return DONE;
            default: return DONE;
        }
    }

    @Override
    public String toString() {
        return label;
    }
}

