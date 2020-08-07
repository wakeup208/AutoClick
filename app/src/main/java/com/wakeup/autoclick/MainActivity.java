package com.wakeup.autoclick;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import service.AutoClicker;
import servicecontrol.AllSettings;

import static android.view.accessibility.AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END;
import static android.view.accessibility.AccessibilityEvent.TYPE_TOUCH_INTERACTION_END;


public class MainActivity extends AppCompatActivity {
    public static Context main_context;
    private Button Btn_Access;
    private Button Btn_Overlay;
    private LinearLayout Lyt_Access;
    private LinearLayout Lyt_Overlay;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        main_context = this;
        getWindow().setFlags(TYPE_TOUCH_INTERACTION_END, TYPE_TOUCH_EXPLORATION_GESTURE_END);
        this.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        widget();
    }

    private void widget() {
        this.Lyt_Overlay = (LinearLayout) findViewById(R.id.overlay_pref);
        this.Lyt_Access = (LinearLayout) findViewById(R.id.access_pref);
        this.Btn_Overlay = (Button) findViewById(R.id.allow_overlay);
        this.Btn_Overlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + MainActivity.this.getPackageName())));
            }
        });
        this.Btn_Access = (Button) findViewById(R.id.allow_access);
        this.Btn_Access.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Settings.canDrawOverlays(this) && AutoClicker.instance != null) {
            startActivity(new Intent(this, AllSettings.class));
            startService(new Intent(this, FloatingViewService.class));
            finish();
        } else if (!Settings.canDrawOverlays(this) && AutoClicker.instance == null) {
        } else {
            if (Settings.canDrawOverlays(this) && AutoClicker.instance == null) {
                this.Lyt_Overlay.setVisibility(View.GONE);
            } else if (!Settings.canDrawOverlays(this) && AutoClicker.instance != null) {
                this.Lyt_Access.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            this.Lyt_Overlay.setVisibility(View.GONE);
            if (AutoClicker.instance != null) {
                startActivity(new Intent(this, AllSettings.class));
                startService(new Intent(this, FloatingViewService.class));
                finish();
            }
        }
    }
}
