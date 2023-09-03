package org.cinema.resource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import org.cinema.TestDatabaseUtils;
import org.cinema.db.DBInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.is;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MovieResourceTest {
  @Inject
  DBInterface db;

  @BeforeEach
  @AfterEach
  public void setUp() {
    TestDatabaseUtils.setUpDatabase(db);
  }

  @Test
  public void testListMovies() {
    given()
        .when().get("/movies")
        .then()
        .statusCode(200)
        .body("size()", is(3)); // Check if the response contains 3 movies
  }
}
