# 第四週 AI 使用紀錄

| 欄位 | 記錄 |
|---|---|
| 使用工具 | Claude Code（Anthropic Claude，桌面版 Code 分頁） |
| 使用目的與提問摘要 | 我請 AI「開始做第四週的作業」。AI 先讀完 week04 的講義、AI 範本、examples/README、投影片與全部 Java 檔，再把 starter 與五個核心 examples 複製到 `2026_10_09/java-04/src`，在副本完成 MissionLog 的 TODO 1–5，並跑講義 B–F 的實驗。F2 的資料結構需求 Prompt 寫在 LAB_REPORT F2；F3 選模擬錯誤第 1 項（刪掉 countCommands 的範圍檢查）。對話截圖見 [CHAT_LOG.md](CHAT_LOG.md)。 |
| AI 建議與本人處理 | TODO 2：switch 後同時 add command、energy、result；TODO 3：一般 for 用同一索引 get 三條清單；TODO 1：加強版 for 走訪，`command >= 0 && command < counts.length` 才累加；TODO 4：frequency 算 OK、RETURN，再算 rejected、ignored；TODO 5：isEmpty 時印 N/A，否則用 Collections.min。採納，因為與講義、投影片的規格一致。模擬建議「刪範圍檢查」拒絕，因為日誌會記錄無效指令；F2 的 int[100] 要補有效筆數與滿載處理才可用，HashSet 拒絕（會去重、不保留順序）。 |
| 實際驗證結果 | 全部在本機以 JDK 21.0.13 實際執行：六條必做路線與五筆固定狀態都和預期一致。F3 反例 {3,9,0} 在刪除檢查後拋出 `ArrayIndexOutOfBoundsException: Index 9 out of bounds for length 4`，恢復後得 counts=[1,0,0,1]、rejected=2、RETURNED；重測 {1,1,2} 得 6/5/3 SUCCESS。完整輸出見 LAB_REPORT E 與 F3。 |

只記錄實際發生的操作。AI 宣稱正確不能取代編譯執行。
