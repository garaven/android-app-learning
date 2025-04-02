package com.example.calculatorandform;

import java.io.Serializable;

public class User implements Serializable {
    String name, secondName, email, docType, gender, educationLevel, musicalTastes, sports;
    Integer age, id;
    Long docNumber;

    // Updated constructor to include all fields
    public User(String name, String secondName, String email, String docType, String gender,
                Integer age, Integer id, Long docNumber, String educationLevel,
                String musicalTastes, String sports) {
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.docType = docType;
        this.gender = gender;
        this.age = age;
        this.id = id;
        this.docNumber = docNumber;
        this.educationLevel = educationLevel;
        this.musicalTastes = musicalTastes;
        this.sports = sports;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setDocNumber(Long docNumber) {
        this.docNumber = docNumber;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public void setMusicalTastes(String musicalTastes) {
        this.musicalTastes = musicalTastes;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getEmail() {
        return email;
    }

    public String getDocType() {
        return docType;
    }

    public String getGender() {
        return gender;
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

    public String getEducationLevel() {
        return educationLevel;
    }

    public String getMusicalTastes() {
        return musicalTastes;
    }

    public String getSports() {
        return sports;
    }
}
