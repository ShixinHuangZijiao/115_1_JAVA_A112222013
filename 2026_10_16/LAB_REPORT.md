# 第五週實作報告：把任務日誌設計成物件

姓名：待填　學號：A112222013　實際上課日期：2026/10/16　實際 JDK：openjdk 21.0.13.1（javac 21.0.13.1，Microsoft Build）

**教材版本說明**：學期 repo 內的課程資料（`115_1_Java_student-main/`）是舊版。本週改用老師最新發布的教材：結果代碼與報表都改成繁體中文，投影片也換成 `week05_slides_codex_claude.html`。本週教材沒有 student_lab.md 與 assignment.md，所以本報告依投影片的講義題號 A–H 整理。

所有「實際」欄位都是本機實際執行的輸出。錯誤實驗都放在 repo 外的暫存副本，`java-05/src` 只保留正確版。執行時加上 `-Dstdout.encoding=UTF-8`，避免 Windows 主控台把中文印成亂碼。

## A｜取回與診斷

1. 使用既有 repo `115_1_JAVA_A112222013`，確認與 origin/main 同步。建立 `2026_10_16/java-05/src`，放入 examples 六檔與 starter 兩檔，共 8 個 Java 檔；teacher 同名檔不放。
2. `java -version`：openjdk version "21.0.13.1" 2026-11-17 LTS；`javac -version`：javac 21.0.13.1。
3. 在 java-05 執行 `javac -encoding UTF-8 -d out src/*.java` 編譯成功。

診斷：第四週假設第 2 步漏掉 `energyLog.add(8)`，energyLog 會變成 [14, 6]。用同一索引回放時，第 2 步會配到第 3 步的能源 6；第 3 步讀 energyLog.get(2) 會拋出 IndexOutOfBoundsException。三條清單沒有任何機制保證同一索引屬於同一步，所以本週改成「一步 = 一個 MissionStep 物件」。

## B｜類別與建構（CreateStepDemo + StepCard）

### B1 一行程式，三個角色

`StepCard first = new StepCard(1, 14);`

- `StepCard`：類別，規定物件有哪些欄位與方法，也是變數 first 的型態。
- `new StepCard(1, 14)`：真正建立一個物件，並呼叫建構子把 1、14 存進欄位。
- `first`：參考變數，保存「找得到那個物件」的參考值，不是把整個物件裝進去。只寫 `StepCard first;` 只有變數、沒有物件。

`this.energy = energy;`：左邊是目前物件的欄位，右邊是這次呼叫傳進來的參數。若寫成 `energy = energy;`，只是參數指定給自己，欄位仍是預設值 0。

### B2 兩次 new

| 時間點 | first 的 command／energy | second 的 command／energy |
|---|---|---|
| 第一個 new 完成 | 1／14 | 尚未建立 |
| 第二個 new 完成 | 1／14（不被覆蓋） | 2／12 |

預測三行輸出：第一次=1/14、第二次=2/12、同一個物件=false。實際：

```text
第一次=1/14
第二次=2/12
同一個物件=false
```

一致。兩次 new 就是兩個物件，各有自己的欄位。

### B3 編譯錯誤（兩個都在副本試過，正式 src 未改）

| 取消註解的行 | 實際編譯訊息 | 原因 |
|---|---|---|
| `first.energy = 99;` | `error: energy has private access in StepCard` | energy 是 private，只有 StepCard 自己能直接存取 |
| `StepCard empty = new StepCard();` | `error: constructor StepCard in class StepCard cannot be applied to given types; required: int,int found: no arguments` | 已經自己宣告了兩參數建構子，編譯器就不再自動補無參數版 |

### static 與實例成員

`main` 是 static，屬於類別，不必先 new 就能由 JVM 執行；command、energy 是實例欄位，每個物件各有一份。若欄位誤加 static，所有物件共用同一格，後建立的會覆蓋前面的值（F3 選項 1 已實測：first 也變成 2/12）。

## C｜封裝（BatteryDemo + RoverBattery）

