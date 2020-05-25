package com.shreshth.cova.models;

import com.google.firebase.firestore.FieldValue;
import com.google.firestore.v1.DocumentTransform;

import java.util.Map;


public class Note {


    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUid() {
        return uid;
    }

    public Note(String message, String name, String timestamp, String uid) {
        this.message = message;
        this.name = name;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    private String message;
    private String name;
    private String timestamp;
    private String uid;

    public Note() {
    }


}
