package micronaut.sample.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.Maybe;
import java.util.Collections;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import micronaut.sample.Role;
import micronaut.sample.config.SystemConfigurationProperties;
import org.reactivestreams.Publisher;

/** 認証処理. */
@Singleton
@RequiredArgsConstructor
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

  private final SystemConfigurationProperties systemConfigurationProperties;

  /**
   * 認証処理.
   *
   * @param httpRequest request
   * @param authenticationRequest 認証 request
   * @return 結果
   */
  @Override
  public Publisher<AuthenticationResponse> authenticate(
      @Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
    return Maybe.<AuthenticationResponse>create(
            emitter -> {
              // 認証処理。本当は DB にアクセスするが、雑にプロパティで管理
              if (Objects.equals(
                      authenticationRequest.getIdentity(),
                      systemConfigurationProperties.getIdentity())
                  && Objects.equals(
                      authenticationRequest.getSecret(),
                      systemConfigurationProperties.getSecret())) {
                // 認証成功時、UserDetails を設定する
                var userDetails =
                    new UserDetails(
                        (String) authenticationRequest.getIdentity(),
                        Collections.singletonList(Role.SYSTEM_ADMIN.name()));
                emitter.onSuccess(userDetails);
              } else {
                emitter.onError(new AuthenticationException(new AuthenticationFailed()));
              }
            })
        .toFlowable();
  }
}
