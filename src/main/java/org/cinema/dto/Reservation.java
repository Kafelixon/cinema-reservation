package org.cinema.dto;

public class Reservation {
    public int id;
    public int showtimeId;
    public int seatNumber;

    public Reservation(int id, int showtimeId, int seatNumber) {
        this.id = id;
        this.showtimeId = showtimeId;
        this.seatNumber = seatNumber;
    }
}
