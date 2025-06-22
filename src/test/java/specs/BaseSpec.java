package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;

public class BaseSpec {
  public static RequestSpecification userRequestSpec = with()
    .filter(withCustomTemplates())
    .header("x-api-key", "reqres-free-v1")
    .contentType("application/json")
    .log().all();

  public static ResponseSpecification responseSpec(int expectedStatusCode) {
    return new ResponseSpecBuilder()
      .expectStatusCode(expectedStatusCode)
      .log(LogDetail.ALL)
      .build();
  }
}