| 呼叫順序 | 預測回傳值／能源 | 實際 |
|---|---|---|
| low 從 5 開始，explore() | false／5 | 探索=false, 能源=5 |
| low.charge() | true／10 | 充電=true, 能源=10 |
| low.explore() | true／4 | 探索=true, 能源=4 |
| edge 從 19 開始，charge() | true／20（封頂） | 充至上限=true, 能源=20 |
| edge 再 charge() | false／20 | 已充滿=false, 能源=20 |

全部一致。energy 是 private，外部只能透過 explore()、charge() 修改，扣電和封頂的規則只寫在一個地方。如果開放 `setEnergy(999)`，每個呼叫端都要自己負責檢查範圍，規則就散掉了。private 只限制存取位置，不會自動檢查數值範圍。

「目前狀態」和「歷史紀錄」需求不同：RoverBattery 代表現在的能源，可以透過守規則的方法改變；StepCard／MissionStep 代表某一步發生了什麼，建立後只讀，所以不提供 setter。

### C1 C# 與 Java 欄位存取差異

| 類別裡的欄位 | C# | Java |
|---|---|---|
| 明寫 private | 只有類別內可用 | 只有類別內可用 |
| 省略修飾字 | 預設 private | 套件存取：同一套件都能讀寫 |
| 外部讀、內部改 | `public int Energy { get; private set; }` | private 欄位 + getEnergy() + 受控方法 |

本人在副本驗證：保留 private 時 `first.energy = 99;` 編譯失敗（見 B3）；把 StepCard 的 `private int energy;` 改成 `int energy;` 後編譯成功，執行印出「改寫後=99」。因為 StepCard 與 CreateStepDemo 都在預設套件，省略 private 就打開了保護缺口。副本已丟棄。

## D｜參考與快照

### D1 ReferenceDemo

| 呼叫後 | 預測 | 實際 |
|---|---|---|
| alias.explore() 後 first/other | 14/20 | 建立別名後=14/20 |
| first == alias | true | 同一個參考=true |
| spend(first) 後 | 8 | 扣除能源後=8 |
| replaceLocal 內部 | 1 | 方法內新物件的能源=1 |
| replaceLocal 返回後 | 8 | 重新指定參考後=8 |

`alias = first` 複製的是參考值，first 和 alias 指向同一顆電池；other 是另一次 new 的電池。Java 一律傳值，物件參數拿到的是參考值的副本：透過副本修改物件，呼叫端看得到；把副本改指新物件，呼叫端不受影響。

### D2 IntelliJ IDEA

待補：本機沒有用 IntelliJ IDEA 的圖形介面實際操作，SnapshotDemo 是用終端機執行的（見 D3）。依投影片規定，未實際用 GUI 操作就寫待補，不能用終端機結果代替。

### D3 SnapshotDemo

預測：指令=1/能源=14、指令=3/能源=19、目前能源=19、各自獨立的紀錄=true。實際：

```text
指令=1, 能源=14
指令=3, 能源=19
目前能源=19
各自獨立的紀錄=true
```

一致。每次 `new StepCard(..., battery.getEnergy())` 都複製了當下的 int 值，之後充電不會把第一筆的 14 改成 19。

想一想：如果兩次 add 的是同一顆電池，清單不會自動複製。add 只存參考，兩個索引會指向同一個物件（F3 選項 3 已實測：19/19、同一個物件=true）。

## E｜作業實作

### E1 MissionStep TODO 1–3 與兩物件檢查

- TODO 1：建構子用 `this.command = command;` 等三行保存參數。
- TODO 2：三個 getter 回傳本物件的欄位。
- TODO 3：`return "成功".equals(result);`，用內容比較，result 為 null 時也不會丟例外。

暫時把檢查碼放在副本的 MissionLog.main 最前面（正式 src 沒有放）：

```java
MissionStep first = new MissionStep(1, 14, new String("成功"));
MissionStep second = new MissionStep(2, 12, "能源不足");
```

