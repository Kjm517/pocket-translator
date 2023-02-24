package com.example.bistalk.model;

public class Wordbank {
    private String cebuano;
    private String english;
    private String picture;
    private String pronunciation;

    public Wordbank(){

    }

    public Wordbank(String cebuano, String english, String pronunciation){
        this.pronunciation = pronunciation;
        this.cebuano = cebuano;
        this.english = english;
    }

    public Wordbank(String cebuano,String english, String picture, String pronunciation) {
        this.cebuano = cebuano;
        this.english = english;
        this.picture = picture;
        this.pronunciation = pronunciation;
    }


    public String getCebuano() {
        return cebuano;
    }

    public void setCebuano(String cebuano) {
        this.cebuano = cebuano;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }
}
