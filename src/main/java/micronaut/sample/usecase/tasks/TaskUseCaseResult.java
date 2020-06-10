package micronaut.sample.usecase.tasks;

import lombok.Value;
import micronaut.sample.domain.tasks.Task;

/** TaskUseCase の Result. */
@Value
public class TaskUseCaseResult {

  /** task_id. */
  String taskId;

  /** task_name. */
  String taskName;

  /** content. */
  String content;

  /**
   * Task から生成.
   *
   * @param task Task
   * @return 生成インスタンス
   */
  public static TaskUseCaseResult fromTask(Task task) {
    return new TaskUseCaseResult(task.getTaskId(), task.getTaskName(), task.getContent());
  }
}
