package save;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.WindowManager.LayoutParams;
import com.wakeup.autoclick.FloatingViewService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SaveItem {
    private Map<String, Integer> delay_target = new HashMap();

    /* renamed from: px */
    private ArrayList<Integer> f49px = new ArrayList<>();

    /* renamed from: py */
    private ArrayList<Integer> f50py = new ArrayList<>();
    private ArrayList<Integer> swipe_target = new ArrayList<>();
    private int target_num;

    public Map<String, Integer> getDelay_target() {
        return this.delay_target;
    }

    public void setDelay_target(Map<String, Integer> map) {
        this.delay_target = map;
    }

    public SaveItem() {
        if (FloatingViewService.mPointerView_auto == null) {
            FloatingViewService.mPointerView_auto = new HashMap();
        }
        setTarget_num(FloatingViewService.mPointerView_auto.size());
        setSwipe_target(FloatingViewService.swipe_target);
        for (int i = 1; i <= FloatingViewService.target_num; i++) {
            Map<String, LayoutParams> map = FloatingViewService.mParams_auto;
            StringBuilder sb = new StringBuilder();
            sb.append("mParams_auto");
            sb.append(String.valueOf(i));
            int i2 = ((LayoutParams) map.get(sb.toString())).x;
            Map<String, LayoutParams> map2 = FloatingViewService.mParams_auto;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mParams_auto");
            sb2.append(String.valueOf(i));
            int i3 = ((LayoutParams) map2.get(sb2.toString())).y;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(String.valueOf(i2));
            sb3.append(" | ");
            sb3.append(String.valueOf(i3));
            Log.d("px py", sb3.toString());
            this.f49px.add(Integer.valueOf(i2));
            this.f50py.add(Integer.valueOf(i3));
        }
        for (int i4 = 1; i4 <= FloatingViewService.target_num; i4++) {
            Map<String, Integer> map3 = this.delay_target;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("mPointer_Auto_Before");
            sb4.append(String.valueOf(i4));
            String sb5 = sb4.toString();
            SharedPreferences sharedPreferences = FloatingViewService.settings;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("mPointer_Auto_Before");
            sb6.append(String.valueOf(i4));
            map3.put(sb5, Integer.valueOf(sharedPreferences.getInt(sb6.toString(), 0)));
            Map<String, Integer> map4 = this.delay_target;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("mPointer_Auto_After");
            sb7.append(String.valueOf(i4));
            String sb8 = sb7.toString();
            SharedPreferences sharedPreferences2 = FloatingViewService.settings;
            StringBuilder sb9 = new StringBuilder();
            sb9.append("mPointer_Auto_After");
            sb9.append(String.valueOf(i4));
            map4.put(sb8, Integer.valueOf(sharedPreferences2.getInt(sb9.toString(), 0)));
        }
        setPx(this.f49px);
        setPy(this.f50py);
    }

    public ArrayList getPx() {
        return this.f49px;
    }

    public void setPx(ArrayList<Integer> arrayList) {
        this.f49px = arrayList;
    }

    public ArrayList getPy() {
        return this.f50py;
    }

    public void setPy(ArrayList<Integer> arrayList) {
        this.f50py = arrayList;
    }

    public ArrayList getSwipe_target() {
        return this.swipe_target;
    }

    public void setSwipe_target(ArrayList<Integer> arrayList) {
        this.swipe_target = arrayList;
    }

    public int getTarget_num() {
        return this.target_num;
    }

    public void setTarget_num(int i) {
        this.target_num = i;
    }
}
