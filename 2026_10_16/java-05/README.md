# java-05：把任務日誌設計成物件

## 類別責任

| 類別 | 責任 | main 入口 |
|---|---|---|
| MissionStep | 保存一筆已發生的任務紀錄（指令、行動後能源、結果），建立後只讀 | 無 |
| MissionLog | 依固定腳本推進任務、每步建立 MissionStep 存進 history、產生回放與摘要報表 | **有（主作業）** |
| StepCard | 範例：只有指令與能源兩欄的簡化紀錄 | 無 |
| RoverBattery | 範例：管理目前能源，只能透過 explore／charge 改變 | 無 |
| CreateStepDemo | 範例：建構子、兩個物件獨立 | 有 |
| BatteryDemo | 範例：封裝、拒絕與充電封頂 | 有 |
| ReferenceDemo | 範例：共用參考與傳值 | 有 |
| SnapshotDemo | 範例：每步建立新快照 | 有 |

StepCard、RoverBattery、MissionStep 沒有 main，不要直接執行。

## JDK 版本

openjdk 21.0.13.1（Microsoft Build of OpenJDK）

## 編譯與執行

在 `java-05` 目錄：

```powershell
javac -encoding UTF-8 -d out src/*.java
java -cp out MissionLog
```

換成其他範例時，把最後的 MissionLog 改成 CreateStepDemo、BatteryDemo、ReferenceDemo 或 SnapshotDemo。

在 Windows 的 PowerShell 或命令提示字元直接執行即可，Java 會配合主控台編碼正常顯示中文。**不要**另外加 `-Dstdout.encoding=UTF-8`：在預設的繁體中文主控台（CP950）反而會變成亂碼。

若 `java`、`javac` 不在 PATH，改用 JDK 完整路徑，例如：

```powershell
& "C:\Program Files\Microsoft\jdk-21.0.13.102-hotspot\bin\java.exe" -cp out MissionLog
```

預設腳本 {1, 1, 2} 的預期輸出：

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

## 完成／未完成項目

- 完成：MissionStep TODO 1–3、MissionLog TODO 4–6；E1 兩物件檢查；六條路線；B3 兩種編譯錯誤；C1 對照；F1 設計卡；F2 審查與 Prompt；F3 選項 2（另兩項也驗證過）。
- 完成（2026-11-27 補做）：D2 用 IntelliJ IDEA 2026.3.0.1 開啟 java-05 並從 SnapshotDemo 執行。
- 未完成：選做的 SampleCase 實作。
