// 類別：SurveyRoverTry（第 8 週課後遷移的測試入口，本人作業）
// 作用：依投影片路線 A 的規格檢查 SurveyRover，並和一般車比較；車子一律用 Rover 參考接收、交給 Mission。
// 編譯：在 java-08 執行 javac -encoding UTF-8 -d out src/*.java transfer/*.java
//       再執行 java -cp out SurveyRoverTry
public class SurveyRoverTry {
    public static void main(String[] args) {
        // 投影片規格四個案例
        check("20/0 跑 {1,1,2}", run(new SurveyRover(20, 0), new int[] {1, 1, 2}), "6/3/3 任務未完成 3步");
        check("6/4 跑 {1,3}", run(new SurveyRover(6, 4), new int[] {1, 3}), "0/5/1 任務成功 1步");
        Rover low = new SurveyRover(5, 0);
        check("5/0 直接 execute(1)", low.execute(1) + " " + state(low), "能源不足 5/0/0");
        Rover done = new SurveyRover(20, 5);
        check("20/5 直接 execute(1)", done.execute(1) + " " + state(done), "任務已停止 20/5/0");

        // 和一般車比較：同一份腳本 {1,1,2}，一般車達標，精細採樣車三步後仍未達標（規格效果，不是錯誤）。
        check("一般車 20/0 跑 {1,1,2}", run(new Rover(20, 0), new int[] {1, 1, 2}), "6/5/3 任務成功 3步");

        // 其他指令仍用父類別規則：掃描得 1、充電 +5。
        Rover other = new SurveyRover(10, 0);
        check("掃描仍用原規則", other.execute(2) + " " + state(other), "成功 8/1/1");
        check("充電仍用原規則", other.execute(3) + " " + state(other), "成功 13/1/2");

        // 本人反例：SurveyRover 6/4 探索一次。
        // 正確產量 1 → 樣本剛好 5 份達標、能源剛好 0，兩者同時成立時以「任務成功」為準。
        // 能抓到的錯：產量方法沒被呼叫或沒覆寫（仍得 2 份 → 樣本 6，數字不同）；
        //             產量方法多計一次行動（行動次數變 2）。
        // 只看「任務成功」抓不到產量錯，所以要比對精確的「能源/樣本/行動次數」。
        check("反例 6/4 探索後狀態", run(new SurveyRover(6, 4), new int[] {1}), "0/5/1 任務成功 1步");
    }

    // run：把車交給新任務跑腳本，回傳「能源/樣本/行動次數 結果 已讀步數」。
    static String run(Rover rover, int[] script) {
        Mission mission = new Mission(rover);
        mission.run(script);
        return state(rover) + " " + mission.getOutcome() + " " + mission.getStepCount() + "步";
    }

    // state：回傳「能源/樣本/行動次數」。
    static String state(Rover rover) {
        return rover.getEnergy() + "/" + rover.getSamples() + "/" + rover.getActionCount();
    }

    // check：印出實際值與預期值；相同印「相符」，不同印「不符」。
    static void check(String label, String actual, String expected) {
        String mark = actual.equals(expected) ? "相符" : "不符";
        System.out.println(label + ": " + actual + " （預期 " + expected + "） " + mark);
    }
}
