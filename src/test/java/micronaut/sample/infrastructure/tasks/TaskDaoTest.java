package micronaut.sample.infrastructure.tasks;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.annotation.MicronautTest;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;

@MicronautTest
@Property(name = "flyway.datasources.default.locations", value = "db/migration,db_fixtures/minimum")
class TaskDaoTest {
  @Inject private TaskDao sut;

  @Inject private LocalTransactionManager transactionManager;

  @Test
  void testInsert() {
    transactionManager.required(
        () -> {
          // setup
          var task = new TaskEntity(UUID.randomUUID().toString(), "タスク名", "内容");

          // exercise
          var actual = sut.insert(task);

          // verify
          assertThat(actual.getCount()).isEqualTo(1);
          assertThat(actual.getEntity()).isEqualTo(task);
          assertThat(sut.selectAll()).hasSize(3); // 元々2件 + 1件 insert
        });
  }

  @Test
  void testSelectAll() {
    transactionManager.required(
        () -> {
          // exercise
          var actual = sut.selectAll();

          // verify
          assertThat(actual).hasSize(2);
          assertThat(actual.get(0).getTaskId()).isEqualTo("001-dummy");
          assertThat(actual.get(1).getTaskId()).isEqualTo("002-dummy");
        });
  }
}