| 物件 | 預期 command／energy／result／isSuccessful | 實際 |
|---|---|---|
| first | 1／14／成功／true | 1/14/成功/true |
| second | 2／12／能源不足／false | 2/12/能源不足/false |

建立 second 後 first 沒有被覆蓋；`new String("成功")` 內容相同、參考不同，仍判為 true。

### E2 MissionLog TODO 4–6

- TODO 4：switch 之後 `history.add(new MissionStep(command, energy, result));`。放在行動完成之後，result 才有值、energy 才是行動後的能源；成功、拒絕、無效、返回每一筆都記。
- TODO 5：一般 for 迴圈 `history.get(i)` 取出物件，再用三個 getter 印出「步驟 i+1: 指令=…, 結果=…, 能源=…」。
- TODO 6：加強版 for 走訪 history，`isSuccessful()` 為 true 時 ok++，`"返回".equals(step.getResult())` 時 returned++。

最低能源是老師提供的支援碼：清單裝的是 MissionStep 不是 Integer，沒有定義「誰比較小」，所以不能用 Collections.min；起點用第一筆的能源，用 0 會把 14、8、6 錯算成 0。

### E3 六條路線驗收

| script | 預期 能源/樣本/行動、結果、步數/成功/拒絕/略過、次數、最低 | 本人實際 | 相符 |
|---|---|---|---|
| {1,1,2} | 6/5/3 任務成功、3/3/0/0、[0,2,1,0]、6 | 6/5/3 任務成功、3/3/0/0、[0, 2, 1, 0]、6 | 是 |
| {3,9,0} | 20/0/0 已返回、3/0/2/0、[1,0,0,1]、20 | 20/0/0 已返回、3/0/2/0、[1, 0, 0, 1]、20 | 是 |
| {0} | 20/0/0 已返回、1/0/0/0、[1,0,0,0]、20 | 20/0/0 已返回、1/0/0/0、[1, 0, 0, 0]、20 | 是 |
| {2,2,2,2,2} | 10/5/5 任務成功、5/5/0/0、[0,0,5,0]、10 | 10/5/5 任務成功、5/5/0/0、[0, 0, 5, 0]、10 | 是 |
| {1,1,1,1} | 2/6/3 任務成功、3/3/0/1、[0,3,0,0]、2 | 2/6/3 任務成功、3/3/0/1、[0, 3, 0, 0]、2 | 是 |
| {2,1} | 12/3/2 任務未完成、2/2/0/0、[0,1,1,0]、12 | 12/3/2 任務未完成、2/2/0/0、[0, 1, 1, 0]、12 | 是 |

完整輸出（每列在副本修改 main 的 script 後重新編譯；繳交版維持 {1,1,2}）：

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

### 教師固定狀態示範

我沒有看到老師現場示範，以下全部是**本人複驗**：在副本把 runMission 的初始值與 script 改成表列數值後執行。

| 初始能源/樣本；script | 預期 | 本人複驗結果 |
|---|---|---|
| 5/0；{1,3,0} | 10/0/1 已返回，3 筆，最低 5 | 10/0/1 已返回，3 筆（能源不足、成功、返回），最低能源=5 |
| 2/4；{2} | 0/5/1 任務成功，1 筆 | 0/5/1 任務成功，1 筆，最低能源=0 |
| 0/0；{3,1} | 能源耗盡，0 筆 | 0/0/0 能源耗盡，0 筆，略過=2，最低能源=無紀錄 |
| 20/5；{1} | 任務成功，0 筆 | 20/5/0 任務成功，0 筆，略過=1，最低能源=無紀錄 |
| 20/0；{} | 任務未完成，最低無紀錄 | 20/0/0 任務未完成，0 筆，最低能源=無紀錄 |

## F｜新需求與反例

### F1 設計卡：樣本運送盒（紙上設計，未實作）

