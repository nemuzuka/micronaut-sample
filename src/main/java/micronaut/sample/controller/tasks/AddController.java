package micronaut.sample.controller.tasks;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.views.View;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import micronaut.sample.controller.ConstraintViolationMessageConverter;
import micronaut.sample.controller.CustumConstraintViolation;
import micronaut.sample.exceptions.OverTaskException;
import micronaut.sample.usecase.tasks.TaskUseCase;

@Controller("/tasks/add")
@RequiredArgsConstructor
public class AddController {

  @Inject private final TaskUseCase taskUseCase;

  @Inject private final ConstraintViolationMessageConverter constraintViolationMessageConverter;

  /**
   * Task 新規登録画面表示.
   *
   * @return Task 新規登録画面表示用データ
   */
  @Get
  @View("tasks/edit")
  public HttpResponse<Map<String, Object>> index() {
    Map<String, Object> responseMap = Map.of("taskForm", new TaskForm());
    return HttpResponse.ok(responseMap);
  }

  /**
   * Task 登録.
   *
   * @param taskForm Form
   * @return レスポンス
   */
  @Post
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public HttpResponse<String> addTask(@Body @Valid TaskForm taskForm) {
    try {
      var createTaskUseCaseRequest = taskForm.toCreateTaskUseCaseRequest();
      taskUseCase.createTask(createTaskUseCaseRequest);
    } catch (OverTaskException e) {
      // 上限を超えた場合、画面を再描画
      var constraintViolation = new CustumConstraintViolation(e.getMessage());
      throw new ConstraintViolationException(Collections.singleton(constraintViolation));
    }

    var uri = UriBuilder.of("/tasks").build();
    return HttpResponse.redirect(uri);
  }

  /**
   * エラー発生時のハンドリング.
   *
   * @param request リクエスト
   * @param ex catch したエラー
   * @return レスポンス
   */
  @View("tasks/edit")
  @Error(exception = ConstraintViolationException.class)
  public Map<String, Object> onFailed(
      HttpRequest<Map<String, Object>> request, ConstraintViolationException ex) {
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(
        "errors",
        constraintViolationMessageConverter.violationsMessages(ex.getConstraintViolations()));
    request.getBody(TaskForm.class).ifPresent(form -> responseMap.put("taskForm", form));
    return responseMap;
  }
}
