package servicecontrol;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.wakeup.autoclick.FloatingViewService;
import com.wakeup.autoclick.R;
import com.wakeup.autoclick.ViewPointerAuto;
import com.wakeup.autoclick.ViewPointerAutoSwipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import save.SaveItem;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;

public class Configuration  extends AppCompatActivity {
    static Context context;
    private LayoutInflater inflater;
    public ArrayList<Integer> item_px = new ArrayList<>();
    public ArrayList<Integer> item_py = new ArrayList<>();

    /* renamed from: l1 */
    private Button f57l1;
    private Button l10;

    /* renamed from: l2 */
    private Button f58l2;

    /* renamed from: l3 */
    private Button f59l3;

    /* renamed from: l4 */
    private Button f60l4;

    /* renamed from: l5 */
    private Button f61l5;

    /* renamed from: l6 */
    private Button f62l6;

    /* renamed from: l7 */
    private Button f63l7;

    /* renamed from: l8 */
    private Button f64l8;

    /* renamed from: l9 */
    private Button f65l9;
    private boolean loadingAd_flag;
    private RelativeLayout mAssistiveView;
    private ShareActionProvider mShareActionProvider;
    public WindowManager mWindowManager;

    /* renamed from: s1 */
    private Button f66s1;
    private Button s10;

    /* renamed from: s2 */
    private Button f67s2;

    /* renamed from: s3 */
    private Button f68s3;

    /* renamed from: s4 */
    private Button f69s4;

    /* renamed from: s5 */
    private Button f70s5;

    /* renamed from: s6 */
    private Button f71s6;

    /* renamed from: s7 */
    private Button f72s7;

    /* renamed from: s8 */
    private Button f73s8;

    /* renamed from: s9 */
    private Button f74s9;

    public Configuration() {
    }

    public Configuration(Context context2) {
        context = context2;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        getActionBar();
        this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mAssistiveView = (RelativeLayout) this.inflater.inflate(R.layout.activity_setting, null);
        this.mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        widget();
    }

