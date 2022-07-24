package com.example.chatting;

import java.util.ArrayList;

public class Users implements  Comparable<Users>{

    String uid;
    String name;
    String email;
    String imageUri;
    String status;

    public Users() {
    }

    public Users(String uid, String name, String email, String imageUri, String status) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(Users users) {
        return this.name.compareTo(users.name);
    }
}
