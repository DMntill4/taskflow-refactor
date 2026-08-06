# TaskFlow - Gestor de Tareas

TaskFlow es una aplicación de gestión de tareas inspirada en plataformas como Jira y Trello, desarrollada en Java con una interfaz gráfica Swing de alto contraste y persistencia en archivos JSON.

---

## Estructura Simplificada del Proyecto

El código está estructurado de manera directa y accesible para nivel Junior:

```text
src/main/java/com/taskflow/
├── Main.java              # Punto de entrada principal (inicio de la GUI)
├── TaskManager.java       # Gestor único de datos, listas y guardado en JSON
├── model/                 # Entidades y enumeraciones del dominio
│   ├── Task.java          # Modelo de la Tarea (título, descripción, prioridad, estado)
│   ├── User.java          # Modelo del Usuario (id, nombre)
│   ├── Priority.java      # Enums de Prioridad (Alta, Media, Baja)
│   └── TaskStatus.java    # Enums de Estado (Por Hacer, En Proceso, Finalizado)
└── ui/                    # Interfaz gráfica Swing
    └── MainFrame.java     # Ventana principal con pestañas (Kanban y Vista por Usuario)
```

---

## Características Principales

1. **Tablero Kanban Visual**:
   - Organizado en tres columnas: **Por Hacer**, **En Proceso** y **Finalizado**.
   - Avance rápido de estado mediante el botón `▶ Mover`.
2. **Prioridades con Código de Colores de Alto Contraste**:
   - Rojo: Prioridad Alta.
   - Naranja/Amarillo: Prioridad Media.
   - Gris: Prioridad Baja.
3. **Asignación de Tareas y Vista por Usuario**:
   - Asignación de tareas a usuarios del equipo.
   - Pestaña para filtrar tareas por usuario agrupadas ordenadamente por prioridad.
4. **Diálogos Interactivos con `JOptionPane`**:
   - Creación rápida de usuarios y tareas mediante diálogos estándar de Swing.
5. **Persistencia de Datos en JSON**:
   - Los datos se guardan automáticamente en la carpeta `data/` usando la librería Gson.

---

## Compilación y Ejecución

Para compilar y ejecutar desde la terminal o mediante los archivos `.bat`:

```bash
# Compilar el proyecto
javac -cp "lib/*" -d out src/main/java/com/taskflow/model/*.java src/main/java/com/taskflow/ui/*.java src/main/java/com/taskflow/*.java

# Ejecutar la aplicación
java -cp "out;lib/*" com.taskflow.Main
```

---

## Guía de Explicación para la Exposición del Lunes

Para defender el proyecto ante tu profesor o evaluadores:

1. **`Main.java`**:
   - *"Es la clase de inicio. Crea una instancia de `TaskManager` que carga los datos en JSON y abre la ventana `MainFrame`."*
2. **`TaskManager.java`**:
   - *"Es el gestor central. Administra las listas de tareas y usuarios en memoria, realiza las búsquedas mediante bucles `for` y guarda todo en archivos JSON usando Gson."*
3. **`MainFrame.java`**:
   - *"Es la interfaz gráfica en Swing. Utiliza un `JTabbedPane` para separar el tablero Kanban de la vista por usuarios, y `JOptionPane` para pedir los datos al crear tareas o usuarios."*

---

## Autores

- **Andrés Guerra**
- **Diego Mantilla**
