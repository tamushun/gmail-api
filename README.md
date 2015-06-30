# gmail-api

## alias
alias exec-gmail-api='cd ~/gmail-api/google/ ; pwd ; mvn install -Dmaven.test.skip=true ; mvn exec:java -Dmaven.test.skip=true'
alias exec-gmail-api-jar='java -jar ~/gmail-api/google/target/google-0.0.1.jar'

## `.gitignore`しているファイル
- `client_secret.json`
  - Google Developers ConsoleからDLしてくる。
- `Constants.java`
  - APIキーなどgithubにあげられないキーなどを保持。

## 参考
### Gmail API
- GmailAPI Quickstart Java
  - https://developers.google.com/gmail/api/quickstart/java
- API Reference
  - https://developers.google.com/gmail/api/v1/reference/
- APIキーの取得
  - http://www.ajaxtower.jp/googlemaps/charset/index1.html
- Google Gmail APIでメールを取得する
  - http://qiita.com/ryurock/items/4b063372ede81780c3c8
- `~/.credential/`配下を一度消さないとscope権限の変更は反映されない。
- getMessageApiのquery（Request Parameter）として使える表現例
  - from:someuser@example.com is:unread
  - after:2014/01/01 before:2014/01/30
  - ((label:from hoge) OR (label:from fuga)) is:unread

### Slack API
- API仕様
  - https://api.slack.com/methods
- postMessage APIの仕様
  - https://api.slack.com/methods/chat.postMessage
  - `channel`として使えるのは、`%23チャンネル名`・`チャンネルID`
    - `as_user`が`false`なら、`channel`として`%40ユーザー名`・`ユーザーID`も使える

### Etc
- mavenでjarファイル実行環境を作る（maven-jar-plugin＆maven-shade-plugin）
  - http://tomoyamkung.net/2014/01/23/java-spring-standalone-jar/#09
- MavenでMANIFEST.MFを取り込む（maven-jar-plugin）
  - http://namihira.hatenablog.com/entry/2012/09/01/111901

