package org.cinema;

import org.cinema.db.DBInterface;
import org.cinema.db.DBInterface.IsolationLevel;
import org.cinema.db.DBInterface.TransactionalTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class TestDatabaseUtils {

  public static void setUpDatabase(DBInterface db) {
    try {
      List<String> lines = Files.readAllLines(Paths.get("cinema_db_setup.sql"));
      String sql = String.join("\n", lines);
      String[] commands = sql.split(";");

      db.transactionalOperation(IsolationLevel.READ_COMMITTED, new TransactionalTask<Void>() {
        @Override
        public Void execute(Connection connection) throws SQLException {
          try (Statement stmt = connection.createStatement()) {
            for (String command : commands) {
              if (command.trim().isEmpty()) {
                continue;
              }
              stmt.executeUpdate(command);
            }
          }
          return null;
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
      fail("Setup failed: " + e.getMessage());
    }
  }
}
