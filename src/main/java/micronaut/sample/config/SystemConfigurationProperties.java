package micronaut.sample.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;
import lombok.ToString;

/** システム管理者用の設定. */
@Data
@ToString(exclude = "secret")
@ConfigurationProperties("system.admin")
public class SystemConfigurationProperties {

  /** システム管理者ID. */
  private String identity;

  /** システム管理者パスワード. */
  private String secret;

  /** 登録可能な Task の上限. */
  private int maxTaskCount;
}
