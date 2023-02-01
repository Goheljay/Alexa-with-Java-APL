package com.example.alexa.modal;

public class GlobalEntity {
    private String name;
    private String artistName;
    private String chooseWay;
    private Boolean keyword = false;
    private Boolean artist = false;
    private Boolean genere = false;

    public Boolean getKeyword() {
        return keyword;
    }

    public void setKeyword(Boolean keyword) {
        this.keyword = keyword;
    }

    public Boolean getArtist() {
        return artist;
    }

    public void setArtist(Boolean artist) {
        this.artist = artist;
    }

    public Boolean getGenere() {
        return genere;
    }

    public void setGenere(Boolean genere) {
        this.genere = genere;
    }

    public String getChooseWay() {
        return chooseWay;
    }

    public void setChooseWay(String chooseWay) {
        this.chooseWay = chooseWay;
    }

    public GlobalEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
