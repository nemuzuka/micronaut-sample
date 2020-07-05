package micronaut.sample.domain.tasks;

import java.util.List;
import micronaut.sample.exceptions.OverTaskException;

/** {@link Task} Repository. */
public interface TaskRepository {

  /**
   * Task 登録.
   *
   * @param task 対象 Task
   * @return 登録後 Task
   * @throws OverTaskException 登録上限を超えた
   */
  Task createTask(Task task);

  List<Task> findAll();
}
