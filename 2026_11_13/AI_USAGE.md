# 第六週AI使用紀錄

| 欄位 | 本人紀錄 |
|---|---|
| 使用工具／方式 | 真實 AI：Claude Code（Anthropic Claude，桌面版 Code 分頁）。E2 的錯誤類別圖是投影片上的教師模擬建議，不是 Claude 的建議 |
| 目的與提問摘要 | 我請 AI「開始做第六週的作業」。AI 讀完教材後發現老師發布的學生版缺 examples 六檔、三份練習圖範本、DESIGN_template.md 與 PlantUML；我決定依投影片自行重建 examples，並同意下載 PlantUML 官方 jar。AI 協助完成 Mission TODO 1–3、三份類別圖、SampleCase 人工初版設計與 DESIGN.md。對話截圖見 [CHAT_LOG.md](CHAT_LOG.md) |
| 建議與本人處理 | Mission.run：每輪先檢查 isStopped、execute 後用行動後能源建立 MissionStep、指令 0 記錄後才停；countOk 用 isSuccessful；lowestEnergy 空清單回 -1、非空以首筆為起點。採納，因為符合投影片 D2 的順序表與六條路線。樣本盒採用 private 欄位、`store : boolean`、剩餘空間計算得出、提供 unload；E2 模擬 AI 圖的 6 項問題全部拒絕（store 回 void、直接累加、欄位 public、缺 unload、缺剩餘空間查詢、無容量修正） |
| 本人實際驗證 | 全部在本機以 JDK 21.0.13 實際執行：六條路線與五筆固定狀態和投影片表格一致；RoverDemo、RelationDemo、SampleCaseDemo 輸出與投影片答案逐行一致。E3 錯誤副本（刪超量檢查）得 超量存放=true, 存量=4，恢復後 超量存放=false, 存量=2，再重測 {1,1,2}、{3,9,0} 通過。D3 反例（刪停止檢查）在 {1,1,1,1} 多出一筆「任務已停止」。圖用 PlantUML 1.2026.10 產生並以 PNG 預覽檢查。細節見 LAB_REPORT D3、E3 |

AI 的正確宣稱不能代替編譯執行。examples 六檔是依投影片規格自行重建，不是老師原檔。IntelliJ IDEA 圖形介面操作由本人於 2026-11-27 親自補做（AI 只提供操作步驟說明），執行截圖與輸出見 LAB_REPORT D1。

## 我的反思

哪一句需求幫我判斷AI建議：「呼叫端要知道成敗」直接否定了 `store : void`；「只接受正數且放得下的整批樣本，拒絕不改存量」則用 E3 超量反例實際抓到「直接累加」的錯誤。

只看類別圖還不能證明什麼：類別圖只顯示欄位和方法簽名，不能證明方法內的邏輯正確（例如有沒有檢查超量），也不顯示執行順序（例如 Mission 要先 execute 再建立快照）。圖能正常顯示，也不代表設計符合需求，仍要用需求句對照、再用反例實際執行。
