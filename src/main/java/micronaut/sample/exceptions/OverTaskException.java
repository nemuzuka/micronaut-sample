package micronaut.sample.exceptions;

/** 登録可能な Task の上限を超えた. */
public class OverTaskException extends RuntimeException {
  public OverTaskException(String message) {
    super(message);
  }
}
