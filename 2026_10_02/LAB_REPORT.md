# 第三週學生實作：火星探測車任務控制員

作業的必做範圍、繳交清單與驗收標準見 assignment.md（課程 repo 的 week03）。本講義另存後作為實作與測試報告。

姓名／暱稱：A112222013　上課日期：2026/10/02　JDK 版本：OpenJDK 21.0.13（Microsoft Build）

本週 A–F 必做，G 選做。所有「實際」欄位都是本機實際執行的輸出。

## A｜取回成果與暖身

1. 使用既有 repo `115_1_JAVA_A112222013`，`git status` 確認無未提交工作、與 origin/main 同步。
2. 建立 `2026_10_02/java-03/src`，複製 examples 四個 Java 檔及 starter/RoverMission.java（先複製再修改，課程原始檔不動）。
3. 講義另存 `2026_10_02/LAB_REPORT.md`，AI 範本另存 `2026_10_02/AI_USAGE.md`；repo 根沒有 .gitignore，所以把 starter/.gitignore 放到 `java-03/.gitignore`（排除 out、*.class）。
4. 在 java-03 執行 `javac --release 21 -encoding UTF-8 -d out src/*.java` 編譯成功，`java -cp out RoverMission` 輸入 0 → RETURNED。

| 診斷 | 我的預測／理由 | 執行核對 |
|---|---|---|
| `7 / 20 * 100.0` 與 `7 / 20.0 * 100` | 0.0／35.0；前者 7/20 是整數除法得 0 | 0.0、35.0，一致 |
| energy=6、locked=false，`energy >= 6 && !locked` | true；6>=6 為 true，!false 為 true | true，一致 |
| `calculateBatteryPercent(6)` 回傳什麼？誰負責輸出？ | 回傳 30.0；方法只回傳，由 showDashboard 印出 | 30.0，一致 |

本機 repo 路徑：`C:\Users\qkall\OneDrive\文件\GitHub\115_1_JAVA_A112222013`。當日專案：`2026_10_02/java-03`。初版 commit：見 F。

## B｜單次指令：先判斷，再更新

### B1 能源分類

| energy | 預測 INVALID／LOW／READY | 實際 | 是否一致 |
|---:|---|---|---|
| -1 | INVALID | INVALID | 是 |
| 0 | LOW | LOW | 是 |
| 5 | LOW | LOW | 是 |
| 6 | READY | READY | 是 |
| 20 | READY | READY | 是 |
| 21 | INVALID | INVALID | 是 |

如果先寫 `energy < 6`，再檢查是否小於 0，哪筆資料會被誤分類？理由：-1 會被判成 LOW，因為 -1 < 6 先成立，後面的 else if 不會再檢查。

### B2 單次行動

| 初始 energy／command | 預測 energy／samples／訊息 | 實際 |
|---|---|---|
| 6／1 | 0／2／無 | energy=0, samples=2 |
| 5／1 | 5／0／NOT_ENOUGH_ENERGY | NOT_ENOUGH_ENERGY、energy=5, samples=0 |
| 6／2 | 4／1／無 | energy=4, samples=1 |
| 6／9 | 6／0／UNKNOWN_COMMAND | UNKNOWN_COMMAND、energy=6, samples=0 |

暫時移除 case 1 的 break，用 20／1 測試：原版預測 energy=14, samples=2；移除 break 後預測 12／3，實際 energy=12, samples=3。一次指令做了幾種行動？2 種（探索後掉進 case 2 又掃描一次）。與 C# 的差異：C# 編譯器不允許非空 case 落入下一個 case（會編譯錯誤），Java 會靜默 fall-through，所以 break 不能漏。測後已恢復原版。

## C｜迴圈讓任務持續

### C1 固定三輪充電

| 起點 | 第 1 輪預測／實際 | 第 2 輪預測／實際 | 第 3 輪預測／實際 |
|---|---|---|---|
| 4 | 9／9 | 14／14 | 19／19 |
| 18 | 20／20 | 20／20 | 20／20 |

改成 `round < 3` 會跑幾次？2 次（實測只印 round=1、2）。為何把 energy 初始化放在迴圈外？放在迴圈內每輪都會被重設，無法累加。迴圈結束後可以使用 round 嗎？不行，round 宣告在 for 內，範圍只到迴圈結束。驗證後已恢復原版。

### C2 讀取指令

TODO 原版輸入 9 再輸入 0：9 顯示 UNKNOWN_COMMAND，0 之後顯示 RETURNED。為何輸入 0 後仍會印最後一次儀表板？因為 case 0 的 break 只離開 switch，switch 後的 showDashboard 在同一輪仍會執行，下一輪 while 判斷才停止。

### C3 先判斷或後判斷、狀態追蹤

LoopComparison 預測 whileCount=0、doCount=1；實際=while=0, do-while=1。差異：while 先判斷，條件一開始為 false 就一次都不跑；do-while 先執行一次才判斷。

