# アプリの動かし方
## 前提
mysqlを2つ起動している。1つはホストでポート3306を使用して起動した。もう1つはdockerコンテナ内で、ホスト側ポート番号が8880となるように起動した。

## スキーマの定義
```
java -jar scalardb-schema-loader-<version>.jar --config scalardb.properties --schema-file health_data.json --coordinator
```
`<version>` -> 3.9.0に変更。

## 初期データの読み込み
`build.gradle`において`mainClassName`を以下のように設定。
```
mainClassName = "healthdata.HealthDataMain"
```
以下のコマンドを実行。
```
./gradlew run --args="-action loadInitialData"
```
以下のデータが読み込まれる。


## GUI上での操作
1. `build.gradle`において`mainClassName`を以下のように設定。
```
mainClassName = "healthdata.GUI"
```
2. GUIを起動するには以下のコマンドを実行。
```
./gradlew run
```
3. GUI上から、`ID`と`表示データ`を入力し、`登録`ボタンを押す。
※ 表示可能な`表示データ`は、`weight`, `muscle_mass`, `body_fat_percentage`

4. 選択した`表示データ`の時間変化が表示される。
※ 異なる`表示データ`を閲覧する場合は、GUIを閉じてから、再度2.の操作から始める。
