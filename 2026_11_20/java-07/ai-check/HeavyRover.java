// 教師設計的模擬 AI 錯誤回覆，並非真實對話，也不是參考答案。
// 與完成的 Rover、HeavyRoverTry 放在獨立 ai-check 內編譯，不能和正式版混編。
// [fix] 本人最小修正：刪掉重複宣告的 energy、samples 與建構子裡的設定，
//       drill 改用 Rover 的 isStopped、spendEnergy、addSamples、countAction，讓狀態只有 Rover 那一份。
public class HeavyRover extends Rover {
    private int drillCount;

    public HeavyRover(int energy, int samples) {
        super(energy, samples);
        drillCount = 0;
    }

    public int getDrillCount() { return drillCount; }

    public String drill() {
        if (isStopped()) { return "任務已停止"; }
        if (!spendEnergy(8)) { return "能源不足"; }
        addSamples(3);
        countAction();
        drillCount++;
        return "成功";
    }
}
