package servicecontrol;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.ShareActionProvider.OnShareTargetSelectedListener;
import android.widget.Toast;

import com.wakeup.autoclick.FloatingViewService;
import com.wakeup.autoclick.R;

import java.util.HashMap;

public class Preference extends Activity {
    static Context context;
    private LinearLayout Lyt_Access;
    private LinearLayout Lyt_Overlay;

    /* renamed from: Rl */
    private ScrollView f37Rl;
    private boolean afterAd = false;
    private int bannerNum = 1;
    private int banner_change = 0;
    private View decorView;
    Handler handler;
    private LayoutInflater inflater;
    private boolean loadingAd_flag;
    private RelativeLayout mAssistiveView;
    private ShareActionProvider mShareActionProvider;
    public WindowManager mWindowManager;
    private ImageView pbanner;
    private HashMap<String, Object> properties;
    private boolean rating_drag_flag = false;

    /* renamed from: rl */
    private RelativeLayout f38rl;
    Runnable runnable;
    private int stars;
    private int startCnt;
    private int style_num;
    private int style_type;
    private OnClickListener yesListener = new OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {
            Preference.this.launchMarket();
        }
    };

    public Preference() {
    }

    public Preference(Context context2) {
        context = context2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.flags = 2;
        layoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_pref);
        this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        widget();
    }

    /* access modifiers changed from: private */
    public void launchMarket() {

        Editor edit = FloatingViewService.settings.edit();
        edit.putBoolean("rate", true);
        edit.apply();
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.good.autoclicker"));
        try {
            Toast.makeText(getApplicationContext(), "Thank you for your support! :)", 0).show();
            startActivity(intent);
            finish();
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, " unable to find market app", 1).show();
        }
    }

    public Intent doShare() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "Easy Touch & Assistive Touch & On-Screen-Pointer");
        StringBuilder sb = new StringBuilder();
        sb.append("\nLet me recommend you this application\n\n");
        sb.append("https://play.google.com/store/apps/details?id=com.auto.easytouch \n\n");
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        FloatingViewService.settings = PreferenceManager.getDefaultSharedPreferences(this);
        this.startCnt = FloatingViewService.settings.getInt("start_cnt", 0) + 1;
        Editor edit = FloatingViewService.settings.edit();
        edit.putInt("start_cnt", this.startCnt);
        edit.apply();
    }

    private void widget() {
        this.Lyt_Overlay = (LinearLayout) findViewById(R.id.overlay_pref);
        this.Lyt_Overlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Editor edit = FloatingViewService.settings.edit();
                edit.putBoolean("rate", true);
                edit.apply();
                Preference.this.launchMarket();
            }
        });
        this.Lyt_Access = (LinearLayout) findViewById(R.id.lyt_later);
        this.Lyt_Access.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Preference.this.finish();
            }
        });
    }
}
