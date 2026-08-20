package com.taskflow.model;

public class Team {
    private int idTeam;
    private String name;

    public Team(int idTeam, String name) {
        this.idTeam = idTeam;
        this.name = name;
    }

    public int getIdTeam() { return idTeam; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}
