package org.cinema.resource;

import org.cinema.db.DBInterface;
import org.cinema.db.DBInterface.DBException;
import org.cinema.db.DBInterface.IsolationLevel;
import org.cinema.db.DBInterface.TransactionalTask;
import org.cinema.dto.Movie;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/movies")
public class MovieResource {

  private final DBInterface db;

  public MovieResource(DBInterface db) {
    this.db = db;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response listMovies() {
    try {
      List<Movie> movies = db.transactionalOperation(IsolationLevel.READ_COMMITTED,
          new TransactionalTask<List<Movie>>() {
            @Override
            public List<Movie> execute(Connection connection) throws SQLException {
              List<Movie> result = new ArrayList<>();
              try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM movies");
                  ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                  result.add(new Movie(rs.getInt("id"), rs.getString("name"), rs.getInt("duration")));
                }
              }
              return result;
            }
          });
      return Response.ok(movies).build();
    } catch (DBException e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Database operation failed: " + e.getMessage()).build();
    }
  }
}
