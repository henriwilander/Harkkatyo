package com.example.henriwilander.harkkaty;

public class MultiPurposeHall extends CommunityCenter {

    private String[] genres;

    public MultiPurposeHall(String name, int id, int capacity, String[] genres, int status) {
        super(name, id, capacity, true, status);
        this.genres = genres;
    }

    public void setNewGenres(String[] newGenres) {
        this.genres = newGenres;
    }

    public String[] getGenres() {
        return genres;
    }
}
