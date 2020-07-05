package micronaut.sample.infrastructure.doma;

import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.jdbc.tx.TransactionManager;

@Singleton
@RequiredArgsConstructor
public class DomaConfig implements Config {

  private final LocalTransactionDataSource dataSource;

  private final LocalTransactionManager transactionManager;

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public Dialect getDialect() {
    // TODO プロパティからとるとか良い感じに変えてください
    return new H2Dialect();
  }

  @Override
  public TransactionManager getTransactionManager() {
    return transactionManager;
  }
}
