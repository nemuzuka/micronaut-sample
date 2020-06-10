# これは何？

[micronaut](https://micronaut.io/) の 1.3.x のサンプルプロジェクト。
SpringBoot っぽい使い方でアプリケーションを作ってみる。

## 作るもの

* ログイン
    * ログインしないと処理できない奴。[Session Authentication](https://micronaut-projects.github.io/micronaut-security/latest/guide/#session) を使う
* 


## やらないこと

* RDBMS との連携
    * やるなら [doma2](https://doma.readthedocs.io/en/latest/) だと思うけど、micronaut のサンプルなので


# 開発用コマンド

## 初回だけ実施

```
$ cd src/main/resources
$ cp _sample-application-local.yml application-local.yml
```

コピーした application-local.yml の情報(ex. 認証情報)を変更してください

## ローカル起動

```
$ MICRONAUT_ENVIRONMENTS=local ./gradlew run
```

## フォーマット

```
$ ./gradlew goJF
```

チェックは

```
$ ./gradlew verGJF
```
