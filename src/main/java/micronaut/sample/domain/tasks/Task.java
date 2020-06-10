package micronaut.sample.domain.tasks;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
  private final String taskId;

  /** task_name. */
  private final String taskName;

  /** content. */
  private final String content;
}
