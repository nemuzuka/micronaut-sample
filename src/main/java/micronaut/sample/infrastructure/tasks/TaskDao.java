package micronaut.sample.infrastructure.tasks;

import java.util.List;
import micronaut.sample.infrastructure.doma.DaoConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.jdbc.Result;

@Dao
@DaoConfig
public interface TaskDao {

  @Insert
  Result<TaskEntity> insert(TaskEntity taskEntity);

  @Select
  @Sql("SELECT * FROM tasks ORDER BY task_id")
  List<TaskEntity> selectAll();
}
