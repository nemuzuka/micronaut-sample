package micronaut.sample.infrastructure.tasks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/** Task entity. */
@Entity(immutable = true)
@Table(name = "tasks")
@Data
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class TaskEntity {

  @Id
  @Column(name = "task_id")
  private String taskId;

  @Column(name = "task_name")
  private String taskName;

  @Column(name = "content")
  private String content;
}
