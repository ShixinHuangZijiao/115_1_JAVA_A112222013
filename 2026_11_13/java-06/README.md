# java-06：用類別圖分配責任

## 四個任務類別的責任

| 類別 | 責任 | main 入口 |
|---|---|---|
| MissionStep | 保存一步行動後的只讀快照（指令、能源、結果） | 無 |
| Rover | 保存目前能源、樣本、行動次數；管理行動規則；成功或耗盡後不再行動 | 無 |
| Mission | 執行腳本、保存日誌、記住是否返回；停止後不再讀不再記；提供摘要查詢 | 無 |
| MissionLog | 建立 Rover 與 Mission、執行預設腳本 {1,1,2}、輸出報表 | **有（主作業）** |

其他入口：`RoverDemo`（只示範 Rover）、`RelationDemo`（共享電池範例）、`SampleCaseDemo`（樣本盒驗收）。Battery、Lamp、Charger、SampleCase 沒有 main。

本週只寫了 `Mission.java` 的 TODO 1–3；Rover、MissionStep、MissionLog、RoverDemo 是老師提供的完成版。examples 六檔（Battery、Lamp、Charger、RelationDemo、SampleCase、SampleCaseDemo）因為學生版教材沒有附，依投影片規格自行重建。

設計與類別圖見 [DESIGN.md](DESIGN.md)。

## JDK 版本

openjdk 21.0.13.1（Microsoft Build of OpenJDK）

## 編譯與執行

在 `java-06` 目錄：

```powershell
javac -encoding UTF-8 -d out src/*.java
java -cp out MissionLog
java -cp out SampleCaseDemo
```

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

## 產生類別圖

PlantUML jar 不放進 repo。在 `java-06` 執行（jar 位置換成實際位置）：

```powershell
java -jar C:\Users\qkall\ClassTools\plantuml.jar -charset UTF-8 -tsvg diagrams\*.puml
```

## 完成／未完成

- 完成：Mission TODO 1–3；六條路線；D3 停止檢查反例；三份 puml 與 SVG；DESIGN.md 責任表與關係證據；SampleCase 人工初版、E2 審查、E3 超量反例、E4 Prompt。
- 完成（2026-11-27 補做）：用 IntelliJ IDEA 2026.3.0.1 開啟 java-06 並從 MissionLog 執行。
- 未完成：選做的社團器材借用圖。
