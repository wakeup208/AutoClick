package servicecontrol;

import android.accessibilityservice.GestureDescription;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wakeup.autoclick.FloatingViewService;
import com.wakeup.autoclick.R;

import java.util.HashMap;

public class AllSettings extends AppCompatActivity {
    public static TextView ClickTimes = null;
    public static final String TAG = "BillingManager";
    public static boolean auto_flag;
    public static boolean back_flag;
    static Context context;
    static Drawable currDrawable1;
    static Drawable currDrawable2;
    static Drawable currDrawable3;
    static Drawable currDrawable4;
    static Drawable currDrawable5;
    public static boolean quick_flag;
    private static long startTime1;
    private static long startTime2;
    private static long startTime3;
    private static long startTime4;
    private static long startTime5;
    public static boolean task_flag;
    private Button AutoClickSpeed;
    private Button AutoSwipeSpeed;
    private Button Config;
    private Button Instruction;
    private Button MoreApps;
    /* access modifiers changed from: private */
    public Button Pro;
    private int SMART_BANNERNum = 1;
    private int SMART_BANNER_change = 0;
    private Button VoiceRecog;
    Activity activity;
    private boolean afterAd = false;
    private Button buttonEventTab;
    private Button buttonRequestPlacement;
    private Button buttonShowPlacement;
    private Button buttonUserTab;
    //    ConsumeResponseListener consumeListener = new ConsumeResponseListener() {
//        public void onConsumeResponse(int i, String str) {
//        }
//    };
    private Button currentButton;
    private View decorView;
    private String displayText = "";
    private Button getCurrencyBalanceButton;
    private Button getDirectPlayVideoAd;
    Handler handler;
    private LayoutInflater inflater;
    private boolean loadingAd_flag;
    private RelativeLayout mAssistiveView;
    //    /* access modifiers changed from: private */
//    public BillingClient mBillingClient;
//    /* access modifiers changed from: private */
//    public BillingManager mBillingManager;
    private Handler mHandler1;
    private Handler mHandler2;
    private Handler mHandler3;
    private Handler mHandler4;
    private Handler mHandler5;
    private ShareActionProvider mShareActionProvider;
    private Runnable mUpdateTimeTask1;
    private Runnable mUpdateTimeTask2;
    private Runnable mUpdateTimeTask3;
    private Runnable mUpdateTimeTask4;
    private Runnable mUpdateTimeTask5;
    public WindowManager mWindowManager;
    private ScrollView mainScrollView;
    private TextView outputTextView;
    private ImageView pSMART_BANNER;
    private EditText placementNameInput;
    private HashMap<String, Object> properties;

/*    Purchase purchase;
    PurchasesResult purchasesResult;*/

    /* renamed from: rl */
    private RelativeLayout f51rl;
    Runnable runnable;
    private boolean share_flag = false;
    //    Builder skuDetailsParams;
    private int startCnt;
    private int style_num;
    private int style_type;

    public AllSettings() {
    }

    public AllSettings(Context context2) {
        context = context2;
    }
    private void widget() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.activity = this;

        widget();
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        this.AutoClickSpeed = (Button) findViewById(R.id.autoclick_btn);
        this.AutoClickSpeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               AllSettings.this.startActivity(new Intent(AllSettings.this, AutoClickSpeed.class));
            }
        });
        this.AutoSwipeSpeed = (Button) findViewById(R.id.swipespeed_btn);
        this.AutoSwipeSpeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AllSettings.this.startActivity(new Intent(AllSettings.this, AutoSwipeSpeed.class));
            }
        });
        this.Instruction = (Button) findViewById(R.id.instruction_btn);
//        this.Instruction.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//               AllSettings.this.startActivity(new Intent(AllSettings.this, Instruction.class));
//            }
//        });

        FloatingViewService.target_limit = 48;
        this.Config = (Button) findViewById(R.id.save_btn);
        this.Config.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AllSettings.this.startActivity(new Intent(AllSettings.this, Configuration.class));
            }
        });



        FloatingViewService.settings = PreferenceManager.getDefaultSharedPreferences(this);
        this.startCnt = FloatingViewService.settings.getInt("start_cnt", 0) + 1;
        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
        edit.putInt("start_cnt", this.startCnt);
        edit.apply();
        this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        FloatingViewService.settings = PreferenceManager.getDefaultSharedPreferences(this);
        this.startCnt = FloatingViewService.settings.getInt("start_cnt", 0) + 1;
        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
        edit.putInt("start_cnt", this.startCnt);
        edit.apply();
        Log.d("startCnt : ", String.valueOf(this.startCnt));
        if (this.startCnt % 7 == 1) {
            FloatingViewService.rate_flag = true;
        }
//        if (FloatingViewService.rate_flag && !FloatingViewService.settings.getBoolean("rate", false)) {
//            FloatingViewService.rate_flag = false;
//            startActivity(new Intent(this, RateActivity.class));
//        }
    }
}
