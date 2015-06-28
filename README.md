# gmail-api

## alias
alias exec-gmail-api='cd ~/gmail-api/google/ ; pwd ; mvn install -Dmaven.test.skip=true ; mvn exec:java -Dmaven.test.skip=true'

## client_secret.json
Google Developers ConsoleからDLしてくる。

## 参考
- GmailAPI Quickstart Java
  - https://developers.google.com/gmail/api/quickstart/java
- API Reference
  - https://developers.google.com/gmail/api/v1/reference/
- APIキーの取得
  - http://www.ajaxtower.jp/googlemaps/charset/index1.html
- Google Gmail APIでメールを取得する
  - http://qiita.com/ryurock/items/4b063372ede81780c3c8

## 注意
- ~/.credential/配下を一度消さないとscope権限の変更は反映されない。

