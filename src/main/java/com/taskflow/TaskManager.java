package com.taskflow;

import com.taskflow.model.Person;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.model.User;
import com.taskflow.persistence.PersonDAO;
import com.taskflow.persistence.TaskDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor único de tareas y personas/usuarios.
 * Conectado directamente a la base de datos MySQL (con fallback si no hay conexión).
 */
public class TaskManager {
    private final TaskDAO taskDAO;
    private final PersonDAO personDAO;

    private List<Task> tareas;
    private List<User> usuarios;

    public TaskManager() {
        this.taskDAO = new TaskDAO();
        this.personDAO = new PersonDAO();
        this.tareas = new ArrayList<>();
        this.usuarios = new ArrayList<>();

        cargarDatos();
    }

    public void cargarDatos() {
        // Cargar desde MySQL
        tareas = taskDAO.obtenerTodas();
        List<Person> personas = personDAO.obtenerTodas();
        
        usuarios = new ArrayList<>();
        for (Person p : personas) {
            User u = new User();
            u.setId(String.valueOf(p.getIdPerson()));
            u.setName(p.getName());
            usuarios.add(u);
        }
    }

    public void guardarDatos() {
        for (Task t : tareas) {
            taskDAO.guardar(t);
        }
    }



    // ==================== OPERACIONES DE USUARIOS / PERSONAS ====================

    public List<User> obtenerUsuarios() {
        return usuarios;
    }

    public User crearUsuario(String nombre) {
        return crearUsuario(nombre, 1, 1);
    }

    public User crearUsuario(String nombre, int idTypePerson, int idTeam) {
        Person p = personDAO.guardar(nombre, idTypePerson, idTeam);
        User u = new User();
        u.setId(String.valueOf(p.getIdPerson()));
        u.setName(p.getName());
        usuarios.add(u);
        return u;
    }

    public PersonDAO getPersonDAO() {
        return personDAO;
    }

    public User obtenerUsuarioPorId(String id) {
        if (id == null) return null;
        for (User u : usuarios) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    // ==================== OPERACIONES DE TAREAS ====================

    public List<Task> obtenerTareas() {
        return tareas;
    }

    public Task crearTarea(String titulo, String descripcion, Priority prioridad, String usuarioId) {
        Task tarea = new Task(titulo, descripcion, prioridad);
        tarea.setAssignedUserId(usuarioId);
        tareas.add(tarea);
        taskDAO.guardar(tarea);
        return tarea;
    }

    public void avanzarEstadoTarea(String tareaId) {
        for (Task t : tareas) {
            if (t.getId().equals(tareaId)) {
                TaskStatus nuevo = t.getStatus().next();
                t.setStatus(nuevo);
                taskDAO.actualizarEstado(tareaId, nuevo);
                break;
            }
        }
    }

    public void cambiarEstadoTarea(String tareaId, TaskStatus nuevoEstado) {
        if (nuevoEstado == null) return;
        for (Task t : tareas) {
            if (t.getId().equals(tareaId)) {
                t.setStatus(nuevoEstado);
                taskDAO.actualizarEstado(tareaId, nuevoEstado);
                break;
            }
        }
    }

    public void eliminarTarea(String tareaId) {
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).getId().equals(tareaId)) {
                tareas.remove(i);
                taskDAO.eliminar(tareaId);
                break;
            }
        }
    }

    public List<Task> obtenerTareasPorEstado(TaskStatus estado) {
        List<Task> resultado = new ArrayList<>();
        for (Task t : tareas) {
            if (t.getStatus() == estado) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Task> obtenerTareasPorUsuarioYPrioridad(String usuarioId, Priority prioridad) {
        List<Task> resultado = new ArrayList<>();
        if (usuarioId == null) return resultado;
        for (Task t : tareas) {
            if (usuarioId.equals(t.getAssignedUserId()) && t.getPriority() == prioridad) {
                resultado.add(t);
            }
        }
        return resultado;
    }
}
