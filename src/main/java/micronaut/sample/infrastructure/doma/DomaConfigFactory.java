package micronaut.sample.infrastructure.doma;

import io.micronaut.context.annotation.Factory;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.ConfigSupport;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;

@Factory
public class DomaConfigFactory {

  @Singleton
  public LocalTransactionDataSource localTransactionDataSource(DataSource dataSource) {
    return new LocalTransactionDataSource(dataSource);
  }

  @Singleton
  public LocalTransactionManager localTransactionManager(LocalTransactionDataSource dataSource) {
    return new LocalTransactionManager(
        dataSource.getLocalTransaction(ConfigSupport.defaultJdbcLogger));
  }
}
