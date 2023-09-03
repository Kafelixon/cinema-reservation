package org.cinema.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ShowtimeResourceTest {

  @Test
  public void testListShowtimes() {
    given()
        .when().get("/showtimes?movieId=1")
        .then()
        .statusCode(200);
  }
}
