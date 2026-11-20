# java-04：火星探測車任務日誌

## 功能

`MissionLog` 依 main 裡的固定腳本（預設 `{1, 1, 2}`）執行第三週的探測車規則，並把每一步的指令、行動後能源、結果分別存進三條 ArrayList，最後輸出：

- 每一步的回放（replay）
- 0–3 四種指令的次數（無效指令不計）
- 摘要：steps、ok、rejected、ignored
- 最低能源（空日誌顯示 N/A）
- 最終狀態與結果（SUCCESS／EXHAUSTED／RETURNED／UNFINISHED）

`src` 另有五個課堂示範：ArrayBasics、CommandCounter、ArrayReference、ListBasics、CollectionsDemo。

## JDK 版本

openjdk 21.0.13.1（Microsoft Build of OpenJDK）

## 執行方式

在 `java-04` 目錄：

```powershell
javac -encoding UTF-8 -d out src/*.java
java -cp out MissionLog
```

## 預設腳本 {1, 1, 2} 的預期輸出

```text
=== replay ===
step 1: command=1, result=OK, energy=14
step 2: command=1, result=OK, energy=8
step 3: command=2, result=OK, energy=6
counts[0..3]=[0, 2, 1, 0]
steps=3, ok=3, rejected=0, ignored=0
lowest energy=6
final energy=6, samples=5, actionCount=3 -> SUCCESS
battery=30.0
```
