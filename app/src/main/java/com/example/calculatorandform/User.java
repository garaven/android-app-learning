package com.example.calculatorandform;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    String name, secondName, email, docType, gender;
    Integer age, id;
    Long docNumber;

    public User(String name, String secondName, String email, String docType, String gender, Integer age, Integer id, Long docNumber) {
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.age = age;
        this.id = id;
        this.docNumber = docNumber;
        this.docType = docType;
        this.gender = gender;
    }

    // Set

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

    public void setDocNumber(Long docNumber) {
        this.docNumber = docNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    // Get

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

    public Long getDocNumber() {
        return docNumber;
    }

    public String getDocType() {
        return docType;
    }

    public String getGender() {
        return gender;
    }
}