package org.cinema.dto;

public class Showtime {
    public int id;
    public int hallId;
    public int movieId;
    public String showTime;

    public Showtime(int id, int hallId, int movieId, String showTime) {
        this.id = id;
        this.hallId = hallId;
        this.movieId = movieId;
        this.showTime = showTime;
    }
}
