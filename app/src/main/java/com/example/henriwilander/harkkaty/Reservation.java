package com.example.henriwilander.harkkaty;

import java.util.ArrayList;

public abstract class Reservation {

    private int reservationID;
    private String date;
    private String time;
    private String surname;
    private String type;
    private int hallID;
    private int userID;
    private String genre;
    private int capacity;
    ArrayList<Registrations> registrationArrayList;

    public Reservation(int reservationID, int hallID, String date, String time, String surname, String type, String genre, int userID, int capacity) {
        this.reservationID = reservationID;
        this.date = date;
        this.time = time;
        this.surname = surname;
        this.type = type;
        this.hallID = hallID;
        this.userID = userID;
        this.genre = genre;
        this.capacity = capacity;
        registrationArrayList = new ArrayList();
    }

    public void createNewRegistration() {
        registrationArrayList.add(new Registrations(surname, reservationID, userID, (reservationID*10)));
    }

    public void editGymasiumID(String name) {
    }

    public int getCapacity() {
        return capacity;
    }

    public void editDate(String date) {
        this.date = date;
    }

    public void editTime(String time) {
        this.time = time;
    }

    public String getOrganizer() {

        return getOrganizer();
    }

    public String getUserSurname() {
        return surname;
    }

    public int getResevationId() {
        return reservationID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() { return type;}

    public int getUserID() {
        return userID;
    }

    public int getHallID() {
        return hallID;
    }

    public String getGenre() {
        return genre;
    }

    public void editGenre(String genre) {
        this.genre = genre;
    }


}