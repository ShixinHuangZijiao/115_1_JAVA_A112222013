// 類別：SampleCase（第 6 週作業 E，教材未提供參考實作，依投影片 E1 需求自行實作）
// 意義：樣本運送盒，管理目前存量；獨立於探測車任務，不接進 Mission。
// 作用：建構時修正容量到 1～8；整批存入，放不下或非正數就拒絕且存量不變；可一次卸空。
public class SampleCase {
    // capacity：容量，建構後不變（1～8）。
    private int capacity;
    // count：目前存量（0～capacity）。外部只能透過 store、unload 改變。
    private int count;

    // 建構子：容量小於 1 設為 1、大於 8 設為 8；新盒為空。
    public SampleCase(int capacity) {
        if (capacity < 1) {
            capacity = 1;
        }
        if (capacity > 8) {
            capacity = 8;
        }
        this.capacity = capacity;
        count = 0;
    }

    public int getCapacity() { return capacity; }
    public int getCount() { return count; }
    // getFreeSpace：剩餘空間由 capacity − count 算出，不另存欄位。
    public int getFreeSpace() { return capacity - count; }

    // store：整批存入 amount 份；非正數或超過剩餘空間就拒絕、回傳 false、存量不變。
    public boolean store(int amount) {
        if (amount <= 0 || amount > getFreeSpace()) {
            return false;
        }
        count += amount;
        return true;
    }

    // unload：一次卸空，回傳卸下份數；空盒回傳 0。
    public int unload() {
        int unloaded = count;
        count = 0;
        return unloaded;
    }
}
