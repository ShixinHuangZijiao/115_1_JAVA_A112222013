// 類別：RelationDemo（第 6 週範例，教材未提供，依投影片 C1 的程式片段自行重建）
// 意義：示範兩支燈共用同一顆電池，以及充電器只透過參數使用電池。
// 作用：main 以區域變數建立 Lamp、Battery、Charger → 與它們是相依。
public class RelationDemo {
    // main：程式進入點。A、B 共用 shared，C 用另一顆電池。
    public static void main(String[] args) {
        Battery shared = new Battery(8);
        Lamp a = new Lamp(shared);
        Lamp b = new Lamp(shared);
        Lamp c = new Lamp(new Battery(8));
        System.out.println("燈具 A=" + a.turnOn(6));
        System.out.println("燈具 B=" + b.turnOn(6));
        System.out.println("燈具 C=" + c.turnOn(6));
        System.out.println("共用電池能源=" + shared.getEnergy());
        Charger charger = new Charger();
        System.out.println("充電=" + charger.refill(shared));
        System.out.println("燈具 B 再開啟=" + b.turnOn(6));
        System.out.println("共用電池能源=" + shared.getEnergy());
    }
}
