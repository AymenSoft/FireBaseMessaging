package com.aymensoft.firebasemessaging.model;

public class Message {

    private String userid;
    private String text;
    private String photoUrl;

    public Message(String userid, String text, String photoUrl) {
        this.userid = userid;
        this.text = text;
        this.photoUrl = photoUrl;
    }

    public Message(){}

    public String getUserid() {
        return userid;
    }

    public String getText() {
        return text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userid='" + userid + '\'' +
                ", text='" + text + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
