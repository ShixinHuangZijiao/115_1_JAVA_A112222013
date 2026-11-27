// 類別：BoundaryTry（第 8 週 D5，本人測試入口）
// 作用：用同一份 Mission 程式比較一般車與重型車的邊界案例，並放一個本人設計的反例。
// 每個案例都用新車、新任務；車子一律用 Rover 型態參考接收，不用 if 判斷車種。
// 編譯：在 java-08 執行 javac -encoding UTF-8 -d out src/*.java experiments/BoundaryTry.java
//       再執行 java -cp out BoundaryTry
public class BoundaryTry {
    public static void main(String[] args) {
        // 邊界 1：6 格、2 份，腳本 {1, 3}
        check("一般車 6/2 跑 {1,3}", run(new Rover(6, 2), new int[] {1, 3}), "0/4/1 能源耗盡 1步");
        check("重型車 6/2 跑 {1,3}", run(new HeavyRover(6, 2), new int[] {1, 3}), "0/5/1 任務成功 1步");

        // 邊界 2：5 格、0 份，腳本 {1}（能源不足 6，探索被拒絕）
        check("一般車 5/0 跑 {1}", run(new Rover(5, 0), new int[] {1}), "5/0/0 任務未完成 1步");
        check("重型車 5/0 跑 {1}", run(new HeavyRover(5, 0), new int[] {1}), "5/0/0 任務未完成 1步");

        // 本人反例：重型車 6 格、1 份探索一次。
        // 正確：耗 6、得 3（1+3=4 份，未達標），行動只計 1 次，能源 0 → 能源耗盡。
        // 能抓到的錯：
        //   (a) 產量方法裡多呼叫 countAction → 行動次數變 2；
        //   (b) TODO 1 漏做（父 explore 仍寫死 2）→ 樣本只有 3；
        //   (c) 重型車產量沒被覆寫成 3 → 樣本只有 3。
        // 選 1 份起點是為了讓結果「不達標」：若起點太高，5 份和 6 份都會顯示任務成功，抓不到產量錯誤。
        check("反例 重型車 6/1 跑 {1}", run(new HeavyRover(6, 1), new int[] {1}), "0/4/1 能源耗盡 1步");
    }

    // run：把車交給新任務跑腳本，回傳「能源/樣本/行動次數 結果 已讀步數」。
    static String run(Rover rover, int[] script) {
        Mission mission = new Mission(rover);
        mission.run(script);
        return rover.getEnergy() + "/" + rover.getSamples() + "/" + rover.getActionCount()
                + " " + mission.getOutcome() + " " + mission.getStepCount() + "步";
    }

    // check：印出實際值與預期值；相同印「相符」，不同印「不符」。
    static void check(String label, String actual, String expected) {
        String mark = actual.equals(expected) ? "相符" : "不符";
        System.out.println(label + ": " + actual + " （預期 " + expected + "） " + mark);
    }
}
