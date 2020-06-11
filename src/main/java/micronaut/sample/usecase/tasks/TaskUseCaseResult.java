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
    var taskId = task.getTaskId();
    var taskDetail = task.getTaskDetail();
    return new TaskUseCaseResult(
        taskId.getValue(), taskDetail.getTaskName(), taskDetail.getContent());
  }
}
