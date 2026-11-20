# java-04：火星探測車任務日誌

## 功能

`MissionLog` 依 main 裡的固定腳本（預設 `{1, 1, 2}`）執行第三週的探測車規則，並把每一步的指令、行動後能源、結果分別存進三條 ArrayList，最後輸出（繁體中文）：

- 每一步的任務回放
- 0–3 四種指令的次數（無效指令不計）
- 摘要：步數、成功、拒絕、略過
- 最低能源（空日誌顯示「無紀錄」）
- 最終狀態與結果（任務成功／能源耗盡／已返回／任務未完成）

`src` 另有五個課堂示範：ArrayBasics、CommandCounter、ArrayReference、ListBasics、CollectionsDemo。本專案使用老師新版（中文輸出）教材。

## JDK 版本

openjdk 21.0.13.1（Microsoft Build of OpenJDK）

## 執行方式

在 `java-04` 目錄：

```powershell
javac -encoding UTF-8 -d out src/*.java
java -cp out MissionLog
```

若 Windows 主控台中文顯示成亂碼，加上 `-Dstdout.encoding=UTF-8`：

```powershell
java -Dstdout.encoding=UTF-8 -cp out MissionLog
```

## 預設腳本 {1, 1, 2} 的預期輸出

```text
=== 任務回放 ===
步驟 1: 指令=1, 結果=成功, 能源=14
步驟 2: 指令=1, 結果=成功, 能源=8
步驟 3: 指令=2, 結果=成功, 能源=6
指令次數[0..3]=[0, 2, 1, 0]
步數=3, 成功=3, 拒絕=0, 略過=0
最低能源=6
最終能源=6, 樣本數=5, 行動次數=3 -> 任務成功
電量百分比=30.0
```
