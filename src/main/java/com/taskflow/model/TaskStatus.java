package com.taskflow.model;

import java.awt.Color;

/**
 * Enum que representa los estados posibles de una tarea.
 * Cada estado tiene un label en español y un color asociado.
 */
public enum TaskStatus {
    TODO("Por Hacer", new Color(148, 163, 184), "📋"),
    IN_PROGRESS("En Progreso", new Color(59, 130, 246), "🔄"),
    DONE("Finalizado", new Color(34, 197, 94), "✅");

    private final String label;
    private final Color color;
    private final String icon;

    TaskStatus(String label, Color color, String icon) {
        this.label = label;
        this.color = color;
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
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
