package micronaut.sample.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.Path;

@Singleton
public class ConstraintViolationMessageConverter {
  /**
   * メッセージ Map 生成.
   *
   * <p>key は当該プロパティです。null の場合、全体向けのメッセージになります。
   *
   * @param violations 構成情報
   * @return 生成メッセージ Map
   */
  public Map<String, List<String>> violationsMessages(Set<ConstraintViolation<?>> violations) {
    var map = new HashMap<String, List<String>>();
    for (ConstraintViolation<?> violation : violations) {
      var mapKey = getKey(violation);
      map.computeIfAbsent(mapKey, key -> new ArrayList<>()).add(violation.getMessage());
    }
    return map;
  }

  private static String getKey(ConstraintViolation<?> violation) {
    Path.Node lastNode = lastNode(violation.getPropertyPath());
    String key = null;
    if (lastNode != null) {
      key = lastNode.getName();
    }
    return key;
  }

  private static Path.Node lastNode(Path path) {
    if (path == null) {
      return null;
    }

    Path.Node lastNode = null;
    for (final Path.Node node : path) {
      lastNode = node;
    }
    return lastNode;
  }
}
