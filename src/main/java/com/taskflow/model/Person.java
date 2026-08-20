package com.taskflow.model;

/**
 * Modelo para representar una Persona/Usuario conectada a type_person.
 */
public class Person {
    private int idPerson;
    private String name;
    private String email;
    private Integer idTypePerson;

    public Person(int idPerson, String name, String email, Integer idTypePerson) {
        this.idPerson = idPerson;
        this.name = name;
        this.email = email;
        this.idTypePerson = idTypePerson;
    }

    public Person(String name) {
        this.name = name;
    }

    public Person() {}

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdTypePerson() {
        return idTypePerson;
    }

    public void setIdTypePerson(Integer idTypePerson) {
        this.idTypePerson = idTypePerson;
    }

    @Override
    public String toString() {
        return name;
    }
}
