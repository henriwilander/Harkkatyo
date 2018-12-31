package com.example.henriwilander.harkkaty;

public class CommonReservation extends Reservation {

    private int registrations;

    public CommonReservation(int reservationID, int hallID, String date, String time, String userSurname, String genre,int userID, int capacity, int registrations) {
        super(reservationID, hallID, date, time, userSurname,"Common Reservation",genre, userID, capacity);
        this.registrations = registrations;
    }

    public int getRegistrations() {
        return registrations;
    }

    public void addRegistrations() {
        registrations = registrations + 1;
    }

}
