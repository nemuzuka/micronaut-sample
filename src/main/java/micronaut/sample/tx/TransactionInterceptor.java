package micronaut.sample.tx;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import java.util.function.Supplier;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;

@Singleton
@RequiredArgsConstructor
public class TransactionInterceptor implements MethodInterceptor<Object, Object> {

  private final LocalTransactionManager transactionManager;

  @Override
  public Object intercept(MethodInvocationContext<Object, Object> context) {
    return transactionManager.required((Supplier<Object>) context::proceed);
  }
}
