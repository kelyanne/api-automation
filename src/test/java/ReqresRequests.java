import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReqresRequests {

    int getUserId = 0;

    @BeforeAll
    public static void setup(){
        baseURI = "https://reqres.in/api/";
    }

    @Test
    @Order(1)
    public void getAllUsersRequest(){
        Response response = given()
                .when()
                .get("users?page=2")
                .then()
                .extract().response();

        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertEquals("6", response.jsonPath().getString("per_page"));
    }

    @Test
    @Order(2)
    public void createUserRequest(){
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Morpheus");
        requestParams.put("job", "leader");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestParams.toString())
                .when()
                .post("users")
                .then()
                .extract().response();

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals("Morpheus", response.jsonPath().getString("name"));
        Assertions.assertEquals("leader", response.jsonPath().getString("job"));
        getUserId = response.jsonPath().getInt("id");

    }

    @Test
    @Order(3)
    public void updateUserRequest(){
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Morpheus");
        requestParams.put("job", "zion resident");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestParams.toString())
                .when()
                .put("users/" + getUserId)
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Morpheus", response.jsonPath().getString("name"));
        Assertions.assertEquals("zion resident", response.jsonPath().getString("job"));
    }

    @Test
    @Order(4)
    public void updatePatchUserRequest(){
        JSONObject requestParams = new JSONObject();
        requestParams.put("job", "human resource");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestParams.toString())
                .when()
                .patch("users/" + getUserId)
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("human resource", response.jsonPath().getString("job"));
    }

    @Test
    @Order(5)
    public void deleteUserRequest(){
        Response response = given()
                .when()
                .delete("users/" + getUserId)
                .then()
                .extract().response();

        Assertions.assertEquals(204, response.statusCode());
    }
}
