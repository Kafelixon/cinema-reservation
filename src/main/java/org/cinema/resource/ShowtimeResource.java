package org.cinema.resource;

import org.cinema.db.DBInterface;
import org.cinema.db.DBInterface.DBException;
import org.cinema.db.DBInterface.IsolationLevel;
import org.cinema.db.DBInterface.TransactionalTask;
import org.cinema.dto.Showtime;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/showtimes")
public class ShowtimeResource {

  private final DBInterface db;

  public ShowtimeResource(DBInterface db) {
    this.db = db;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response listShowtimes(@QueryParam("movieId") int movieId) {
    try {
      List<Showtime> showtimes = db.transactionalOperation(IsolationLevel.READ_COMMITTED,
          new TransactionalTask<List<Showtime>>() {
            @Override
            public List<Showtime> execute(Connection connection) throws SQLException {
              List<Showtime> result = new ArrayList<>();
              try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM showtimes WHERE movie_id = ?")) {
                stmt.setInt(1, movieId);
                try (ResultSet rs = stmt.executeQuery()) {
                  while (rs.next()) {
                    result.add(new Showtime(rs.getInt("id"), rs.getInt("hall_id"), rs.getInt("movie_id"),
                        rs.getString("show_time")));
                  }
                }
              }
              return result;
            }
          });
      return Response.ok(showtimes).build();
    } catch (DBException e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Database operation failed: " + e.getMessage()).build();
    }
  }
}
