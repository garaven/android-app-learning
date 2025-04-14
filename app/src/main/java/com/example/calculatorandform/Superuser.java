package com.example.calculatorandform;

import java.io.Serializable;

public class Superuser implements Serializable {
    String username;
    String password;

    public Superuser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Usuario: " + username;
    }
}
