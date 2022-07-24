package com.example.chatting;

import java.util.ArrayList;

public class InterestsFirebase {

    String uID;
    ArrayList<String> arr;

    public InterestsFirebase() {
    }

    public InterestsFirebase(String uID, ArrayList<String> arr) {
        this.uID = uID;
        this.arr = arr;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public ArrayList<String> getArr() {
        return arr;
    }

    public void setArr(ArrayList<String> arr) {
        this.arr = arr;
    }
}
