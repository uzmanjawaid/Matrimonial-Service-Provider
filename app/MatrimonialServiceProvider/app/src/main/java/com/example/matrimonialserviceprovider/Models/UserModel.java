package com.example.matrimonialserviceprovider.Models;

public class UserModel {

    public String id,name,email,imageUrl,gender,religion,caste,marital,status,age;

    public String getAge() {
        if (age ==null){
            return  "";
        }
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getReligion() {
        if (religion ==null){
            return  "";
        }
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCaste() {
        if (caste ==null){
            return  "";
        }
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getMarital() {
        if (marital ==null){
            return  "";
        }
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name ==null){
            return  "";
        }
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
}
