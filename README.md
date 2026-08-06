# 📋 TaskFlow — Gestor de Tareas

> Aplicación de gestión de tareas inspirada en Jira, Trello y Notion.  
> Desarrollada en Java con interfaz gráfica Swing y persistencia JSON.

## 🚀 Características

- **Tablero Kanban** visual con 3 columnas: Por Hacer, En Progreso, Finalizado
- **Prioridades** con código de color: 🔴 Alta, 🟡 Media, 🟢 Baja
- **Asignación de tareas** a usuarios del equipo
- **Filtros** por prioridad, usuario y proyecto
- **Vista de usuario** — ver tareas asignadas agrupadas por prioridad
- **Proyectos** — agrupar tareas en microproyectos
- **Persistencia JSON** — los datos se guardan automáticamente

## 📦 Requisitos

- Java 17 o superior (JDK)
- Gson 2.10+ (incluido en `lib/`)

## 🔧 Compilación

```bash
# Compilar todo el proyecto
javac -cp "lib/*" -d out src/main/java/com/taskflow/**/*.java src/main/java/com/taskflow/*.java

# Ejecutar
java -cp "out;lib/*" com.taskflow.Main
```

## 📁 Estructura del Proyecto

```
src/main/java/com/taskflow/
├── Main.java              # Punto de entrada
├── model/                 # Entidades y enums
│   ├── Priority.java
│   ├── TaskStatus.java
│   ├── Task.java
│   ├── User.java
│   └── Project.java
├── service/               # Lógica de negocio
│   ├── TaskService.java
│   ├── UserService.java
│   └── ProjectService.java
├── repository/            # Persistencia JSON
│   └── JsonRepository.java
└── ui/                    # Interfaz gráfica Swing
    ├── MainFrame.java
    ├── TaskCard.java
    ├── KanbanColumn.java
    ├── TaskDialog.java
    ├── UserPanel.java
    └── ThemeManager.java
```

## 🎨 Conventional Commits

Este proyecto sigue la convención de commits:

| Tipo | Descripción |
|------|------------|
| `feat:` | Nueva funcionalidad |
| `fix:` | Corrección de errores |
| `docs:` | Documentación |
| `style:` | Cambios de estilo/UI |
| `refactor:` | Refactorización |
| `test:` | Tests |

## 👥 Equipo

- Proyecto académico — Gestor de Tareas en Java

## 📄 Licencia

Proyecto educativo — Todos los derechos reservados.
