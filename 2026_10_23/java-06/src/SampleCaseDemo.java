// 類別：SampleCaseDemo（第 6 週作業 E3，教材未提供，依投影片 E3 的正確輸出自行重建）
// 意義：樣本盒驗收程式。
// 作用：容量 3 的盒子依序存 2、再存 2（超量）、存 1（剛好裝滿）、卸空、空盒再卸空。
public class SampleCaseDemo {
    // main：程式進入點。每一步印出回傳值與存量，對照投影片 E3 的預期。
    public static void main(String[] args) {
        SampleCase box = new SampleCase(3);
        System.out.println("第一次=" + box.store(2) + ", 存量=" + box.getCount());
        System.out.println("超量存放=" + box.store(2) + ", 存量=" + box.getCount());
        System.out.println("剛好裝滿=" + box.store(1) + ", 存量=" + box.getCount());
        System.out.println("卸載=" + box.unload());
        System.out.println("空盒卸載=" + box.unload());
    }
}
