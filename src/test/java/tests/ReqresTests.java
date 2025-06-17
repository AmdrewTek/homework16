package tests;

import models.CreateUserResponseModel;
import models.ErrorResponseModel;
import models.UserCreateRequestModel;
import models.UserListResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.UpdateUserRequestSpec;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.CreateUserSpec.*;
import static specs.GetUserListSpec.getUserListRequestSpec;
import static specs.GetUserListSpec.getUserListResponseSpec;
import static specs.UpdateUserRequestSpec.updateUserRequestSpec;
import static specs.UpdateUserRequestSpec.updateUserResponseSpec;

@DisplayName("API тесты")
public class ReqresTests extends TestBase {

  @Test
  @DisplayName("Лист пользователей")
  void usersListTest() {
    UserListResponseModel response = step("Получение списка пользователей", () ->
      given(getUserListRequestSpec)
        .when()
        .get("/users?page=2")
        .then()
        .spec(getUserListResponseSpec)
        .extract().as(UserListResponseModel.class));
    step("Проверяем содержимое ответа", () -> {
      assertThat(response.getPage()).isEqualTo(2);
      assertThat(response.getPer_page()).isEqualTo(6);
      assertThat(response.getData()).isNotEmpty();
    });
  }

  @Test
  @DisplayName("Успешное создание пользователя")
  void successfulUserCreateTest() {
    UserCreateRequestModel requestModel = new UserCreateRequestModel();
    requestModel.setName("morpheus");
    requestModel.setJob("leader");
    CreateUserResponseModel response = step("Успешное создание пользователя", () ->
      given(createUserRequestSpec)
        .body(requestModel)
        .when()
        .post("/users")
        .then()
        .spec(createUserResponseSpec)
        .extract().as(CreateUserResponseModel.class));
    step("Проверка данных созданного пользователя", () -> {
      assertThat(response.getName()).isEqualTo("morpheus");
      assertThat(response.getJob()).isEqualTo("leader");
      assertThat(response.getId()).isNotNull();
    });
  }

  @Test
  @DisplayName("Неуспешное создание пользователя")
  void unsuccessfulUserCreateTest() {
    String badRequestData = "{2}";
    String unsuccessfulCreateUser = step("Попытка создания пользователя c неверными данными", () ->
      given(createUserRequestSpec)
        .body(badRequestData)
        .post("/users")
        .then()
        .spec(uncreatedUserResponseSpec)
        .extract()
        .asString()
    );

    ErrorResponseModel errorResponse = step("Создание модели ошибки из HTML", () -> {
      ErrorResponseModel model = new ErrorResponseModel();
      model.setError("HtmlError");
      model.setMessage(unsuccessfulCreateUser);
      return model;
    });

    step("Проверка текста ошибки", () -> {
      assertThat(errorResponse.getMessage().toLowerCase()).contains("bad request");
    });
  }
  @Test
  @DisplayName("Обновление пользователя")
  void successfulUserUpdateTest() {

    UserCreateRequestModel requestModel = new UserCreateRequestModel();
    requestModel.setName("morpheus");
    requestModel.setJob("zion resident");
    CreateUserResponseModel response = step("Обновление пользователя", () ->
      given(updateUserRequestSpec)
        .body(requestModel)
        .when()
        .put("/users/2")
        .then()
        .spec(updateUserResponseSpec)
        .extract().as(CreateUserResponseModel.class));
    step("Проверка данных созданного пользователя", () -> {
      assertThat(response.getName()).isEqualTo("morpheus");
      assertThat(response.getJob()).isEqualTo("zion resident");
    });
  }
  @Test
  @DisplayName("Частичное обновление пользователя")
  void successfulPartialUpdateTest() {

    UserCreateRequestModel requestModel = new UserCreateRequestModel();
    requestModel.setName("morpheus");
    requestModel.setJob("leader");
    CreateUserResponseModel response = step("Частичное обновление пользователя", () ->
      given(updateUserRequestSpec)
        .body(requestModel)
        .when()
        .patch("/users/2")
        .then()
        .spec(updateUserResponseSpec)
        .extract().as(CreateUserResponseModel.class));
    step("Проверка данных созданного пользователя", () -> {
      assertThat(response.getName()).isEqualTo("morpheus");
      assertThat(response.getJob()).isEqualTo("leader");
    });
  }
}
