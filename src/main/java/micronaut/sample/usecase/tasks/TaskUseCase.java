package micronaut.sample.usecase.tasks;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import micronaut.sample.domain.tasks.TaskRepository;
import micronaut.sample.exceptions.OverTaskException;
import micronaut.sample.tx.Transactional;

/** Task の UseCase. */
@Singleton
@RequiredArgsConstructor
@Transactional
public class TaskUseCase {

  private final TaskRepository taskRepo;

  /**
   * Task 登録.
   *
   * @param createTaskUseCaseRequest パラメータ
   * @throws OverTaskException 登録可能な Task の上限を超えた
   */
  public void createTask(CreateTaskUseCaseRequest createTaskUseCaseRequest) {
    var task = createTaskUseCaseRequest.toTask();
    taskRepo.createTask(task);
  }

  /**
   * Task 一覧取得.
   *
   * @return 該当データ
   */
  public List<TaskUseCaseResult> allTask() {
    return taskRepo
        .findAll()
        .stream()
        .map(TaskUseCaseResult::fromTask)
        .collect(Collectors.toList());
  }
}
