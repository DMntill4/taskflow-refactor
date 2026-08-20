package com.taskflow.model;

public class TypePerson {
    private int idTypePerson;
    private String name;

    public TypePerson(int idTypePerson, String name) {
        this.idTypePerson = idTypePerson;
        this.name = name;
    }

    public int getIdTypePerson() { return idTypePerson; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}
