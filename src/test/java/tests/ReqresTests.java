package tests;

import models.CreateUserResponseModel;
import models.UserCreateRequestModel;
import models.UserListResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.UserRequest;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("API тесты")
public class ReqresTests extends TestBase {


  @Test
  @DisplayName("Лист пользователей")
  void usersListTest() {
    UserListResponseModel response = UserRequest.getUsersList(2);
    step("Проверяем содержимое ответа", () -> {
      assertThat(response.getPage()).isEqualTo(2);
      assertThat(response.getPerPage()).isEqualTo(6);
      assertThat(response.getData()).isNotEmpty();
    });
  }

  @Test
  @DisplayName("Успешное создание пользователя")
  void successfulUserCreateTest() {
    UserCreateRequestModel request = new UserCreateRequestModel();
    request.setName("morpheus");
    request.setJob("leader");
    CreateUserResponseModel response = UserRequest.createUser(request);
    step("Проверка данных", () -> {
      assertThat(response.getName()).isEqualTo("morpheus");
      assertThat(response.getJob()).isEqualTo("leader");
      assertThat(response.getId()).isNotBlank();
    });
  }

  @Test
  @DisplayName("Неуспешное создание пользователя")
  void unsuccessfulUserCreateTest() {
    String badRequestData = "{2}";
    String unsuccessfulCreateUser = UserRequest.createUserWithInvalidData(badRequestData);
    step("Проверка текста ошибки", () -> {
      assertThat(unsuccessfulCreateUser.toLowerCase()).contains("bad request");
    });
  }

  @Test
  @DisplayName("Обновление пользователя")
  void successfulUserUpdateTest() {
    UserCreateRequestModel requestModel = new UserCreateRequestModel();
    requestModel.setName("morpheus");
    requestModel.setJob("zion resident");
    CreateUserResponseModel response = UserRequest.updateUser(2, requestModel);
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
    CreateUserResponseModel response = UserRequest.patchUser(2, requestModel);
    step("Проверка данных созданного пользователя", () -> {
      assertThat(response.getName()).isEqualTo("morpheus");
      assertThat(response.getJob()).isEqualTo("leader");
    });
  }
}
