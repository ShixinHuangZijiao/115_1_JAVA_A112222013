# 第三週 AI 使用紀錄

| 欄位 | 記錄 |
|---|---|
| 使用工具 | Claude Code（Anthropic Claude，桌面版 Code 分頁） |
| 使用目的與提問摘要 | 請 AI 讀取第三週的作業說明與 starter，先把 starter 與 examples 複製到 `2026_10_02/java-03/src` 再修改；協助完成 RoverMission 的 D1–D3、安裝 JDK 21、執行講義要求的測試，並整理 LAB_REPORT／README。AI 除錯選教師模擬錯誤第 1 題（`energy > 6`）。主要 Prompt：「這是我的作業……start 資料夾內是我要做的作業……在實作的時候要先複製一份再開始做，我們將會從第三週開始」。對話過程截圖見 [CHAT_LOG.md](CHAT_LOG.md)。 |
| AI 建議與本人處理 | AI 在 case 1／2／3 用 if 判斷能源，成功時才扣能源、加樣本、`actionCount++`，拒絕時只印 NOT_ENOUGH_ENERGY／ALREADY_FULL；充電後用 if 封頂 20。採納，因為符合講義「只有成功行動才計次」與 break、case 0、default 骨架不變的要求。模擬錯誤 `energy > 6` 拒絕，因為規則是「至少 6」，改回 `energy >= 6`。 |
| 實際驗證結果 | 全部在本機以 JDK 21 實際執行：預設狀態 0／1、1、2／五次 2／3、9、0 四條路線與預期一致；固定狀態 5／0、6／0、2／4 三筆一致；`energy > 6` 反例在 6／0 輸入 1 時被拒絕，修正後得 0／2／1 EXHAUSTED。完整數據見 LAB_REPORT.md D 的測試表。 |

只記錄實際發生的使用與測試；AI 說「正確」不能代替實際執行。
