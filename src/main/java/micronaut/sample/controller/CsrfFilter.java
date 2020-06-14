package micronaut.sample.controller;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.OncePerRequestHttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.session.http.SessionForRequest;
import io.micronaut.views.ViewsRenderer;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

/** CSRF 対応の Filter. */
@Slf4j
@RequiredArgsConstructor
@Filter(patterns = "/**", methods = HttpMethod.POST)
public class CsrfFilter extends OncePerRequestHttpServerFilter {

  private final ViewsRenderer viewsRenderer;

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }

  /**
   * CSRF チェック.
   *
   * <p>CSRF のチェックを行い、リクエストを受け付けられない場合、403 ページを表示します。
   *
   * @param request request
   * @param chain filter chain
   * @return レスポンス
   */
  @Override
  protected Publisher<MutableHttpResponse<?>> doFilterOnce(
      HttpRequest<?> request, ServerFilterChain chain) {
    if (validateCsrfRequest(request)) {
      return chain.proceed(request);
    }
    return Publishers.just(
        HttpResponse.status(HttpStatus.FORBIDDEN)
            .body(viewsRenderer.render("forbidden", Collections.EMPTY_MAP))
            .contentType(MediaType.TEXT_HTML));
  }

  /**
   * CSRF のリクエストか検証.
   *
   * <pre>
   * リクエストが POST /login 以外の時に
   *
   * - session 上に格納している CRSF 値
   * - リクエストパラメータで設定している CRSF 値
   *
   * が合致する時に有効なリクエストとして判定します。
   * </pre>
   *
   * @param request リクエスト
   * @return 不正リクエストの場合、false
   */
  private boolean validateCsrfRequest(HttpRequest<?> request) {

    if (Objects.equals(request.getUri(), UriBuilder.of("/login").build())) {
      // 本来はログインの時も CSRF トークンチェックを入れた方が良いと思うけど、
      // むやみに Session 作られたくないのでひとまず除外
      return true;
    }

    try {
      var session =
          SessionForRequest.find(request)
              .orElseThrow(() -> new IllegalStateException("Session is empty."));
      var sessionValue =
          session
              .get(CsrfViewModelProcessor.CSRF_PARAMETER_KEY, String.class)
              .orElseThrow(
                  () ->
                      new IllegalStateException(
                          "Session(" + CsrfViewModelProcessor.CSRF_PARAMETER_KEY + ") is empty."));

      @SuppressWarnings("unchecked")
      Map<String, Object> body = request.getBody(Map.class).orElse(Map.of());
      var requestValue = (String) body.get(CsrfViewModelProcessor.CSRF_PARAMETER_KEY);
      return Objects.equals(sessionValue, requestValue);
    } catch (IllegalStateException e) {
      log.info("Invalid request: {}", e.getMessage(), e);
      return false;
    }
  }
}
