package com.isep.project;

public abstract class Player {
    private String name;
    private int role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Player(String name, int role) {
        this.name = name;
        this.role = role;
    }
}