| 設計卡欄位 | 本人的決定 |
|---|---|
| 類別名稱與一句責任 | SampleCase：管理一個運送盒目前裝了幾份樣本，並依容量規則存入與卸空。不管探索、能源或任務流程 |
| 欄位、型態、可見性 | `private int capacity`（建立後不變）、`private int count`（目前存量）。剩餘空間可由 capacity − count 算出，不另存欄位 |
| 建構子 | `public SampleCase(int capacity)`：小於 1 設為 1，大於 8 設為 8；count 初始為 0 |
| 公開方法 | `boolean store(int amount)`：amount > 0 且 amount ≤ 剩餘空間才加入並回傳 true，否則整批拒絕、回傳 false、存量不變。`int getCount()`、`int getCapacity()`、`int getRemaining()`：查詢。`int unload()`：回傳目前存量並把 count 歸 0，空盒回傳 0 |
| 不提供的方法、恆成立條件 | 不提供 setCount 或任何直接改存量的方法，也不負責印字。恆成立：1 ≤ capacity ≤ 8、0 ≤ count ≤ capacity |
| 測試呼叫（容量 3） | store(2)→true，存量 2、剩 1；store(2)→false，存量仍 2；store(1)→true，存量 3（剛好滿）；unload()→回傳 3，存量 0；再 unload()→回傳 0 |

其他測試：`new SampleCase(0)` 容量應為 1；`new SampleCase(9)` 容量應為 8；`store(0)`、`store(-1)` 應回傳 false 且存量不變；兩個盒子各自存入互不影響。以上是紙上推論，**未實測**（SampleCase 實作是選做，本週沒有寫）。

### F2 審查模擬 AI 建議

| 偏差 | 違反哪句需求 | 哪個呼叫看得出來 |
|---|---|---|
| 存入時沒檢查剩餘空間 | 剩餘空間不夠要整批拒絕、存量不變 | 容量 3，`store(2); store(2);` → count 變 4，超過容量 |
| 沒拒絕非正數 | 每次存入份數要大於 0 | `store(-1)` → count 變 -1，存量反而減少 |
| 建構子沒修正容量 | 容量範圍 1～8，超出改為最接近的合法值 | `new SampleCase(0)` 容量是 0；`new SampleCase(20)` 容量是 20 |
| 欄位是 public | 不能任意改寫存量 | `box.count = 99;` 可以直接改，也能 `box.capacity = -5;` |
| store 回傳 void | 呼叫者要知道是否存入成功 | `boolean ok = box.store(1);` 無法編譯 |
| 缺少卸空與剩餘空間查詢 | 要能查剩餘空間、返航時一次卸空並取得份數 | 沒有 `unload()`、`getRemaining()` 可呼叫 |

改寫後的 Prompt：

> 請設計 SampleCase，責任是管理一個樣本運送盒目前的存量，並依容量規則存入與卸空。
> 建構規則：`SampleCase(int capacity)`，容量小於 1 時設為 1、大於 8 時設為 8；初始存量 0。
> 公開方法：`boolean store(int amount)`，amount 大於 0 且不超過剩餘空間才存入並回傳 true，否則整批拒絕、回傳 false、存量不變；`int getCount()`、`int getCapacity()`、`int getRemaining()` 查詢；`int unload()` 回傳卸下份數並把存量歸 0，空盒回傳 0。
> 不要提供：任何直接改寫存量的方法（例如 setCount）、輸出方法；欄位一律 private。
> 限制：本週一般 Java class、單一建構子，不用 record、繼承、Lambda、例外處理。
> 請列出測試呼叫與預期：容量 3 時 store(2)→true／2、store(2)→false／2、store(1)→true／3、unload()→3、再 unload()→0；store(0) 與 store(-1) 都回傳 false；new SampleCase(0) 容量 1、new SampleCase(9) 容量 8；兩個盒子各自存入互不影響。
> 不得聲稱我已經實測。

### F3 模擬錯誤三選一

選**選項 2**（「固定代碼用 == 就好」）。另外兩個選項也在副本順手驗證過，結果附在表後。

