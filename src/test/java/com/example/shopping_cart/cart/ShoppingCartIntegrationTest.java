//package com.example.shopping_cart.cart;
//
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.ActiveProfiles;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.equalTo;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//public class ShoppingCartIntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @BeforeEach
//    public void setUp() {
//        RestAssured.port = port;
//    }
//
//    @Test
//    void testUploadShoppingCart() {
//        given()
//                .auth().basic("user", "password")
//                .contentType(ContentType.JSON)
//                .body("[{\"productId\": 1, \"quantity\": 2}]")
//                .when()
//                .post("/carts/upload")
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("message", equalTo("Upload cart successfully"));
//    }
//
//    @Test
//    void testFindShoppingCart() {
//        given()
//                .auth().basic("user", "password")
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/carts?page-size=5&page-number=1")
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("message", equalTo("Find cart successfully"));
//    }
//
//    @Test
//    void testUpdateShoppingCart() {
//        given()
//                .auth().basic("user", "password")
//                .contentType(ContentType.JSON)
//                .body("[{\"productId\": 1, \"quantity\": 2}]")
//                .when()
//                .patch("/carts/update")
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("message", equalTo("Upload cart successfully"));
//    }
//}