    private void widget() {
        this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.f66s1 = (Button) findViewById(R.id.save1);
        this.f67s2 = (Button) findViewById(R.id.save2);
        this.f68s3 = (Button) findViewById(R.id.save3);
        this.f69s4 = (Button) findViewById(R.id.save4);
        this.f70s5 = (Button) findViewById(R.id.save5);
        this.f71s6 = (Button) findViewById(R.id.save6);
        this.f72s7 = (Button) findViewById(R.id.save7);
        this.f73s8 = (Button) findViewById(R.id.save8);
        this.f74s9 = (Button) findViewById(R.id.save9);
        this.s10 = (Button) findViewById(R.id.save10);
        this.f57l1 = (Button) findViewById(R.id.load1);
        this.f58l2 = (Button) findViewById(R.id.load2);
        this.f59l3 = (Button) findViewById(R.id.load3);
        this.f60l4 = (Button) findViewById(R.id.load4);
        this.f61l5 = (Button) findViewById(R.id.load5);
        this.f62l6 = (Button) findViewById(R.id.load6);
        this.f63l7 = (Button) findViewById(R.id.load7);
        this.f64l8 = (Button) findViewById(R.id.load8);
        this.f65l9 = (Button) findViewById(R.id.load9);
        this.l10 = (Button) findViewById(R.id.load10);
        this.f66s1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot1", Toast.LENGTH_SHORT).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save1", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), Toast.LENGTH_SHORT).show();
            }
        });
        this.f57l1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save1", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        Toast.makeText(Configuration.this.getApplicationContext(), "Loaded Slot1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(Configuration.this.getApplicationContext(), "Empty Slot1", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), Toast.LENGTH_SHORT).show();
            }
        });
        this.f67s2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot2", Toast.LENGTH_SHORT).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save2", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), Toast.LENGTH_SHORT).show();
            }
        });
        this.f58l2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save2", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        Toast.makeText(Configuration.this.getApplicationContext(), "Loaded Slot2", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(Configuration.this.getApplicationContext(), "Empty Slot2", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f68s3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot3", 0).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save3", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f59l3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save3", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        Toast.makeText(Configuration.this.getApplicationContext(), "Loaded Slot3", 0).show();
                        return;
                    }
                    Toast.makeText(Configuration.this.getApplicationContext(), "Empty Slot3", 0).show();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f69s4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot4", 0).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save4", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f60l4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save4", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        Toast.makeText(Configuration.this.getApplicationContext(), "Loaded Slot4", 0).show();
                        return;
                    }
                    Toast.makeText(Configuration.this.getApplicationContext(), "Empty Slot4", 0).show();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f70s5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot5", 0).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save5", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f61l5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save5", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        Toast.makeText(Configuration.this.getApplicationContext(), "Loaded Slot5", 0).show();
                        return;
                    }
                    Toast.makeText(Configuration.this.getApplicationContext(), "Empty Slot5", 0).show();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f71s6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot6", 0).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save6", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f62l6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save6", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        Toast.makeText(Configuration.this.getApplicationContext(), "Loaded Slot6", 0).show();
                        return;
                    }
                    Toast.makeText(Configuration.this.getApplicationContext(), "Empty Slot6", 0).show();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f72s7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot7", 0).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save7", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f63l7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save7", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        Toast.makeText(Configuration.this.getApplicationContext(), "Loaded Slot7", 0).show();
                        return;
                    }
                    Toast.makeText(Configuration.this.getApplicationContext(), "Empty Slot7", 0).show();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f73s8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot8", 0).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save8", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f64l8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save8", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        Toast.makeText(Configuration.this.getApplicationContext(), "Loaded Slot8", 0).show();
                        return;
                    }
                    Toast.makeText(Configuration.this.getApplicationContext(), "Empty Slot8", 0).show();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f74s9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot9", 0).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save9", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.f65l9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save9", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        Toast.makeText(Configuration.this.getApplicationContext(), "Loaded Slot9", 0).show();
                        return;
                    }
                    Toast.makeText(Configuration.this.getApplicationContext(), "Empty Slot9", 0).show();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.s10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    Toast.makeText(Configuration.this.getApplicationContext(), "Saved Slot10", 0).show();
                    SaveItem saveItem = new SaveItem();
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putString("save10", new Gson().toJson((Object) saveItem));
                    edit.commit();
                    return;
                }
                Toast.makeText(Configuration.this.getApplicationContext(), Configuration.this.getResources().getText(R.string.purchase_yet), 0).show();
            }
        });
        this.l10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FloatingViewService.purchased) {
                    String string = FloatingViewService.settings.getString("save10", "");
                    if (!string.equals("")) {
                        System.out.print(string);
                        SaveItem saveItem = (SaveItem) new Gson().fromJson(string, SaveItem.class);
                        Configuration.this.remvoe_PointerViewAll();
                        FloatingViewService.target_num = 0;
                        FloatingViewService.swipe_target = saveItem.getSwipe_target();
                        Configuration.this.put_PointerViewAuto(saveItem.getPx(), saveItem.getPy());
                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                        for (int i = 1; i < 48; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("mPointer_Auto_Before");
                            sb.append(String.valueOf(i));
                            edit.putInt(sb.toString(), 0);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("mPointer_Auto_After");
                            sb2.append(String.valueOf(i));
                            edit.putInt(sb2.toString(), 0);
                        }
                        edit.apply();
                        for (int i2 = 1; i2 <= saveItem.getPx().size(); i2++) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointer_Auto_Before");
                            sb3.append(String.valueOf(i2));
                            String sb4 = sb3.toString();
                            Map delay_target = saveItem.getDelay_target();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("mPointer_Auto_Before");
                            sb5.append(String.valueOf(i2));
                            edit.putInt(sb4, ((Integer) delay_target.get(sb5.toString())).intValue());
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("mPointer_Auto_After");
                            sb6.append(String.valueOf(i2));
                            String sb7 = sb6.toString();
                            Map delay_target2 = saveItem.getDelay_target();
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("mPointer_Auto_After");
                            sb8.append(String.valueOf(i2));
                            edit.putInt(sb7, ((Integer) delay_target2.get(sb8.toString())).intValue());
                        }
                        edit.apply();
                        return;
                    }
                    return;
                }
            }
        });
    }


    public void put_PointerViewAuto(ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2) {
        int i;
        ArrayList<Integer> arrayList3 = arrayList;
        ArrayList<Integer> arrayList4 = arrayList2;

        int i2 = TYPE_APPLICATION_OVERLAY;
        int i3 = Build.VERSION.SDK_INT >= 26 ? TYPE_APPLICATION_OVERLAY : TYPE_SYSTEM_ALERT;
        if (Build.VERSION.SDK_INT < 26) {
            i2 = TYPE_SYSTEM_OVERLAY;
        }
        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
        boolean z = true;
        int i4 = 1;
        while (i4 <= arrayList.size()) {
            Log.d("swipe size", String.valueOf(FloatingViewService.swipe_target.size()));
            boolean z2 = false;
            for (int i5 = 0; i5 < FloatingViewService.swipe_target.size(); i5++) {
                StringBuilder sb = new StringBuilder();
                sb.append(String.valueOf(i4));
                sb.append(", ");
                sb.append(String.valueOf(FloatingViewService.swipe_target.get(i5)));
                Log.d("i / swipe_target num", sb.toString());
                if (i4 == ((Integer) FloatingViewService.swipe_target.get(i5)).intValue()) {
                    z2 = z;
                }
            }
            Log.d("swipe flag", "true");
            if (z2 == z) {
                i = i4 + 1;
                FloatingViewService.target_num += z ? 1 : 0;
                Map<String, ViewPointerAuto> map = FloatingViewService.mPointerView_auto;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("mPointerView_auto");
                sb2.append(String.valueOf(FloatingViewService.target_num));
                map.put(sb2.toString(), new ViewPointerAuto(getApplicationContext()));
                Map<String, ViewPointerAuto> map2 = FloatingViewService.mPointerView_auto;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("mPointerView_auto");
                sb3.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map2.get(sb3.toString())).setClickable(z);
                Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("mPointerView_auto");
                sb4.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map3.get(sb4.toString())).setFocusable(z);
                Map<String, LayoutParams> map4 = FloatingViewService.mParams_auto;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("mParams_auto");
                sb5.append(String.valueOf(FloatingViewService.target_num));
                LayoutParams layoutParams = null;
                String sb6 = sb5.toString();
                LayoutParams layoutParams2 = new LayoutParams(-2, -2, i3, 8, -3);
                map4.put(sb6, layoutParams2);
                Map<String, LayoutParams> map5 = FloatingViewService.mParams_auto;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("mParams_auto");
                sb7.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map5.get(sb7.toString())).gravity = 51;
                Map<String, LayoutParams> map6 = FloatingViewService.mParams_auto;
                StringBuilder sb8 = new StringBuilder();
                sb8.append("mParams_auto");
                sb8.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map6.get(sb8.toString())).x = ((Integer) arrayList3.get(FloatingViewService.target_num - 1)).intValue();
                Map<String, LayoutParams> map7 = FloatingViewService.mParams_auto;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("mParams_auto");
                sb9.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map7.get(sb9.toString())).y = ((Integer) arrayList4.get(FloatingViewService.target_num - 1)).intValue();
                StringBuilder sb10 = new StringBuilder();
                sb10.append("@drawable/sarget");
                sb10.append(String.valueOf(FloatingViewService.target_num));
                final int identifier = getResources().getIdentifier(sb10.toString(), null, getPackageName());
                Map<String, ViewPointerAuto> map8 = FloatingViewService.mPointerView_auto;
                StringBuilder sb11 = new StringBuilder();
                sb11.append("mPointerView_auto");
                sb11.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map8.get(sb11.toString())).setBackgroundResource(identifier);
                Map<String, Float> map9 = FloatingViewService.px_auto;
                StringBuilder sb12 = new StringBuilder();
                sb12.append("px_auto_s");
                sb12.append(String.valueOf(FloatingViewService.target_num));
                map9.put(sb12.toString(), Float.valueOf(0.0f));
                Map<String, Float> map10 = FloatingViewService.py_auto;
                StringBuilder sb13 = new StringBuilder();
                sb13.append("py_auto_s");
                sb13.append(String.valueOf(FloatingViewService.target_num));
                map10.put(sb13.toString(), Float.valueOf(0.0f));
                Map<String, ViewPointerAutoSwipe> map11 = FloatingViewService.mPointerView_auto_swipe;
                StringBuilder sb14 = new StringBuilder();
                sb14.append("mPointerView_auto_s");
                sb14.append(String.valueOf(FloatingViewService.target_num));
                map11.put(sb14.toString(), new ViewPointerAutoSwipe(getApplicationContext()));
                Map<String, ViewPointerAutoSwipe> map12 = FloatingViewService.mPointerView_auto_swipe;
                StringBuilder sb15 = new StringBuilder();
                sb15.append("mPointerView_auto_s");
                sb15.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAutoSwipe) map12.get(sb15.toString())).setClickable(true);
                Map<String, ViewPointerAutoSwipe> map13 = FloatingViewService.mPointerView_auto_swipe;
                StringBuilder sb16 = new StringBuilder();
                sb16.append("mPointerView_auto_s");
                sb16.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAutoSwipe) map13.get(sb16.toString())).setFocusable(true);
                Map<String, LayoutParams> map14 = FloatingViewService.mParams_auto;
                StringBuilder sb17 = new StringBuilder();
                sb17.append("mParams_auto_s");
                sb17.append(String.valueOf(FloatingViewService.target_num));
                LayoutParams layoutParams3 = null;
                String sb18 = sb17.toString();
                LayoutParams layoutParams4 = new LayoutParams(-1, -1, i2, 262456, -3);
                map14.put(sb18, layoutParams4);
                Map<String, LayoutParams> map15 = FloatingViewService.mParams_auto;
                StringBuilder sb19 = new StringBuilder();
                sb19.append("mParams_auto_s");
                sb19.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map15.get(sb19.toString())).gravity = 51;
                Map<String, LayoutParams> map16 = FloatingViewService.mParams_auto;
                StringBuilder sb20 = new StringBuilder();
                sb20.append("mParams_auto_s");
                sb20.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map16.get(sb20.toString())).x = ((Integer) arrayList3.get(FloatingViewService.target_num - 1)).intValue();
                Map<String, LayoutParams> map17 = FloatingViewService.mParams_auto;
                StringBuilder sb21 = new StringBuilder();
                sb21.append("mParams_auto_s");
                sb21.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map17.get(sb21.toString())).y = ((Integer) arrayList4.get(FloatingViewService.target_num - 1)).intValue();
                Map<String, ViewPointerAutoSwipe> map18 = FloatingViewService.mPointerView_auto_swipe;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("mPointerView_auto_s");
                sb22.append(String.valueOf(FloatingViewService.target_num));
                ViewPointerAutoSwipe viewPointerAutoSwipe = (ViewPointerAutoSwipe) map18.get(sb22.toString());
                ViewPointerAutoSwipe.draw_flag = true;
                WindowManager windowManager = this.mWindowManager;
                Map<String, ViewPointerAutoSwipe> map19 = FloatingViewService.mPointerView_auto_swipe;
                StringBuilder sb23 = new StringBuilder();
                sb23.append("mPointerView_auto_s");
                sb23.append(String.valueOf(FloatingViewService.target_num));
                View view = (View) map19.get(sb23.toString());
                Map<String, LayoutParams> map20 = FloatingViewService.mParams_auto;
                StringBuilder sb24 = new StringBuilder();
                sb24.append("mParams_auto_s");
                sb24.append(String.valueOf(FloatingViewService.target_num));
                windowManager.addView(view, (ViewGroup.LayoutParams) map20.get(sb24.toString()));
                Log.d("adjust x", String.valueOf(FloatingViewService.deviceHeight / 15));
                Log.d("adjust y", String.valueOf(FloatingViewService.deviceHeight / 15));
                Map<String, ViewPointerAutoSwipe> map21 = FloatingViewService.mPointerView_auto_swipe;
                StringBuilder sb25 = new StringBuilder();
                sb25.append("mPointerView_auto_s");
                sb25.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAutoSwipe) map21.get(sb25.toString())).assign(((Integer) arrayList3.get(FloatingViewService.target_num - 1)).intValue() + ((FloatingViewService.deviceHeight / 15) / 2), ((Integer) arrayList4.get(FloatingViewService.target_num - 1)).intValue() + (FloatingViewService.deviceHeight / 15), ((Integer) arrayList3.get(FloatingViewService.target_num)).intValue() + ((FloatingViewService.deviceHeight / 15) / 2), ((Integer) arrayList4.get(FloatingViewService.target_num)).intValue() + (FloatingViewService.deviceHeight / 15));
                Map<String, ViewPointerAutoSwipe> map22 = FloatingViewService.mPointerView_auto_swipe;
                StringBuilder sb26 = new StringBuilder();
                sb26.append("mPointerView_auto_s");
                sb26.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAutoSwipe) map22.get(sb26.toString())).invalidate();
                WindowManager windowManager2 = this.mWindowManager;
                Map<String, ViewPointerAutoSwipe> map23 = FloatingViewService.mPointerView_auto_swipe;
                StringBuilder sb27 = new StringBuilder();
                sb27.append("mPointerView_auto_s");
                sb27.append(FloatingViewService.target_num);
                View view2 = (View) map23.get(sb27.toString());
                Map<String, LayoutParams> map24 = FloatingViewService.mParams_auto;
                StringBuilder sb28 = new StringBuilder();
                sb28.append("mParams_auto_s");
                sb28.append(FloatingViewService.target_num);
                windowManager2.updateViewLayout(view2, (ViewGroup.LayoutParams) map24.get(sb28.toString()));
                Map<String, ViewPointerAuto> map25 = FloatingViewService.mPointerView_auto;
                StringBuilder sb29 = new StringBuilder();
                sb29.append("mPointerView_auto");
                sb29.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map25.get(sb29.toString())).setOnTouchListener(new View.OnTouchListener() {
                    private float initialPointX;
                    private float initialPointY;
                    private int initialX;
                    private int initialY;
                    private String touch_target_num;
                    private int vectorX;
                    private int vectorY;

                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        try {
                            switch (motionEvent.getAction()) {
                                case 0:
                                    String resourceEntryName = view.getResources().getResourceEntryName(identifier);
                                    if (resourceEntryName.length() > 7) {
                                        this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 2, resourceEntryName.length());
                                        Log.d("target name : ", resourceEntryName);
                                        Log.d("target sub name : ", this.touch_target_num);
                                    } else {
                                        this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 1, resourceEntryName.length());
                                        Log.d("target name : ", resourceEntryName);
                                        Log.d("target sub name : ", this.touch_target_num);
                                    }
                                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                                    Map<String, LayoutParams> map = FloatingViewService.mParams_auto;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("mParams_auto");
                                    sb.append(this.touch_target_num);
                                    edit.putInt("initialX", ((LayoutParams) map.get(sb.toString())).x);
                                    Map<String, LayoutParams> map2 = FloatingViewService.mParams_auto;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("mParams_auto");
                                    sb2.append(this.touch_target_num);
                                    edit.putInt("initialY", ((LayoutParams) map2.get(sb2.toString())).y);
                                    edit.apply();
                                    Map<String, LayoutParams> map3 = FloatingViewService.mParams_auto;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("mParams_auto");
                                    sb3.append(this.touch_target_num);
                                    this.initialX = ((LayoutParams) map3.get(sb3.toString())).x;
                                    Map<String, LayoutParams> map4 = FloatingViewService.mParams_auto;
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("mParams_auto");
                                    sb4.append(this.touch_target_num);
                                    this.initialY = ((LayoutParams) map4.get(sb4.toString())).y;
                                    this.initialPointX = motionEvent.getRawX();
                                    this.initialPointY = motionEvent.getRawY();
                                    Map<String, Float> map5 = FloatingViewService.px_auto;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("px_auto");
                                    sb5.append(this.touch_target_num);
                                    String sb6 = sb5.toString();
                                    Map<String, LayoutParams> map6 = FloatingViewService.mParams_auto;
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append("mParams_auto");
                                    sb7.append(this.touch_target_num);
                                    map5.put(sb6, Float.valueOf((float) ((LayoutParams) map6.get(sb7.toString())).x));
                                    Map<String, Float> map7 = FloatingViewService.py_auto;
                                    StringBuilder sb8 = new StringBuilder();
                                    sb8.append("py_auto");
                                    sb8.append(this.touch_target_num);
                                    String sb9 = sb8.toString();
                                    Map<String, LayoutParams> map8 = FloatingViewService.mParams_auto;
                                    StringBuilder sb10 = new StringBuilder();
                                    sb10.append("mParams_auto");
                                    sb10.append(this.touch_target_num);
                                    map7.put(sb9, Float.valueOf((float) ((LayoutParams) map8.get(sb10.toString())).y));
                                    break;
                                case 1:
                                    StringBuilder sb11 = new StringBuilder();
                                    sb11.append(this.touch_target_num);
                                    sb11.append(" ");
                                    sb11.append(this.touch_target_num);
                                    Log.d("action_up", sb11.toString());
                                    Map<String, Float> map9 = FloatingViewService.px_auto;
                                    StringBuilder sb12 = new StringBuilder();
                                    sb12.append("px_auto");
                                    sb12.append(this.touch_target_num);
                                    String sb13 = sb12.toString();
                                    Map<String, LayoutParams> map10 = FloatingViewService.mParams_auto;
                                    StringBuilder sb14 = new StringBuilder();
                                    sb14.append("mParams_auto");
                                    sb14.append(this.touch_target_num);
                                    map9.put(sb13, Float.valueOf((float) ((LayoutParams) map10.get(sb14.toString())).x));
                                    Map<String, Float> map11 = FloatingViewService.py_auto;
                                    StringBuilder sb15 = new StringBuilder();
                                    sb15.append("py_auto");
                                    sb15.append(this.touch_target_num);
                                    String sb16 = sb15.toString();
                                    Map<String, LayoutParams> map12 = FloatingViewService.mParams_auto;
                                    StringBuilder sb17 = new StringBuilder();
                                    sb17.append("mParams_auto");
                                    sb17.append(this.touch_target_num);
                                    map11.put(sb16, Float.valueOf((float) ((LayoutParams) map12.get(sb17.toString())).y));
                                    int rawX = (int) (motionEvent.getRawX() - this.initialPointX);
                                    int rawY = (int) (motionEvent.getRawY() - this.initialPointY);
                                    if (Math.abs(rawX) < 5 && Math.abs(rawY) < 5 && !FloatingViewService.auto_click) {
                                        Intent intent = new Intent(Configuration.this.getApplicationContext(), DelayActivity.class);
                                        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("target_number", this.touch_target_num);
                                        intent.putExtras(bundle);
                                        Configuration.this.startActivity(intent);
                                        break;
                                    }
                                case 2:
                                    StringBuilder sb18 = new StringBuilder();
                                    sb18.append(this.touch_target_num);
                                    sb18.append(" ");
                                    sb18.append(this.touch_target_num);
                                    Log.d("action_move", sb18.toString());
                                    this.vectorX = (int) (motionEvent.getRawX() - this.initialPointX);
                                    this.vectorY = (int) (motionEvent.getRawY() - this.initialPointY);
                                    Map<String, LayoutParams> map13 = FloatingViewService.mParams_auto;
                                    StringBuilder sb19 = new StringBuilder();
                                    sb19.append("mParams_auto");
                                    sb19.append(this.touch_target_num);
                                    ((LayoutParams) map13.get(sb19.toString())).x = (int) (((float) this.initialX) + (((float) this.vectorX) * 1.0f));
                                    Map<String, LayoutParams> map14 = FloatingViewService.mParams_auto;
                                    StringBuilder sb20 = new StringBuilder();
                                    sb20.append("mParams_auto");
                                    sb20.append(this.touch_target_num);
                                    ((LayoutParams) map14.get(sb20.toString())).y = (int) (((float) this.initialY) + (((float) this.vectorY) * 1.0f));
                                    WindowManager windowManager = Configuration.this.mWindowManager;
                                    Map<String, ViewPointerAuto> map15 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb21 = new StringBuilder();
                                    sb21.append("mPointerView_auto");
                                    sb21.append(this.touch_target_num);
                                    View view2 = (View) map15.get(sb21.toString());
                                    Map<String, LayoutParams> map16 = FloatingViewService.mParams_auto;
                                    StringBuilder sb22 = new StringBuilder();
                                    sb22.append("mParams_auto");
                                    sb22.append(this.touch_target_num);
                                    windowManager.updateViewLayout(view2, (ViewGroup.LayoutParams) map16.get(sb22.toString()));
                                    Map<String, ViewPointerAutoSwipe> map17 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb23 = new StringBuilder();
                                    sb23.append("mPointerView_auto_s");
                                    sb23.append(String.valueOf(this.touch_target_num));
                                    ViewPointerAutoSwipe viewPointerAutoSwipe = (ViewPointerAutoSwipe) map17.get(sb23.toString());
                                    ViewPointerAutoSwipe.draw_flag = true;
                                    Map<String, ViewPointerAutoSwipe> map18 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb24 = new StringBuilder();
                                    sb24.append("mPointerView_auto_s");
                                    sb24.append(String.valueOf(this.touch_target_num));
                                    ViewPointerAutoSwipe viewPointerAutoSwipe2 = (ViewPointerAutoSwipe) map18.get(sb24.toString());
                                    Map<String, LayoutParams> map19 = FloatingViewService.mParams_auto;
                                    StringBuilder sb25 = new StringBuilder();
                                    sb25.append("mParams_auto");
                                    sb25.append(this.touch_target_num);
                                    int i = ((LayoutParams) map19.get(sb25.toString())).x;
                                    Map<String, ViewPointerAuto> map20 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb26 = new StringBuilder();
                                    sb26.append("mPointerView_auto");
                                    sb26.append(String.valueOf(this.touch_target_num));
                                    int width = i + (((ViewPointerAuto) map20.get(sb26.toString())).getWidth() / 2);
                                    Map<String, LayoutParams> map21 = FloatingViewService.mParams_auto;
                                    StringBuilder sb27 = new StringBuilder();
                                    sb27.append("mParams_auto");
                                    sb27.append(this.touch_target_num);
                                    int i2 = ((LayoutParams) map21.get(sb27.toString())).y;
                                    Map<String, ViewPointerAuto> map22 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb28 = new StringBuilder();
                                    sb28.append("mPointerView_auto");
                                    sb28.append(String.valueOf(this.touch_target_num));
                                    int width2 = i2 + ((ViewPointerAuto) map22.get(sb28.toString())).getWidth();
                                    Map<String, LayoutParams> map23 = FloatingViewService.mParams_auto;
                                    StringBuilder sb29 = new StringBuilder();
                                    sb29.append("mParams_auto");
                                    sb29.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() + 1));
                                    int i3 = ((LayoutParams) map23.get(sb29.toString())).x;
                                    Map<String, ViewPointerAuto> map24 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb30 = new StringBuilder();
                                    sb30.append("mPointerView_auto");
                                    sb30.append(String.valueOf(this.touch_target_num));
                                    int width3 = i3 + (((ViewPointerAuto) map24.get(sb30.toString())).getWidth() / 2);
                                    Map<String, LayoutParams> map25 = FloatingViewService.mParams_auto;
                                    StringBuilder sb31 = new StringBuilder();
                                    sb31.append("mParams_auto");
                                    sb31.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() + 1));
                                    int i4 = ((LayoutParams) map25.get(sb31.toString())).y;
                                    Map<String, ViewPointerAuto> map26 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb32 = new StringBuilder();
                                    sb32.append("mPointerView_auto");
                                    sb32.append(String.valueOf(this.touch_target_num));
                                    viewPointerAutoSwipe2.assign(width, width2, width3, i4 + ((ViewPointerAuto) map26.get(sb32.toString())).getWidth());
                                    Map<String, ViewPointerAutoSwipe> map27 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb33 = new StringBuilder();
                                    sb33.append("mPointerView_auto_s");
                                    sb33.append(String.valueOf(this.touch_target_num));
                                    ((ViewPointerAutoSwipe) map27.get(sb33.toString())).invalidate();
                                    WindowManager windowManager2 = Configuration.this.mWindowManager;
                                    Map<String, ViewPointerAutoSwipe> map28 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb34 = new StringBuilder();
                                    sb34.append("mPointerView_auto_s");
                                    sb34.append(this.touch_target_num);
                                    View view3 = (View) map28.get(sb34.toString());
                                    Map<String, LayoutParams> map29 = FloatingViewService.mParams_auto;
                                    StringBuilder sb35 = new StringBuilder();
                                    sb35.append("mParams_auto_s");
                                    sb35.append(this.touch_target_num);
                                    windowManager2.updateViewLayout(view3, (ViewGroup.LayoutParams) map29.get(sb35.toString()));
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
                Map<String, ViewPointerAuto> map26 = FloatingViewService.mPointerView_auto;
                StringBuilder sb30 = new StringBuilder();
                sb30.append("mPointerView_auto");
                sb30.append(String.valueOf(FloatingViewService.target_num));
                if (map26.get(sb30.toString()) != null) {
                    Map<String, ViewPointerAuto> map27 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb31 = new StringBuilder();
                    sb31.append("mPointerView_auto");
                    sb31.append(String.valueOf(FloatingViewService.target_num));
                    if (!((ViewPointerAuto) map27.get(sb31.toString())).isShown()) {
                        WindowManager windowManager3 = this.mWindowManager;
                        Map<String, ViewPointerAuto> map28 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb32 = new StringBuilder();
                        sb32.append("mPointerView_auto");
                        sb32.append(String.valueOf(FloatingViewService.target_num));
                        View view3 = (View) map28.get(sb32.toString());
                        Map<String, LayoutParams> map29 = FloatingViewService.mParams_auto;
                        StringBuilder sb33 = new StringBuilder();
                        sb33.append("mParams_auto");
                        sb33.append(String.valueOf(FloatingViewService.target_num));
                        windowManager3.addView(view3, (ViewGroup.LayoutParams) map29.get(sb33.toString()));
                    }
                }
                edit.putInt("auto_target", FloatingViewService.target_num);
                edit.apply();
                Log.d("add_btn target_num : ", String.valueOf(FloatingViewService.target_num));
                FloatingViewService.target_num++;
                Map<String, Float> map30 = FloatingViewService.px_auto;
                StringBuilder sb34 = new StringBuilder();
                sb34.append("px_auto");
                sb34.append(String.valueOf(FloatingViewService.target_num));
                map30.put(sb34.toString(), Float.valueOf(0.0f));
                Map<String, Float> map31 = FloatingViewService.py_auto;
                StringBuilder sb35 = new StringBuilder();
                sb35.append("py_auto");
                sb35.append(String.valueOf(FloatingViewService.target_num));
                map31.put(sb35.toString(), Float.valueOf(0.0f));
                Map<String, ViewPointerAuto> map32 = FloatingViewService.mPointerView_auto;
                StringBuilder sb36 = new StringBuilder();
                sb36.append("mPointerView_auto");
                sb36.append(String.valueOf(FloatingViewService.target_num));
                map32.put(sb36.toString(), new ViewPointerAuto(getApplicationContext()));
                Map<String, ViewPointerAuto> map33 = FloatingViewService.mPointerView_auto;
                StringBuilder sb37 = new StringBuilder();
                sb37.append("mPointerView_auto");
                sb37.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map33.get(sb37.toString())).setClickable(true);
                Map<String, ViewPointerAuto> map34 = FloatingViewService.mPointerView_auto;
                StringBuilder sb38 = new StringBuilder();
                sb38.append("mPointerView_auto");
                sb38.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map34.get(sb38.toString())).setFocusable(true);
                Map<String, LayoutParams> map35 = FloatingViewService.mParams_auto;
                StringBuilder sb39 = new StringBuilder();
                sb39.append("mParams_auto");
                sb39.append(String.valueOf(FloatingViewService.target_num));
                String sb40 = sb39.toString();
                LayoutParams layoutParams5 = new LayoutParams(-2, -2, i3, 8, -3);
                map35.put(sb40, layoutParams5);
                Map<String, LayoutParams> map36 = FloatingViewService.mParams_auto;
                StringBuilder sb41 = new StringBuilder();
                sb41.append("mParams_auto");
                sb41.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map36.get(sb41.toString())).gravity = 51;
                Map<String, LayoutParams> map37 = FloatingViewService.mParams_auto;
                StringBuilder sb42 = new StringBuilder();
                sb42.append("mParams_auto");
                sb42.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map37.get(sb42.toString())).x = ((Integer) arrayList3.get(FloatingViewService.target_num - 1)).intValue();
                Map<String, LayoutParams> map38 = FloatingViewService.mParams_auto;
                StringBuilder sb43 = new StringBuilder();
                sb43.append("mParams_auto");
                sb43.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map38.get(sb43.toString())).y = ((Integer) arrayList4.get(FloatingViewService.target_num - 1)).intValue();
                StringBuilder sb44 = new StringBuilder();
                sb44.append("@drawable/sarget");
                sb44.append(String.valueOf(FloatingViewService.target_num));
                final int identifier2 = getResources().getIdentifier(sb44.toString(), null, getPackageName());
                Map<String, ViewPointerAuto> map39 = FloatingViewService.mPointerView_auto;
                StringBuilder sb45 = new StringBuilder();
                sb45.append("mPointerView_auto");
                sb45.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map39.get(sb45.toString())).setBackgroundResource(identifier2);
                Map<String, ViewPointerAuto> map40 = FloatingViewService.mPointerView_auto;
                StringBuilder sb46 = new StringBuilder();
                sb46.append("mPointerView_auto");
                sb46.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map40.get(sb46.toString())).setOnTouchListener(new View.OnTouchListener() {
                    private float initialPointX;
                    private float initialPointY;
                    private int initialX;
                    private int initialY;
                    private String touch_target_num;
                    private int vectorX;
                    private int vectorY;

                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        try {
                            switch (motionEvent.getAction()) {
                                case 0:
                                    String resourceEntryName = view.getResources().getResourceEntryName(identifier2);
                                    if (resourceEntryName.length() > 7) {
                                        this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 2, resourceEntryName.length());
                                        Log.d("target name : ", resourceEntryName);
                                        Log.d("target sub name : ", this.touch_target_num);
                                    } else {
                                        this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 1, resourceEntryName.length());
                                        Log.d("target name : ", resourceEntryName);
                                        Log.d("target sub name : ", this.touch_target_num);
                                    }
                                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                                    Map<String, LayoutParams> map = FloatingViewService.mParams_auto;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("mParams_auto");
                                    sb.append(this.touch_target_num);
                                    edit.putInt("initialX", ((LayoutParams) map.get(sb.toString())).x);
                                    Map<String, LayoutParams> map2 = FloatingViewService.mParams_auto;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("mParams_auto");
                                    sb2.append(this.touch_target_num);
                                    edit.putInt("initialY", ((LayoutParams) map2.get(sb2.toString())).y);
                                    edit.apply();
                                    Map<String, LayoutParams> map3 = FloatingViewService.mParams_auto;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("mParams_auto");
                                    sb3.append(this.touch_target_num);
                                    this.initialX = ((LayoutParams) map3.get(sb3.toString())).x;
                                    Map<String, LayoutParams> map4 = FloatingViewService.mParams_auto;
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("mParams_auto");
                                    sb4.append(this.touch_target_num);
                                    this.initialY = ((LayoutParams) map4.get(sb4.toString())).y;
                                    this.initialPointX = motionEvent.getRawX();
                                    this.initialPointY = motionEvent.getRawY();
                                    Map<String, Float> map5 = FloatingViewService.px_auto;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("px_auto");
                                    sb5.append(this.touch_target_num);
                                    String sb6 = sb5.toString();
                                    Map<String, LayoutParams> map6 = FloatingViewService.mParams_auto;
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append("mParams_auto");
                                    sb7.append(this.touch_target_num);
                                    map5.put(sb6, Float.valueOf((float) ((LayoutParams) map6.get(sb7.toString())).x));
                                    Map<String, Float> map7 = FloatingViewService.py_auto;
                                    StringBuilder sb8 = new StringBuilder();
                                    sb8.append("py_auto");
                                    sb8.append(this.touch_target_num);
                                    String sb9 = sb8.toString();
                                    Map<String, LayoutParams> map8 = FloatingViewService.mParams_auto;
                                    StringBuilder sb10 = new StringBuilder();
                                    sb10.append("mParams_auto");
                                    sb10.append(this.touch_target_num);
                                    map7.put(sb9, Float.valueOf((float) ((LayoutParams) map8.get(sb10.toString())).y));
                                    break;
                                case 1:
                                    StringBuilder sb11 = new StringBuilder();
                                    sb11.append(this.touch_target_num);
                                    sb11.append(" ");
                                    sb11.append(this.touch_target_num);
                                    Log.d("action_up", sb11.toString());
                                    Map<String, Float> map9 = FloatingViewService.px_auto;
                                    StringBuilder sb12 = new StringBuilder();
                                    sb12.append("px_auto");
                                    sb12.append(this.touch_target_num);
                                    String sb13 = sb12.toString();
                                    Map<String, LayoutParams> map10 = FloatingViewService.mParams_auto;
                                    StringBuilder sb14 = new StringBuilder();
                                    sb14.append("mParams_auto");
                                    sb14.append(this.touch_target_num);
                                    map9.put(sb13, Float.valueOf((float) ((LayoutParams) map10.get(sb14.toString())).x));
                                    Map<String, Float> map11 = FloatingViewService.py_auto;
                                    StringBuilder sb15 = new StringBuilder();
                                    sb15.append("py_auto");
                                    sb15.append(this.touch_target_num);
                                    String sb16 = sb15.toString();
                                    Map<String, LayoutParams> map12 = FloatingViewService.mParams_auto;
                                    StringBuilder sb17 = new StringBuilder();
                                    sb17.append("mParams_auto");
                                    sb17.append(this.touch_target_num);
                                    map11.put(sb16, Float.valueOf((float) ((LayoutParams) map12.get(sb17.toString())).y));
                                    int rawX = (int) (motionEvent.getRawX() - this.initialPointX);
                                    int rawY = (int) (motionEvent.getRawY() - this.initialPointY);
                                    if (Math.abs(rawX) < 5 && Math.abs(rawY) < 5 && !FloatingViewService.auto_click) {
                                        Intent intent = new Intent(Configuration.this.getApplicationContext(), DelayActivity.class);
                                        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("target_number", this.touch_target_num);
                                        intent.putExtras(bundle);
                                        Configuration.this.startActivity(intent);
                                        break;
                                    }
                                case 2:
                                    StringBuilder sb18 = new StringBuilder();
                                    sb18.append(this.touch_target_num);
                                    sb18.append(" ");
                                    sb18.append(this.touch_target_num);
                                    Log.d("action_move", sb18.toString());
                                    this.vectorX = (int) (motionEvent.getRawX() - this.initialPointX);
                                    this.vectorY = (int) (motionEvent.getRawY() - this.initialPointY);
                                    Map<String, LayoutParams> map13 = FloatingViewService.mParams_auto;
                                    StringBuilder sb19 = new StringBuilder();
                                    sb19.append("mParams_auto");
                                    sb19.append(this.touch_target_num);
                                    ((LayoutParams) map13.get(sb19.toString())).x = (int) (((float) this.initialX) + (((float) this.vectorX) * 1.0f));
                                    Map<String, LayoutParams> map14 = FloatingViewService.mParams_auto;
                                    StringBuilder sb20 = new StringBuilder();
                                    sb20.append("mParams_auto");
                                    sb20.append(this.touch_target_num);
                                    ((LayoutParams) map14.get(sb20.toString())).y = (int) (((float) this.initialY) + (((float) this.vectorY) * 1.0f));
                                    WindowManager windowManager = Configuration.this.mWindowManager;
                                    Map<String, ViewPointerAuto> map15 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb21 = new StringBuilder();
                                    sb21.append("mPointerView_auto");
                                    sb21.append(this.touch_target_num);
                                    View view2 = (View) map15.get(sb21.toString());
                                    Map<String, LayoutParams> map16 = FloatingViewService.mParams_auto;
                                    StringBuilder sb22 = new StringBuilder();
                                    sb22.append("mParams_auto");
                                    sb22.append(this.touch_target_num);
                                    windowManager.updateViewLayout(view2, (ViewGroup.LayoutParams) map16.get(sb22.toString()));
                                    Map<String, ViewPointerAutoSwipe> map17 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb23 = new StringBuilder();
                                    sb23.append("mPointerView_auto_s");
                                    sb23.append(String.valueOf(this.touch_target_num));
                                    ViewPointerAutoSwipe viewPointerAutoSwipe = (ViewPointerAutoSwipe) map17.get(sb23.toString());
                                    ViewPointerAutoSwipe.draw_flag = true;
                                    Map<String, ViewPointerAutoSwipe> map18 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb24 = new StringBuilder();
                                    sb24.append("mPointerView_auto_s");
                                    sb24.append(String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)));
                                    ViewPointerAutoSwipe viewPointerAutoSwipe2 = (ViewPointerAutoSwipe) map18.get(sb24.toString());
                                    Map<String, LayoutParams> map19 = FloatingViewService.mParams_auto;
                                    StringBuilder sb25 = new StringBuilder();
                                    sb25.append("mParams_auto");
                                    sb25.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
                                    int i = ((LayoutParams) map19.get(sb25.toString())).x;
                                    Map<String, ViewPointerAuto> map20 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb26 = new StringBuilder();
                                    sb26.append("mPointerView_auto");
                                    sb26.append(String.valueOf(this.touch_target_num));
                                    int width = i + (((ViewPointerAuto) map20.get(sb26.toString())).getWidth() / 2);
                                    Map<String, LayoutParams> map21 = FloatingViewService.mParams_auto;
                                    StringBuilder sb27 = new StringBuilder();
                                    sb27.append("mParams_auto");
                                    sb27.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
                                    int i2 = ((LayoutParams) map21.get(sb27.toString())).y;
                                    Map<String, ViewPointerAuto> map22 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb28 = new StringBuilder();
                                    sb28.append("mPointerView_auto");
                                    sb28.append(String.valueOf(this.touch_target_num));
                                    int width2 = i2 + ((ViewPointerAuto) map22.get(sb28.toString())).getWidth();
                                    Map<String, LayoutParams> map23 = FloatingViewService.mParams_auto;
                                    StringBuilder sb29 = new StringBuilder();
                                    sb29.append("mParams_auto");
                                    sb29.append(this.touch_target_num);
                                    int i3 = ((LayoutParams) map23.get(sb29.toString())).x;
                                    Map<String, ViewPointerAuto> map24 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb30 = new StringBuilder();
                                    sb30.append("mPointerView_auto");
                                    sb30.append(String.valueOf(this.touch_target_num));
                                    int width3 = i3 + (((ViewPointerAuto) map24.get(sb30.toString())).getWidth() / 2);
                                    Map<String, LayoutParams> map25 = FloatingViewService.mParams_auto;
                                    StringBuilder sb31 = new StringBuilder();
                                    sb31.append("mParams_auto");
                                    sb31.append(this.touch_target_num);
                                    int i4 = ((LayoutParams) map25.get(sb31.toString())).y;
                                    Map<String, ViewPointerAuto> map26 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb32 = new StringBuilder();
                                    sb32.append("mPointerView_auto");
                                    sb32.append(String.valueOf(this.touch_target_num));
                                    viewPointerAutoSwipe2.assign(width, width2, width3, i4 + ((ViewPointerAuto) map26.get(sb32.toString())).getWidth());
                                    Map<String, ViewPointerAutoSwipe> map27 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb33 = new StringBuilder();
                                    sb33.append("mPointerView_auto_s");
                                    sb33.append(String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)));
                                    ((ViewPointerAutoSwipe) map27.get(sb33.toString())).invalidate();
                                    WindowManager windowManager2 = Configuration.this.mWindowManager;
                                    Map<String, ViewPointerAutoSwipe> map28 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb34 = new StringBuilder();
                                    sb34.append("mPointerView_auto_s");
                                    sb34.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
                                    View view3 = (View) map28.get(sb34.toString());
                                    Map<String, LayoutParams> map29 = FloatingViewService.mParams_auto;
                                    StringBuilder sb35 = new StringBuilder();
                                    sb35.append("mParams_auto_s");
                                    sb35.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
                                    windowManager2.updateViewLayout(view3, (ViewGroup.LayoutParams) map29.get(sb35.toString()));
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
                Map<String, ViewPointerAuto> map41 = FloatingViewService.mPointerView_auto;
                StringBuilder sb47 = new StringBuilder();
                sb47.append("mPointerView_auto");
                sb47.append(String.valueOf(FloatingViewService.target_num));
                if (map41.get(sb47.toString()) != null) {
                    Map<String, ViewPointerAuto> map42 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb48 = new StringBuilder();
                    sb48.append("mPointerView_auto");
                    sb48.append(String.valueOf(FloatingViewService.target_num));
                    if (!((ViewPointerAuto) map42.get(sb48.toString())).isShown()) {
                        WindowManager windowManager4 = this.mWindowManager;
                        Map<String, ViewPointerAuto> map43 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb49 = new StringBuilder();
                        sb49.append("mPointerView_auto");
                        sb49.append(String.valueOf(FloatingViewService.target_num));
                        View view4 = (View) map43.get(sb49.toString());
                        Map<String, LayoutParams> map44 = FloatingViewService.mParams_auto;
                        StringBuilder sb50 = new StringBuilder();
                        sb50.append("mParams_auto");
                        sb50.append(String.valueOf(FloatingViewService.target_num));
                        windowManager4.addView(view4, (ViewGroup.LayoutParams) map44.get(sb50.toString()));
                    }
                }
                edit.putInt("auto_target", FloatingViewService.target_num);
                edit.apply();
                Log.d("add_btn target_num : ", String.valueOf(FloatingViewService.target_num));
            } else {
                FloatingViewService.target_num++;
                Map<String, Float> map45 = FloatingViewService.px_auto;
                StringBuilder sb51 = new StringBuilder();
                sb51.append("px_auto");
                sb51.append(String.valueOf(FloatingViewService.target_num));
                map45.put(sb51.toString(), Float.valueOf(0.0f));
                Map<String, Float> map46 = FloatingViewService.py_auto;
                StringBuilder sb52 = new StringBuilder();
                sb52.append("py_auto");
                sb52.append(String.valueOf(FloatingViewService.target_num));
                map46.put(sb52.toString(), Float.valueOf(0.0f));
                Map<String, ViewPointerAuto> map47 = FloatingViewService.mPointerView_auto;
                StringBuilder sb53 = new StringBuilder();
                sb53.append("mPointerView_auto");
                sb53.append(String.valueOf(FloatingViewService.target_num));
                map47.put(sb53.toString(), new ViewPointerAuto(getApplicationContext()));
                Map<String, ViewPointerAuto> map48 = FloatingViewService.mPointerView_auto;
                StringBuilder sb54 = new StringBuilder();
                sb54.append("mPointerView_auto");
                sb54.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map48.get(sb54.toString())).setClickable(true);
                Map<String, ViewPointerAuto> map49 = FloatingViewService.mPointerView_auto;
                StringBuilder sb55 = new StringBuilder();
                sb55.append("mPointerView_auto");
                sb55.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map49.get(sb55.toString())).setFocusable(true);
                Map<String, LayoutParams> map50 = FloatingViewService.mParams_auto;
                StringBuilder sb56 = new StringBuilder();
                sb56.append("mParams_auto");
                sb56.append(String.valueOf(FloatingViewService.target_num));
                String sb57 = sb56.toString();
                LayoutParams layoutParams6 = new LayoutParams(-2, -2, i3, 8, -3);
                map50.put(sb57, layoutParams6);
                Map<String, LayoutParams> map51 = FloatingViewService.mParams_auto;
                StringBuilder sb58 = new StringBuilder();
                sb58.append("mParams_auto");
                sb58.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map51.get(sb58.toString())).gravity = 51;

                Map<String, LayoutParams> map52 = FloatingViewService.mParams_auto;
                StringBuilder sb59 = new StringBuilder();
                sb59.append("mParams_auto");
                sb59.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map52.get(sb59.toString())).x = ((Integer) arrayList3.get(FloatingViewService.target_num - 1)).intValue();
                Map<String, LayoutParams> map53 = FloatingViewService.mParams_auto;
                StringBuilder sb60 = new StringBuilder();
                sb60.append("mParams_auto");
                sb60.append(String.valueOf(FloatingViewService.target_num));
                ((LayoutParams) map53.get(sb60.toString())).y = ((Integer) arrayList4.get(FloatingViewService.target_num - 1)).intValue();
                StringBuilder sb61 = new StringBuilder();
                sb61.append("@drawable/target");
                sb61.append(String.valueOf(FloatingViewService.target_num));
                String sb62 = sb61.toString();
                Log.d("drawable uri : ", sb62);
                final int identifier3 = getResources().getIdentifier(sb62, null, getPackageName());
                Log.d("imageResource : ", String.valueOf(identifier3));
                Map<String, ViewPointerAuto> map54 = FloatingViewService.mPointerView_auto;
                StringBuilder sb63 = new StringBuilder();
                sb63.append("mPointerView_auto");
                sb63.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map54.get(sb63.toString())).setBackgroundResource(identifier3);
                Map<String, ViewPointerAuto> map55 = FloatingViewService.mPointerView_auto;
                StringBuilder sb64 = new StringBuilder();
                sb64.append("mPointerView_auto");
                sb64.append(String.valueOf(FloatingViewService.target_num));
                ((ViewPointerAuto) map55.get(sb64.toString())).setOnTouchListener(new View.OnTouchListener() {
                    private float initialPointX;
                    private float initialPointY;
                    private int initialX;
                    private int initialY;
                    private String touch_target_num;
                    private int vectorX;
                    private int vectorY;

                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        try {
                            switch (motionEvent.getAction()) {
                                case 0:
                                    String resourceEntryName = view.getResources().getResourceEntryName(identifier3);
                                    if (resourceEntryName.length() > 7) {
                                        this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 2, resourceEntryName.length());
                                        Log.d("target name : ", resourceEntryName);
                                        Log.d("target sub name : ", this.touch_target_num);
                                    } else {
                                        this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 1, resourceEntryName.length());
                                        Log.d("target name : ", resourceEntryName);
                                        Log.d("target sub name : ", this.touch_target_num);
                                    }
                                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                                    Map<String, LayoutParams> map = FloatingViewService.mParams_auto;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("mParams_auto");
                                    sb.append(this.touch_target_num);
                                    edit.putInt("initialX", ((LayoutParams) map.get(sb.toString())).x);
                                    Map<String, LayoutParams> map2 = FloatingViewService.mParams_auto;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("mParams_auto");
                                    sb2.append(this.touch_target_num);
                                    edit.putInt("initialY", ((LayoutParams) map2.get(sb2.toString())).y);
                                    edit.apply();
                                    Map<String, LayoutParams> map3 = FloatingViewService.mParams_auto;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("mParams_auto");
                                    sb3.append(this.touch_target_num);
                                    this.initialX = ((LayoutParams) map3.get(sb3.toString())).x;
                                    Map<String, LayoutParams> map4 = FloatingViewService.mParams_auto;
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("mParams_auto");
                                    sb4.append(this.touch_target_num);
                                    this.initialY = ((LayoutParams) map4.get(sb4.toString())).y;
                                    this.initialPointX = motionEvent.getRawX();
                                    this.initialPointY = motionEvent.getRawY();
                                    Map<String, Float> map5 = FloatingViewService.px_auto;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("px_auto");
                                    sb5.append(this.touch_target_num);
                                    String sb6 = sb5.toString();
                                    Map<String, LayoutParams> map6 = FloatingViewService.mParams_auto;
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append("mParams_auto");
                                    sb7.append(this.touch_target_num);
                                    map5.put(sb6, Float.valueOf((float) ((LayoutParams) map6.get(sb7.toString())).x));
                                    Map<String, Float> map7 = FloatingViewService.py_auto;
                                    StringBuilder sb8 = new StringBuilder();
                                    sb8.append("py_auto");
                                    sb8.append(this.touch_target_num);
                                    String sb9 = sb8.toString();
                                    Map<String, LayoutParams> map8 = FloatingViewService.mParams_auto;
                                    StringBuilder sb10 = new StringBuilder();
                                    sb10.append("mParams_auto");
                                    sb10.append(this.touch_target_num);
                                    map7.put(sb9, Float.valueOf((float) ((LayoutParams) map8.get(sb10.toString())).y));
                                    break;
                                case 1:
                                    StringBuilder sb11 = new StringBuilder();
                                    sb11.append(this.touch_target_num);
                                    sb11.append(" ");
                                    sb11.append(this.touch_target_num);
                                    Log.d("action_up", sb11.toString());
                                    Map<String, Float> map9 = FloatingViewService.px_auto;
                                    StringBuilder sb12 = new StringBuilder();
                                    sb12.append("px_auto");
                                    sb12.append(this.touch_target_num);
                                    String sb13 = sb12.toString();
                                    Map<String, LayoutParams> map10 = FloatingViewService.mParams_auto;
                                    StringBuilder sb14 = new StringBuilder();
                                    sb14.append("mParams_auto");
                                    sb14.append(this.touch_target_num);
                                    map9.put(sb13, Float.valueOf((float) ((LayoutParams) map10.get(sb14.toString())).x));
                                    Map<String, Float> map11 = FloatingViewService.py_auto;
                                    StringBuilder sb15 = new StringBuilder();
                                    sb15.append("py_auto");
                                    sb15.append(this.touch_target_num);
                                    String sb16 = sb15.toString();
                                    Map<String, LayoutParams> map12 = FloatingViewService.mParams_auto;
                                    StringBuilder sb17 = new StringBuilder();
                                    sb17.append("mParams_auto");
                                    sb17.append(this.touch_target_num);
                                    map11.put(sb16, Float.valueOf((float) ((LayoutParams) map12.get(sb17.toString())).y));
                                    int rawX = (int) (motionEvent.getRawX() - this.initialPointX);
                                    int rawY = (int) (motionEvent.getRawY() - this.initialPointY);
                                    if (Math.abs(rawX) < 5 && Math.abs(rawY) < 5 && !FloatingViewService.auto_click) {
                                        Intent intent = new Intent(Configuration.this.getApplicationContext(), DelayActivity.class);
                                        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("target_number", this.touch_target_num);
                                        intent.putExtras(bundle);
                                        Configuration.this.startActivity(intent);
                                        break;
                                    }
                                case 2:
                                    StringBuilder sb18 = new StringBuilder();
                                    sb18.append(this.touch_target_num);
                                    sb18.append(" ");
                                    sb18.append(this.touch_target_num);
                                    Log.d("action_move", sb18.toString());
                                    this.vectorX = (int) (motionEvent.getRawX() - this.initialPointX);
                                    this.vectorY = (int) (motionEvent.getRawY() - this.initialPointY);
                                    Map<String, LayoutParams> map13 = FloatingViewService.mParams_auto;
                                    StringBuilder sb19 = new StringBuilder();
                                    sb19.append("mParams_auto");
                                    sb19.append(this.touch_target_num);
                                    ((LayoutParams) map13.get(sb19.toString())).x = (int) (((float) this.initialX) + (((float) this.vectorX) * 1.0f));
                                    Map<String, LayoutParams> map14 = FloatingViewService.mParams_auto;
                                    StringBuilder sb20 = new StringBuilder();
                                    sb20.append("mParams_auto");
                                    sb20.append(this.touch_target_num);
                                    ((LayoutParams) map14.get(sb20.toString())).y = (int) (((float) this.initialY) + (((float) this.vectorY) * 1.0f));
                                    WindowManager windowManager = Configuration.this.mWindowManager;
                                    Map<String, ViewPointerAuto> map15 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb21 = new StringBuilder();
                                    sb21.append("mPointerView_auto");
                                    sb21.append(this.touch_target_num);
                                    View view2 = (View) map15.get(sb21.toString());
                                    Map<String, LayoutParams> map16 = FloatingViewService.mParams_auto;
                                    StringBuilder sb22 = new StringBuilder();
                                    sb22.append("mParams_auto");
                                    sb22.append(this.touch_target_num);
                                    windowManager.updateViewLayout(view2, (ViewGroup.LayoutParams) map16.get(sb22.toString()));
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
                Map<String, ViewPointerAuto> map56 = FloatingViewService.mPointerView_auto;
                StringBuilder sb65 = new StringBuilder();
                sb65.append("mPointerView_auto");
                sb65.append(String.valueOf(FloatingViewService.target_num));
                if (map56.get(sb65.toString()) != null) {
                    Map<String, ViewPointerAuto> map57 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb66 = new StringBuilder();
                    sb66.append("mPointerView_auto");
                    sb66.append(String.valueOf(FloatingViewService.target_num));
                    if (!((ViewPointerAuto) map57.get(sb66.toString())).isShown()) {
                        WindowManager windowManager5 = this.mWindowManager;
                        Map<String, ViewPointerAuto> map58 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb67 = new StringBuilder();
                        sb67.append("mPointerView_auto");
                        sb67.append(String.valueOf(FloatingViewService.target_num));
                        View view5 = (View) map58.get(sb67.toString());
                        Map<String, LayoutParams> map59 = FloatingViewService.mParams_auto;
                        StringBuilder sb68 = new StringBuilder();
                        sb68.append("mParams_auto");
                        sb68.append(String.valueOf(FloatingViewService.target_num));
                        windowManager5.addView(view5, (ViewGroup.LayoutParams) map59.get(sb68.toString()));
                    }
                }
                edit.putInt("auto_target", FloatingViewService.target_num);
                edit.apply();
                Log.d("add_btn target_num : ", String.valueOf(FloatingViewService.target_num));
                i = i4;
            }
            z = true;
            i4 = i + 1;
        }
    }

    @SuppressLint("LongLogTag")
    public void remvoe_PointerViewAll() {
        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
        while (true) {
            if (FloatingViewService.target_num == 0 || FloatingViewService.mPointerView_auto == null) {
            } else {
                FloatingViewService.target_num = FloatingViewService.settings.getInt("auto_target", 0);
                Log.d("remove_btn  target_num : ", String.valueOf(FloatingViewService.target_num));
                boolean z = false;
                for (int i = 0; i < FloatingViewService.swipe_target.size(); i++) {
                    if (FloatingViewService.target_num == ((Integer) FloatingViewService.swipe_target.get(i)).intValue()) {
                        Log.d("target_num == swipe_target.get : ", String.valueOf(FloatingViewService.target_num));
                        FloatingViewService.swipe_target.remove(i);
                        FloatingViewService.swipe_target.remove(i - 1);
                        z = true;
                    }
                }
                if (z) {
                    Map<String, ViewPointerAuto> map = FloatingViewService.mPointerView_auto;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mPointerView_auto");
                    sb.append(String.valueOf(FloatingViewService.target_num));
                    if (map.get(sb.toString()) != null) {
                        Map<String, ViewPointerAuto> map2 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("mPointerView_auto");
                        sb2.append(String.valueOf(FloatingViewService.target_num));
                        if (((ViewPointerAuto) map2.get(sb2.toString())).isShown()) {
                            WindowManager windowManager = this.mWindowManager;
                            Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointerView_auto");
                            sb3.append(String.valueOf(FloatingViewService.target_num));
                            windowManager.removeView((View) map3.get(sb3.toString()));
                            WindowManager windowManager2 = this.mWindowManager;
                            Map<String, ViewPointerAutoSwipe> map4 = FloatingViewService.mPointerView_auto_swipe;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("mPointerView_auto_s");
                            sb4.append(String.valueOf(FloatingViewService.target_num - 1));
                            windowManager2.removeView((View) map4.get(sb4.toString()));
                            edit.putInt("auto_target", FloatingViewService.target_num - 1);
                            edit.apply();
                            FloatingViewService.target_num--;
                            Log.d("target_num-- : ", String.valueOf(FloatingViewService.target_num));
                        }
                    }
                    Map<String, ViewPointerAuto> map5 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("mPointerView_auto");
                    sb5.append(String.valueOf(FloatingViewService.target_num));
                    if (map5.get(sb5.toString()) != null) {
                        Map<String, ViewPointerAuto> map6 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("mPointerView_auto");
                        sb6.append(String.valueOf(FloatingViewService.target_num));
                        if (((ViewPointerAuto) map6.get(sb6.toString())).isShown()) {
                            WindowManager windowManager3 = this.mWindowManager;
                            Map<String, ViewPointerAuto> map7 = FloatingViewService.mPointerView_auto;
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("mPointerView_auto");
                            sb7.append(String.valueOf(FloatingViewService.target_num));
                            windowManager3.removeView((View) map7.get(sb7.toString()));
                            edit.putInt("auto_target", FloatingViewService.target_num - 1);
                            edit.apply();
                            FloatingViewService.target_num--;
                            Log.d("target_num-- : ", String.valueOf(FloatingViewService.target_num));
                        }
                    }
                } else {
                    Map<String, ViewPointerAuto> map8 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("mPointerView_auto");
                    sb8.append(String.valueOf(FloatingViewService.target_num));
                    if (map8.get(sb8.toString()) != null) {
                        Map<String, ViewPointerAuto> map9 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("mPointerView_auto");
                        sb9.append(String.valueOf(FloatingViewService.target_num));
                        if (((ViewPointerAuto) map9.get(sb9.toString())).isShown()) {
                            WindowManager windowManager4 = this.mWindowManager;
                            Map<String, ViewPointerAuto> map10 = FloatingViewService.mPointerView_auto;
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append("mPointerView_auto");
                            sb10.append(String.valueOf(FloatingViewService.target_num));
                            windowManager4.removeView((View) map10.get(sb10.toString()));
                            edit.putInt("auto_target", FloatingViewService.target_num - 1);
                            edit.apply();
                            FloatingViewService.target_num--;
                            Log.d("target_num-- : ", String.valueOf(FloatingViewService.target_num));
                        }
                    }
                }
            }
        }
//        for (int i2 = 0; i2 < FloatingViewService.swipe_target.size(); i2++) {
//            FloatingViewService.swipe_target.remove(i2);
//        }
//        edit.putInt("auto_target", 0);
//        edit.apply();
//        FloatingViewService.target_num = 0;
//        FloatingViewService.mPointerView_auto = null;
//        FloatingViewService.mPointerView_auto_swipe = null;
//        FloatingViewService.mParams_auto = null;
//        FloatingViewService.mPointerView_auto = new HashMap();
//        FloatingViewService.mPointerView_auto_swipe = new HashMap();
//        FloatingViewService.mParams_auto = new HashMap();
    }
}
