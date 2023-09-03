package org.cinema.resource;

import io.quarkus.test.junit.QuarkusTest;

import org.cinema.TestDatabaseUtils;
import org.cinema.db.DBInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import static org.hamcrest.CoreMatchers.is;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ShowtimeResourceTest {

  @Inject
  DBInterface db;

  @BeforeEach
  @AfterEach
  public void setUp() {
    TestDatabaseUtils.setUpDatabase(db);
  }

  @Test
  public void testListShowtimes() {
    given()
        .when().get("/showtimes?movieId=1")
        .then()
        .statusCode(200)
        .body("size()", is(1)); // Check if the response contains 1 showtime
  }
}
