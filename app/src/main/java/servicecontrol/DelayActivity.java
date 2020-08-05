package servicecontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wakeup.autoclick.FloatingViewService;
import com.wakeup.autoclick.R;

import java.util.HashMap;

public class DelayActivity extends AppCompatActivity {
    static Context context;
    private Button Btn_Initialize;
    private Button Btn_Initialize_All;
    private boolean Btn_Initialize_All_flag = false;
    /* access modifiers changed from: private */
    public EditText Edt_After;
    /* access modifiers changed from: private */
    public EditText Edt_Before;
    private LinearLayout Lyt_NO;
    private LinearLayout Lyt_OK;

    /* renamed from: Rl */
    private ScrollView f75Rl;
    private boolean afterAd = false;
    private LayoutInflater inflater;
    private ShareActionProvider mShareActionProvider;
    public WindowManager mWindowManager;
    private HashMap<String, Object> properties;
    /* access modifiers changed from: private */
    public String target_number;


    public DelayActivity() {
    }

    public DelayActivity(Context context2) {
        context = context2;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.flags = 2;
        layoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_delay);
        this.target_number = getIntent().getExtras().getString("target_number");
        this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        widget();


    }


    private void widget() {
        this.Edt_Before = (EditText) findViewById(R.id.edt_before);
        this.Edt_After = (EditText) findViewById(R.id.edt_after);
        SharedPreferences sharedPreferences = FloatingViewService.settings;
        StringBuilder sb = new StringBuilder();
        sb.append("mPointer_Auto_Before");
        sb.append(String.valueOf(this.target_number));
        if (sharedPreferences.getInt(sb.toString(), 0) >= 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("setText mPointer_Auto_Before");
            sb2.append(String.valueOf(this.target_number));
            String sb3 = sb2.toString();
            SharedPreferences sharedPreferences2 = FloatingViewService.settings;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("mPointer_Auto_Before");
            sb4.append(String.valueOf(this.target_number));
            EditText editText = this.Edt_Before;
            SharedPreferences sharedPreferences3 = FloatingViewService.settings;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("mPointer_Auto_Before");
            sb5.append(String.valueOf(this.target_number));
            editText.setText(String.valueOf(sharedPreferences3.getInt(sb5.toString(), 0)));
        } else {
            this.Edt_Before.setText(null);
        }
        SharedPreferences sharedPreferences4 = FloatingViewService.settings;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("mPointer_Auto_After");
        sb6.append(String.valueOf(this.target_number));
        if (sharedPreferences4.getInt(sb6.toString(), 0) >= 0) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("setText mPointer_Auto_After");
            sb7.append(String.valueOf(this.target_number));
            String sb8 = sb7.toString();
            SharedPreferences sharedPreferences5 = FloatingViewService.settings;
            StringBuilder sb9 = new StringBuilder();
            sb9.append("mPointer_Auto_After");
            sb9.append(String.valueOf(this.target_number));
            EditText editText2 = this.Edt_After;
            SharedPreferences sharedPreferences6 = FloatingViewService.settings;
            StringBuilder sb10 = new StringBuilder();
            sb10.append("mPointer_Auto_After");
            sb10.append(String.valueOf(this.target_number));
            editText2.setText(String.valueOf(sharedPreferences6.getInt(sb10.toString(), 0)));
        } else {
            this.Edt_After.setText(null);
        }
        this.Btn_Initialize = (Button) findViewById(R.id.btn_initialize);
        this.Btn_Initialize_All = (Button) findViewById(R.id.btn_initialize_all);
        this.Btn_Initialize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DelayActivity.this.Edt_Before.setText("0");
                DelayActivity.this.Edt_After.setText("0");
                SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                StringBuilder sb = new StringBuilder();
                sb.append("mPointer_Auto_Before");
                sb.append(String.valueOf(DelayActivity.this.target_number));
                edit.putInt(sb.toString(), 0);
                edit.apply();
            }
        });
        this.Btn_Initialize_All.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DelayActivity.this.Edt_Before.setText("0");
                DelayActivity.this.Edt_After.setText("0");
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
            }
        });
        this.Lyt_OK = (LinearLayout) findViewById(R.id.lyt_ok);
        this.Lyt_OK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (DelayActivity.this.Edt_Before.getText().toString().equals("")) {
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    StringBuilder sb = new StringBuilder();
                    sb.append("mPointer_Auto_Before");
                    sb.append(String.valueOf(DelayActivity.this.target_number));
                    edit.putInt(sb.toString(), 0);
                    edit.apply();
                } else if (Integer.parseInt(DelayActivity.this.Edt_Before.getText().toString()) > 0) {
                    SharedPreferences.Editor edit2 = FloatingViewService.settings.edit();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("mPointer_Auto_Before");
                    sb2.append(String.valueOf(DelayActivity.this.target_number));
                    edit2.putInt(sb2.toString(), Integer.parseInt(DelayActivity.this.Edt_Before.getText().toString()));
                    edit2.apply();
                } else {
                    SharedPreferences.Editor edit3 = FloatingViewService.settings.edit();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("mPointer_Auto_Before");
                    sb3.append(String.valueOf(DelayActivity.this.target_number));
                    edit3.putInt(sb3.toString(), 0);
                    edit3.apply();
                }
                if (DelayActivity.this.Edt_After.getText().toString().equals("")) {
                    SharedPreferences.Editor edit4 = FloatingViewService.settings.edit();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("mPointer_Auto_After");
                    sb4.append(String.valueOf(DelayActivity.this.target_number));
                    edit4.putInt(sb4.toString(), 0);
                    edit4.apply();
                } else if (Integer.parseInt(DelayActivity.this.Edt_After.getText().toString()) > 0) {
                    SharedPreferences.Editor edit5 = FloatingViewService.settings.edit();
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("mPointer_Auto_After");
                    sb5.append(String.valueOf(DelayActivity.this.target_number));
                    edit5.putInt(sb5.toString(), Integer.parseInt(DelayActivity.this.Edt_After.getText().toString()));
                    edit5.apply();
                } else {
                    SharedPreferences.Editor edit6 = FloatingViewService.settings.edit();
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("mPointer_Auto_After");
                    sb6.append(String.valueOf(DelayActivity.this.target_number));
                    edit6.putInt(sb6.toString(), 0);
                    edit6.apply();
                }
                DelayActivity.this.finish();
            }
        });
        this.Lyt_NO = (LinearLayout) findViewById(R.id.lyt_no);
        this.Lyt_NO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DelayActivity.this.finish();
            }
        });
    }
}