RoverMission 的 while 條件 `running && energy > 0 && samples < 5` 各部分為 false 的原因依序是：使用者輸入 0 主動返回／能源耗盡／樣本已達 5 份。命令 0 的 break 只離 switch；真正讓下一輪停止的是：`running = false`。

路線 1、1、2：

| 輪次／指令 | energy 前→後 | samples 前→後 | actionCount 前→後 | 下輪條件 |
|---|---|---|---|---|
| 1／1 | 20→14 | 0→2 | 0→1 | true，繼續 |
| 2／1 | 14→8 | 2→4 | 1→2 | true，繼續 |
| 3／2 | 8→6 | 4→5 | 2→3 | false（samples=5），SUCCESS |

若每輪開頭都執行 `samples = 0;` 會怎樣？樣本永遠無法累積到 5，例如 1、1、2 最後 samples 只會是 1，任務只能耗盡或返回。

## D｜補完互動任務與 AI 除錯

D1、D2、D3 已在 `java-03/src/RoverMission.java` 完成：成功才扣能源、加樣本、actionCount++；拒絕時只顯示 NOT_ENOUGH_ENERGY／ALREADY_FULL。

| 指令序列 | 預期最終 energy／samples／actionCount／結果 | 實測 |
|---|---|---|
| 0 | 20／0／0／RETURNED | 20／0／0／RETURNED |
| 1、1、2 | 6／5／3／SUCCESS | 6／5／3／SUCCESS |
| 2、2、2、2、2 | 10／5／5／SUCCESS | 10／5／5／SUCCESS |
| 3、9、0 | 20／0／0／RETURNED | 20／0／0／RETURNED（3 顯示 ALREADY_FULL、9 顯示 UNKNOWN_COMMAND） |

固定狀態三筆（課後自行複驗：在測試副本暫改 `runMission(input, 20, 0)` 的引數，繳交版仍為 20、0）：

| 初始 energy／samples | 指令 | 預期最終 energy／samples／actionCount／結果 | 實測 |
|---|---|---|---|
| 5／0 | 1、3、0 | 10／0／1／RETURNED | 10／0／1／RETURNED |
| 6／0 | 1 | 0／2／1／EXHAUSTED | 0／2／1／EXHAUSTED |
| 2／4 | 2 | 0／5／1／SUCCESS | 0／5／1／SUCCESS |

AI 除錯選第 1 題（探索條件改成 `energy > 6`）。預測會錯的輸入：energy 剛好 6 時輸入 1，應可探索卻被拒絕。最小修正：改回 `energy >= 6`。拒絕原因：規則是「至少 6 格」，包含 6。

| 證據 | 初始狀態／輸入 | 預期 | 實際結果 |
|---|---|---|---|
| 反例：修改前與修正後比較 | 6／0，修改前輸入 1、0；修正後輸入 1 | 0／2／1 EXHAUSTED | 修改前：6／0／0 RETURNED（探索被拒絕）；修正後：0／2／1 EXHAUSTED |
| 修正後一般成功 | 20／0，2、2、2、2、2 | 10／5／5 SUCCESS | 10／5／5 SUCCESS |
| 修正後返回／拒絕 | 20／0，3、9、0 | 20／0／0 RETURNED | 20／0／0 RETURNED |

## E｜課末檢核

1. 1、1、2 只要 3 次行動但剩 6 格能源；五次 2 要 5 次行動但剩 10 格能源——前者省行動次數，後者省能源。
2. energy=2、samples=4 掃描後 energy=0、samples=5，結束判斷先檢查 `samples >= 5`，所以成功優先於耗盡。
3. 只寫 case 0: break，running 仍是 true，下一輪會繼續要求輸入，無法返回。
4. 不能回放每一步，只有最後狀態；缺少保存每一步指令與狀態的能力（下週用陣列與集合）。

## F｜保存、推送、取回驗證

- [x] 存檔並重新編譯；LAB_REPORT、AI_USAGE、src、README 齊全，out 已由 .gitignore 排除。
- [x] commit、push 到 origin。
- [x] 已有副本，作業前確認與 origin/main 同步。
- [x] 以不同本機路徑建立全新 clone，在 java-03 重編譯並跑 1、1、2。

最新 commit／遠端核對：作業 commit `d934545`（完成第三週作業）已 push 到 origin/main；本段驗證紀錄另以後續 commit 補上。全新 clone 路徑、版本、指令、結果：從 GitHub clone 到另一個本機暫存資料夾（不同於原 repo 路徑，原副本未刪），clone 內 HEAD 為 `d934545`；JDK 21.0.13；在 `2026_10_02/java-03` 執行 `javac --release 21 -encoding UTF-8 -d out src/*.java` 與 `java -cp out RoverMission`，輸入 1、1、2，結果 energy=6, samples=5, actionCount=3, SUCCESS，與預期一致。未完成事項：無。

## G｜課後選做

未做。
