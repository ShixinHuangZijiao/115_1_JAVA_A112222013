# 第五週 AI 使用紀錄

| 欄位 | 本人紀錄 |
|---|---|
| 使用工具／方式 | 真實 AI：Claude Code（Anthropic Claude，桌面版 Code 分頁）。F3 的錯誤建議是老師投影片提供的模擬建議，不是 Claude 的建議 |
| 目的與提問摘要 | 我請 AI「開始做第五週的作業，老師有時候的作業規範要改什麼檔案你要看清楚」。AI 讀完教材後，發現學期 repo 裡的課程資料是舊版，老師新發布的版本把結果代碼與報表改成繁體中文，所以改用新版；並依投影片確認只需修改 starter 的 MissionStep（TODO 1–3）與 MissionLog（TODO 4–6），examples 只執行不修改。也請 AI 協助 F1 SampleCase 設計卡、F2 審查與 Prompt。F3 選模擬錯誤選項 2（用 == 比字串）。對話截圖見 [CHAT_LOG.md](CHAT_LOG.md) |
| 建議與本人處理 | AI 的寫法：建構子用 this 保存三欄；getter 回傳欄位；isSuccessful 用 `"成功".equals(result)`；TODO 4 在 switch 之後 `history.add(new MissionStep(command, energy, result))`；TODO 5 用索引 for 與 getter 印中文回放；TODO 6 用 isSuccessful 與 `"返回".equals(...)` 計數。採納，因為和投影片規格與預期輸出一致。模擬建議 `result == "成功"` 拒絕：== 比參考不比內容 |
| 本人實際驗證 | 全部在本機以 JDK 21.0.13 實際執行：E1 兩物件得 1/14/成功/true、2/12/能源不足/false；六條路線與五筆固定狀態都和投影片表格一致。F3 選項 2 錯誤副本 E1 第一筆變 false，但 {1,1,2} 摘要仍「成功=3」，只看主任務抓不到；改回 equals 後 E1 恢復 true，重測 {1,1,2} 得 6/5/3 任務成功、{3,9,0} 得 20/0/0 已返回。完整輸出見 LAB_REPORT E3、F3 |

AI 的正確宣稱不能代替編譯執行。D2 的 IntelliJ IDEA 圖形介面操作由本人於 2026-11-27 親自補做（AI 只提供操作步驟說明），執行截圖與輸出見 LAB_REPORT D2。
