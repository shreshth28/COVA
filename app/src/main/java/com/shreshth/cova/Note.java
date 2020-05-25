package com.shreshth.cova;

import com.google.firestore.v1.DocumentTransform;


public class Note {


    public Note(String message, String name, DocumentTransform.FieldTransform.ServerValue timestamp, String uid) {
        this.message = message;
        this.name = name;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public DocumentTransform.FieldTransform.ServerValue getTimestamp() {
        return timestamp;
    }

    public String getUid() {
        return uid;
    }

    private String message;
    private String name;
    private DocumentTransform.FieldTransform.ServerValue timestamp;
    private String uid;

    public Note() {
    }


}