| 證據 | 本人紀錄 |
|---|---|
| 選項與預測 | 副本把 isSuccessful 改成 `return result == "成功";`。預測：E1 的 `new String("成功")` 內容相同但參考不同，== 比的是參考，所以 first 會回傳 false；主任務用的是字串常值，可能「碰巧通過」 |
| 錯誤結果 | E1：`1/14/成功/false`（應為 true）、`2/12/能源不足/false`。同一個副本跑 {1,1,2}，摘要仍是「步數=3, 成功=3」、任務成功，**只看主任務抓不到這個錯** |
| 最小修正與理由 | 改回 `return "成功".equals(result);`。字串要比內容不是比參考；常值剛好是同一個物件只是巧合，資料來源一改（例如 new String、讀檔、輸入）就會錯 |
| 修正後結果 | E1：`1/14/成功/true`、`2/12/能源不足/false` |
| 修正後 {1,1,2} | 6/5/3 任務成功，回放能源 14、8、6，步數=3, 成功=3（完整輸出見 E3） |
| 修正後 {3,9,0} | 20/0/0 已返回，步數=3, 成功=0, 拒絕=2，指令次數 [1, 0, 0, 1]（完整輸出見 E3） |

另外兩個選項的副本實測：

- 選項 1（三欄加 static）：E1 兩行都印 `2/12/能源不足/false`，第一筆被第二筆覆蓋。跑 {1,1,2} 時指令次數變成 [0, 0, 3, 0]（三步都讀到最後一步的指令 2），終態仍是任務成功，所以只看終態抓不到。
- 選項 3（同一顆電池存兩次）：印出 `19/19`、`同一個物件=true`；正確的 SnapshotDemo 是 14、19、各自獨立的紀錄=true。

### F4 反思

1. 只讀 vs 可改各符合什麼需求？MissionStep 記錄已發生的歷史，沒有改寫需求，所以建立後只讀；SampleCase 的存量本來就會變，但只能經過 store、unload 這兩個守規則的方法改變。
2. 只測最後能源能證明快照都對嗎？不能。選項 3 錯誤版和正確版的目前能源都是 19；選項 1 錯誤版的終態也還是任務成功。要逐筆讀歷史才抓得到。
3. 誰負責傳入值與建立時機？呼叫端 MissionLog。private 只限制存取，不會判斷時機；如果在探索「之前」就記下能源 20，值合法、欄位也是 private，歷史仍然記錯。所以 MissionLog 要在行動完成後，才用當步結果建立 MissionStep。

## G｜課末檢核

1. `MissionStep step` 只宣告參考變數，還沒有物件；`new MissionStep(...)` 才真正建立物件並呼叫建構子。
2. `this.energy` 是目前物件的欄位；參數 `energy` 是這次呼叫傳進來的值。
3. main 是 static，屬於類別，JVM 不必 new 就能從這裡開始；MissionStep 的資料不加 static，才能每個物件各自保存一份，否則後建立的會覆蓋前面的。
4. 不會。add 只存參考，同一個物件 add 兩次是兩個索引指向同一個物件。
5. MissionStep：保存一筆已發生的任務紀錄。MissionLog：推進腳本、保存歷史、產生報表。RoverBattery：管理目前能源，並只透過規則改變（本週示範用，主任務的能源與樣本仍在 MissionLog）。

## H｜繳交與保存

- 正式 src 保留正確版：欄位不加 static、用 equals、main 的 script 為 {1,1,2}、沒有 E1 暫時輸出。重新編譯執行通過。
- `java-05/src` 共 8 個 Java 檔（examples 6 檔＋完成的 MissionStep、MissionLog）；`java-05/README.md`、本報告、AI_USAGE.md 已完成。out、.class 由 .gitignore 排除。
- commit、push 後到遠端核對，另取回新副本重新編譯。

GitHub repo／作業目錄：https://github.com/ShixinHuangZijiao/115_1_JAVA_A112222013 ／ `2026_10_16/`；最終 commit：待推送後填寫。
新副本 JDK／編譯／預設輸出：待推送後填寫。

## 選做

未做。
