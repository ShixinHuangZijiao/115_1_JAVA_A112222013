// 類別：Charger（第 6 週範例，教材未提供，依投影片 C1、C2 規格自行重建）
// 意義：充電器，只在「方法參數」中使用電池、不保存它 → 與 Battery 是相依（dependency）。
// 作用：refill 替傳入的電池充電一次。
public class Charger {
    // refill：替傳入的電池充電，回傳是否成功（已滿為 false）。
    public boolean refill(Battery battery) {
        return battery.charge();
    }
}
