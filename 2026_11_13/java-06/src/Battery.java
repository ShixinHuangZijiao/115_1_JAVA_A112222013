// 類別：Battery（第 6 週範例，教材未提供，依投影片 B1、C1 規格自行重建）
// 意義：一顆一般電池，保存目前能源。
// 作用：use 扣能源、charge 充電；沒有「能源 0 任務結束」的規則，不能代替 Rover。
public class Battery {
    // energy：目前能源（0～20）。
    private int energy;

    // 建構子：設定初始能源。
    public Battery(int energy) {
        this.energy = energy;
    }

    // getEnergy：回傳目前能源。
    public int getEnergy() {
        return energy;
    }

    // use：使用 amount 格能源；amount 大於目前能源就拒絕、回傳 false、不扣。
    public boolean use(int amount) {
        if (amount > energy) {
            return false;
        }
        energy -= amount;
        return true;
    }

    // charge：充電 +5，最多 20；已滿回傳 false、不改值。
    public boolean charge() {
        if (energy == 20) {
            return false;
        }
        energy += 5;
        if (energy > 20) {
            energy = 20;
        }
        return true;
    }
}
