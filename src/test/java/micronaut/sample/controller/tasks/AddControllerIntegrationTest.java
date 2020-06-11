package micronaut.sample.controller.tasks;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MicronautTest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import micronaut.sample.controller.IntegrationTestHelper;
import micronaut.sample.usecase.tasks.TaskUseCase;
import micronaut.sample.usecase.tasks.TaskUseCaseResult;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** {@link AddController} の IntegrationTest. */
@Slf4j
@MicronautTest
class AddControllerIntegrationTest {

  @Inject IntegrationTestHelper integrationTestHelper;

  @Inject TaskUseCase taskUseCase;

  @BeforeEach
  void setup() {
    taskUseCase.deleteAll(); // RDBMS 使わない弊害が...
  }

  @Test
  @DisplayName("GET /tasks/add のテスト")
  void testIndex() throws Exception {
    // setup
    var client = integrationTestHelper.login(); // login 状態にした client を取得
    var request = integrationTestHelper.buildGetRequest("/tasks/add");

    // exercise
    var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

    // verify
    assertResponseWithBody(
        response,
        HttpStatus.OK,
        html -> {
          assertElementExists(html, "input[name='csrfToken']");
          assertTextEquals(html, "h3.title.is-3", "Task 登録");
        });
  }

  @Test
  @DisplayName("POST /tasks/add のテスト")
  void testAdd() throws Exception {
    assertThat(taskUseCase.allTask()).isEmpty();

    // setup
    var client = integrationTestHelper.login();
    var csrfToken = integrationTestHelper.getCsrfToken(client, "/tasks/add"); // csrf トークンを取得

    var postParameter =
        "csrfToken=" + csrfToken + "&taskName=task_name_001&content=task_content_001";
    var request = integrationTestHelper.buildPostRequest("/tasks/add", postParameter);

    // exercise
    var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

    // verify
    assertResponseWithLocation(
        response,
        HttpStatus.MOVED_PERMANENTLY,
        location -> assertThat(location).isEqualTo("/tasks"));

    // 永続化していること
    var actual = taskUseCase.allTask();
    assertThat(actual).hasSize(1);
    var actualTask = actual.get(0);
    assertThat(actualTask.getTaskId()).isNotNull();
    assertThat(actualTask)
        .returns("task_name_001", TaskUseCaseResult::getTaskName)
        .returns("task_content_001", TaskUseCaseResult::getContent);
  }

  @Test
  @DisplayName("task の登録上限を超えた。profile は test なので application-test.yml が優先。3件目の登録で失敗する")
  void testAdd_OverTask() throws Exception {
    assertThat(taskUseCase.allTask()).isEmpty();

    // setup
    var client = integrationTestHelper.login();
    var csrfToken = integrationTestHelper.getCsrfToken(client, "/tasks/add");

    var postParameter =
        "csrfToken=" + csrfToken + "&taskName=task_name_001&content=task_content_001";
    var request = integrationTestHelper.buildPostRequest("/tasks/add", postParameter);
    client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)); // 2回登録

    // exercise
    var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

    // verify
    assertResponseWithBody(
        response,
        HttpStatus.INTERNAL_SERVER_ERROR,
        html -> {
          assertValueEquals(html, "input[name='csrfToken']", csrfToken);
          assertTextEquals(html, "h3.title.is-3", "Task 登録");
          assertTextEquals(html, "div.notification.is-danger", "登録可能な Task の上限を超えました");
        });
  }

  @Test
  @DisplayName("validation でエラー")
  void testAdd_ValidationError() throws Exception {
    assertThat(taskUseCase.allTask()).isEmpty();

    // setup
    var client = integrationTestHelper.login();
    var csrfToken = integrationTestHelper.getCsrfToken(client, "/tasks/add");

    var postParameter = "csrfToken=" + csrfToken + "&taskName=&content=task_content_001";
    var request = integrationTestHelper.buildPostRequest("/tasks/add", postParameter);

    // exercise
    var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

    // verify
    assertResponseWithBody(
        response,
        HttpStatus.INTERNAL_SERVER_ERROR,
        html -> {
          log.info("{}", html);
          assertValueEquals(html, "input[name='csrfToken']", csrfToken);
          assertTextEquals(html, "h3.title.is-3", "Task 登録");
          assertTextEquals(html, "p.help.is-danger", "size must be between 1 and 256");
        });
  }

  private static void assertResponseWithBody(
      HttpResponse<?> response, HttpStatus expectedStatus, Consumer<String> entityVerifier) {
    assertThat(response.statusCode()).isEqualTo(expectedStatus.getCode());
    var body = (String) response.body();
    entityVerifier.accept(body);
  }

  private static void assertElementExists(String html, String selector) {
    assertThat(Jsoup.parse(html).select(selector).isEmpty()).isFalse();
  }

  private static void assertTextEquals(String html, String selector, String expectedValue) {
    assertThat(Jsoup.parse(html).select(selector).text()).isEqualTo(expectedValue);
  }

  private static void assertResponseWithLocation(
      HttpResponse<?> response, HttpStatus expectedStatus, Consumer<String> locationVerifier) {
    assertThat(response.statusCode()).isEqualTo(expectedStatus.getCode());
    var location =
        response
            .headers()
            .firstValue(HttpHeaders.LOCATION)
            .orElseThrow(() -> new AssertionError("not found location"));
    locationVerifier.accept(location);
  }

  private static void assertValueEquals(String html, String selector, String expectedValue) {
    assertThat(Jsoup.parse(html).select(selector).val()).isEqualTo(expectedValue);
  }
}
