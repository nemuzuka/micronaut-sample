package micronaut.sample.controller;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/** 画面にエラー表示するための ConstraintViolation. */
@EqualsAndHashCode
@Getter
public class CustumConstraintViolation implements ConstraintViolation<String> {
  private String rootBean;
  private Object invalidValue;
  private String message;
  private String messageTemplate;
  private Path path;
  private Class<String> rootBeanClass;
  private Object leafBean;
  private ConstraintDescriptor<?> constraintDescriptor;
  private Object[] executableParams;

  /**
   * コンストラクタ.
   *
   * @param message メッセージ
   */
  public CustumConstraintViolation(String message) {
    this.message = message;
  }

  @Override
  public Object[] getExecutableParameters() {
    return new Object[0];
  }

  @Override
  public Object getExecutableReturnValue() {
    return null;
  }

  @Override
  public Path getPropertyPath() {
    return null;
  }

  @Override
  public <U> U unwrap(Class<U> type) {
    return null;
  }
}
