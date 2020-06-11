package micronaut.sample.usecase.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;
import micronaut.sample.config.SystemConfigurationProperties;
import micronaut.sample.domain.tasks.Task;
import micronaut.sample.exceptions.OverTaskException;

/**
 * Task の UseCase.
 *
 * <p>Singleton なのに状態を持っているのは間違いですが、あえてやっています。 本来は RDBMS 等に永続化します。
 */
@Singleton
@AllArgsConstructor
public class TaskUseCase {

  private static final List<Task> TASKS = new ArrayList<>(); // Repository の代わり。これが良くないのは十分承知しています。

  private final SystemConfigurationProperties configurationProperties;

  /**
   * Task 登録.
   *
   * @param createTaskUseCaseRequest パラメータ
   * @throws OverTaskException 登録可能な Task の上限を超えた
   */
  public void createTask(CreateTaskUseCaseRequest createTaskUseCaseRequest) {

    if (configurationProperties.getMaxTaskCount() < TASKS.size() + 1) {
      // 登録した時に上限を超えるかチェックする
      // 本当は登録してからの方が良いんだろうけど、rollback が面倒なので...
      throw new OverTaskException("登録可能な Task の上限を超えました");
    }

    var task = createTaskUseCaseRequest.toTask();
    TASKS.add(task);
  }

  /**
   * Task 一覧取得.
   *
   * @return 該当データ
   */
  public List<TaskUseCaseResult> allTask() {
    return TASKS.stream().map(TaskUseCaseResult::fromTask).collect(Collectors.toList());
  }

  /** Task 初期化. */
  public void deleteAll() {
    TASKS.clear();
  }
}
