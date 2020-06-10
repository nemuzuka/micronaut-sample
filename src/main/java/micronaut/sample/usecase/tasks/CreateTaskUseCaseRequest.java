package micronaut.sample.usecase.tasks;

import java.util.UUID;
import lombok.Value;
import micronaut.sample.domain.tasks.Task;

/** CreateTaskUseCase の Request. */
@Value
public class CreateTaskUseCaseRequest {

  /** task_name. */
  String taskName;

  /** content. */
  String content;

  public Task toTask() {
    return new Task(UUID.randomUUID().toString(), taskName, content);
  }
}
