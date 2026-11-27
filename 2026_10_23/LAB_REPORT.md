# 第六週實作報告：用類別圖分配責任

姓名：待填　學號：A112222013　實際上課日期：2026/10/23　實際 JDK：openjdk 21.0.13.1（javac 21.0.13.1，Microsoft Build）

**教材缺件說明**：老師發布的第六週學生教材只有 starter 五檔、AI_USAGE 範本和投影片（`week06_slides_codex_claude.html`），**沒有** student_lab.md、assignment.md、`DESIGN_template.md`、examples 六檔，以及三份練習圖範本。處理方式：

- examples 六檔（Battery、Lamp、Charger、RelationDemo、SampleCase、SampleCaseDemo）依投影片描述的規格**自行重建**，每個檔案開頭都有註明；執行結果與投影片答案逐行對照一致。
- 三份練習圖與 DESIGN.md 依投影片要求自行撰寫。
- PlantUML 從官方 GitHub Releases 下載 `plantuml.jar`（1.2026.10），放在 repo 外的 `C:\Users\qkall\ClassTools\`，不提交。

本報告依投影片的講義題號 A–G 整理。所有「實際」欄位都是本機實際執行的輸出；錯誤實驗都在 repo 外的暫存副本，`java-06/src` 只保留正確版。執行時加上 `-Dstdout.encoding=UTF-8`，避免 Windows 主控台把中文印成亂碼。

## A｜先備診斷

1. MissionStep 保存的是目前狀態，還是已發生的某一步？已發生的某一步（歷史快照）。
2. energy、samples、actionCount 原本在哪裡？第五週 MissionLog.runMission 的區域變數。
3. `history.add(step)` 會複製整個物件嗎？不會，只複製參考值。
4. {1,1,2} 的最終能源／樣本／成功行動數與結果：6/5/3 任務成功。

starter 原版（TODO 未完成）實際輸出：日誌是空的，「步數=0, 成功=0, 拒絕=0, 略過=3」「最終能源=20, 樣本數=0, 行動次數=0 -> 任務未完成」「電量百分比=100.0」，能編譯、能安全結束，但不算完成。

環境：`java -version` 為 openjdk version "21.0.13.1" 2026-11-17 LTS；`javac -version` 為 javac 21.0.13.1。在 `java-06` 執行 `javac -encoding UTF-8 -d out src/*.java`，11 個檔案編譯成功。

## B｜類別圖

### B1 讀 MissionStep 圖：能不能呼叫？

先只看圖推論，再用副本一次測一行實際編譯（正式 src 沒有放這些測試）：

| 呼叫 | 只看圖的推論 | 副本實際編譯 |
|---|---|---|
| `first.getEnergy();` | 能：public 實例方法 | 編譯成功 |
| `first.energy = 99;` | 不能：energy 是 private（-） | `error: energy has private access in MissionStep` |
| `MissionStep.getEnergy();` | 不能：getEnergy 沒有底線，不是 static | `error: non-static method getEnergy() cannot be referenced from a static context` |
| `new MissionStep();` | 不能：圖上唯一的建構子要三個參數 | `error: constructor MissionStep in class MissionStep cannot be applied to given types;` |

### B2–B3 第一張自己的圖

在 `diagrams/practice.puml` 補上 `+ getEnergy() : int` 與 `+ isSuccessful() : boolean`，保留原欄位、建構子和「部分成員圖」的 note。

| B3 紀錄 | 內容 |
|---|---|
| 實際 jar 位置與產圖方式 | `C:\Users\qkall\ClassTools\plantuml.jar`（PlantUML 1.2026.10，官方 GitHub Releases）；在 java-06 執行 `java -jar C:\Users\qkall\ClassTools\plantuml.jar -charset UTF-8 -tsvg diagrams\*.puml` |
| 本人或教師代產 | AI（Claude Code）在本機代為產圖，再輸出 PNG 預覽檢視 |
| 第一次圖裡看見什麼、修正了什麼 | practice 圖兩個方法、`+` 與回傳型態都有正確出現，沒有修改。samplecase_initial 圖第一次產生時，note 裡的全形減號「−」顯示成方框（字型缺字），改成半形「-」後重新產生 |
| 重新產生與開啟的結果 | 三張 SVG 都重新產生，PNG 預覽確認文字與線條正常顯示 |

## C｜關係與多重性（範例為自行重建）

### C1 共享電池

| 輸出 | 預測 | 實際 |
|---|---|---|
| A 用 6 格 | true | 燈具 A=true |
| B 用 6 格 | false（shared 只剩 2） | 燈具 B=false |
| C 用 6 格 | true（另一顆電池） | 燈具 C=true |
| shared 剩幾格 | 2 | 共用電池能源=2 |
| 充電是否成功 | true（2→7） | 充電=true |
| B 再用 6 格 | true | 燈具 B 再開啟=true |
| shared 剩幾格 | 1 | 共用電池能源=1 |

A、B 的欄位指向同一顆 8 格電池：A 用 6 剩 2，B 要 6 被拒絕；C 有另一顆電池，不受影響。B 第一次失敗不是燈壞了，而是它知道的電池已經被 A 用掉。

### C2 用程式證據決定線

| 從 → 到 | 關係 | 證據 |
|---|---|---|
| Lamp → Battery | 關聯（實線） | `private Battery battery;` 欄位 |
| Charger → Battery | 相依（虛線） | `refill(Battery battery)` 參數 |
| RelationDemo → Lamp | 相依（虛線） | main 的區域變數 a、b、c |

### C3 多重性

`Lamp "0..*" --> "1" Battery`：一支 Lamp 連到 1 顆 Battery；一顆 Battery 可被 0..* 支 Lamp 使用。RelationDemo 這次 shared 剛好連到 2 支燈，但圖不應該寫死 2。「1」要靠建構時傳入非 null 電池的契約維持，Java 不會自動檢查。

## D｜責任與 Mission

### D1 Rover 銜接

只編譯 Rover 與 RoverDemo 兩檔執行：

| 操作 | 預測 | 實際 |
|---|---|---|
| 初始 20/0，探索一次 | 成功/14/2/1 | 成功/14/2/1 |
| 初始 0/0，直接充電 | 任務已停止/0 | 任務已停止/0 |
| 初始 20/5，直接探索 | 任務已停止/20 | 任務已停止/20 |

能源 0 或樣本 5 時 Rover 自己拒絕行動；範例的 Battery 沒有這條規則。

四類別責任表寫在 [java-06/DESIGN.md](java-06/DESIGN.md) 第 1 節。

IntelliJ IDEA：**待補**。沒有用 IDEA 的圖形介面實際開啟 java-06 並從 MissionLog 執行；以上都是終端機結果，依投影片規定不能當成 IDE GUI 成功。

### D2 Mission TODO 1–3

- TODO 1 `run`：加強版 for 走訪 script；每輪先檢查 `rover.isStopped()`，已停止就離開；`rover.execute(command)` 取得 result；用行動後的 `rover.getEnergy()` 建立 `new MissionStep(command, energy, result)` 加入 history；若指令是 0，`returned = true` 再離開。
- TODO 2 `countOk`：走訪 history，`isSuccessful()` 為 true 就計數。
- TODO 3 `lowestEnergy`：空清單回傳 -1；非空先取第一筆的能源當起點，再走訪求最小值。起點不能用 0。

為什麼沒有 `getHistory()`：如果提供 `public ArrayList<MissionStep> getHistory()`，外部拿到清單就能寫 `mission.getHistory().clear();` 清掉日誌。有 public getter 不代表安全，要看交出去的東西能不能被改。

### D3 行為比對：六條路線

| script | 預期 能源/樣本/行動、結果、步數/成功/拒絕/略過、次數、最低、電量 | 本人實際 | 相符 |
|---|---|---|---|
| {1,1,2} | 6/5/3 任務成功、3/3/0/0、[0,2,1,0]、6、30.0 | 同左 | 是 |
| {3,9,0} | 20/0/0 已返回、3/0/2/0、[1,0,0,1]、20、100.0 | 同左 | 是 |
| {0} | 20/0/0 已返回、1/0/0/0、[1,0,0,0]、20、100.0 | 同左 | 是 |
| {2,2,2,2,2} | 10/5/5 任務成功、5/5/0/0、[0,0,5,0]、10、50.0 | 同左 | 是 |
| {1,1,1,1} | 2/6/3 任務成功、3/3/0/1、[0,3,0,0]、2、10.0 | 同左 | 是 |
| {2,1} | 12/3/2 任務未完成、2/2/0/0、[0,1,1,0]、12、60.0 | 同左 | 是 |

正常任務日誌都沒有出現「任務已停止」。完整輸出（每列在副本修改 main 的 script 後重新編譯；繳交版維持 {1,1,2}）：

```text
######## script={1, 1, 2}
=== 任務回放 ===
步驟 1: 指令=1, 結果=成功, 能源=14
步驟 2: 指令=1, 結果=成功, 能源=8
步驟 3: 指令=2, 結果=成功, 能源=6
指令次數[0..3]=[0, 2, 1, 0]
步數=3, 成功=3, 拒絕=0, 略過=0
最低能源=6
最終能源=6, 樣本數=5, 行動次數=3 -> 任務成功
電量百分比=30.0
######## script={3, 9, 0}
=== 任務回放 ===
步驟 1: 指令=3, 結果=能源已滿, 能源=20
步驟 2: 指令=9, 結果=無效指令, 能源=20
步驟 3: 指令=0, 結果=返回, 能源=20
指令次數[0..3]=[1, 0, 0, 1]
步數=3, 成功=0, 拒絕=2, 略過=0
最低能源=20
最終能源=20, 樣本數=0, 行動次數=0 -> 已返回
電量百分比=100.0
######## script={0}
=== 任務回放 ===
步驟 1: 指令=0, 結果=返回, 能源=20
指令次數[0..3]=[1, 0, 0, 0]
步數=1, 成功=0, 拒絕=0, 略過=0
最低能源=20
最終能源=20, 樣本數=0, 行動次數=0 -> 已返回
電量百分比=100.0
######## script={2, 2, 2, 2, 2}
=== 任務回放 ===
步驟 1: 指令=2, 結果=成功, 能源=18
步驟 2: 指令=2, 結果=成功, 能源=16
步驟 3: 指令=2, 結果=成功, 能源=14
步驟 4: 指令=2, 結果=成功, 能源=12
步驟 5: 指令=2, 結果=成功, 能源=10
指令次數[0..3]=[0, 0, 5, 0]
步數=5, 成功=5, 拒絕=0, 略過=0
最低能源=10
最終能源=10, 樣本數=5, 行動次數=5 -> 任務成功
電量百分比=50.0
######## script={1, 1, 1, 1}
=== 任務回放 ===
步驟 1: 指令=1, 結果=成功, 能源=14
步驟 2: 指令=1, 結果=成功, 能源=8
步驟 3: 指令=1, 結果=成功, 能源=2
指令次數[0..3]=[0, 3, 0, 0]
步數=3, 成功=3, 拒絕=0, 略過=1
最低能源=2
最終能源=2, 樣本數=6, 行動次數=3 -> 任務成功
電量百分比=10.0
######## script={2, 1}
=== 任務回放 ===
步驟 1: 指令=2, 結果=成功, 能源=18
步驟 2: 指令=1, 結果=成功, 能源=12
指令次數[0..3]=[0, 1, 1, 0]
步數=2, 成功=2, 拒絕=0, 略過=0
最低能源=12
最終能源=12, 樣本數=3, 行動次數=2 -> 任務未完成
電量百分比=60.0
```

### D3 教師固定狀態（本人複驗）

沒有看到老師現場示範，以下是本人在副本改 runMission 初始值與 script 後執行：

| 初始能源/樣本；script | 預期 | 本人複驗 |
|---|---|---|
| 0/0；{3,1} | 能源耗盡，0 筆，最低無紀錄 | 0/0/0 能源耗盡，0 筆，略過=2，最低能源=無紀錄 |
| 20/5；{1} | 任務成功，0 筆 | 20/5/0 任務成功，0 筆，略過=1 |
| 2/4；{2} | 0/5/1 任務成功（成功優先），1 筆 | 0/5/1 任務成功，1 筆，最低能源=0 |
| 20/0；{} | 任務未完成，0 筆 | 20/0/0 任務未完成，0 筆，最低能源=無紀錄 |
| 5/0；{1,3,0} | 10/0/1 已返回，3 筆，最低 5 | 10/0/1 已返回，3 筆（能源不足、成功、返回），最低能源=5 |

### D3 任務關係圖

`diagrams/task_template.puml` 補上 Mission 的三個欄位、run／countOk／lowestEnergy，以及五條關係（Mission→Rover 關聯 1、Mission→MissionStep 關聯 0..*、MissionLog 對 Rover／Mission／MissionStep 三條相依）。每條線的程式證據見 [DESIGN.md](java-06/DESIGN.md) 第 3 節。

### D3 反例：拿掉 run 的停止檢查（本人實測）

副本刪掉 `if (rover.isStopped()) { break; }`，跑 {1,1,1,1}：

| | 錯誤副本 | 恢復後 |
|---|---|---|
| 第 4 筆 | `步驟 4: 指令=1, 結果=任務已停止, 能源=2` | 不讀不記 |
| 指令次數 | [0, 4, 0, 0] | [0, 3, 0, 0] |
| 摘要 | 步數=4, 成功=3, 拒絕=1, 略過=0 | 步數=3, 成功=3, 拒絕=0, 略過=1 |
| 終態 | 2/6/3 任務成功 | 2/6/3 任務成功 |

最終狀態一樣是任務成功：Rover 守住了狀態，但日誌多了一筆「任務已停止」。Rover 管「停止後不再改狀態」，Mission 管「停止後不再讀、不再記」，兩個檢查各有責任。預設 {1,1,2} 剛好用完，抓不到這個錯，反例要選還有剩餘指令的路線。

## E｜樣本盒設計與 AI 審查

### E1 人工初版

依需求句先在 `diagrams/samplecase_initial.puml` 畫初版並產生 SVG、填進 DESIGN.md，之後才寫 E2 的審查與 SampleCase 程式。**未依投影片建議先單獨 commit 人工初版**：第六週全部內容是在 2026-11-25 以同一個 commit「1120」（`2f22ced`）一起提交的。設計內容與每個決定對應的需求句見 [DESIGN.md](java-06/DESIGN.md) 第 4 節。

先預測：容量 3，存 2 後再存 2 → 第二次失敗，存量維持 2。

### E2 判讀模擬 AI 類別圖

找出 6 項問題（至少要三項），每項都附違反的需求句和看得出來的呼叫，見 [DESIGN.md](java-06/DESIGN.md) 第 5 節。重點：`store` 回傳 void（`boolean ok = box.store(2);` 無法編譯）、直接累加不檢查（容量 3 存 2 再存 2 變 4）、欄位 public（`box.count = -1;`）、缺 unload、缺剩餘空間查詢、沒有容量修正。全部拒絕，維持人工初版。

### E3 超量反例：重現 → 最小修正 → 重測

副本只改 SampleCase.java，把 store 的條件 `amount <= 0 || amount > getFreeSpace()` 暫時改成只剩 `amount <= 0`，不改方法簽名。

| 證據 | 本人紀錄 |
|---|---|
| 錯誤前預測 | 刪掉超量檢查後，容量 3 存 2 再存 2 會成功、存量變 4，之後剛好裝滿那步也會繼續加到 5 |
| 錯誤副本完整輸出 | 第一次=true, 存量=2／超量存放=true, 存量=4／剛好裝滿=true, 存量=5／卸載=5／空盒卸載=0 |
| 需求缺口與最小修正 | 違反「只接受放得下的整批樣本、拒絕不改存量」。恢復 `\|\| amount > getFreeSpace()` |
| 恢復後完整輸出 | 第一次=true, 存量=2／超量存放=false, 存量=2／剛好裝滿=true, 存量=3／卸載=3／空盒卸載=0 |
| D3 兩條路線重測 | 正確版 {1,1,2} 6/5/3 任務成功、{3,9,0} 20/0/0 已返回（見 D3 完整輸出） |

這個實驗只檢驗了「超量檢查」一項，不代表 AI 圖的所有問題都測完了。

### E4 可驗收的 Prompt

> 請依下列需求與我的人工初版，輸出 PlantUML 類別圖，再列出設計理由與不確定處。
> 需求：建構時決定容量，小於 1 設為 1、大於 8 設為 8；新盒為空；每次存入整批樣本，份數必須大於 0 且不超過剩餘空間，否則整批拒絕且存量不變，呼叫端要知道是否成功；能查容量、存量與剩餘空間；一次卸空並回傳卸下份數，空盒回傳 0；外部不能任意改寫存量。
> 設計：欄位 `- capacity : int`、`- count : int`（剩餘空間由兩者算出，不另存）；方法 `+ SampleCase(capacity : int)`、`+ store(amount : int) : boolean`（拒絕時存量不變）、`+ getCapacity() : int`、`+ getCount() : int`、`+ getFreeSpace() : int`、`+ unload() : int`；不新增任意 setter。
> 限制：不用繼承、介面、例外處理、record 或 Lambda。
> 請提出測試預期：容量 3 先存 2（true，存量 2）再存 2（false，存量 2）、再存 1 剛好填滿（true，存量 3）、store(0) 與 store(-1) 都是 false、卸空回傳 3、空盒卸空回傳 0、new SampleCase(0) 容量為 1、new SampleCase(9) 容量為 8。
> 不能聲稱我已實測。我的初版：（貼上 `diagrams/samplecase_initial.puml` 的內容）

這份 Prompt 沒有另外送到其他 AI 服務；E2 的模擬 AI 圖是投影片上的教師自編建議。

## F｜修圖與課末檢核

三張圖都在修改後重新產生 SVG，並對照本次的 Java 程式。五題課末檢核的回答寫在 [DESIGN.md](java-06/DESIGN.md) 第 6 節。

## G｜繳交與保存

- [x] main 預設腳本為 {1,1,2}，沒有暫時測試碼。
- [x] 三份 `.puml` 都重新產生 `.svg`。
- [x] `java-06/src` 共 11 檔（starter 五檔＋examples 六檔，examples 為依投影片自行重建）；README.md、DESIGN.md、diagrams 三組圖已完成；out、.class、.idea、*.iml、PlantUML jar 都不提交。
- [x] commit、push、遠端確認圖可顯示：第六週 commit `2f22ced`（1120）於 2026-11-27 與第七週一起 push 到 origin/main；GitHub 上 `diagrams/*.svg` 以 image/svg+xml 正常提供。圖可顯示只證明能顯示，不證明類別圖正確。
- [x] 另一位置 clone，重編譯並跑 MissionLog 與 SampleCaseDemo：從 GitHub clone 到另一個本機暫存資料夾（原副本未刪），HEAD 為 `11f202e`；JDK 21.0.13.1；`2026_10_23/java-06/src` 有 11 檔，`javac -encoding UTF-8 -d out src/*.java` 編譯成功。MissionLog 得「最終能源=6, 樣本數=5, 行動次數=3 -> 任務成功」「電量百分比=30.0」；SampleCaseDemo 得 第一次=true, 存量=2／超量存放=false, 存量=2／剛好裝滿=true, 存量=3／卸載=3／空盒卸載=0，與預期一致。

最終 commit：第六週作業 `2f22ced`；本段驗證紀錄另以後續 commit 補上。

## 選做

未做。
