package micronaut.sample.controller.tasks;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@MicronautTest
class TaskFormTest {

  @Inject Validator validator;

  @Test
  @DisplayName("validate でエラーが起きないケース")
  void testValidate() {
    // setup
    var sut = TaskForm.builder().taskName("name_0001").content("content_001").build();

    // exercise
    var actual = validator.validate(sut);

    // verify
    assertThat(actual).isEmpty();
  }

  @Test
  @DisplayName("null を渡した時")
  void testValidate_NullValue() {
    // setup
    var sut = new TaskForm();

    // exercise
    var actual = validator.validate(sut);

    // verify
    var messages =
        actual
            .stream()
            .map(value -> value.getPropertyPath() + ":" + value.getMessage())
            .collect(Collectors.toList());
    assertThat(messages).containsOnly("taskName:must not be null", "content:must not be null");
  }

  @Test
  @DisplayName("min より少ない文字数")
  void testValidate_MinValue() {
    // setup
    var sut = TaskForm.builder().content("").taskName("").build();

    // exercise
    var actual = validator.validate(sut);

    // verify
    var messages =
        actual
            .stream()
            .map(value -> value.getPropertyPath() + ":" + value.getMessage())
            .collect(Collectors.toList());
    assertThat(messages)
        .containsOnly(
            "taskName:size must be between 1 and 256", "content:size must be between 1 and 1024");
  }

  @Test
  @DisplayName("max より大きい文字数")
  void testValidate_MaxValue() {
    // setup
    var sut = TaskForm.builder().content("a".repeat(1025)).taskName("a".repeat(257)).build();

    // exercise
    var actual = validator.validate(sut);

    // verify
    var messages =
        actual
            .stream()
            .map(value -> value.getPropertyPath() + ":" + value.getMessage())
            .collect(Collectors.toList());
    assertThat(messages)
        .containsOnly(
            "taskName:size must be between 1 and 256", "content:size must be between 1 and 1024");
  }
}
