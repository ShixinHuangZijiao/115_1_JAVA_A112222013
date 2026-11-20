# java-03：火星探測車互動任務

## 功能

主控台輸入整數指令控制探測車（初始能源 20、樣本 0，目標 5 份樣本）：

| 指令 | 效果 |
|---|---|
| 1 探索 | 能源 ≥ 6：扣 6、樣本 +2、行動 +1；否則 NOT_ENOUGH_ENERGY |
| 2 掃描 | 能源 ≥ 2：扣 2、樣本 +1、行動 +1；否則 NOT_ENOUGH_ENERGY |
| 3 充電 | 能源 < 20：+5（封頂 20）、行動 +1；滿電 ALREADY_FULL |
| 0 返回 | 結束任務 |
| 其他 | UNKNOWN_COMMAND，狀態不變 |

樣本達 5 → SUCCESS（優先）；能源 0 → EXHAUSTED；主動返回 → RETURNED。

## JDK 版本

OpenJDK 21.0.13（Microsoft Build of OpenJDK）

## 執行方式

在 `java-03` 目錄：

```powershell
javac --release 21 -encoding UTF-8 -d out src/*.java
java -cp out RoverMission
```

## 成功路線預期結果

依序輸入 `1`、`1`、`2`，最後顯示：

```text
energy=6, samples=5, actionCount=3, battery=30.0
SUCCESS
```
