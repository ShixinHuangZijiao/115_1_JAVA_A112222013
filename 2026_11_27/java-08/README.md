# java-08：覆寫與多型基礎

## 內容

| 位置 | 類別 | 說明 | main 入口 |
|---|---|---|---|
| src | Rover | TODO 1：explore 改呼叫可覆寫的 `exploreYield()` | 無 |
| src | HeavyRover | TODO 2：`@Override exploreYield()` 回 3；TODO 4：`@Override execute()` 讓指令 4 鑽探 | 無 |
| src | CompareMissions | TODO 3：一般車、重型車各配新任務跑同一份腳本並比較 | **有** |
| src | HeavyRoverTry、DrillMissionTry | 老師提供的檢查程式（15 行、14 行） | 有 |
| src | Mission、MissionStep、MissionLog | 老師提供，與第七週相同 | MissionLog 有 |
| src | Member、VipMember、MemberDemo | 會員覆寫範例 | MemberDemo 有 |
| experiments | BoundaryTry | 本人 D5 邊界測試與反例 | 有 |
| transfer | SurveyRover、SurveyRoverTry | 本人課後遷移（路線 A） | SurveyRoverTry 有 |
| ai-check | Rover、HeavyRoverTry、HeavyRover | E 段模擬 AI 審查（HeavyRover 為教師模擬 AI 原稿） | HeavyRoverTry 有 |

`src` 依投影片規定只放 examples 三檔＋starter 八檔共 11 檔。ai-check 和 src 都有 HeavyRover，**必須分開編譯**。

設計理由、類別圖、遷移規格見 [DESIGN.md](DESIGN.md)。

## JDK 版本

openjdk 21.0.13.1（Microsoft Build of OpenJDK）

## 編譯與執行

在 `java-08` 目錄：

```powershell
javac -encoding UTF-8 -d out src/*.java
java -cp out MemberDemo
java -cp out MissionLog
java -cp out HeavyRoverTry
java -cp out CompareMissions
java -cp out DrillMissionTry
```

本人測試入口（和 src 一起編譯）：

```powershell
javac -encoding UTF-8 -d out src/*.java experiments/BoundaryTry.java transfer/*.java
java -cp out BoundaryTry
java -cp out SurveyRoverTry
```

ai-check 分開編譯：

```powershell
javac -encoding UTF-8 -d out-ai ai-check/*.java
java -cp out-ai HeavyRoverTry
```

在 Windows 的 PowerShell 或命令提示字元直接執行即可，Java 會配合主控台編碼正常顯示中文。**不要**另外加 `-Dstdout.encoding=UTF-8`：在預設的繁體中文主控台（CP950）反而會變成亂碼。若 `java`、`javac` 不在 PATH，改用 JDK 完整路徑，例如：

```powershell
& "C:\Program Files\Microsoft\jdk-21.0.13.102-hotspot\bin\java.exe" -cp out CompareMissions
```

預期：HeavyRoverTry 15 行、DrillMissionTry 14 行、BoundaryTry 5 行、SurveyRoverTry 8 行全部「相符」；CompareMissions 印出一般車 6/5/3 與重型車 8/6/2 兩段報表。

## 完成度

- 完成：TODO 1–4；五個入口；D5 邊界與本人反例；B2、B3、C2 實驗；E1 預測、E2 不符證據、E3 @Override 與 static 判讀；F1 類別圖；SurveyRover 遷移與測試。
- 完成（本人操作）：用 IntelliJ IDEA 2026.3.0.1 開啟 java-08，src 為來源根（ai-check 不可標成 Sources，否則和 src 的 HeavyRover 撞名），從 MemberDemo 執行。
- 待完成：E4 最小修正（依規定須先以 `[AI]` commit 保存模擬原稿後再修正、以 `[fix]` commit 保存）。
