// 類別：SurveyRover（第 8 週課後遷移，路線 A，本人作業）
// 意義：精細採樣車，是一種探測車（extends Rover）。探索一樣耗 6 格能源，但每次只得 1 份樣本。
// 作用：只覆寫父類別的 exploreYield()，其餘探索流程（扣能源、停止檢查、計次）全部沿用 Rover；
//       不重寫 execute、不重複宣告能源與樣本。
public class SurveyRover extends Rover {
    // 建構子：能源與樣本交給父類別 Rover 保存。
    public SurveyRover(int energy, int samples) {
        super(energy, samples);
    }

    // exploreYield：精細採樣，一次只得 1 份。只回傳產量，不扣能源、不加樣本、不計次。
    @Override
    protected int exploreYield() {
        return 1;
    }
}
