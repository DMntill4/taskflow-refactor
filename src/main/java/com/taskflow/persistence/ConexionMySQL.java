package com.taskflow.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionMySQL {
    // Parámetros de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/taskflowDb";
    private static final String USER = "root";
    private static final String PASSWORD = "-3ta9}OK`4[Y";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer conexión
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion establecida");

            // Crear las tablas automáticamente si no existen y vaciar datos de prueba
            crearTablasSiNoExisten(conexion);
            vaciarDatosPrevios(conexion);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontro el driver JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL: " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }

    private static void crearTablasSiNoExisten(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS type_person (" +
                    "id_type_person INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50) NOT NULL)");

            stmt.executeUpdate("INSERT IGNORE INTO type_person (id_type_person, name) VALUES (1, 'Desarrollador'), (2, 'Líder de Proyecto'), (3, 'Administrador')");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS person (" +
                    "id_person INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, email VARCHAR(100), id_type_person INT, " +
                    "FOREIGN KEY (id_type_person) REFERENCES type_person(id_type_person) ON DELETE SET NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS team (" +
                    "id_team INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL)");

            stmt.executeUpdate("INSERT IGNORE INTO team (id_team, name) VALUES (1, 'Equipo Desarrollo Backend'), (2, 'Equipo Frontend')");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS team_person (" +
                    "id_team INT, id_person INT, PRIMARY KEY (id_team, id_person), " +
                    "FOREIGN KEY (id_team) REFERENCES team(id_team) ON DELETE CASCADE, " +
                    "FOREIGN KEY (id_person) REFERENCES person(id_person) ON DELETE CASCADE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS status_task (" +
                    "id_status_task INT PRIMARY KEY, name VARCHAR(50) NOT NULL)");

            stmt.executeUpdate("INSERT IGNORE INTO status_task (id_status_task, name) VALUES (0, 'Por Hacer'), (1, 'En Progreso'), (2, 'Completada')");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS task (" +
                    "id_task VARCHAR(50) PRIMARY KEY, title VARCHAR(150) NOT NULL, description TEXT, priority VARCHAR(20) NOT NULL, id_status_task INT NOT NULL, " +
                    "FOREIGN KEY (id_status_task) REFERENCES status_task(id_status_task))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS assement_task (" +
                    "id_assement INT AUTO_INCREMENT PRIMARY KEY, id_task VARCHAR(50) NOT NULL, id_person INT NOT NULL, assigned_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, notes TEXT, " +
                    "FOREIGN KEY (id_task) REFERENCES task(id_task) ON DELETE CASCADE, " +
                    "FOREIGN KEY (id_person) REFERENCES person(id_person) ON DELETE CASCADE)");

        } catch (SQLException e) {
            System.err.println("Error al crear tablas en MySQL: " + e.getMessage());
        }
    }

    private static boolean yaLimpio = false;

    private static void vaciarDatosPrevios(Connection conn) {
        if (yaLimpio) return;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("TRUNCATE TABLE assement_task");
            stmt.executeUpdate("TRUNCATE TABLE task");
            stmt.executeUpdate("TRUNCATE TABLE team_person");
            stmt.executeUpdate("TRUNCATE TABLE person");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            yaLimpio = true;
        } catch (SQLException e) {
            System.err.println("Error al limpiar base de datos: " + e.getMessage());
        }
    }
}
