// 類別：CargoRover（第 7 週課後獨立遷移，路線 A，本人作業）
// 意義：一種探測車（is-a Rover），多了一個容量固定 3 的貨艙；貨物不是科學樣本。
// 作用：沿用 Rover 的探索、掃描、充電與停止規則，新增 load 裝貨。
//       能源、樣本、行動次數仍只由 Rover 保存一份；本類別只保存自己的 cargo。
public class CargoRover extends Rover {
    // CAPACITY：貨艙容量，固定 3 份。
    public static final int CAPACITY = 3;

    // cargo：目前貨艙裝了幾份貨物（0～CAPACITY）。
    private int cargo;

    // 建構子：第一行交給 Rover 保存能源與樣本，再把貨艙設為空。
    public CargoRover(int energy, int samples) {
        super(energy, samples);
        cargo = 0;
    }

    // getCargo：回傳目前貨物份數。
    public int getCargo() { return cargo; }

    // load：裝貨 amount 份，回傳「成功」「已拒絕」或「任務已停止」。
    // 這是新的公開入口，不經過 execute，所以自己先檢查停止。
    // 裝貨不耗能源、不加樣本、不加 actionCount。
    public String load(int amount) {
        if (isStopped()) { return "任務已停止"; }
        // 用 amount > 剩餘貨位，避免先相加再判斷。
        if (amount <= 0 || amount > CAPACITY - cargo) { return "已拒絕"; }
        cargo += amount;
        return "成功";
    }
}
