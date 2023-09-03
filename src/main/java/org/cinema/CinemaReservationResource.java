package org.cinema;

import java.sql.Connection;
import java.sql.SQLException;

import org.cinema.db.DBInterface;
import org.cinema.db.DBInterface.DBException;
import org.cinema.db.DBInterface.IsolationLevel;
import org.cinema.db.DBInterface.TransactionalTask;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cinema")
public class CinemaReservationResource {

  private final DBInterface db;

  public CinemaReservationResource(DBInterface db) {
    this.db = db;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response cinema() {
    try {
      String result = db.transactionalOperation(IsolationLevel.READ_COMMITTED, new TransactionalTask<String>() {
        @Override
        public String execute(Connection connection) throws SQLException {
          // Perform your SQL operations here
          return "Your SQL operation was successful!";
        }
      });
      return Response.ok(result).build();
    } catch (DBException e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Database operation failed: " + e.getMessage()).build();
    }
  }
}
