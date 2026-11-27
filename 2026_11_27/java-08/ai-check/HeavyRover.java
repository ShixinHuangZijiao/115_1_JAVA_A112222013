// 教師設計的模擬AI回覆，刻意有錯，不是真實服務輸出。
// 宣稱：「加同名private explore，execute就會自動使用它。」
// 依講義E先預測，再獨立編譯，使用HeavyRoverTry檢查需求。
public class HeavyRover extends Rover {
    public static final int DRILL_COST = 8;
    public static final int DRILL_YIELD = 3;
    private int drillCount;

    public HeavyRover(int energy, int samples) {
        super(energy, samples);
        drillCount = 0;
    }

    public int getDrillCount() { return drillCount; }

    // [fix] 本人最小修正：刪除模擬AI的 private explore()（父類別 private 方法不被繼承、不能覆寫，
    //       Rover.execute 從不呼叫它），改成覆寫父流程會呼叫的產量方法；不改 execute、drill 與父類別。
    @Override
    protected int exploreYield() {
        return 3;
    }

    // 其餘部分保留模擬原稿，供後續最小修正比較。
    public String drill() {
        if (isStopped()) { return "任務已停止"; }
        if (!spendEnergy(DRILL_COST)) { return "能源不足"; }
        addSamples(DRILL_YIELD);
        countAction();
        drillCount++;
        return "成功";
    }
}
