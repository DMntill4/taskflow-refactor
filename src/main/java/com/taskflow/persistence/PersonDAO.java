package com.taskflow.persistence;

import com.taskflow.model.Person;
import com.taskflow.model.Team;
import com.taskflow.model.TypePerson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

    public List<Person> obtenerTodas() {
        List<Person> personas = new ArrayList<>();
        String sql = "SELECT id_person, name, email, id_type_person FROM person";

        try (Connection conn = ConexionMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) return personas;

            while (rs.next()) {
                Person p = new Person();
                p.setIdPerson(rs.getInt("id_person"));
                p.setName(rs.getString("name"));
                p.setEmail(rs.getString("email"));
                int typeId = rs.getInt("id_type_person");
                p.setIdTypePerson(rs.wasNull() ? null : typeId);
                personas.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener personas desde MySQL: " + e.getMessage());
        }
        return personas;
    }

    public List<TypePerson> obtenerTiposPersona() {
        List<TypePerson> tipos = new ArrayList<>();
        String sql = "SELECT id_type_person, name FROM type_person";
        try (Connection conn = ConexionMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (conn != null) {
                while (rs.next()) {
                    tipos.add(new TypePerson(rs.getInt("id_type_person"), rs.getString("name")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tipos de persona: " + e.getMessage());
        }
        return tipos;
    }

    public List<Team> obtenerEquipos() {
        List<Team> equipos = new ArrayList<>();
        String sql = "SELECT id_team, name FROM team";
        try (Connection conn = ConexionMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (conn != null) {
                while (rs.next()) {
                    equipos.add(new Team(rs.getInt("id_team"), rs.getString("name")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener equipos: " + e.getMessage());
        }
        return equipos;
    }

    public Person guardar(String nombre, int idTypePerson, int idTeam) {
        String sql = "INSERT INTO person (name, id_type_person) VALUES (?, ?)";

        try (Connection conn = ConexionMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null) return new Person(nombre);

            stmt.setString(1, nombre);
            stmt.setInt(2, idTypePerson);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    
                    // Asociar al equipo seleccionado en team_person
                    String sqlTeam = "INSERT IGNORE INTO team_person (id_team, id_person) VALUES (?, ?)";
                    try (PreparedStatement stmtTeam = conn.prepareStatement(sqlTeam)) {
                        stmtTeam.setInt(1, idTeam);
                        stmtTeam.setInt(2, id);
                        stmtTeam.executeUpdate();
                    }

                    return new Person(id, nombre, null, idTypePerson);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar persona en MySQL: " + e.getMessage());
        }
        return new Person(nombre);
    }

    public Person obtenerPorId(int idPerson) {
        String sql = "SELECT id_person, name, email, id_type_person FROM person WHERE id_person = ?";
        try (Connection conn = ConexionMySQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return null;
            stmt.setInt(1, idPerson);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Person p = new Person();
                    p.setIdPerson(rs.getInt("id_person"));
                    p.setName(rs.getString("name"));
                    p.setEmail(rs.getString("email"));
                    int typeId = rs.getInt("id_type_person");
                    p.setIdTypePerson(rs.wasNull() ? null : typeId);
                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar persona por ID: " + e.getMessage());
        }
        return null;
    }
}
