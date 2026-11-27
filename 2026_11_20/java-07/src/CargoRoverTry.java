// 類別：CargoRoverTry（第 7 週課後獨立遷移的測試入口，本人作業）
// 作用：依投影片規格與本人補充的邊界案例檢查 CargoRover，實際值和預期並排印出，不同時印「不符」。
public class CargoRoverTry {
    public static void main(String[] args) {
        // 正常：存 2、再存 2（超過剩餘 1 格）、存 1 剛好裝滿、存 0。
        CargoRover c = new CargoRover(20, 0);
        check("裝 2", c.load(2) + "/" + c.getCargo(), "成功/2");
        check("再裝 2（只剩 1 格）", c.load(2) + "/" + c.getCargo(), "已拒絕/2");
        check("裝 1 剛好滿載", c.load(1) + "/" + c.getCargo(), "成功/3");
        check("裝 0", c.load(0) + "/" + c.getCargo(), "已拒絕/3");
        check("滿載後再裝 1", c.load(1) + "/" + c.getCargo(), "已拒絕/3");
        // 裝貨不耗能源、不加樣本、不加行動次數。
        check("原有三個 getter", c.getEnergy() + "/" + c.getSamples() + "/" + c.getActionCount(), "20/0/0");

        // 本人補充：負數、一次裝滿、超過總容量。
        CargoRover neg = new CargoRover(20, 0);
        check("裝 -1", neg.load(-1) + "/" + neg.getCargo(), "已拒絕/0");
        check("一次裝 3 剛好滿載", neg.load(3) + "/" + neg.getCargo(), "成功/3");
        CargoRover over = new CargoRover(20, 0);
        check("一次裝 4 超過容量", over.load(4) + "/" + over.getCargo(), "已拒絕/0");

        // 停止：能源耗盡或樣本已達標時拒絕裝貨，貨物不變。
        CargoRover empty = new CargoRover(0, 0);
        check("能源耗盡時裝貨", empty.load(1) + "/" + empty.getCargo(), "任務已停止/0");
        CargoRover done = new CargoRover(20, 5);
        check("已達標時裝貨", done.load(1) + "/" + done.getCargo(), "任務已停止/0");

        // 繼承的行動照樣可用：探索後能源、樣本、行動次數由 Rover 那一份更新，貨物不受影響。
        CargoRover mix = new CargoRover(20, 0);
        mix.load(2);
        check("裝 2 後探索", mix.execute(1) + "/" + mix.getEnergy() + "/" + mix.getSamples()
                + "/" + mix.getActionCount() + "/" + mix.getCargo(), "成功/14/2/1/2");
    }

    // check：印出實際值與預期值；相同印「相符」，不同印「不符」。
    static void check(String label, String actual, String expected) {
        String mark = actual.equals(expected) ? "相符" : "不符";
        System.out.println(label + ": " + actual + " （預期 " + expected + "） " + mark);
    }
}
