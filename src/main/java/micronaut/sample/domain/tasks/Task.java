package micronaut.sample.domain.tasks;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

/**
 * Task ドメイン.
 *
 * <p>※適当です.
 */
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor
@Builder(access = AccessLevel.PROTECTED)
public class Task {

  /** task_id. */
  private final TaskId taskId;

  /** task 詳細. */
  private final TaskDetail taskDetail;

  @Value
  public static class TaskId {
    String value;
  }

  @Value
  public static class TaskDetail {
    String taskName;
    String content;
  }
}
