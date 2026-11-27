# 第七週與 AI 的討論過程

使用工具：Claude Code。

## 討論流程摘要

1. 我請 AI 先去看老師 GitHub 上第七週的規定再確認一次。AI 找到老師的課程 repo（shuochenghu/115_1_Java_student），確認最新 commit 與我下載的教材相同，也確認第六週缺的 examples 在 GitHub 上同樣沒有。
2. AI 讀完第七週的投影片、examples、starter、模擬 AI 程式與範本圖，整理出規定：要寫 Rover 的 TODO 1 與 HeavyRover 的 TODO 2、3；E 段要分 `[AI]`、`[fix]` 兩個 commit；課後獨立遷移必做。遷移題 AI 依建議選路線 A：CargoRover。
3. AI 先跑 starter 原版確認起點，再完成 TODO 1–3，跑 HeavyRoverTry 十五行、六條舊路線、B2、B3、C1–C3 與會員對照實驗，並設計自己的反例 HeavyRover(8,2)。
4. 依投影片順序 commit：先提交 D 段完成碼與 E1 預測，再放入原封不動的模擬 AI 版並 `[AI]` commit，執行看到 9 行不符、用探查程式證實一個物件裡有兩份狀態，最小修正後 `[fix]` commit。
5. AI 寫 CargoRover 與十二項測試，並用拿掉停止檢查的錯誤副本確認測試有效；最後補繼承圖、DESIGN.md、README、AI_USAGE 與 LAB_REPORT。

## 截圖

<!-- 截圖存到 2026_10_30/images/，檔名 chat01.png、chat02.png… -->
