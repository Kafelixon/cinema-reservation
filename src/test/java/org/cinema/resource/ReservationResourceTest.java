package org.cinema.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

import org.cinema.TestDatabaseUtils;
import org.cinema.db.DBInterface;;

@QuarkusTest
public class ReservationResourceTest {

  @Inject
  DBInterface db;

  // Sample reservation ID and seat number for testing
  private static final int SAMPLE_SEAT_NUMBER = 5;
  private static final int SAMPLE_SHOWTIME_ID = 1;

  @BeforeEach
  @AfterEach
  public void setUp() {
    TestDatabaseUtils.setUpDatabase(db);
  }

  @Test
  public void testReserveSeat() {
    given()
        .when().post("/reservations/reserve?showtimeId=" + SAMPLE_SHOWTIME_ID + "&seatNumber=" + SAMPLE_SEAT_NUMBER)
        .then()
        .statusCode(200)
        .body(containsString("Reservation successful! Reservation ID: "));
  }

  @Test
  public void testCancelReservation() {
    // First, make a reservation and get the reservation ID from the response
    String reservationResponse = given()
        .when().post("/reservations/reserve?showtimeId=" + SAMPLE_SHOWTIME_ID + "&seatNumber=" + SAMPLE_SEAT_NUMBER)
        .then()
        .statusCode(200)
        .extract().body().asString();

    // Parse the reservation ID from the response
    String[] parts = reservationResponse.split(": ");
    int reservationId = Integer.parseInt(parts[1]);

    // Then, cancel it using the extracted reservation ID
    given()
        .when().delete("/reservations/cancel?reservationId=" + reservationId)
        .then()
        .statusCode(200)
        .body(is("Reservation canceled successfully!"));
  }

  @Test
  public void testShowAll(){
    given()
        .when().get("/reservations/show-all")
        .then()
        .statusCode(200)
        .body(is("[{\"id\":1,\"showtimeId\":1,\"seatNumber\":1},{\"id\":2,\"showtimeId\":1,\"seatNumber\":2},{\"id\":3,\"showtimeId\":2,\"seatNumber\":3}]"));
  }

  @Test
  public void testGetReservationForShowtime(){
    given()
        .when().get("/reservations/get-reservations?showtimeId=1")
        .then()
        .statusCode(200)
        .body(is("[{\"id\":1,\"showtimeId\":1,\"seatNumber\":1},{\"id\":2,\"showtimeId\":1,\"seatNumber\":2}]"));
  }
}
