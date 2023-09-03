package org.cinema;

import io.quarkus.test.junit.QuarkusTest;
import org.cinema.db.DBInterface;
import org.cinema.db.DBInterface.DBException;
import org.cinema.db.DBInterface.IsolationLevel;
import org.cinema.db.DBInterface.TransactionalTask;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class CinemaReservationResourceTest {

    @Inject
    DBInterface db;

    @Test
    public void testDatabase() {
        try {
            String result = db.transactionalOperation(IsolationLevel.READ_COMMITTED, new TransactionalTask<String>() {
                @Override
                public String execute(Connection connection) throws SQLException {
                    StringBuilder sb = new StringBuilder();
                    PreparedStatement stmt = connection.prepareStatement("SELECT * FROM cinema_halls");
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        sb.append("ID: ").append(rs.getInt("id"))
                                .append(", Name: ").append(rs.getString("name"))
                                .append(", Capacity: ").append(rs.getInt("capacity"))
                                .append("\n");
                    }
                    return sb.toString();
                }
            });

            // Check if the result contains expected data (adjust according to your sample
            // data)
            assertTrue(result.contains("Hall 1"));
            assertTrue(result.contains("Hall 2"));
            assertTrue(result.contains("Hall 3"));

        } catch (DBException e) {
            // If an exception occurs, make the test fail
            assertTrue(false, "Database operation failed: " + e.getMessage());
        }
    }
}
