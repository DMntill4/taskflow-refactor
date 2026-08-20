package com.taskflow.persistence;

import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public List<Task> obtenerTodas() {
        List<Task> tareas = new ArrayList<>();
        String sql = "SELECT t.id_task, t.title, t.description, t.priority, t.id_status_task, a.id_person " +
                     "FROM task t " +
                     "LEFT JOIN assement_task a ON t.id_task = a.id_task";

        try (Connection conn = ConexionMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) return tareas;

            while (rs.next()) {
                Task t = new Task();
                t.setId(rs.getString("id_task"));
                t.setTitle(rs.getString("title"));
                t.setDescription(rs.getString("description"));
                t.setPriority(Priority.valueOf(rs.getString("priority")));
                
                int statusOrdinal = rs.getInt("id_status_task");
                if (statusOrdinal == 0) t.setStatus(TaskStatus.TODO);
                else if (statusOrdinal == 1) t.setStatus(TaskStatus.IN_PROGRESS);
                else if (statusOrdinal == 2) t.setStatus(TaskStatus.DONE);

                int assignedPersonId = rs.getInt("id_person");
                if (!rs.wasNull()) {
                    t.setAssignedUserId(String.valueOf(assignedPersonId));
                }

                tareas.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar tareas de MySQL: " + e.getMessage());
        }
        return tareas;
    }

    public void guardar(Task tarea) {
        String sqlTask = "INSERT INTO task (id_task, title, description, priority, id_status_task) VALUES (?, ?, ?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE title=?, description=?, priority=?, id_status_task=?";

        try (Connection conn = ConexionMySQL.conectar()) {
            if (conn == null) return;

            int statusId = tarea.getStatus() == TaskStatus.TODO ? 0 :
                           tarea.getStatus() == TaskStatus.IN_PROGRESS ? 1 : 2;

            try (PreparedStatement stmt = conn.prepareStatement(sqlTask)) {
                stmt.setString(1, tarea.getId());
                stmt.setString(2, tarea.getTitle());
                stmt.setString(3, tarea.getDescription());
                stmt.setString(4, tarea.getPriority().name());
                stmt.setInt(5, statusId);

                // For update
                stmt.setString(6, tarea.getTitle());
                stmt.setString(7, tarea.getDescription());
                stmt.setString(8, tarea.getPriority().name());
                stmt.setInt(9, statusId);

                stmt.executeUpdate();
            }

            // Actualizar asignación en assement_task si hay un usuario asignado
            actualizarAsignacion(conn, tarea);

        } catch (SQLException e) {
            System.err.println("Error al guardar tarea en MySQL: " + e.getMessage());
        }
    }

    private void actualizarAsignacion(Connection conn, Task tarea) throws SQLException {
        // Eliminar asignación previa para este id_task
        String sqlDelete = "DELETE FROM assement_task WHERE id_task = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
            stmt.setString(1, tarea.getId());
            stmt.executeUpdate();
        }

        // Si tiene persona asignada, insertar en assement_task
        if (tarea.getAssignedUserId() != null && !tarea.getAssignedUserId().isEmpty()) {
            try {
                int idPerson = Integer.parseInt(tarea.getAssignedUserId());
                String sqlInsert = "INSERT INTO assement_task (id_task, id_person) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                    stmt.setString(1, tarea.getId());
                    stmt.setInt(2, idPerson);
                    stmt.executeUpdate();
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    public void actualizarEstado(String idTask, TaskStatus nuevoEstado) {
        int statusId = nuevoEstado == TaskStatus.TODO ? 0 :
                       nuevoEstado == TaskStatus.IN_PROGRESS ? 1 : 2;

        String sql = "UPDATE task SET id_status_task = ? WHERE id_task = ?";
        try (Connection conn = ConexionMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return;
            stmt.setInt(1, statusId);
            stmt.setString(2, idTask);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado en MySQL: " + e.getMessage());
        }
    }

    public void eliminar(String idTask) {
        String sql = "DELETE FROM task WHERE id_task = ?";
        try (Connection conn = ConexionMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return;
            stmt.setString(1, idTask);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar tarea de MySQL: " + e.getMessage());
        }
    }
}
