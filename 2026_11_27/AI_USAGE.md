# 第八週AI使用與驗證紀錄

學生姓名／學號：曹臣緯／A112222013。不上傳帳密、個資或未授權內容。

| 使用目的與來源 | Prompt／建議摘要 | 採納或拒絕及理由 | 人工驗證方式與結果 | 最終修改 |
|---|---|---|---|---|
| 課堂 Code Review，**教師設計的模擬 AI**（`ai/HeavyRover.java`，非真實 AI 對話） | 讓重型車探索得 3 份；模擬 AI 建議在子類別新增同名 private explore，聲稱 execute 會自動使用它 | **拒絕**：父類別 private 方法不被繼承、不能覆寫，Rover.execute 仍走 Rover 自己的 explore，子類別那個 explore 從不被呼叫 | 先預測能編譯但產量不變（鑽探後探索 samples 為 5）；ai-check 實際 HeavyRoverTry「探索後」「完成後狀態維持」兩行 6/5/2/1 不符（預期 6/6/2/1），其他 13 行相符；在 AI explore 前加 @Override 出現 `method does not override or implement a method from a supertype`。見 LAB_REPORT E1–E3 | 最小修正：移除 private explore，改覆寫 `@Override protected int exploreYield()` 回 3；重跑同一個 HeavyRoverTry 15/15 相符。`[AI]` commit 03ec6f6 → `[fix]` commit dc45890 |
| **真實 AI**：Claude Code（Anthropic Claude，桌面版 Code 分頁），2026-11-27 | 我請 AI「開始做第八週的作業」；AI 先抓老師 GitHub 最新版（含當天新增的 AI_USAGE 範本）、讀完投影片與教材，再協助完成 TODO 1–4、B／C／E 實驗、D5 邊界測試、SurveyRover 遷移、UML 與報告 | **採納**：TODO 1 只改一行讓父流程呼叫 exploreYield()；TODO 2、4 用相同簽名加 @Override；TODO 4 用 super.execute 交回其他指令；遷移只覆寫產量方法。理由：符合投影片 D1–D6 規格，不改 Mission、不重抄 switch、不重複狀態 | 本機 JDK 21.0.13 實測：HeavyRoverTry 15/15、DrillMissionTry 14/14、BoundaryTry 5/5、SurveyRoverTry 8/8 相符；CompareMissions 兩段報表與投影片一致。D5 與遷移的本人反例，在副本植入「產量方法多計次」「TODO 1 漏做」「方法名拼錯拿掉 @Override」都被抓到 | 我先用 VS Code 與 IntelliJ IDEA 檢查、親自執行 MemberDemo 與自訂腳本 {1,4,2} 後才同意 commit；D 段 commit 9929f2b |

本人E1預測：能編譯；鑽探後再 execute(1) 的 samples 為 5（不是 6）；execute 走的是 Rover 自己的 private explore，AI 加的 private explore 不會被呼叫。
修正前commit／證據：`[AI]` commit 03ec6f6；不符證據見 LAB_REPORT E2（兩行 6/5/2/1）。
修正後commit／相同案例重測：`[fix]` commit dc45890；重跑同一個 HeavyRoverTry，15 行全部相符（原不符兩行變成 6/6/2/1 相符）。
三至五句補強Prompt：見 LAB_REPORT E5（指定 `@Override protected int exploreYield()` 的精確簽名、禁止扣能源／加樣本／計次、不改 private explore 與 Mission，並附 20/0 跑 {1,1,2} 與 6/2 跑 {1,3} 的預期）。

AI 宣稱通過不算人工驗證；以上結果都是實際編譯執行的輸出。
