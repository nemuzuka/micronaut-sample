package micronaut.sample.infrastructure.tasks;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import micronaut.sample.config.SystemConfigurationProperties;
import micronaut.sample.domain.tasks.Task;
import micronaut.sample.domain.tasks.TaskRepository;
import micronaut.sample.exceptions.OverTaskException;

@Singleton
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

  private final TaskDao taskDao;

  private final SystemConfigurationProperties configurationProperties;

  @Override
  public Task createTask(Task task) {

    var taskDetail = task.getTaskDetail();
    var taskEntity =
        new TaskEntity(
            task.getTaskId().getValue(), taskDetail.getTaskName(), taskDetail.getContent());
    taskDao.insert(taskEntity);

    if (configurationProperties.getMaxTaskCount() < taskDao.selectAll().size()) {
      // 登録した後上限を超えるかチェックする
      throw new OverTaskException("登録可能な Task の上限を超えました");
    }
    return task;
  }

  @Override
  public List<Task> findAll() {
    return taskDao
        .selectAll()
        .stream()
        .map(
            task -> {
              var taskId = new Task.TaskId(task.getTaskId());
              var taskDetail = new Task.TaskDetail(task.getTaskName(), task.getContent());
              return new Task(taskId, taskDetail);
            })
        .collect(Collectors.toList());
  }
}
