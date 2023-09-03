package org.cinema.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MovieResourceTest {

  @Test
  public void testListMovies() {
    given()
        .when().get("/movies")
        .then()
        .statusCode(200);
  }
}
