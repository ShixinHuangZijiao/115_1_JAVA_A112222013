// 類別：Lamp（第 6 週範例，教材未提供，依投影片 C1、C2 規格自行重建）
// 意義：一支燈，用「欄位」保存一顆電池 → 與 Battery 是關聯（association）。
// 作用：turnOn 向自己知道的那顆電池要能源；多支燈可以共用同一顆電池。
public class Lamp {
    // battery：這支燈連到的電池（建構時傳入，契約：不可為 null）。
    private Battery battery;

    // 建構子：記住要使用的電池（保存的是參考值，不是電池副本）。
    public Lamp(Battery battery) {
        this.battery = battery;
    }

    // turnOn：開燈一次，使用 amount 格能源；電池能源不足就回傳 false。
    public boolean turnOn(int amount) {
        return battery.use(amount);
    }
}
