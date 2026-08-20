# TaskFlow - Gestor de Tareas (MySQL Database Architecture)

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg?style=flat-square)](#)
[![UI Framework](https://img.shields.io/badge/UI-Java%20Swing-green.svg?style=flat-square)](#)
[![Database](https://img.shields.io/badge/Database-MySQL-blue.svg?style=flat-square)](#)
[![Build Tool](https://img.shields.io/badge/Build-Maven-red.svg?style=flat-square)](#)

> TaskFlow es una plataforma de gestión de tareas con arquitectura de base de datos MySQL relacional (7 tablas en 4NF), interfaz Swing interactiva y patrón DAO para la persistencia transaccional.

---

## Stack Tecnológico

| Componente | Tecnología / Librería | Propósito |
| :--- | :--- | :--- |
| **Lenguaje de Programación** | Java 17 (JDK) | Desarrollo orientado a objetos estructurado. |
| **Base de Datos** | MySQL Server / JDBC | Almacenamiento relacional normalizado. |
| **Gestión de Proyectos** | Apache Maven | Administración de dependencias (`mysql-connector-j`). |
| **Interfaz de Usuario** | Java Swing / AWT | Componentes gráficos nativos e interactivos. |
| **Patrón de Arquitectura** | DAO (Data Access Object) | Separación de lógica de negocio y persistencia SQL. |

---

## Esquema de Base de Datos Relacional

La base de datos `taskflowDb` está estructurada en las siguientes 7 tablas interconectadas:

- `type_person`: Catálogo de roles (Desarrollador, Líder de Proyecto, Administrador).
- `person`: Registro de usuarios/integrantes.
- `team`: Grupos o equipos de trabajo.
- `team_person`: Relación N:M entre personas y equipos.
- `status_task`: Catálogo de estados (Por Hacer, En Progreso, Completada).
- `task`: Registro principal de tareas.
- `assement_task`: Asignación y evaluación de tareas a personas.

---

## Arquitectura del Sistema

```mermaid
graph TD
    Main[Main.java: Punto de Entrada] --> TaskManager[TaskManager.java: Gestor Principal]
    Main --> MainFrame[MainFrame.java: Interfaz Swing]
    MainFrame --> TaskManager
    TaskManager --> PersonDAO[PersonDAO.java]
    TaskManager --> TaskDAO[TaskDAO.java]
    PersonDAO <--> MySQL[(MySQL: taskflowDb)]
    TaskDAO <--> MySQL[(MySQL: taskflowDb)]
```

---

## Compilación y Ejecución

### Mediante Maven

```bash
# Compilar proyecto
mvn clean compile

# Ejecutar proyecto desde consola
mvn exec:java -Dexec.mainClass="com.taskflow.Main"
```

### Ejecución Directa en IDE
Ejecuta la clase `com.taskflow.Main` directamente desde tu IDE preferido (VS Code, IntelliJ, Eclipse, NetBeans).

---

## Autores y Desarrolladores

- **Diego Mantilla**
- **Andrés Guerra**
