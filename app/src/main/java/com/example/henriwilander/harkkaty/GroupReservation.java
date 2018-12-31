package com.example.henriwilander.harkkaty;

public class GroupReservation extends Reservation {

    private String organizer;
    public GroupReservation(int reservationID, int hallID, String date, String time, String userSurname, String organizer, String genre, int userID, int capacity) {
        super(reservationID, hallID, date, time, userSurname,"Group Reservation",genre, userID, capacity);
        this.organizer = organizer;
    }
    public String getOrganizer() {
        return organizer;
    }

    public void editOrganizer(String organizer) {
        this.organizer = organizer;
    }

}
