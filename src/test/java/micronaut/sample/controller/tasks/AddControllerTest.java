package micronaut.sample.controller.tasks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.netty.NettyMutableHttpResponse;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import micronaut.sample.controller.ConstraintViolationMessageConverter;
import micronaut.sample.controller.CustumConstraintViolation;
import micronaut.sample.exceptions.OverTaskException;
import micronaut.sample.usecase.tasks.TaskUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Controller の Unit Test. */
@ExtendWith(MockitoExtension.class)
class AddControllerTest {

  @Mock private TaskUseCase taskUseCase;

  @Mock private ConstraintViolationMessageConverter constraintViolationMessageConverter;

  @Mock private HttpRequest<Map<String, Object>> request;

  @InjectMocks AddController sut;

  @Test
  void testIndex() {
    // exercise
    var actual = sut.index();

    // verify
    assertThat(actual)
        .isInstanceOfSatisfying(
            NettyMutableHttpResponse.class,
            response -> {
              var responseBody = Map.of("taskForm", new TaskForm());
              assertThat(response)
                  .returns(HttpStatus.OK, NettyMutableHttpResponse::getStatus)
                  .returns(Optional.of(responseBody), NettyMutableHttpResponse::getBody);
            });
  }

  @Test
  @DisplayName("addTask のテスト.")
  void testAddTask() {
    // setup
    doNothing().when(taskUseCase).createTask(any());
    var taskForm = TaskForm.builder().taskName("タスク名").content("内容").build();

    // exercise
    var actual = sut.addTask(taskForm);

    // verify
    assertThat(actual)
        .isInstanceOfSatisfying(
            NettyMutableHttpResponse.class,
            response -> {
              assertThat(response.getHeaders().get(HttpHeaders.LOCATION)).isEqualTo("/tasks");
              assertThat(response)
                  .returns(HttpStatus.MOVED_PERMANENTLY, NettyMutableHttpResponse::getStatus);
            });
    verify(taskUseCase).createTask(taskForm.toCreateTaskUseCaseRequest());
  }

  @Test
  @DisplayName("addTask のテスト.上限を超えた")
  void testAddTask_OverTaskException() {
    // setup
    var message = "task の登録上限を超えました";
    doThrow(new OverTaskException(message)).when(taskUseCase).createTask(any());
    var taskForm = TaskForm.builder().taskName("タスク名").content("内容").build();

    // exercise(Unit テストでは Exception を throw することを verify)
    var actual = catchThrowable(() -> sut.addTask(taskForm));

    // verify
    assertThat(actual)
        .isInstanceOfSatisfying(
            ConstraintViolationException.class,
            e ->
                assertThat(e.getConstraintViolations())
                    .isEqualTo(Collections.singleton(new CustumConstraintViolation(message))));
  }

  @Test
  @DisplayName("onFailed のテスト.ConstraintViolationException を throw した時の振る舞い")
  void testOnFailed() {
    // setup
    var messages = Map.of("key", Collections.singletonList("error_01"));
    when(constraintViolationMessageConverter.violationsMessages(any())).thenReturn(messages);

    var taskForm = TaskForm.builder().taskName("タスク名").content("内容").build();
    when(request.getBody(ArgumentMatchers.<Class<TaskForm>>any()))
        .thenReturn(Optional.of(taskForm));

    // exercise
    var ex =
        new ConstraintViolationException(
            Collections.singleton(new CustumConstraintViolation("error message.")));
    var actual = sut.onFailed(request, ex);

    // verify
    var expected = Map.of("taskForm", taskForm, "errors", messages);
    assertThat(actual).isEqualTo(expected);

    verify(constraintViolationMessageConverter).violationsMessages(ex.getConstraintViolations());
  }
}
