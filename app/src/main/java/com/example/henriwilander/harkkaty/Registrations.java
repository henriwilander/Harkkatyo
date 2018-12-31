package com.example.henriwilander.harkkaty;

public class Registrations {

    private String userSurname;
    private int eventID;
    private int userID;
    private int registrationID;

    public Registrations(String userSurname, int eventID, int userID, int registrationID) {
        this.userSurname = userSurname;
        this.eventID = eventID;
        this.userID = userID;
        this.registrationID = registrationID;
    }

    public String getUserSurname() {

        return userSurname;
    }

    public int getEventID() {
        return eventID;
    }

    public int getUserID() {

        return userID;
    }
}

