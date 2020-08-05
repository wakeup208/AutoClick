package servicecontrol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wakeup.autoclick.FloatingViewService;
import com.wakeup.autoclick.R;

import java.util.Random;

public class AutoClickSpeed extends AppCompatActivity {

    public static boolean auto_flag;
    public static boolean back_flag;
    public static int clickpersecond;
    static Context context;
    static Drawable currDrawable1;
    static Drawable currDrawable2;
    static Drawable currDrawable3;
    static Drawable currDrawable4;
    static Drawable currDrawable5;
    public static double duration_time;
    public static double interval;
    public static ProgressDialog mDialog;
    public static boolean quick_flag;
    public static double start_time;
    public static double swipe_duration_time;
    public static boolean task_flag;
    public static boolean test_flag;
    /* access modifiers changed from: private */
    public EditText Duration_Edt;
    /* access modifiers changed from: private */
    public EditText Interval_Edt;
    private Button Reset_Btn;

    /* renamed from: Rl */
    private ScrollView f52Rl;
    private Button Start_Btn;
    private Button Stop_Btn;
    private LinearLayout TestPanel_Lyt;
    /* access modifiers changed from: private */
    public TextView Test_Num;

    /* renamed from: a1 */
    private TextView f53a1;

    /* renamed from: a2 */
    private TextView f54a2;

    /* renamed from: a3 */
    private TextView f55a3;
    private View adContainer;
    private ImageView banner;
    Handler handler;
    private LayoutInflater inflater;
    private boolean loadingAd_flag;
    private int[] loc = new int[2];
    private RelativeLayout mAssistiveView;
    private ShareActionProvider mShareActionProvider;
    public WindowManager mWindowManager;
    private ImageView pbanner;
    Runnable runnable;
    private int startCnt;
    private int style_num;
    private int style_type;

