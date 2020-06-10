package micronaut.sample.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.views.ViewsRenderer;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** 全体で使用するエラーハンドラ. */
@Slf4j
@RequiredArgsConstructor
@Controller("/errors")
public class GlobalErrorHandler {

  private final ViewsRenderer viewsRenderer;

  /**
   * 404 ページ表示.
   *
   * @return レスポンス
   */
  @Error(status = HttpStatus.NOT_FOUND, global = true)
  public HttpResponse<?> notFoundForPage() {
    return HttpResponse.ok(viewsRenderer.render("notFound", Collections.EMPTY_MAP))
        .contentType(MediaType.TEXT_HTML);
  }

  /**
   * 403 ページ表示.
   *
   * @return レスポンス
   */
  @Error(status = HttpStatus.FORBIDDEN, global = true)
  public HttpResponse<?> forbiddenForPage() {
    return HttpResponse.ok(viewsRenderer.render("forbidden", Collections.EMPTY_MAP))
        .contentType(MediaType.TEXT_HTML);
  }

  /**
   * ログイン画面で失敗した.
   *
   * @return レスポンス
   */
  @Error(status = HttpStatus.UNAUTHORIZED, global = true)
  public HttpResponse<?> unauthorized() {
    return HttpResponse.redirect(UriBuilder.of("/login").build());
  }
}
