package com.example.calculatorandform;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    String name, secondName, email;
    Integer age, id;

    public User(String name, String secondName, String email, Integer age, Integer id) {
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.age = age;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + name + " " + secondName + ", Email: " + email + ", Edad: " + age;
    }
}