package micronaut.sample.controller;

import io.micronaut.http.MediaType;
import io.micronaut.runtime.server.EmbeddedServer;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import micronaut.sample.config.SystemConfigurationProperties;
import org.jsoup.Jsoup;

/** IntegrationTest 用の Helper. */
@Slf4j
@Singleton
public class IntegrationTestHelper {

  @Inject EmbeddedServer embeddedServer;

  @Inject SystemConfigurationProperties systemConfigurationProperties;

  /**
   * ログイン済み状態の HttpClient 生成.
   *
   * @return HttpClient インスタンス
   */
  public HttpClient login() {
    var cookieHandler = new CookieManager();
    var client =
        HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NEVER)
            .cookieHandler(cookieHandler)
            .build();

    var bodyParameter =
        String.format(
            Locale.ENGLISH,
            "username=%s&password=%s",
            URLEncoder.encode(systemConfigurationProperties.getIdentity(), StandardCharsets.UTF_8),
            URLEncoder.encode(systemConfigurationProperties.getSecret(), StandardCharsets.UTF_8));
    var request =
        HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + embeddedServer.getPort() + "/login"))
            .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
            .POST(HttpRequest.BodyPublishers.ofString(bodyParameter))
            .build();
    try {
      client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
      return client;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * GET リクエスト生成.
   *
   * @param url url
   * @return GET リクエスト
   */
  public HttpRequest buildGetRequest(String url) {
    return HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + embeddedServer.getPort() + url))
        .GET()
        .build();
  }

  /**
   * POST リクエスト生成.
   *
   * @param url url
   * @param parameter body リクエスト
   * @return POST リクエスト
   */
  public HttpRequest buildPostRequest(String url, String parameter) {
    return HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + embeddedServer.getPort() + url))
        .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
        .POST(HttpRequest.BodyPublishers.ofString(parameter))
        .build();
  }

  /**
   * 指定した URL にアクセスし、csrf トークンを取得します。
   *
   * @param client client
   * @param url url
   * @return csrf トークン
   */
  public String getCsrfToken(HttpClient client, String url) {
    var request = buildGetRequest(url);
    HttpResponse<String> response;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new AssertionError(e);
    }

    var body = response.body();
    return Jsoup.parse(body).select("input[name='csrfToken']").val();
  }
}
