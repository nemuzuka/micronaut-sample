![CircleCI](https://circleci.com/gh/nemuzuka/micronaut-sample.svg?style=shield&circle-token=ffa249da071f2247cd527e62d3d7354e7216ee2c)

# これは何？

[micronaut](https://micronaut.io/) の 2.0.0 のサンプルプロジェクト。  
SpringBoot のような使い方でアプリケーションを作ってみる。

# 動作環境

* Java 11+

## 作るもの

* [Session Authentication](https://micronaut-projects.github.io/micronaut-security/latest/guide/#session)
    * ログインしないと処理できない奴
* [Thymeleaf と連携](https://micronaut-projects.github.io/micronaut-views/latest/guide/#thymeleaf)
* [Bean Validation](https://docs.micronaut.io/1.2.6/guide/index.html#beanValidation)
* [Error Handling](https://docs.micronaut.io/1.2.6/guide/index.html#errorHandling)

## 今のところやらないこと

* RDBMS との連携
    * やるなら [doma2](https://doma.readthedocs.io/en/latest/) だと思うけど、micronaut のサンプルなので

# 開発用コマンド

## 初回だけ実施

```
$ cd src/main/resources
$ cp _sample-application-local.yml application-local.yml
```

コピーした application-local.yml の情報(e.g. 認証情報)を変更してください。

## ローカル起動

```
$ MICRONAUT_ENVIRONMENTS=local ./gradlew run
```

で

```
22:53:54.646 [main] INFO  i.m.context.env.DefaultEnvironment - Established active environments: [local]
22:53:55.635 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 1049ms. Server Running: http://localhost:8080
```
と表示されたら

http://localhost:8080

にアクセスしてください。

ログイン画面が表示されるので、`application-local.yml` に指定した identity と secret でログインしてください。

### プロファイル

`MICRONAUT_ENVIRONMENTS=local` が SpringBoot で言うところの `-Dspring.profiles.active=local` になります。

このようにすることで、プロパティは

1. `src/main/resources/application.yml` を適応
2. `src/main/resources/application-local.yml` を適応(同じ項目があれば上書き)

となります。

## フォーマット

```
$ ./gradlew goJF
```

チェックは

```
$ ./gradlew verGJF
```
