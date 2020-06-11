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
    var taskId = new Task.TaskId(UUID.randomUUID().toString());
    var taskDetail = new Task.TaskDetail(taskName, content);
    return new Task(taskId, taskDetail);
  }
}
