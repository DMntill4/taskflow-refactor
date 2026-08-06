package com.taskflow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor único de tareas y usuarios.
 * Carga y guarda listas en JSON utilizando arreglos simples (Task[].class, User[].class)
 * sin genéricos avanzados o TypeToken.
 */
public class TaskManager {
    private static final String RUTA_DATA = "data";
    private static final String RUTA_TAREAS = RUTA_DATA + "/tasks.json";
    private static final String RUTA_USUARIOS = RUTA_DATA + "/users.json";

    private final Gson gson;
    private List<Task> tareas;
    private List<User> usuarios;

    public TaskManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.tareas = new ArrayList<>();
        this.usuarios = new ArrayList<>();

        crearCarpetaSiNoExiste();
        cargarDatos();

        // Si el sistema no tiene usuarios, crear los usuarios por defecto Diego y Guerra
        if (usuarios.isEmpty()) {
            crearDatosDemo();
        }
    }

    private void crearCarpetaSiNoExiste() {
        File folder = new File(RUTA_DATA);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public void cargarDatos() {
        tareas = cargarTareasDesdeJson();
        usuarios = cargarUsuariosDesdeJson();
    }

    public void guardarDatos() {
        guardarTareasEnJson();
        guardarUsuariosEnJson();
    }

    /**
     * Carga las tareas desde el archivo JSON convirtiéndolo a un arreglo Task[] simple.
     */
    private List<Task> cargarTareasDesdeJson() {
        File archivo = new File(RUTA_TAREAS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(archivo)) {
            Task[] arreglo = gson.fromJson(reader, Task[].class);
            List<Task> lista = new ArrayList<>();
            if (arreglo != null) {
                for (Task t : arreglo) {
                    lista.add(t);
                }
            }
            return lista;
        } catch (Exception e) {
            System.err.println("Error al cargar tareas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Carga los usuarios desde el archivo JSON convirtiéndolo a un arreglo User[] simple.
     */
    private List<User> cargarUsuariosDesdeJson() {
        File archivo = new File(RUTA_USUARIOS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(archivo)) {
            User[] arreglo = gson.fromJson(reader, User[].class);
            List<User> lista = new ArrayList<>();
            if (arreglo != null) {
                for (User u : arreglo) {
                    lista.add(u);
                }
            }
            return lista;
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void guardarTareasEnJson() {
        try (Writer writer = new FileWriter(RUTA_TAREAS)) {
            gson.toJson(tareas, writer);
        } catch (Exception e) {
            System.err.println("Error al guardar tareas: " + e.getMessage());
        }
    }

    private void guardarUsuariosEnJson() {
        try (Writer writer = new FileWriter(RUTA_USUARIOS)) {
            gson.toJson(usuarios, writer);
        } catch (Exception e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    /**
     * Crea los datos iniciales predeterminados para la demo con Diego y Guerra.
     */
    public void crearDatosDemo() {
        tareas.clear();
        usuarios.clear();

        User diego = crearUsuario("Diego");
        User guerra = crearUsuario("Guerra");

        Task t1 = crearTarea("Diseñar Interfaz Swing", "Crear componentes de interfaz clara y legible en Java.", Priority.HIGH, diego.getId());
        t1.setStatus(TaskStatus.IN_PROGRESS);

        Task t2 = crearTarea("Crear Estructura JSON", "Definir formato de guardado de tareas en archivos locales.", Priority.MEDIUM, guerra.getId());
        t2.setStatus(TaskStatus.DONE);

        Task t3 = crearTarea("Preparar Presentación", "Revisar el código y flujo para la exposición del lunes.", Priority.HIGH, diego.getId());

        guardarDatos();
    }

    // ==================== OPERACIONES DE USUARIOS ====================

    public List<User> obtenerUsuarios() {
        return usuarios;
    }

    public User crearUsuario(String nombre) {
        User usuario = new User(nombre);
        usuarios.add(usuario);
        guardarDatos();
        return usuario;
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
        guardarDatos();
        return tarea;
    }

    public void avanzarEstadoTarea(String tareaId) {
        for (Task t : tareas) {
            if (t.getId().equals(tareaId)) {
                t.setStatus(t.getStatus().next());
                break;
            }
        }
        guardarDatos();
    }

    public void eliminarTarea(String tareaId) {
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).getId().equals(tareaId)) {
                tareas.remove(i);
                break;
            }
        }
        guardarDatos();
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
