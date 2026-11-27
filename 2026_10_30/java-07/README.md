# java-07：繼承與新增能力

## 內容

| 類別 | 說明 | main 入口 |
|---|---|---|
| Rover | 探測車；本週完成 TODO 1：`spendEnergy`，並用它重構 explore、scan | 無 |
| HeavyRover | `extends Rover`；本週完成 TODO 2、3：`drillCount` 與 `drill()`（耗 8 得 3） | 無 |
| HeavyRoverTry | 老師提供的十五行檢查 | 有 |
| CargoRover | 本人課後獨立遷移：`extends Rover`，容量 3 的貨艙與 `load()` | 無 |
| CargoRoverTry | CargoRover 的十二項檢查 | 有 |
| Mission、MissionStep | 老師提供的完成版（同第六週） | 無 |
| MissionLog | 任務入口，預設腳本 {1,1,2} | 有 |
| Member、VipMember、MemberDemo | 會員繼承範例 | MemberDemo 有 |

`ai-check/` 放 E 段的模擬 AI 除錯：完成的 Rover、HeavyRoverTry，以及修正後的 AI HeavyRover。兩邊都有 HeavyRover，**src 和 ai-check 必須分開編譯**。git 歷史中 `[AI]` commit 是模擬 AI 原文，`[fix]` commit 是最小修正。

設計說明、繼承圖、CargoRover 規格與測試見 [DESIGN.md](DESIGN.md)。

## JDK 版本

openjdk 21.0.13.1（Microsoft Build of OpenJDK）

## 編譯與執行

在 `java-07` 目錄：

```powershell
javac -encoding UTF-8 -d out src/*.java
java -cp out HeavyRoverTry
java -cp out CargoRoverTry
java -cp out MissionLog
java -cp out MemberDemo
```

ai-check 分開編譯：

```powershell
javac -encoding UTF-8 -d out-ai ai-check/*.java
java -cp out-ai HeavyRoverTry
```

在 Windows 的 PowerShell 或命令提示字元直接執行即可，Java 會配合主控台編碼正常顯示中文。**不要**另外加 `-Dstdout.encoding=UTF-8`：在預設的繁體中文主控台（CP950）反而會變成亂碼。

若 `java`、`javac` 不在 PATH，改用 JDK 完整路徑，例如：

```powershell
& "C:\Program Files\Microsoft\jdk-21.0.13.102-hotspot\bin\java.exe" -cp out HeavyRoverTry
```

預期：HeavyRoverTry 十五行、CargoRoverTry 十二行全部「相符」；MissionLog 最後一行附近為 `最終能源=6, 樣本數=5, 行動次數=3 -> 任務成功`。

## 完成度

- 完成：TODO 1–3；HeavyRoverTry 十五行相符與本人反例；六條舊路線；E 段預測、不符證據、兩份狀態追蹤、`[AI]`／`[fix]` 兩個 commit、補強 Prompt；CargoRover 遷移與測試；繼承圖與 DESIGN.md。
- 未完成：選做項目。
