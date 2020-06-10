package micronaut.sample.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

/** ログイン画面表示. */
@Controller("/login")
public class LoginController {
  @Get
  @View("login")
  public HttpResponse<String> index() {
    return HttpResponse.ok();
  }
}
