package specs;

import io.qameta.allure.Step;
import models.CreateUserResponseModel;
import models.UserCreateRequestModel;
import models.UserListResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.responseSpec;
import static specs.BaseSpec.userRequestSpec;

public class UserRequest {

  @Step("Получение списка пользователей")
  public static UserListResponseModel getUsersList(int page) {
    return given(userRequestSpec)
      .get("/users?page=" + page)
      .then()
      .spec(responseSpec(200))
      .extract().as(UserListResponseModel.class);
  }

  @Step("Создание пользователя")
  public static CreateUserResponseModel createUser(UserCreateRequestModel body) {
    return given(userRequestSpec)
      .body(body)
      .post("/users")
      .then()
      .spec(responseSpec(201))
      .extract().as(CreateUserResponseModel.class);
  }

  @Step("Обновление пользователя")
  public static CreateUserResponseModel updateUser(int id, UserCreateRequestModel body) {
    return given(userRequestSpec)
      .body(body)
      .put("/users/" + id)
      .then()
      .spec(responseSpec(200))
      .extract().as(CreateUserResponseModel.class);
  }

  @Step("Частичное обновление пользователя")
  public static CreateUserResponseModel patchUser(int id, UserCreateRequestModel body) {
    return given(userRequestSpec)
      .body(body)
      .patch("/users/" + id)
      .then()
      .spec(responseSpec(200))
      .extract().as(CreateUserResponseModel.class);
  }

  @Step("Попытка создания пользователя c неверными данными")
  public static String createUserWithInvalidData(String badRequestData) {
    return given(userRequestSpec)
      .body(badRequestData)
      .post("/users")
      .then()
      .spec(responseSpec(400))
      .extract()
      .asString();
  }
}
