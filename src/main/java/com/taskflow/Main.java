package com.taskflow;

import com.taskflow.ui.MainFrame;
import javax.swing.SwingUtilities;

/**
 * Clase principal y punto de entrada de la aplicación TaskFlow.
 * Sencilla y fácil de explicar en la exposición.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando TaskFlow y conectando a MySQL...");
        
        // Inicializar el gestor de tareas (conecta a MySQL y carga datos)
        TaskManager taskManager = new TaskManager();

        // Lanzar la ventana gráfica principal en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame ventana = new MainFrame(taskManager);
            ventana.setVisible(true);
            System.out.println("Aplicacion iniciada correctamente desde Main.");
        });
    }
}

