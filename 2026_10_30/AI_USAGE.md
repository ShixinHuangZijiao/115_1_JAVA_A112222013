# 第七週AI使用紀錄

姓名／學號：待填／A112222013
工具及來源：(1) 真實 AI：Claude Code（Anthropic Claude，桌面版 Code 分頁）；(2) 教師模擬：`ai/HeavyRover.java`（教師設計的模擬 AI 錯誤回覆，不是真實對話）

## 精簡紀錄

| 使用目的與Prompt摘要（註明來源） | 採納／拒絕與理由 | 本人驗證：預期、實際、證據位置 | 最終修改與commit |
|---|---|---|---|
| 【教師模擬】Prompt「請繼承 Rover 新增 drill，耗 8 格得 3 份，並記鑽探次數」；模擬 AI 聲稱父類別欄位 private，所以子類別重新宣告一份 energy、samples | 拒絕：子類別重複宣告同名欄位會讓一個物件有兩份狀態，getter 讀 Rover 那份、drill 改子類別那份；另外沒有檢查停止、沒有計行動次數 | 預測第一次 drill 後 getEnergy 仍為 20。實際 HeavyRoverTry 9 行不符，例如「鑽探後: 20/0/0/1 （預期 12/3/1/1）」「任務完成後鑽探: 成功 （預期 任務已停止）」「能源耗盡後無法充電: 成功」；探查程式讀到 drill 後 Rover 那份 20/0/0、子類別那份 12/3。見 LAB_REPORT E2、E3 | 刪掉重複欄位與建構子設定，drill 改用 isStopped、spendEnergy、addSamples、countAction；重測十五行全相符。`[AI]` commit 0f597ec、`[fix]` commit e551194 |
| 【真實 AI，Claude Code】我請 AI「開始做第七週的作業」並先確認老師 GitHub 上的最新規定。AI 協助完成 TODO 1–3、執行 B、C、D、E 段實驗、撰寫 CargoRover 與測試、繼承圖與報告 | 採納：spendEnergy 先檢查再扣；explore、scan 改用三個 protected 方法；drill、load 先檢查停止。理由是符合投影片 D2、D4 的規格表，且不重複宣告狀態、不新增 setter、不覆寫 | 本機 JDK 21.0.13 實測：HeavyRoverTry 十五行相符、六條舊路線不變、CargoRoverTry 十二行相符；自己的反例 HeavyRover(8,2) 抓出「只回成功卻沒更新」的錯誤版。見 LAB_REPORT D、遷移段 | D 段 commit fa0c55f；遷移與報告於後續 commit |

## 反思

- 哪個案例讓我發現「回成功卻沒更新正確狀態」：HeavyRoverTry 第一行「鑽探: 成功」是相符的，但第二行「鑽探後: 20/0/0/1」才露出問題——只看回傳字串會被騙，一定要讀 getter。我自己的反例 HeavyRover(8,2) 也是同樣道理：錯誤版回「成功」，狀態卻停在 8/2/0/0。
- 我保留或拒絕AI建議的理由：拒絕模擬 AI 的「重新宣告一份欄位」，因為 private 不被繼承不代表物件沒有那份資料，重新宣告只會多出第二份；修正時也沒有把欄位改成 protected，而是改用 Rover 提供的 protected 方法，讓規則只放在 Rover。
- 我仍無法解釋或尚未完成的部分：E5 的補強 Prompt 沒有實際送到其他 AI 服務測試；選做項目未做。