    public AutoClickSpeed() {
    }
    public AutoClickSpeed(Context context2) {
        context = context2;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoclickspeed);
        this.loadingAd_flag = true;
        getActionBar();
        this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mAssistiveView = (RelativeLayout) this.inflater.inflate(R.layout.activity_setting, null);
        this.mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        widget();
    }

    private void widget() {
        this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.Duration_Edt = (EditText) findViewById(R.id.duration_edt);
        this.Interval_Edt = (EditText) findViewById(R.id.interval_edt);
        this.Test_Num = (TextView) findViewById(R.id.test_num);
        if (FloatingViewService.settings.getInt("duration", -1) >= 0) {
            this.Duration_Edt.setText(String.valueOf(FloatingViewService.settings.getInt("duration", -1)));
        } else {
            this.Duration_Edt.setText(null);
        }
        if (clickpersecond == 30) {
            this.Interval_Edt.setText(null);
        } else {
            this.Interval_Edt.setText(String.valueOf(FloatingViewService.settings.getInt("clickpersecond", 30)));
        }
        this.TestPanel_Lyt = (LinearLayout) findViewById(R.id.test_panel);
        this.Start_Btn = (Button) findViewById(R.id.test_start);
        this.Stop_Btn = (Button) findViewById(R.id.test_stop);
        this.Reset_Btn = (Button) findViewById(R.id.test_reset);
        this.Duration_Edt.setSelection(this.Duration_Edt.getText().length());
        this.Interval_Edt.setSelection(this.Interval_Edt.getText().length());

        this.Duration_Edt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (AutoClickSpeed.this.ParseDouble(AutoClickSpeed.this.Duration_Edt.getText().toString()) > 0.0d && AutoClickSpeed.this.ParseDouble(AutoClickSpeed.this.Duration_Edt.getText().toString()) < 1000000.0d) {
                    AutoClickSpeed.duration_time = Double.parseDouble(AutoClickSpeed.this.Duration_Edt.getText().toString()) * 1000.0d;
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putInt("duration", Integer.parseInt(AutoClickSpeed.this.Duration_Edt.getText().toString()));
                    edit.apply();
                } else if (AutoClickSpeed.this.ParseDouble(AutoClickSpeed.this.Duration_Edt.getText().toString()) > 1000000.0d) {
                    AutoClickSpeed.this.Duration_Edt.setText("1000000");
                    AutoClickSpeed.duration_time = Double.parseDouble(AutoClickSpeed.this.Duration_Edt.getText().toString()) * 1000.0d;
                    SharedPreferences.Editor edit2 = FloatingViewService.settings.edit();
                    edit2.putInt("duration", Integer.parseInt(AutoClickSpeed.this.Duration_Edt.getText().toString()));
                    edit2.apply();
                    Toast.makeText(AutoClickSpeed.this.getApplicationContext(), "Max duration seconds : 1000000 ", Toast.LENGTH_SHORT).show();
                } else {
                    AutoClickSpeed.duration_time = -1.0d;
                    SharedPreferences.Editor edit3 = FloatingViewService.settings.edit();
                    edit3.putInt("duration", -1);
                    edit3.apply();
                }
            }
        });
        this.Interval_Edt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (AutoClickSpeed.this.ParseInt(AutoClickSpeed.this.Interval_Edt.getText().toString()) > 50) {
                    AutoClickSpeed.this.Interval_Edt.setText("50");
                    AutoClickSpeed.clickpersecond = 50;
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putInt("clickpersecond", 50);
                    edit.apply();
                    Toast.makeText(AutoClickSpeed.this.getApplicationContext(), "Max click per second : 50 ", Toast.LENGTH_SHORT).show();
                } else if (AutoClickSpeed.this.ParseInt(AutoClickSpeed.this.Interval_Edt.getText().toString()) > 0) {
                    AutoClickSpeed.clickpersecond = Integer.parseInt(AutoClickSpeed.this.Interval_Edt.getText().toString());
                    SharedPreferences.Editor edit2 = FloatingViewService.settings.edit();
                    edit2.putInt("clickpersecond", Integer.parseInt(AutoClickSpeed.this.Interval_Edt.getText().toString()));
                    edit2.apply();
                } else {
                    AutoClickSpeed.clickpersecond = 30;
                    SharedPreferences.Editor edit3 = FloatingViewService.settings.edit();
                    edit3.putInt("clickpersecond", 30);
                    edit3.apply();
                }
            }
        });

        this.TestPanel_Lyt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AutoClickSpeed.this.Test_Num.setText(String.valueOf(Integer.parseInt(AutoClickSpeed.this.Test_Num.getText().toString()) + 1));
            }
        });
        this.Stop_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AutoClickSpeed.test_flag = false;
            }
        });
        this.Reset_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AutoClickSpeed.this.Test_Num.setText("0");
            }
        });
        this.Start_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AutoClickSpeed.test_flag = true;
                AutoClickSpeed.start_time = (double) System.currentTimeMillis();
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    public void run() {
                        AutoClickSpeed.interval = ((double) System.currentTimeMillis()) - AutoClickSpeed.start_time;
                        if (AutoClickSpeed.test_flag && AutoClickSpeed.duration_time < 0.0d) {
                            AutoClickSpeed.this.Test_Num.setText(String.valueOf(Integer.parseInt(AutoClickSpeed.this.Test_Num.getText().toString()) + 1));
                            handler.postDelayed(this, (long) (1000 / AutoClickSpeed.clickpersecond));
                        } else if (AutoClickSpeed.test_flag && AutoClickSpeed.interval < AutoClickSpeed.duration_time) {
                            AutoClickSpeed.this.Test_Num.setText(String.valueOf(Integer.parseInt(AutoClickSpeed.this.Test_Num.getText().toString()) + 1));
                            handler.postDelayed(this, (long) (1000 / AutoClickSpeed.clickpersecond));
                        }
                    }
                });
            }
        });
    }



    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.loadingAd_flag = false;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    public boolean getRandomBoolean() {
        return new Random().nextBoolean();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: 0000 */
    public double ParseDouble(String str) {
        if (str == null || str.length() <= 0) {
            return 0.0d;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception unused) {
            return -1.0d;
        }
    }

    /* access modifiers changed from: 0000 */
    public int ParseInt(String str) {
        if (str == null || str.length() <= 0) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return -1;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
