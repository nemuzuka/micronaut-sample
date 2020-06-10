package micronaut.sample.controller.tasks;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import java.util.Map;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import micronaut.sample.usecase.tasks.TaskUseCase;

/** Task 管理 TOP 画面. */
@Controller("/tasks")
@RequiredArgsConstructor
public class IndexController {

  @Inject private final TaskUseCase taskUseCase;

  @Get
  @View("tasks/index")
  public HttpResponse<Map<String, Object>> index() {
    Map<String, Object> responseMap = Map.of("tasks", taskUseCase.allTask());
    return HttpResponse.ok(responseMap);
  }
}
