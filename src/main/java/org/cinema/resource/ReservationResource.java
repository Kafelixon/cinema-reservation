package org.cinema.resource;

import org.cinema.db.DBInterface;
import org.cinema.db.DBInterface.DBException;
import org.cinema.db.DBInterface.IsolationLevel;
import org.cinema.db.DBInterface.TransactionalTask;
import org.cinema.dto.Reservation;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/reservations")
public class ReservationResource {

  private final DBInterface db;

  public ReservationResource(DBInterface db) {
    this.db = db;
  }

  @POST
  @Path("/reserve")
  @Produces(MediaType.TEXT_PLAIN)
  public Response reserveSeat(@QueryParam("showtimeId") int showtimeId, @QueryParam("seatNumber") int seatNumber) {
    try {
      String result = db.transactionalOperation(IsolationLevel.READ_COMMITTED, new TransactionalTask<String>() {
        @Override
        public String execute(Connection connection) throws SQLException {
          int generatedReservationId = -1; // Initialize with a default value

          // Check if the seat is already reserved
          try (PreparedStatement checkStmt = connection
              .prepareStatement("SELECT * FROM reservations WHERE showtime_id = ? AND seat_number = ?")) {

            checkStmt.setInt(1, showtimeId);
            checkStmt.setInt(2, seatNumber);

            try (ResultSet rs = checkStmt.executeQuery()) {
              if (rs.next()) {
                return "Seat already reserved.";
              }
            }
          }

          // Reserve the seat
          try (PreparedStatement insertStmt = connection
              .prepareStatement("INSERT INTO reservations (showtime_id, seat_number) VALUES (?, ?)",
                  Statement.RETURN_GENERATED_KEYS)) {

            insertStmt.setInt(1, showtimeId);
            insertStmt.setInt(2, seatNumber);
            insertStmt.executeUpdate();

            // Retrieve the generated key (reservation ID)
            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
              if (generatedKeys.next()) {
                generatedReservationId = generatedKeys.getInt(1);
              } else {
                throw new SQLException("Creating reservation failed, no ID obtained.");
              }
            }
          }

          return "Reservation successful! Reservation ID: " + generatedReservationId;
        }
      });
      return Response.ok(result).build();
    } catch (DBException e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Database operation failed: " + e.getMessage()).build();
    }
  }

  @DELETE
  @Path("/cancel")
  @Produces(MediaType.TEXT_PLAIN)
  public Response cancelReservation(@QueryParam("reservationId") int reservationId) {
    try {
      String result = db.transactionalOperation(IsolationLevel.READ_COMMITTED, new TransactionalTask<String>() {
        @Override
        public String execute(Connection connection) throws SQLException {
          // Cancel the reservation
          try (PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM reservations WHERE id = ?")) {
            deleteStmt.setInt(1, reservationId);
            int rowsDeleted = deleteStmt.executeUpdate();
            if (rowsDeleted > 0) {
              return "Reservation canceled successfully!";
            } else {
              return "Reservation not found!";
            }
          }
        }
      });
      return Response.ok(result).build();
    } catch (DBException e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Database operation failed: " + e.getMessage()).build();
    }
  }

  @GET
  @Path("/get-reservations")
  @Produces(MediaType.APPLICATION_JSON)
  public Response showAllReservations(@QueryParam("showtimeId") int showtimeId) {
    try {
      List<Reservation> result = db.transactionalOperation(IsolationLevel.READ_COMMITTED,
          new TransactionalTask<List<Reservation>>() {
            @Override
            public List<Reservation> execute(Connection connection) throws SQLException {
              List<Reservation> result = new ArrayList<>();
              try (PreparedStatement selectStmt = connection
                  .prepareStatement("SELECT * FROM reservations WHERE showtime_id = ?")) {
                selectStmt.setInt(1, showtimeId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                  while (rs.next()) {
                    result.add(new Reservation(rs.getInt("id"), rs.getInt("showtime_id"), rs.getInt("seat_number")));
                  }
                }
              }
              return result;
            }
          });
      return Response.ok(result).build();
    } catch (DBException e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Database operation failed: " + e.getMessage()).build();
    }
  }

  @GET
  @Path("/show-all")
  @Produces(MediaType.APPLICATION_JSON)
  public Response showAllReservations() {
    try {
      List<Reservation> result = db.transactionalOperation(IsolationLevel.READ_COMMITTED,
          new TransactionalTask<List<Reservation>>() {
            @Override
            public List<Reservation> execute(Connection connection) throws SQLException {
              List<Reservation> result = new ArrayList<>();
              try (PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM reservations");
                  ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                  result.add(new Reservation(rs.getInt("id"), rs.getInt("showtime_id"), rs.getInt("seat_number")));
                }
              }
              return result;
            }
          });
      return Response.ok(result).build();
    } catch (DBException e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Database operation failed: " + e.getMessage()).build();
    }
  }
}
