package micronaut.sample.controller.tasks;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import micronaut.sample.usecase.tasks.CreateTaskUseCaseRequest;

/** Task Form. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Introspected
public class TaskForm {

  /** task_name. */
  @NotNull
  @Size(min = 1, max = 256)
  private String taskName;

  /** content. */
  @NotNull
  @Size(min = 1, max = 1024)
  private String content;

  /**
   * CreateTaskUseCaseRequest インスタンス生成.
   *
   * @return CreateTaskUseCaseRequest インスタンス
   */
  public CreateTaskUseCaseRequest toCreateTaskUseCaseRequest() {
    return new CreateTaskUseCaseRequest(taskName, content);
  }
}
