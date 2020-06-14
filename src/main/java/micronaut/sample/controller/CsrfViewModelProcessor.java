package micronaut.sample.controller;

import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.session.http.SessionForRequest;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.model.ViewModelProcessor;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

/** CSRF をレスポンスする ViewModelProcessor. */
@Slf4j
@Singleton
public class CsrfViewModelProcessor implements ViewModelProcessor {

  public static final String CSRF_PARAMETER_KEY = "csrfToken";

  /**
   * CSRF トークン設定.
   *
   * <pre>
   * GET リクエストでレスポンスの model, Session が存在する場合、csrf トークンを設定します。
   * POST リクエストの場合、リクエストパラメータの csrf トークンをレスポンスに設定します。
   * </pre>
   *
   * @param request request
   * @param modelAndView レスポンス
   */
  @Override
  public void process(
      @Nonnull HttpRequest<?> request, @Nonnull ModelAndView<Map<String, Object>> modelAndView) {

    if (Objects.equals(request.getMethod(), HttpMethod.GET)) {
      setCsrfForGet(request, modelAndView);
    } else {
      setCsrfForPost(request, modelAndView);
    }
  }

  private void setCsrfForPost(
      HttpRequest<?> request, ModelAndView<Map<String, Object>> modelAndView) {

    var requestParameterOpt = request.getBody(Map.class);
    if (requestParameterOpt.isEmpty()) {
      return;
    }

    var modelOpt = modelAndView.getModel();
    if (modelOpt.isEmpty()) {
      return;
    }

    @SuppressWarnings("unchecked")
    var requestParameter =
        (Map<String, Object>) requestParameterOpt.orElseThrow(() -> new AssertionError("invalid"));
    var responseMap = modelOpt.orElseThrow(() -> new AssertionError("invalid"));

    var csrf = (String) requestParameter.get(CSRF_PARAMETER_KEY);
    responseMap.put(CSRF_PARAMETER_KEY, csrf);
  }

  private void setCsrfForGet(
      HttpRequest<?> request, ModelAndView<Map<String, Object>> modelAndView) {
    var modelOpt = modelAndView.getModel();
    if (modelOpt.isEmpty()) {
      return;
    }

    var sessionOpt = SessionForRequest.find(request);
    if (sessionOpt.isEmpty()) {
      return;
    }

    sessionOpt.ifPresent(
        session -> {
          var csrf = UUID.randomUUID().toString();
          session.put(CSRF_PARAMETER_KEY, csrf);
          var responseMap = modelOpt.orElseThrow(() -> new AssertionError("invalid"));
          responseMap.put(CSRF_PARAMETER_KEY, csrf);
        });
  }
}
