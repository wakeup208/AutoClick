package com.wakeup.autoclick;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import service.AutoClicker;
import servicecontrol.AllSettings;
import servicecontrol.AutoClickSpeed;
import servicecontrol.DelayActivity;
import swipe.DrawView;

import static android.content.Intent.FLAG_RECEIVER_FOREGROUND;
import static android.graphics.PixelFormat.TRANSLUCENT;
import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FORMAT_CHANGED;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;

public class FloatingViewService extends Service {

    public static boolean adding = true;
    public static boolean auto_click = false;
    public static int deviceHeight = 0;
    public static int deviceHorizontalCenter = 0;
    public static int deviceVerticalCenter = 0;
    public static int deviceWidth = 0;
    public static View expandedView = null;
    public static int expanded_shape = 0;
    public static View mFloatingView = null;
    public static Map<String, WindowManager.LayoutParams> mParams_auto = null;
    public static Map<String, ViewPointerAuto> mPointerView_auto = null;
    public static Map<String, ViewPointerAutoSwipe> mPointerView_auto_swipe = null;
    public static ViewTouchpad mTouchpadView = null;
    public static Map<String, View.OnTouchListener> mViewTouchListener_auto = null;
    public static ArrayList<View.OnTouchListener> mViewtouchListener_array = new ArrayList<>();
    public static WindowManager mWindowManager = null;
    public static WindowManager.LayoutParams params = null;
    public static int purchase_num = 10;
    public static boolean purchased = true;

    /* renamed from: px */
    public static float f35px = 0.0f;
    public static Map<String, Float> px_auto = null;

    /* renamed from: py */
    public static float f36py = 0.0f;
    public static Map<String, Float> py_auto = null;
    public static boolean rate_flag = false;
    public static SharedPreferences settings = null;
    public static ImageView share = null;
    public static ArrayList<Integer> swipe_target = new ArrayList<>();
    public static int target_cnt = 1;
    public static int target_limit = 48;
    public static int target_num = 0;
    public static int target_rotation = 1;
    public static int times = 0;
    public static boolean touch_first = false;
    DrawView drawView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            switch (((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation()) {
                case 0:
                    System.out.println("SCREEN_ORIENTATION_PORTRAIT");
                    params.gravity = 51;
                    break;
                case 1:
                    System.out.println("SCREEN_ORIENTATION_LANDSCAPE");
                    params.gravity = 51;
                    break;
                case 2:
                    System.out.println("SCREEN_ORIENTATION_REVERSE_PORTRAIT");
                    params.gravity = 85;
                    break;
                case 3:
                    System.out.println("SCREEN_ORIENTATION_REVERSE_LANDSCAPE");
                    params.gravity = 51;
                    break;
            }
            displayMetrics2();
            params.softInputMode = SOFT_INPUT_ADJUST_RESIZE;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(new ScreenReceiver(), new IntentFilter("android.intent.action.SCREEN_OFF"));
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);


        int i = TYPE_APPLICATION_OVERLAY;
        final int i2 = Build.VERSION.SDK_INT >= 26 ? TYPE_APPLICATION_OVERLAY : TYPE_SYSTEM_ALERT;
        if (Build.VERSION.SDK_INT < 26) {
            i = TYPE_SYSTEM_OVERLAY;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, i2, FLAG_NOT_FOCUSABLE, TRANSLUCENT);

        params = layoutParams;
        params.gravity = 51;
        params.x = 0;
        params.y = 100;
        mWindowManager.addView(mFloatingView, params);
        mPointerView_auto = new HashMap();
        mPointerView_auto_swipe = new HashMap();
        mParams_auto = new HashMap();
        mViewTouchListener_auto = new HashMap();
        px_auto = new HashMap();
        py_auto = new HashMap();
        try {
            displayMetrics2();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt("auto_target", 0);
        edit.apply();
        target_num = 0;
        AutoClickSpeed.clickpersecond = settings.getInt("clickpersecond", 30);
        AutoClickSpeed.duration_time = ((double) settings.getInt("duration", -1)) * 1000.0d;
        AutoClickSpeed.swipe_duration_time = (double) settings.getInt("swipe_duration", 300);
        expanded_shape = settings.getInt("expanded_shape", 1);

        purchased = true;
        if (purchased) {
            purchase_num = 48;
        }

        final View findViewById = mFloatingView.findViewById(R.id.collapse_view);
        if (expanded_shape == 1) {
            expandedView = mFloatingView.findViewById(R.id.expanded_container);
        } else if (expanded_shape == 2) {
            expandedView = mFloatingView.findViewById(R.id.vexpanded_container);
        }
        share = (ImageView) mFloatingView.findViewById(R.id.share_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "Auto Clicker - Super Fast");
                StringBuilder sb = new StringBuilder();
                sb.append("\nLet me recommend you this application\n\n");
                sb.append("https://play.google.com/store/apps/details?id=com.good.autoclicker \n\n");
                intent.putExtra("android.intent.extra.TEXT", sb.toString());
                FloatingViewService.this.startActivity(Intent.createChooser(intent, "Share via"));
            }
        });

        ((ImageView) mFloatingView.findViewById(R.id.close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i <= FloatingViewService.mPointerView_auto.size(); i++) {
                    Map<String, ViewPointerAuto> map = FloatingViewService.mPointerView_auto;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mPointerView_auto");
                    sb.append(i);
                    if (((ViewPointerAuto) map.get(sb.toString())).isShown()) {
                        WindowManager windowManager = FloatingViewService.mWindowManager;
                        Map<String, ViewPointerAuto> map2 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("mPointerView_auto");
                        sb2.append(i);
                        windowManager.removeViewImmediate((View) map2.get(sb2.toString()));
                    }
                }
                FloatingViewService.this.stopSelf();
            }
        });

        final int finalI1 = i;
        ((ImageView) mFloatingView.findViewById(R.id.add_swipe)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                FloatingViewService.target_num = FloatingViewService.settings.getInt("auto_target", 0);
                if (FloatingViewService.target_num < FloatingViewService.purchase_num) {
                    FloatingViewService.target_num++;
                    FloatingViewService.swipe_target.add(Integer.valueOf(FloatingViewService.target_num));
                    if (FloatingViewService.mPointerView_auto == null) {
                        FloatingViewService.mPointerView_auto = new HashMap();
                    }
                    if (FloatingViewService.mPointerView_auto_swipe == null) {
                        FloatingViewService.mPointerView_auto_swipe = new HashMap();
                    }
                    FloatingViewService.px_auto.put("px_auto" + String.valueOf(FloatingViewService.target_num), Float.valueOf(0.0f));
                    FloatingViewService.py_auto.put("py_auto" + String.valueOf(FloatingViewService.target_num), Float.valueOf(0.0f));
                    FloatingViewService.mPointerView_auto.put("mPointerView_auto" + String.valueOf(FloatingViewService.target_num), new ViewPointerAuto(FloatingViewService.this.getApplicationContext()));
                    FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).setClickable(true);
                    FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).setFocusable(true);
                    FloatingViewService.mParams_auto.put("mParams_auto" + String.valueOf(FloatingViewService.target_num), new WindowManager.LayoutParams(-2, -2, i2, 8, -3));
                    FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)).gravity = 51;
                    FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)).x = FloatingViewService.deviceHorizontalCenter - 50;
                    FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)).y = FloatingViewService.deviceVerticalCenter - 50;
                    final int identifier = FloatingViewService.this.getResources().getIdentifier("@drawable/sarget" + String.valueOf(FloatingViewService.target_num), (String) null, FloatingViewService.this.getPackageName());
                    FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).setBackgroundResource(identifier);
                    FloatingViewService.px_auto.put("px_auto_s" + String.valueOf(FloatingViewService.target_num), Float.valueOf(0.0f));
                    FloatingViewService.py_auto.put("py_auto_s" + String.valueOf(FloatingViewService.target_num), Float.valueOf(0.0f));
                    FloatingViewService.mPointerView_auto_swipe.put("mPointerView_auto_s" + String.valueOf(FloatingViewService.target_num), new ViewPointerAutoSwipe(FloatingViewService.this.getApplicationContext()));
                    FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(FloatingViewService.target_num)).setClickable(true);
                    FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(FloatingViewService.target_num)).setFocusable(true);
                    WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams(-1, -1, finalI1, 262200, -3);
                    FloatingViewService.mParams_auto.put("mParams_auto_s" + String.valueOf(FloatingViewService.target_num), layoutParams2);
                    FloatingViewService.mParams_auto.get("mParams_auto_s" + String.valueOf(FloatingViewService.target_num)).gravity = 51;
                    FloatingViewService.mParams_auto.get("mParams_auto_s" + String.valueOf(FloatingViewService.target_num)).x = FloatingViewService.deviceHorizontalCenter - 50;
                    FloatingViewService.mParams_auto.get("mParams_auto_s" + String.valueOf(FloatingViewService.target_num)).y = FloatingViewService.deviceVerticalCenter + 300;
                    Map<String, ViewPointerAutoSwipe> map = FloatingViewService.mPointerView_auto_swipe;
                    map.get("mPointerView_auto_s" + String.valueOf(FloatingViewService.target_num)).assign(FloatingViewService.mParams_auto.get("mParams_auto" + FloatingViewService.target_num).x + FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).getWidth(), FloatingViewService.mParams_auto.get("mParams_auto" + FloatingViewService.target_num).y + FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).getWidth(), FloatingViewService.mParams_auto.get("mParams_auto" + FloatingViewService.target_num).x + FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).getWidth(), FloatingViewService.mParams_auto.get("mParams_auto" + FloatingViewService.target_num).y + FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).getWidth());
                    Map<String, ViewPointerAutoSwipe> map2 = FloatingViewService.mPointerView_auto_swipe;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mPointerView_auto_s");
                    sb.append(String.valueOf(FloatingViewService.target_num));
                    ViewPointerAutoSwipe viewPointerAutoSwipe = map2.get(sb.toString());
                    ViewPointerAutoSwipe.draw_flag = false;
                    FloatingViewService.mWindowManager.addView(FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(FloatingViewService.target_num)), FloatingViewService.mParams_auto.get("mParams_auto_s" + String.valueOf(FloatingViewService.target_num)));
                    Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("mPointerView_auto");
                    sb2.append(String.valueOf(FloatingViewService.target_num));
                    map3.get(sb2.toString()).setOnTouchListener(new View.OnTouchListener() {
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
                                        edit.putInt("initialX", FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x);
                                        edit.putInt("initialY", FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y);
                                        edit.apply();
                                        this.initialX = FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x;
                                        this.initialY = FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y;
                                        this.initialPointX = motionEvent.getRawX();
                                        this.initialPointY = motionEvent.getRawY();
                                        FloatingViewService.px_auto.put("px_auto" + this.touch_target_num, Float.valueOf((float) FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x));
                                        FloatingViewService.py_auto.put("py_auto" + this.touch_target_num, Float.valueOf((float) FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y));
                                        FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(this.touch_target_num)).setPaint_Play();
                                        return false;
                                    case 1:
                                        Log.d("action_up", this.touch_target_num + " " + this.touch_target_num);
                                        FloatingViewService.px_auto.put("px_auto" + this.touch_target_num, Float.valueOf((float) FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x));
                                        FloatingViewService.py_auto.put("py_auto" + this.touch_target_num, Float.valueOf((float) FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y));
                                        int rawY = (int) (motionEvent.getRawY() - this.initialPointY);
                                        if (Math.abs((int) (motionEvent.getRawX() - this.initialPointX)) < 5 && Math.abs(rawY) < 5 && !FloatingViewService.auto_click) {
                                            Intent intent = new Intent(FloatingViewService.this.getApplicationContext(), DelayActivity.class);
                                            intent.setFlags(FLAG_RECEIVER_FOREGROUND);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("target_number", this.touch_target_num);
                                            intent.putExtras(bundle);
                                            FloatingViewService.this.startActivity(intent);
                                        }
                                        FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(this.touch_target_num)).setPaint_Not_Play();
                                        FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(this.touch_target_num)).invalidate();
                                        FloatingViewService.mWindowManager.updateViewLayout(FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + this.touch_target_num), FloatingViewService.mParams_auto.get("mParams_auto_s" + this.touch_target_num));
                                        return false;
                                    case 2:
                                        Log.d("action_move", this.touch_target_num + " " + this.touch_target_num);
                                        this.vectorX = (int) (motionEvent.getRawX() - this.initialPointX);
                                        this.vectorY = (int) (motionEvent.getRawY() - this.initialPointY);
                                        FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x = (int) (((float) this.initialX) + (((float) this.vectorX) * 1.0f));
                                        FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y = (int) (((float) this.initialY) + (((float) this.vectorY) * 1.0f));
                                        FloatingViewService.mWindowManager.updateViewLayout(FloatingViewService.mPointerView_auto.get("mPointerView_auto" + this.touch_target_num), FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num));
                                        Map<String, ViewPointerAutoSwipe> map = FloatingViewService.mPointerView_auto_swipe;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("mPointerView_auto_s");
                                        sb.append(String.valueOf(this.touch_target_num));
                                        ViewPointerAutoSwipe viewPointerAutoSwipe = map.get(sb.toString());
                                        ViewPointerAutoSwipe.draw_flag = true;
                                        Map<String, ViewPointerAutoSwipe> map2 = FloatingViewService.mPointerView_auto_swipe;
                                        map2.get("mPointerView_auto_s" + String.valueOf(this.touch_target_num)).assign(FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x + (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(this.touch_target_num)).getWidth() / 2), FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y + (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(this.touch_target_num)).getWidth() / 2), FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(Integer.valueOf(this.touch_target_num).intValue() + 1)).x + (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(this.touch_target_num)).getWidth() / 2), FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(Integer.valueOf(this.touch_target_num).intValue() + 1)).y + (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(this.touch_target_num)).getWidth() / 2));
                                        Map<String, ViewPointerAutoSwipe> map3 = FloatingViewService.mPointerView_auto_swipe;
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("mPointerView_auto_s");
                                        sb2.append(String.valueOf(this.touch_target_num));
                                        map3.get(sb2.toString()).invalidate();
                                        FloatingViewService.mWindowManager.updateViewLayout(FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + this.touch_target_num), FloatingViewService.mParams_auto.get("mParams_auto_s" + this.touch_target_num));
                                        return false;
                                    default:
                                        return false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                    });
                    if (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)) != null) {
                        if (!FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).isShown()) {
                            FloatingViewService.mWindowManager.addView(FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)), FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)));
                        }
                    }
                    edit.putInt("auto_target", FloatingViewService.target_num);
                    edit.apply();
                    Log.d("add_btn target_num : ", String.valueOf(FloatingViewService.target_num));
                    FloatingViewService.target_num++;
                    FloatingViewService.swipe_target.add(Integer.valueOf(FloatingViewService.target_num));
                    if (FloatingViewService.mPointerView_auto == null) {
                        FloatingViewService.mPointerView_auto = new HashMap();
                    }
                    if (FloatingViewService.mPointerView_auto_swipe == null) {
                        FloatingViewService.mPointerView_auto_swipe = new HashMap();
                    }
                    FloatingViewService.px_auto.put("px_auto" + String.valueOf(FloatingViewService.target_num), Float.valueOf(0.0f));
                    FloatingViewService.py_auto.put("py_auto" + String.valueOf(FloatingViewService.target_num), Float.valueOf(0.0f));
                    FloatingViewService.mPointerView_auto.put("mPointerView_auto" + String.valueOf(FloatingViewService.target_num), new ViewPointerAuto(FloatingViewService.this.getApplicationContext()));
                    FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).setClickable(true);
                    FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).setFocusable(true);
                    FloatingViewService.mParams_auto.put("mParams_auto" + String.valueOf(FloatingViewService.target_num), new WindowManager.LayoutParams(-2, -2, i2, 8, -3));
                    FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)).gravity = 51;
                    FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)).x = FloatingViewService.deviceHorizontalCenter - 50;
                    FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)).y = FloatingViewService.deviceVerticalCenter - 50;
                    final int identifier2 = FloatingViewService.this.getResources().getIdentifier("@drawable/sarget" + String.valueOf(FloatingViewService.target_num), (String) null, FloatingViewService.this.getPackageName());
                    FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).setBackgroundResource(identifier2);
                    FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).setOnTouchListener(new View.OnTouchListener() {
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
                                        edit.putInt("initialX", FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x);
                                        edit.putInt("initialY", FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y);
                                        edit.apply();
                                        this.initialX = FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x;
                                        this.initialY = FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y;
                                        this.initialPointX = motionEvent.getRawX();
                                        this.initialPointY = motionEvent.getRawY();
                                        FloatingViewService.px_auto.put("px_auto" + this.touch_target_num, Float.valueOf((float) FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x));
                                        FloatingViewService.py_auto.put("py_auto" + this.touch_target_num, Float.valueOf((float) FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y));
                                        FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1))).setPaint_Play();
                                        return false;
                                    case 1:
                                        Log.d("action_up", this.touch_target_num + " " + this.touch_target_num);
                                        FloatingViewService.px_auto.put("px_auto" + this.touch_target_num, Float.valueOf((float) FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x));
                                        FloatingViewService.py_auto.put("py_auto" + this.touch_target_num, Float.valueOf((float) FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y));
                                        int rawY = (int) (motionEvent.getRawY() - this.initialPointY);
                                        if (Math.abs((int) (motionEvent.getRawX() - this.initialPointX)) < 5 && Math.abs(rawY) < 5 && !FloatingViewService.auto_click) {
                                            Intent intent = new Intent(FloatingViewService.this.getApplicationContext(), DelayActivity.class);
                                            intent.setFlags(268435456);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("target_number", this.touch_target_num);
                                            intent.putExtras(bundle);
                                            FloatingViewService.this.startActivity(intent);
                                        }
                                        FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1))).setPaint_Not_Play();
                                        FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)).invalidate();
                                        FloatingViewService.mWindowManager.updateViewLayout(FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)), FloatingViewService.mParams_auto.get("mParams_auto_s" + String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)));
                                        return false;
                                    case 2:
                                        Log.d("action_move", this.touch_target_num + " " + this.touch_target_num);
                                        this.vectorX = (int) (motionEvent.getRawX() - this.initialPointX);
                                        this.vectorY = (int) (motionEvent.getRawY() - this.initialPointY);
                                        FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x = (int) (((float) this.initialX) + (((float) this.vectorX) * 1.0f));
                                        FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y = (int) (((float) this.initialY) + (((float) this.vectorY) * 1.0f));
                                        FloatingViewService.mWindowManager.updateViewLayout(FloatingViewService.mPointerView_auto.get("mPointerView_auto" + this.touch_target_num), FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num));
                                        Map<String, ViewPointerAutoSwipe> map = FloatingViewService.mPointerView_auto_swipe;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("mPointerView_auto_s");
                                        sb.append(String.valueOf(this.touch_target_num));
                                        ViewPointerAutoSwipe viewPointerAutoSwipe = map.get(sb.toString());
                                        ViewPointerAutoSwipe.draw_flag = true;
                                        Map<String, ViewPointerAutoSwipe> map2 = FloatingViewService.mPointerView_auto_swipe;
                                        map2.get("mPointerView_auto_s" + String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1))).assign(FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)).x + (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(this.touch_target_num)).getWidth() / 2), FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)).y + (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(this.touch_target_num)).getWidth() / 2), FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).x + (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(this.touch_target_num)).getWidth() / 2), FloatingViewService.mParams_auto.get("mParams_auto" + this.touch_target_num).y + (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(this.touch_target_num)).getWidth() / 2));
                                        Map<String, ViewPointerAutoSwipe> map3 = FloatingViewService.mPointerView_auto_swipe;
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("mPointerView_auto_s");
                                        sb2.append(String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)));
                                        map3.get(sb2.toString()).invalidate();
                                        FloatingViewService.mWindowManager.updateViewLayout(FloatingViewService.mPointerView_auto_swipe.get("mPointerView_auto_s" + String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)), FloatingViewService.mParams_auto.get("mParams_auto_s" + String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)));
                                        return false;
                                    default:
                                        return false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                    });
                    if (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)) != null) {
                        if (!FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).isShown()) {
                            FloatingViewService.mWindowManager.addView(FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)), FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)));
                        }
                    }
                    edit.putInt("auto_target", FloatingViewService.target_num);
                    edit.apply();
                    Log.d("add_btn target_num : ", String.valueOf(FloatingViewService.target_num));
                    FloatingViewService.adding = true;
                } else if (!FloatingViewService.purchased) {
                    Toast.makeText(FloatingViewService.this.getApplicationContext(), FloatingViewService.this.getResources().getText(R.string.purchase_yet), 0).show();
                }
            }
        });

//        ((ImageView) mFloatingView.findViewById(R.id.add_swipe)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                SharedPreferences.Editor edit = FloatingViewService.settings.edit();
//                FloatingViewService.target_num = FloatingViewService.settings.getInt("auto_target", 0);
//                StringBuilder sb = new StringBuilder();
//                sb.append(String.valueOf(FloatingViewService.target_num));
//                sb.append(" | ");
//                sb.append(String.valueOf(FloatingViewService.purchase_num));
//                if (FloatingViewService.target_num < FloatingViewService.purchase_num) {
//                    FloatingViewService.target_num++;
//                    FloatingViewService.swipe_target.add(Integer.valueOf(FloatingViewService.target_num));
//                    if (FloatingViewService.mPointerView_auto == null) {
//                        FloatingViewService.mPointerView_auto = new HashMap();
//                    }
//                    if (FloatingViewService.mPointerView_auto_swipe == null) {
//                        FloatingViewService.mPointerView_auto_swipe = new HashMap();
//                    }
//                    Map<String, Float> map = FloatingViewService.px_auto;
//                    StringBuilder sb2 = new StringBuilder();
//                    sb2.append("px_auto");
//                    sb2.append(String.valueOf(FloatingViewService.target_num));
//                    map.put(sb2.toString(), Float.valueOf(0.0f));
//                    Map<String, Float> map2 = FloatingViewService.py_auto;
//                    StringBuilder sb3 = new StringBuilder();
//                    sb3.append("py_auto");
//                    sb3.append(String.valueOf(FloatingViewService.target_num));
//                    map2.put(sb3.toString(), Float.valueOf(0.0f));
//                    Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb4 = new StringBuilder();
//                    sb4.append("mPointerView_auto");
//                    sb4.append(String.valueOf(FloatingViewService.target_num));
//                    map3.put(sb4.toString(), new ViewPointerAuto(FloatingViewService.this.getApplicationContext()));
//                    Map<String, ViewPointerAuto> map4 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb5 = new StringBuilder();
//                    sb5.append("mPointerView_auto");
//                    sb5.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAuto) map4.get(sb5.toString())).setClickable(true);
//                    Map<String, ViewPointerAuto> map5 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb6 = new StringBuilder();
//                    sb6.append("mPointerView_auto");
//                    sb6.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAuto) map5.get(sb6.toString())).setFocusable(true);
//                    Map<String, LayoutParams> map6 = FloatingViewService.mParams_auto;
//                    StringBuilder sb7 = new StringBuilder();
//                    sb7.append("mParams_auto");
//                    sb7.append(String.valueOf(FloatingViewService.target_num));
//                    String sb8 = sb7.toString();
//                    LayoutParams layoutParams = new LayoutParams(-2, -2, i2, 8, -3);
//                    map6.put(sb8, layoutParams);
//                    Map<String, LayoutParams> map7 = FloatingViewService.mParams_auto;
//                    StringBuilder sb9 = new StringBuilder();
//                    sb9.append("mParams_auto");
//                    sb9.append(String.valueOf(FloatingViewService.target_num));
//                    ((LayoutParams) map7.get(sb9.toString())).gravity = 51;
//                    Map<String, LayoutParams> map8 = FloatingViewService.mParams_auto;
//                    StringBuilder sb10 = new StringBuilder();
//                    sb10.append("mParams_auto");
//                    sb10.append(String.valueOf(FloatingViewService.target_num));
//                    ((LayoutParams) map8.get(sb10.toString())).x = FloatingViewService.deviceHorizontalCenter - 50;
//                    Map<String, LayoutParams> map9 = FloatingViewService.mParams_auto;
//                    StringBuilder sb11 = new StringBuilder();
//                    sb11.append("mParams_auto");
//                    sb11.append(String.valueOf(FloatingViewService.target_num));
//                    ((LayoutParams) map9.get(sb11.toString())).y = FloatingViewService.deviceVerticalCenter - 50;
//                    StringBuilder sb12 = new StringBuilder();
//                    sb12.append("@drawable/sarget");
//                    sb12.append(String.valueOf(FloatingViewService.target_num));
//                    final int identifier = FloatingViewService.this.getResources().getIdentifier(sb12.toString(), null, FloatingViewService.this.getPackageName());
//                    Map<String, ViewPointerAuto> map10 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb13 = new StringBuilder();
//                    sb13.append("mPointerView_auto");
//                    sb13.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAuto) map10.get(sb13.toString())).setBackgroundResource(identifier);
//                    Map<String, Float> map11 = FloatingViewService.px_auto;
//                    StringBuilder sb14 = new StringBuilder();
//                    sb14.append("px_auto_s");
//                    sb14.append(String.valueOf(FloatingViewService.target_num));
//                    map11.put(sb14.toString(), Float.valueOf(0.0f));
//                    Map<String, Float> map12 = FloatingViewService.py_auto;
//                    StringBuilder sb15 = new StringBuilder();
//                    sb15.append("py_auto_s");
//                    sb15.append(String.valueOf(FloatingViewService.target_num));
//                    map12.put(sb15.toString(), Float.valueOf(0.0f));
//                    Map<String, ViewPointerAutoSwipe> map13 = FloatingViewService.mPointerView_auto_swipe;
//                    StringBuilder sb16 = new StringBuilder();
//                    sb16.append("mPointerView_auto_s");
//                    sb16.append(String.valueOf(FloatingViewService.target_num));
//                    map13.put(sb16.toString(), new ViewPointerAutoSwipe(FloatingViewService.this.getApplicationContext()));
//                    Map<String, ViewPointerAutoSwipe> map14 = FloatingViewService.mPointerView_auto_swipe;
//                    StringBuilder sb17 = new StringBuilder();
//                    sb17.append("mPointerView_auto_s");
//                    sb17.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAutoSwipe) map14.get(sb17.toString())).setClickable(true);
//                    Map<String, ViewPointerAutoSwipe> map15 = FloatingViewService.mPointerView_auto_swipe;
//                    StringBuilder sb18 = new StringBuilder();
//                    sb18.append("mPointerView_auto_s");
//                    sb18.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAutoSwipe) map15.get(sb18.toString())).setFocusable(true);
//                    Map<String, LayoutParams> map16 = FloatingViewService.mParams_auto;
//                    StringBuilder sb19 = new StringBuilder();
//                    sb19.append("mParams_auto_s");
//                    sb19.append(String.valueOf(FloatingViewService.target_num));
//                    String sb20 = sb19.toString();
//                    LayoutParams layoutParams3 = new LayoutParams(-1, -1, finalI1, 262200, -3);
//                    map16.put(sb20, layoutParams3);
//                    Map<String, LayoutParams> map17 = FloatingViewService.mParams_auto;
//                    StringBuilder sb21 = new StringBuilder();
//                    sb21.append("mParams_auto_s");
//                    sb21.append(String.valueOf(FloatingViewService.target_num));
//                    ((LayoutParams) map17.get(sb21.toString())).gravity = 51;
//                    Map<String, LayoutParams> map18 = FloatingViewService.mParams_auto;
//                    StringBuilder sb22 = new StringBuilder();
//                    sb22.append("mParams_auto_s");
//                    sb22.append(String.valueOf(FloatingViewService.target_num));
//                    ((LayoutParams) map18.get(sb22.toString())).x = FloatingViewService.deviceHorizontalCenter - 50;
//                    Map<String, LayoutParams> map19 = FloatingViewService.mParams_auto;
//                    StringBuilder sb23 = new StringBuilder();
//                    sb23.append("mParams_auto_s");
//                    sb23.append(String.valueOf(FloatingViewService.target_num));
//                    ((LayoutParams) map19.get(sb23.toString())).y = FloatingViewService.deviceVerticalCenter + 300;
//                    Map<String, ViewPointerAutoSwipe> map20 = FloatingViewService.mPointerView_auto_swipe;
//                    StringBuilder sb24 = new StringBuilder();
//                    sb24.append("mPointerView_auto_s");
//                    sb24.append(String.valueOf(FloatingViewService.target_num));
//                    ViewPointerAutoSwipe viewPointerAutoSwipe = (ViewPointerAutoSwipe) map20.get(sb24.toString());
//                    Map<String, LayoutParams> map21 = FloatingViewService.mParams_auto;
//                    StringBuilder sb25 = new StringBuilder();
//                    sb25.append("mParams_auto");
//                    sb25.append(FloatingViewService.target_num);
//                    int i = ((LayoutParams) map21.get(sb25.toString())).x;
//                    Map<String, ViewPointerAuto> map22 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb26 = new StringBuilder();
//                    sb26.append("mPointerView_auto");
//                    sb26.append(String.valueOf(FloatingViewService.target_num));
//                    int width = i + ((ViewPointerAuto) map22.get(sb26.toString())).getWidth();
//                    Map<String, LayoutParams> map23 = FloatingViewService.mParams_auto;
//                    StringBuilder sb27 = new StringBuilder();
//                    sb27.append("mParams_auto");
//                    sb27.append(FloatingViewService.target_num);
//                    int i2 = ((LayoutParams) map23.get(sb27.toString())).y;
//                    Map<String, ViewPointerAuto> map24 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb28 = new StringBuilder();
//                    sb28.append("mPointerView_auto");
//                    sb28.append(String.valueOf(FloatingViewService.target_num));
//                    int width2 = i2 + ((ViewPointerAuto) map24.get(sb28.toString())).getWidth();
//                    Map<String, LayoutParams> map25 = FloatingViewService.mParams_auto;
//                    StringBuilder sb29 = new StringBuilder();
//                    sb29.append("mParams_auto");
//                    sb29.append(FloatingViewService.target_num);
//                    int i3 = ((LayoutParams) map25.get(sb29.toString())).x;
//                    Map<String, ViewPointerAuto> map26 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb30 = new StringBuilder();
//                    sb30.append("mPointerView_auto");
//                    sb30.append(String.valueOf(FloatingViewService.target_num));
//                    int width3 = i3 + ((ViewPointerAuto) map26.get(sb30.toString())).getWidth();
//                    Map<String, LayoutParams> map27 = FloatingViewService.mParams_auto;
//                    StringBuilder sb31 = new StringBuilder();
//                    sb31.append("mParams_auto");
//                    sb31.append(FloatingViewService.target_num);
//                    int i4 = ((LayoutParams) map27.get(sb31.toString())).y;
//                    Map<String, ViewPointerAuto> map28 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb32 = new StringBuilder();
//                    sb32.append("mPointerView_auto");
//                    sb32.append(String.valueOf(FloatingViewService.target_num));
//                    viewPointerAutoSwipe.assign(width, width2, width3, i4 + ((ViewPointerAuto) map28.get(sb32.toString())).getWidth());
//                    Map<String, ViewPointerAutoSwipe> map29 = FloatingViewService.mPointerView_auto_swipe;
//                    StringBuilder sb33 = new StringBuilder();
//                    sb33.append("mPointerView_auto_s");
//                    sb33.append(String.valueOf(FloatingViewService.target_num));
//                    ViewPointerAutoSwipe viewPointerAutoSwipe2 = (ViewPointerAutoSwipe) map29.get(sb33.toString());
//                    ViewPointerAutoSwipe.draw_flag = false;
//                    WindowManager windowManager = FloatingViewService.mWindowManager;
//                    Map<String, ViewPointerAutoSwipe> map30 = FloatingViewService.mPointerView_auto_swipe;
//                    StringBuilder sb34 = new StringBuilder();
//                    sb34.append("mPointerView_auto_s");
//                    sb34.append(String.valueOf(FloatingViewService.target_num));
//                    View view2 = (View) map30.get(sb34.toString());
//                    Map<String, LayoutParams> map31 = FloatingViewService.mParams_auto;
//                    StringBuilder sb35 = new StringBuilder();
//                    sb35.append("mParams_auto_s");
//                    sb35.append(String.valueOf(FloatingViewService.target_num));
//                    windowManager.addView(view2, (ViewGroup.LayoutParams) map31.get(sb35.toString()));
//                    Map<String, ViewPointerAuto> map32 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb36 = new StringBuilder();
//                    sb36.append("mPointerView_auto");
//                    sb36.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAuto) map32.get(sb36.toString())).setOnTouchListener(new View.OnTouchListener() {
//                        private float initialPointX;
//                        private float initialPointY;
//                        private int initialX;
//                        private int initialY;
//                        private String touch_target_num;
//                        private int vectorX;
//                        private int vectorY;
//
//                        public boolean onTouch(View view, MotionEvent motionEvent) {
//                            try {
//                                switch (motionEvent.getAction()) {
//                                    case 0:
//                                        String resourceEntryName = view.getResources().getResourceEntryName(identifier);
//                                        if (resourceEntryName.length() > 7) {
//                                            this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 2, resourceEntryName.length());
//                                            Log.d("target name : ", resourceEntryName);
//                                            Log.d("target sub name : ", this.touch_target_num);
//                                        } else {
//                                            this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 1, resourceEntryName.length());
//                                            Log.d("target name : ", resourceEntryName);
//                                            Log.d("target sub name : ", this.touch_target_num);
//                                        }
//                                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
//                                        Map<String, LayoutParams> map = FloatingViewService.mParams_auto;
//                                        StringBuilder sb = new StringBuilder();
//                                        sb.append("mParams_auto");
//                                        sb.append(this.touch_target_num);
//                                        edit.putInt("initialX", ((LayoutParams) map.get(sb.toString())).x);
//                                        Map<String, LayoutParams> map2 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb2 = new StringBuilder();
//                                        sb2.append("mParams_auto");
//                                        sb2.append(this.touch_target_num);
//                                        edit.putInt("initialY", ((LayoutParams) map2.get(sb2.toString())).y);
//                                        edit.apply();
//                                        Map<String, LayoutParams> map3 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb3 = new StringBuilder();
//                                        sb3.append("mParams_auto");
//                                        sb3.append(this.touch_target_num);
//                                        this.initialX = ((LayoutParams) map3.get(sb3.toString())).x;
//                                        Map<String, LayoutParams> map4 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb4 = new StringBuilder();
//                                        sb4.append("mParams_auto");
//                                        sb4.append(this.touch_target_num);
//                                        this.initialY = ((LayoutParams) map4.get(sb4.toString())).y;
//                                        this.initialPointX = motionEvent.getRawX();
//                                        this.initialPointY = motionEvent.getRawY();
//                                        Map<String, Float> map5 = FloatingViewService.px_auto;
//                                        StringBuilder sb5 = new StringBuilder();
//                                        sb5.append("px_auto");
//                                        sb5.append(this.touch_target_num);
//                                        String sb6 = sb5.toString();
//                                        Map<String, LayoutParams> map6 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb7 = new StringBuilder();
//                                        sb7.append("mParams_auto");
//                                        sb7.append(this.touch_target_num);
//                                        map5.put(sb6, Float.valueOf((float) ((LayoutParams) map6.get(sb7.toString())).x));
//                                        Map<String, Float> map7 = FloatingViewService.py_auto;
//                                        StringBuilder sb8 = new StringBuilder();
//                                        sb8.append("py_auto");
//                                        sb8.append(this.touch_target_num);
//                                        String sb9 = sb8.toString();
//                                        Map<String, LayoutParams> map8 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb10 = new StringBuilder();
//                                        sb10.append("mParams_auto");
//                                        sb10.append(this.touch_target_num);
//                                        map7.put(sb9, Float.valueOf((float) ((LayoutParams) map8.get(sb10.toString())).y));
//                                        Map<String, ViewPointerAutoSwipe> map9 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb11 = new StringBuilder();
//                                        sb11.append("mPointerView_auto_s");
//                                        sb11.append(String.valueOf(this.touch_target_num));
//                                        ((ViewPointerAutoSwipe) map9.get(sb11.toString())).setPaint_Play();
//                                        break;
//                                    case 1:
//                                        StringBuilder sb12 = new StringBuilder();
//                                        sb12.append(this.touch_target_num);
//                                        sb12.append(" ");
//                                        sb12.append(this.touch_target_num);
//                                        Log.d("action_up", sb12.toString());
//                                        Map<String, Float> map10 = FloatingViewService.px_auto;
//                                        StringBuilder sb13 = new StringBuilder();
//                                        sb13.append("px_auto");
//                                        sb13.append(this.touch_target_num);
//                                        String sb14 = sb13.toString();
//                                        Map<String, LayoutParams> map11 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb15 = new StringBuilder();
//                                        sb15.append("mParams_auto");
//                                        sb15.append(this.touch_target_num);
//                                        map10.put(sb14, Float.valueOf((float) ((LayoutParams) map11.get(sb15.toString())).x));
//                                        Map<String, Float> map12 = FloatingViewService.py_auto;
//                                        StringBuilder sb16 = new StringBuilder();
//                                        sb16.append("py_auto");
//                                        sb16.append(this.touch_target_num);
//                                        String sb17 = sb16.toString();
//                                        Map<String, LayoutParams> map13 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb18 = new StringBuilder();
//                                        sb18.append("mParams_auto");
//                                        sb18.append(this.touch_target_num);
//                                        map12.put(sb17, Float.valueOf((float) ((LayoutParams) map13.get(sb18.toString())).y));
//                                        int rawX = (int) (motionEvent.getRawX() - this.initialPointX);
//                                        int rawY = (int) (motionEvent.getRawY() - this.initialPointY);
//                                        if (Math.abs(rawX) < 5 && Math.abs(rawY) < 5 && !FloatingViewService.auto_click) {
//                                            Intent intent = new Intent(FloatingViewService.this.getApplicationContext(), DelayActivity.class);
//                                            intent.setFlags(FLAG_RECEIVER_FOREGROUND);
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString("target_number", this.touch_target_num);
//                                            intent.putExtras(bundle);
//                                            FloatingViewService.this.startActivity(intent);
//                                        }
//                                        Map<String, ViewPointerAutoSwipe> map14 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb19 = new StringBuilder();
//                                        sb19.append("mPointerView_auto_s");
//                                        sb19.append(String.valueOf(this.touch_target_num));
//                                        ((ViewPointerAutoSwipe) map14.get(sb19.toString())).setPaint_Not_Play();
//                                        Map<String, ViewPointerAutoSwipe> map15 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb20 = new StringBuilder();
//                                        sb20.append("mPointerView_auto_s");
//                                        sb20.append(String.valueOf(this.touch_target_num));
//                                        ((ViewPointerAutoSwipe) map15.get(sb20.toString())).invalidate();
//                                        WindowManager windowManager = FloatingViewService.mWindowManager;
//                                        Map<String, ViewPointerAutoSwipe> map16 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb21 = new StringBuilder();
//                                        sb21.append("mPointerView_auto_s");
//                                        sb21.append(this.touch_target_num);
//                                        View view2 = (View) map16.get(sb21.toString());
//                                        Map<String, LayoutParams> map17 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb22 = new StringBuilder();
//                                        sb22.append("mParams_auto_s");
//                                        sb22.append(this.touch_target_num);
//                                        windowManager.updateViewLayout(view2, (ViewGroup.LayoutParams) map17.get(sb22.toString()));
//                                        break;
//                                    case 2:
//                                        StringBuilder sb23 = new StringBuilder();
//                                        sb23.append(this.touch_target_num);
//                                        sb23.append(" ");
//                                        sb23.append(this.touch_target_num);
//                                        Log.d("action_move", sb23.toString());
//                                        this.vectorX = (int) (motionEvent.getRawX() - this.initialPointX);
//                                        this.vectorY = (int) (motionEvent.getRawY() - this.initialPointY);
//                                        Map<String, LayoutParams> map18 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb24 = new StringBuilder();
//                                        sb24.append("mParams_auto");
//                                        sb24.append(this.touch_target_num);
//                                        ((LayoutParams) map18.get(sb24.toString())).x = (int) (((float) this.initialX) + (((float) this.vectorX) * 1.0f));
//                                        Map<String, LayoutParams> map19 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb25 = new StringBuilder();
//                                        sb25.append("mParams_auto");
//                                        sb25.append(this.touch_target_num);
//                                        ((LayoutParams) map19.get(sb25.toString())).y = (int) (((float) this.initialY) + (((float) this.vectorY) * 1.0f));
//                                        WindowManager windowManager2 = FloatingViewService.mWindowManager;
//                                        Map<String, ViewPointerAuto> map20 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb26 = new StringBuilder();
//                                        sb26.append("mPointerView_auto");
//                                        sb26.append(this.touch_target_num);
//                                        View view3 = (View) map20.get(sb26.toString());
//                                        Map<String, LayoutParams> map21 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb27 = new StringBuilder();
//                                        sb27.append("mParams_auto");
//                                        sb27.append(this.touch_target_num);
//                                        windowManager2.updateViewLayout(view3, (ViewGroup.LayoutParams) map21.get(sb27.toString()));
//                                        Map<String, ViewPointerAutoSwipe> map22 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb28 = new StringBuilder();
//                                        sb28.append("mPointerView_auto_s");
//                                        sb28.append(String.valueOf(this.touch_target_num));
//                                        ViewPointerAutoSwipe viewPointerAutoSwipe = (ViewPointerAutoSwipe) map22.get(sb28.toString());
//                                        ViewPointerAutoSwipe.draw_flag = true;
//                                        Map<String, ViewPointerAutoSwipe> map23 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb29 = new StringBuilder();
//                                        sb29.append("mPointerView_auto_s");
//                                        sb29.append(String.valueOf(this.touch_target_num));
//                                        ViewPointerAutoSwipe viewPointerAutoSwipe2 = (ViewPointerAutoSwipe) map23.get(sb29.toString());
//                                        Map<String, LayoutParams> map24 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb30 = new StringBuilder();
//                                        sb30.append("mParams_auto");
//                                        sb30.append(this.touch_target_num);
//                                        int i = ((LayoutParams) map24.get(sb30.toString())).x;
//                                        Map<String, ViewPointerAuto> map25 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb31 = new StringBuilder();
//                                        sb31.append("mPointerView_auto");
//                                        sb31.append(String.valueOf(this.touch_target_num));
//                                        int width = i + (((ViewPointerAuto) map25.get(sb31.toString())).getWidth() / 2);
//                                        Map<String, LayoutParams> map26 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb32 = new StringBuilder();
//                                        sb32.append("mParams_auto");
//                                        sb32.append(this.touch_target_num);
//                                        int i2 = ((LayoutParams) map26.get(sb32.toString())).y;
//                                        Map<String, ViewPointerAuto> map27 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb33 = new StringBuilder();
//                                        sb33.append("mPointerView_auto");
//                                        sb33.append(String.valueOf(this.touch_target_num));
//                                        int width2 = i2 + (((ViewPointerAuto) map27.get(sb33.toString())).getWidth() / 2);
//                                        Map<String, LayoutParams> map28 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb34 = new StringBuilder();
//                                        sb34.append("mParams_auto");
//                                        sb34.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() + 1));
//                                        int i3 = ((LayoutParams) map28.get(sb34.toString())).x;
//                                        Map<String, ViewPointerAuto> map29 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb35 = new StringBuilder();
//                                        sb35.append("mPointerView_auto");
//                                        sb35.append(String.valueOf(this.touch_target_num));
//                                        int width3 = i3 + (((ViewPointerAuto) map29.get(sb35.toString())).getWidth() / 2);
//                                        Map<String, LayoutParams> map30 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb36 = new StringBuilder();
//                                        sb36.append("mParams_auto");
//                                        sb36.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() + 1));
//                                        int i4 = ((LayoutParams) map30.get(sb36.toString())).y;
//                                        Map<String, ViewPointerAuto> map31 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb37 = new StringBuilder();
//                                        sb37.append("mPointerView_auto");
//                                        sb37.append(String.valueOf(this.touch_target_num));
//                                        viewPointerAutoSwipe2.assign(width, width2, width3, i4 + (((ViewPointerAuto) map31.get(sb37.toString())).getWidth() / 2));
//                                        Map<String, ViewPointerAutoSwipe> map32 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb38 = new StringBuilder();
//                                        sb38.append("mPointerView_auto_s");
//                                        sb38.append(String.valueOf(this.touch_target_num));
//                                        ((ViewPointerAutoSwipe) map32.get(sb38.toString())).invalidate();
//                                        WindowManager windowManager3 = FloatingViewService.mWindowManager;
//                                        Map<String, ViewPointerAutoSwipe> map33 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb39 = new StringBuilder();
//                                        sb39.append("mPointerView_auto_s");
//                                        sb39.append(this.touch_target_num);
//                                        View view4 = (View) map33.get(sb39.toString());
//                                        Map<String, LayoutParams> map34 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb40 = new StringBuilder();
//                                        sb40.append("mParams_auto_s");
//                                        sb40.append(this.touch_target_num);
//                                        windowManager3.updateViewLayout(view4, (ViewGroup.LayoutParams) map34.get(sb40.toString()));
//                                        break;
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            return false;
//                        }
//                    });
//
//
//                    Map<String, ViewPointerAuto> map33 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb37 = new StringBuilder();
//                    sb37.append("mPointerView_auto");
//                    sb37.append(String.valueOf(FloatingViewService.target_num));
//                    if (map33.get(sb37.toString()) != null) {
//                        Map<String, ViewPointerAuto> map34 = FloatingViewService.mPointerView_auto;
//                        StringBuilder sb38 = new StringBuilder();
//                        sb38.append("mPointerView_auto");
//                        sb38.append(String.valueOf(FloatingViewService.target_num));
//                        if (!((ViewPointerAuto) map34.get(sb38.toString())).isShown()) {
//                            WindowManager windowManager2 = FloatingViewService.mWindowManager;
//                            Map<String, ViewPointerAuto> map35 = FloatingViewService.mPointerView_auto;
//                            StringBuilder sb39 = new StringBuilder();
//                            sb39.append("mPointerView_auto");
//                            sb39.append(String.valueOf(FloatingViewService.target_num));
//                            View view3 = (View) map35.get(sb39.toString());
//                            Map<String, LayoutParams> map36 = FloatingViewService.mParams_auto;
//                            StringBuilder sb40 = new StringBuilder();
//                            sb40.append("mParams_auto");
//                            sb40.append(String.valueOf(FloatingViewService.target_num));
//                            windowManager2.addView(view3, (ViewGroup.LayoutParams) map36.get(sb40.toString()));
//                        }
//                    }
//                    edit.putInt("auto_target", FloatingViewService.target_num);
//                    edit.apply();
//                    Log.d("add_btn target_num : ", String.valueOf(FloatingViewService.target_num));
//                    FloatingViewService.target_num++;
//                    FloatingViewService.swipe_target.add(Integer.valueOf(FloatingViewService.target_num));
//                    if (FloatingViewService.mPointerView_auto == null) {
//                        FloatingViewService.mPointerView_auto = new HashMap();
//                    }
//                    if (FloatingViewService.mPointerView_auto_swipe == null) {
//                        FloatingViewService.mPointerView_auto_swipe = new HashMap();
//                    }
//                    Map<String, Float> map37 = FloatingViewService.px_auto;
//                    StringBuilder sb41 = new StringBuilder();
//                    sb41.append("px_auto");
//                    sb41.append(String.valueOf(FloatingViewService.target_num));
//                    map37.put(sb41.toString(), Float.valueOf(0.0f));
//                    Map<String, Float> map38 = FloatingViewService.py_auto;
//                    StringBuilder sb42 = new StringBuilder();
//                    sb42.append("py_auto");
//                    sb42.append(String.valueOf(FloatingViewService.target_num));
//                    map38.put(sb42.toString(), Float.valueOf(0.0f));
//                    Map<String, ViewPointerAuto> map39 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb43 = new StringBuilder();
//                    sb43.append("mPointerView_auto");
//                    sb43.append(String.valueOf(FloatingViewService.target_num));
//                    map39.put(sb43.toString(), new ViewPointerAuto(FloatingViewService.this.getApplicationContext()));
//                    Map<String, ViewPointerAuto> map40 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb44 = new StringBuilder();
//                    sb44.append("mPointerView_auto");
//                    sb44.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAuto) map40.get(sb44.toString())).setClickable(true);
//                    Map<String, ViewPointerAuto> map41 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb45 = new StringBuilder();
//                    sb45.append("mPointerView_auto");
//                    sb45.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAuto) map41.get(sb45.toString())).setFocusable(true);
//                    Map<String, LayoutParams> map42 = FloatingViewService.mParams_auto;
//                    StringBuilder sb46 = new StringBuilder();
//                    sb46.append("mParams_auto");
//                    sb46.append(String.valueOf(FloatingViewService.target_num));
//                    String sb47 = sb46.toString();
//                    LayoutParams layoutParams4 = new LayoutParams(-2, -2, i2, 8, -3);
//                    map42.put(sb47, layoutParams4);
//                    Map<String, LayoutParams> map43 = FloatingViewService.mParams_auto;
//                    StringBuilder sb48 = new StringBuilder();
//                    sb48.append("mParams_auto");
//                    sb48.append(String.valueOf(FloatingViewService.target_num));
//                    ((LayoutParams) map43.get(sb48.toString())).gravity = 51;
//                    Map<String, LayoutParams> map44 = FloatingViewService.mParams_auto;
//                    StringBuilder sb49 = new StringBuilder();
//                    sb49.append("mParams_auto");
//                    sb49.append(String.valueOf(FloatingViewService.target_num));
//                    ((LayoutParams) map44.get(sb49.toString())).x = FloatingViewService.deviceHorizontalCenter - 50;
//                    Map<String, LayoutParams> map45 = FloatingViewService.mParams_auto;
//                    StringBuilder sb50 = new StringBuilder();
//                    sb50.append("mParams_auto");
//                    sb50.append(String.valueOf(FloatingViewService.target_num));
//                    ((LayoutParams) map45.get(sb50.toString())).y = FloatingViewService.deviceVerticalCenter - 50;
//                    StringBuilder sb51 = new StringBuilder();
//                    sb51.append("@drawable/sarget");
//                    sb51.append(String.valueOf(FloatingViewService.target_num));
//                    final int identifier2 = FloatingViewService.this.getResources().getIdentifier(sb51.toString(), null, FloatingViewService.this.getPackageName());
//                    Map<String, ViewPointerAuto> map46 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb52 = new StringBuilder();
//                    sb52.append("mPointerView_auto");
//                    sb52.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAuto) map46.get(sb52.toString())).setBackgroundResource(identifier2);
//                    Map<String, ViewPointerAuto> map47 = FloatingViewService.mPointerView_auto;
//                    StringBuilder sb53 = new StringBuilder();
//                    sb53.append("mPointerView_auto");
//                    sb53.append(String.valueOf(FloatingViewService.target_num));
//                    ((ViewPointerAuto) map47.get(sb53.toString())).setOnTouchListener(new View.OnTouchListener() {
//                        private float initialPointX;
//                        private float initialPointY;
//                        private int initialX;
//                        private int initialY;
//                        private String touch_target_num;
//                        private int vectorX;
//                        private int vectorY;
//
//                        public boolean onTouch(View view, MotionEvent motionEvent) {
//                            try {
//                                switch (motionEvent.getAction()) {
//                                    case 0:
//                                        String resourceEntryName = view.getResources().getResourceEntryName(identifier2);
//                                        if (resourceEntryName.length() > 7) {
//                                            this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 2, resourceEntryName.length());
//                                            Log.d("target name : ", resourceEntryName);
//                                            Log.d("target sub name : ", this.touch_target_num);
//                                        } else {
//                                            this.touch_target_num = resourceEntryName.substring(resourceEntryName.length() - 1, resourceEntryName.length());
//                                            Log.d("target name : ", resourceEntryName);
//                                            Log.d("target sub name : ", this.touch_target_num);
//                                        }
//                                        SharedPreferences.Editor edit = FloatingViewService.settings.edit();
//                                        Map<String, LayoutParams> map = FloatingViewService.mParams_auto;
//                                        StringBuilder sb = new StringBuilder();
//                                        sb.append("mParams_auto");
//                                        sb.append(this.touch_target_num);
//                                        edit.putInt("initialX", ((LayoutParams) map.get(sb.toString())).x);
//                                        Map<String, LayoutParams> map2 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb2 = new StringBuilder();
//                                        sb2.append("mParams_auto");
//                                        sb2.append(this.touch_target_num);
//                                        edit.putInt("initialY", ((LayoutParams) map2.get(sb2.toString())).y);
//                                        edit.apply();
//                                        Map<String, LayoutParams> map3 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb3 = new StringBuilder();
//                                        sb3.append("mParams_auto");
//                                        sb3.append(this.touch_target_num);
//                                        this.initialX = ((LayoutParams) map3.get(sb3.toString())).x;
//                                        Map<String, LayoutParams> map4 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb4 = new StringBuilder();
//                                        sb4.append("mParams_auto");
//                                        sb4.append(this.touch_target_num);
//                                        this.initialY = ((LayoutParams) map4.get(sb4.toString())).y;
//                                        this.initialPointX = motionEvent.getRawX();
//                                        this.initialPointY = motionEvent.getRawY();
//                                        Map<String, Float> map5 = FloatingViewService.px_auto;
//                                        StringBuilder sb5 = new StringBuilder();
//                                        sb5.append("px_auto");
//                                        sb5.append(this.touch_target_num);
//                                        String sb6 = sb5.toString();
//                                        Map<String, LayoutParams> map6 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb7 = new StringBuilder();
//                                        sb7.append("mParams_auto");
//                                        sb7.append(this.touch_target_num);
//                                        map5.put(sb6, Float.valueOf((float) ((LayoutParams) map6.get(sb7.toString())).x));
//                                        Map<String, Float> map7 = FloatingViewService.py_auto;
//                                        StringBuilder sb8 = new StringBuilder();
//                                        sb8.append("py_auto");
//                                        sb8.append(this.touch_target_num);
//                                        String sb9 = sb8.toString();
//                                        Map<String, LayoutParams> map8 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb10 = new StringBuilder();
//                                        sb10.append("mParams_auto");
//                                        sb10.append(this.touch_target_num);
//                                        map7.put(sb9, Float.valueOf((float) ((LayoutParams) map8.get(sb10.toString())).y));
//                                        Map<String, ViewPointerAutoSwipe> map9 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb11 = new StringBuilder();
//                                        sb11.append("mPointerView_auto_s");
//                                        sb11.append(String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)));
//                                        ((ViewPointerAutoSwipe) map9.get(sb11.toString())).setPaint_Play();
//                                        break;
//                                    case 1:
//                                        StringBuilder sb12 = new StringBuilder();
//                                        sb12.append(this.touch_target_num);
//                                        sb12.append(" ");
//                                        sb12.append(this.touch_target_num);
//                                        Log.d("action_up", sb12.toString());
//                                        Map<String, Float> map10 = FloatingViewService.px_auto;
//                                        StringBuilder sb13 = new StringBuilder();
//                                        sb13.append("px_auto");
//                                        sb13.append(this.touch_target_num);
//                                        String sb14 = sb13.toString();
//                                        Map<String, LayoutParams> map11 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb15 = new StringBuilder();
//                                        sb15.append("mParams_auto");
//                                        sb15.append(this.touch_target_num);
//                                        map10.put(sb14, Float.valueOf((float) ((LayoutParams) map11.get(sb15.toString())).x));
//                                        Map<String, Float> map12 = FloatingViewService.py_auto;
//                                        StringBuilder sb16 = new StringBuilder();
//                                        sb16.append("py_auto");
//                                        sb16.append(this.touch_target_num);
//                                        String sb17 = sb16.toString();
//                                        Map<String, LayoutParams> map13 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb18 = new StringBuilder();
//                                        sb18.append("mParams_auto");
//                                        sb18.append(this.touch_target_num);
//                                        map12.put(sb17, Float.valueOf((float) ((LayoutParams) map13.get(sb18.toString())).y));
//                                        int rawX = (int) (motionEvent.getRawX() - this.initialPointX);
//                                        int rawY = (int) (motionEvent.getRawY() - this.initialPointY);
//                                        if (Math.abs(rawX) < 5 && Math.abs(rawY) < 5 && !FloatingViewService.auto_click) {
//                                            Intent intent = new Intent(FloatingViewService.this.getApplicationContext(), DelayActivity.class);
//                                            intent.setFlags(FLAG_RECEIVER_FOREGROUND);
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString("target_number", this.touch_target_num);
//                                            intent.putExtras(bundle);
//                                            FloatingViewService.this.startActivity(intent);
//                                        }
//                                        Map<String, ViewPointerAutoSwipe> map14 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb19 = new StringBuilder();
//                                        sb19.append("mPointerView_auto_s");
//                                        sb19.append(String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)));
//                                        ((ViewPointerAutoSwipe) map14.get(sb19.toString())).setPaint_Not_Play();
//                                        Map<String, ViewPointerAutoSwipe> map15 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb20 = new StringBuilder();
//                                        sb20.append("mPointerView_auto_s");
//                                        sb20.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
//                                        ((ViewPointerAutoSwipe) map15.get(sb20.toString())).invalidate();
//                                        WindowManager windowManager = FloatingViewService.mWindowManager;
//                                        Map<String, ViewPointerAutoSwipe> map16 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb21 = new StringBuilder();
//                                        sb21.append("mPointerView_auto_s");
//                                        sb21.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
//                                        View view2 = (View) map16.get(sb21.toString());
//                                        Map<String, LayoutParams> map17 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb22 = new StringBuilder();
//                                        sb22.append("mParams_auto_s");
//                                        sb22.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
//                                        windowManager.updateViewLayout(view2, (ViewGroup.LayoutParams) map17.get(sb22.toString()));
//                                        break;
//                                    case 2:
//                                        StringBuilder sb23 = new StringBuilder();
//                                        sb23.append(this.touch_target_num);
//                                        sb23.append(" ");
//                                        sb23.append(this.touch_target_num);
//                                        Log.d("action_move", sb23.toString());
//                                        this.vectorX = (int) (motionEvent.getRawX() - this.initialPointX);
//                                        this.vectorY = (int) (motionEvent.getRawY() - this.initialPointY);
//                                        Map<String, LayoutParams> map18 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb24 = new StringBuilder();
//                                        sb24.append("mParams_auto");
//                                        sb24.append(this.touch_target_num);
//                                        ((LayoutParams) map18.get(sb24.toString())).x = (int) (((float) this.initialX) + (((float) this.vectorX) * 1.0f));
//                                        Map<String, LayoutParams> map19 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb25 = new StringBuilder();
//                                        sb25.append("mParams_auto");
//                                        sb25.append(this.touch_target_num);
//                                        ((LayoutParams) map19.get(sb25.toString())).y = (int) (((float) this.initialY) + (((float) this.vectorY) * 1.0f));
//                                        WindowManager windowManager2 = FloatingViewService.mWindowManager;
//                                        Map<String, ViewPointerAuto> map20 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb26 = new StringBuilder();
//                                        sb26.append("mPointerView_auto");
//                                        sb26.append(this.touch_target_num);
//                                        View view3 = (View) map20.get(sb26.toString());
//                                        Map<String, LayoutParams> map21 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb27 = new StringBuilder();
//                                        sb27.append("mParams_auto");
//                                        sb27.append(this.touch_target_num);
//                                        windowManager2.updateViewLayout(view3, (ViewGroup.LayoutParams) map21.get(sb27.toString()));
//                                        Map<String, ViewPointerAutoSwipe> map22 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb28 = new StringBuilder();
//                                        sb28.append("mPointerView_auto_s");
//                                        sb28.append(String.valueOf(this.touch_target_num));
//                                        ViewPointerAutoSwipe viewPointerAutoSwipe = (ViewPointerAutoSwipe) map22.get(sb28.toString());
//                                        ViewPointerAutoSwipe.draw_flag = true;
//                                        Map<String, ViewPointerAutoSwipe> map23 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb29 = new StringBuilder();
//                                        sb29.append("mPointerView_auto_s");
//                                        sb29.append(String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)));
//                                        ViewPointerAutoSwipe viewPointerAutoSwipe2 = (ViewPointerAutoSwipe) map23.get(sb29.toString());
//                                        Map<String, LayoutParams> map24 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb30 = new StringBuilder();
//                                        sb30.append("mParams_auto");
//                                        sb30.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
//                                        int i = ((LayoutParams) map24.get(sb30.toString())).x;
//                                        Map<String, ViewPointerAuto> map25 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb31 = new StringBuilder();
//                                        sb31.append("mPointerView_auto");
//                                        sb31.append(String.valueOf(this.touch_target_num));
//                                        int width = i + (((ViewPointerAuto) map25.get(sb31.toString())).getWidth() / 2);
//                                        Map<String, LayoutParams> map26 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb32 = new StringBuilder();
//                                        sb32.append("mParams_auto");
//                                        sb32.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
//                                        int i2 = ((LayoutParams) map26.get(sb32.toString())).y;
//                                        Map<String, ViewPointerAuto> map27 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb33 = new StringBuilder();
//                                        sb33.append("mPointerView_auto");
//                                        sb33.append(String.valueOf(this.touch_target_num));
//                                        int width2 = i2 + (((ViewPointerAuto) map27.get(sb33.toString())).getWidth() / 2);
//                                        Map<String, LayoutParams> map28 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb34 = new StringBuilder();
//                                        sb34.append("mParams_auto");
//                                        sb34.append(this.touch_target_num);
//                                        int i3 = ((LayoutParams) map28.get(sb34.toString())).x;
//                                        Map<String, ViewPointerAuto> map29 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb35 = new StringBuilder();
//                                        sb35.append("mPointerView_auto");
//                                        sb35.append(String.valueOf(this.touch_target_num));
//                                        int width3 = i3 + (((ViewPointerAuto) map29.get(sb35.toString())).getWidth() / 2);
//                                        Map<String, LayoutParams> map30 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb36 = new StringBuilder();
//                                        sb36.append("mParams_auto");
//                                        sb36.append(this.touch_target_num);
//                                        int i4 = ((LayoutParams) map30.get(sb36.toString())).y;
//                                        Map<String, ViewPointerAuto> map31 = FloatingViewService.mPointerView_auto;
//                                        StringBuilder sb37 = new StringBuilder();
//                                        sb37.append("mPointerView_auto");
//                                        sb37.append(String.valueOf(this.touch_target_num));
//                                        viewPointerAutoSwipe2.assign(width, width2, width3, i4 + (((ViewPointerAuto) map31.get(sb37.toString())).getWidth() / 2));
//                                        Map<String, ViewPointerAutoSwipe> map32 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb38 = new StringBuilder();
//                                        sb38.append("mPointerView_auto_s");
//                                        sb38.append(String.valueOf(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1)));
//                                        ((ViewPointerAutoSwipe) map32.get(sb38.toString())).invalidate();
//                                        WindowManager windowManager3 = FloatingViewService.mWindowManager;
//                                        Map<String, ViewPointerAutoSwipe> map33 = FloatingViewService.mPointerView_auto_swipe;
//                                        StringBuilder sb39 = new StringBuilder();
//                                        sb39.append("mPointerView_auto_s");
//                                        sb39.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
//                                        View view4 = (View) map33.get(sb39.toString());
//                                        Map<String, LayoutParams> map34 = FloatingViewService.mParams_auto;
//                                        StringBuilder sb40 = new StringBuilder();
//                                        sb40.append("mParams_auto_s");
//                                        sb40.append(String.valueOf(Integer.valueOf(this.touch_target_num).intValue() - 1));
//                                        windowManager3.updateViewLayout(view4, (ViewGroup.LayoutParams) map34.get(sb40.toString()));
//                                        break;
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            return false;
//                        }
//                    });
//                    if (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)) != null) {
//                        if (!FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).isShown()) {
//                            FloatingViewService.mWindowManager.addView(FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)), FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)));
//                        }
//                    }
////                    Map<String, ViewPointerAuto> map48 = FloatingViewService.mPointerView_auto;
////                    StringBuilder sb54 = new StringBuilder();
////                    sb54.append("mPointerView_auto");
////                    sb54.append(String.valueOf(FloatingViewService.target_num));
////                    if (map48.get(sb54.toString()) != null) {
////                        Map<String, ViewPointerAuto> map49 = FloatingViewService.mPointerView_auto;
////                        StringBuilder sb55 = new StringBuilder();
////                        sb55.append("mPointerView_auto");
////                        sb55.append(String.valueOf(FloatingViewService.target_num));
////                        if (!((ViewPointerAuto) map49.get(sb55.toString())).isShown()) {
////                            WindowManager windowManager3 = FloatingViewService.mWindowManager;
////                            Map<String, ViewPointerAuto> map50 = FloatingViewService.mPointerView_auto;
////                            StringBuilder sb56 = new StringBuilder();
////                            sb56.append("mPointerView_auto");
////                            sb56.append(String.valueOf(FloatingViewService.target_num));
////                            View view4 = (View) map50.get(sb56.toString());
////                            Map<String, LayoutParams> map51 = FloatingViewService.mParams_auto;
////                            StringBuilder sb57 = new StringBuilder();
////                            sb57.append("mParams_auto");
////                            sb57.append(String.valueOf(FloatingViewService.target_num));
////                            windowManager3.addView(view4, (ViewGroup.LayoutParams) map51.get(sb57.toString()));
////                        }
////                    }
//                    edit.putInt("auto_target", FloatingViewService.target_num);
//                    edit.apply();
//                    Log.d("add_btn target_num : ", String.valueOf(FloatingViewService.target_num));
//                    FloatingViewService.adding = true;
//                }
//            }
//        });


        ((ImageView) mFloatingView.findViewById(R.id.add_btn)).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                FloatingViewService.target_num = FloatingViewService.settings.getInt("auto_target", 0);
                StringBuilder sb = new StringBuilder();
                sb.append(String.valueOf(FloatingViewService.target_num));
                sb.append(" | ");
                sb.append(String.valueOf(FloatingViewService.purchase_num));
                Log.d("target_num | purchase_num", sb.toString());
                if (FloatingViewService.target_num < FloatingViewService.purchase_num) {
                    FloatingViewService.target_num++;
                    if (FloatingViewService.mPointerView_auto == null) {
                        FloatingViewService.mPointerView_auto = new HashMap();
                    }
                    Map<String, Float> map = FloatingViewService.px_auto;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("px_auto");
                    sb2.append(String.valueOf(FloatingViewService.target_num));
                    map.put(sb2.toString(), Float.valueOf(0.0f));
                    Map<String, Float> map2 = FloatingViewService.py_auto;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("py_auto");
                    sb3.append(String.valueOf(FloatingViewService.target_num));
                    map2.put(sb3.toString(), Float.valueOf(0.0f));
                    Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("mPointerView_auto");
                    sb4.append(String.valueOf(FloatingViewService.target_num));
                    map3.put(sb4.toString(), new ViewPointerAuto(FloatingViewService.this.getApplicationContext()));
                    Map<String, ViewPointerAuto> map4 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("mPointerView_auto");
                    sb5.append(String.valueOf(FloatingViewService.target_num));
                    ((ViewPointerAuto) map4.get(sb5.toString())).setClickable(true);
                    Map<String, ViewPointerAuto> map5 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("mPointerView_auto");
                    sb6.append(String.valueOf(FloatingViewService.target_num));
                    ((ViewPointerAuto) map5.get(sb6.toString())).setFocusable(true);
                    Map<String, LayoutParams> map6 = FloatingViewService.mParams_auto;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("mParams_auto");
                    sb7.append(String.valueOf(FloatingViewService.target_num));
                    String sb8 = sb7.toString();
                    LayoutParams layoutParams = new LayoutParams(-2, -2, i2, 8, -3);
                    map6.put(sb8, layoutParams);
                    Map<String, LayoutParams> map7 = FloatingViewService.mParams_auto;
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("mParams_auto");
                    sb9.append(String.valueOf(FloatingViewService.target_num));
                    ((LayoutParams) map7.get(sb9.toString())).gravity = 51;
                    Map<String, LayoutParams> map8 = FloatingViewService.mParams_auto;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("mParams_auto");
                    sb10.append(String.valueOf(FloatingViewService.target_num));
                    ((LayoutParams) map8.get(sb10.toString())).x = FloatingViewService.deviceHorizontalCenter - 50;
                    Map<String, LayoutParams> map9 = FloatingViewService.mParams_auto;
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append("mParams_auto");
                    sb11.append(String.valueOf(FloatingViewService.target_num));
                    ((LayoutParams) map9.get(sb11.toString())).y = FloatingViewService.deviceVerticalCenter - 50;
                    StringBuilder sb12 = new StringBuilder();
                    sb12.append("@drawable/target");
                    sb12.append(String.valueOf(FloatingViewService.target_num));
                    String sb13 = sb12.toString();
                    Log.d("drawable uri : ", sb13);
                    final int identifier = FloatingViewService.this.getResources().getIdentifier(sb13, null, FloatingViewService.this.getPackageName());
                    Log.d("imageResource : ", String.valueOf(identifier));
                    Map<String, ViewPointerAuto> map10 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb14 = new StringBuilder();
                    sb14.append("mPointerView_auto");
                    sb14.append(String.valueOf(FloatingViewService.target_num));
                    ((ViewPointerAuto) map10.get(sb14.toString())).setBackgroundResource(identifier);
                    Map<String, ViewPointerAuto> map11 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb15 = new StringBuilder();
                    sb15.append("mPointerView_auto");
                    sb15.append(String.valueOf(FloatingViewService.target_num));
                    ((ViewPointerAuto) map11.get(sb15.toString())).setOnTouchListener(new View.OnTouchListener() {
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
                                            Intent intent = new Intent(FloatingViewService.this.getApplicationContext(), DelayActivity.class);
                                            intent.setFlags(FLAG_RECEIVER_FOREGROUND);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("target_number", this.touch_target_num);
                                            intent.putExtras(bundle);
                                            FloatingViewService.this.startActivity(intent);
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
                                        WindowManager windowManager = FloatingViewService.mWindowManager;
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
//                    if (FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)) != null) {
//                        if (!FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)).isShown()) {
//                            FloatingViewService.mWindowManager.addView(FloatingViewService.mPointerView_auto.get("mPointerView_auto" + String.valueOf(FloatingViewService.target_num)), FloatingViewService.mParams_auto.get("mParams_auto" + String.valueOf(FloatingViewService.target_num)));
//                        }
//                    }

                    Map<String, ViewPointerAuto> map12 = FloatingViewService.mPointerView_auto;
                    StringBuilder sb16 = new StringBuilder();
                    sb16.append("mPointerView_auto");
                    sb16.append(String.valueOf(FloatingViewService.target_num));
                    if (map12.get(sb16.toString()) != null) {
                        Map<String, ViewPointerAuto> map13 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb17 = new StringBuilder();
                        sb17.append("mPointerView_auto");
                        sb17.append(String.valueOf(FloatingViewService.target_num));
                        if (!((ViewPointerAuto) map13.get(sb17.toString())).isShown()) {
                            WindowManager windowManager = FloatingViewService.mWindowManager;
                            Map<String, ViewPointerAuto> map14 = FloatingViewService.mPointerView_auto;
                            StringBuilder sb18 = new StringBuilder();
                            sb18.append("mPointerView_auto");
                            sb18.append(String.valueOf(FloatingViewService.target_num));
                            View view2 = (View) map14.get(sb18.toString());
                            Map<String, LayoutParams> map15 = FloatingViewService.mParams_auto;
                            StringBuilder sb19 = new StringBuilder();
                            sb19.append("mParams_auto");
                            sb19.append(String.valueOf(FloatingViewService.target_num));
                            windowManager.addView(view2, (ViewGroup.LayoutParams) map15.get(sb19.toString()));
                        }
                    }
                    edit.putInt("auto_target", FloatingViewService.target_num);
                    edit.apply();
                    Log.d("add_btn target_num : ", String.valueOf(FloatingViewService.target_num));
                    FloatingViewService.adding = true;
                }
            }
        });

        ImageView imageView = (ImageView) mFloatingView.findViewById(R.id.remove_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                FloatingViewService.target_num = FloatingViewService.settings.getInt("auto_target", 0);
                if (FloatingViewService.target_num > 0 && FloatingViewService.mPointerView_auto != null) {
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
                                WindowManager windowManager = FloatingViewService.mWindowManager;
                                Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("mPointerView_auto");
                                sb3.append(String.valueOf(FloatingViewService.target_num));
                                windowManager.removeView((View) map3.get(sb3.toString()));
                                WindowManager windowManager2 = FloatingViewService.mWindowManager;
                                Map<String, ViewPointerAutoSwipe> map4 = FloatingViewService.mPointerView_auto_swipe;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("mPointerView_auto_s");
                                sb4.append(String.valueOf(FloatingViewService.target_num - 1));
                                windowManager2.removeView((View) map4.get(sb4.toString()));
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("mPointer_Auto_Before");
                                sb5.append(String.valueOf(FloatingViewService.target_num));
                                edit.putInt(sb5.toString(), 0);
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("mPointer_Auto_Before");
                                sb6.append(String.valueOf(FloatingViewService.target_num - 1));
                                edit.putInt(sb6.toString(), 0);
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append("mPointer_Auto_After");
                                sb7.append(String.valueOf(FloatingViewService.target_num));
                                edit.putInt(sb7.toString(), 0);
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append("mPointer_Auto_After");
                                sb8.append(String.valueOf(FloatingViewService.target_num - 1));
                                edit.putInt(sb8.toString(), 0);
                                edit.putInt("auto_target", FloatingViewService.target_num - 1);
                                edit.apply();
                                FloatingViewService.target_num--;
                                Log.d("target_num-- : ", String.valueOf(FloatingViewService.target_num));
                            }
                        }
                        Map<String, ViewPointerAuto> map5 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("mPointerView_auto");
                        sb9.append(String.valueOf(FloatingViewService.target_num));
                        if (map5.get(sb9.toString()) != null) {
                            Map<String, ViewPointerAuto> map6 = FloatingViewService.mPointerView_auto;
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append("mPointerView_auto");
                            sb10.append(String.valueOf(FloatingViewService.target_num));
                            if (((ViewPointerAuto) map6.get(sb10.toString())).isShown()) {
                                WindowManager windowManager3 = FloatingViewService.mWindowManager;
                                Map<String, ViewPointerAuto> map7 = FloatingViewService.mPointerView_auto;
                                StringBuilder sb11 = new StringBuilder();
                                sb11.append("mPointerView_auto");
                                sb11.append(String.valueOf(FloatingViewService.target_num));
                                windowManager3.removeView((View) map7.get(sb11.toString()));
                                StringBuilder sb12 = new StringBuilder();
                                sb12.append("mPointer_Auto_Before");
                                sb12.append(String.valueOf(FloatingViewService.target_num));
                                edit.putInt(sb12.toString(), 0);
                                StringBuilder sb13 = new StringBuilder();
                                sb13.append("mPointer_Auto_After");
                                sb13.append(String.valueOf(FloatingViewService.target_num));
                                edit.putInt(sb13.toString(), 0);
                                edit.putInt("auto_target", FloatingViewService.target_num - 1);
                                edit.apply();
                                FloatingViewService.target_num--;
                                Log.d("target_num-- : ", String.valueOf(FloatingViewService.target_num));
                            }
                        }
                    } else {
                        Map<String, ViewPointerAuto> map8 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb14 = new StringBuilder();
                        sb14.append("mPointerView_auto");
                        sb14.append(String.valueOf(FloatingViewService.target_num));
                        if (map8.get(sb14.toString()) != null) {
                            Map<String, ViewPointerAuto> map9 = FloatingViewService.mPointerView_auto;
                            StringBuilder sb15 = new StringBuilder();
                            sb15.append("mPointerView_auto");
                            sb15.append(String.valueOf(FloatingViewService.target_num));
                            if (((ViewPointerAuto) map9.get(sb15.toString())).isShown()) {
                                WindowManager windowManager4 = FloatingViewService.mWindowManager;
                                Map<String, ViewPointerAuto> map10 = FloatingViewService.mPointerView_auto;
                                StringBuilder sb16 = new StringBuilder();
                                sb16.append("mPointerView_auto");
                                sb16.append(String.valueOf(FloatingViewService.target_num));
                                windowManager4.removeView((View) map10.get(sb16.toString()));
                                StringBuilder sb17 = new StringBuilder();
                                sb17.append("mPointer_Auto_Before");
                                sb17.append(String.valueOf(FloatingViewService.target_num));
                                edit.putInt(sb17.toString(), 0);
                                StringBuilder sb18 = new StringBuilder();
                                sb18.append("mPointer_Auto_After");
                                sb18.append(String.valueOf(FloatingViewService.target_num));
                                edit.putInt(sb18.toString(), 0);
                                edit.putInt("auto_target", FloatingViewService.target_num - 1);
                                edit.apply();
                                FloatingViewService.target_num--;
                                Log.d("target_num-- : ", String.valueOf(FloatingViewService.target_num));
                            }
                        }
                    }
                }
                if (FloatingViewService.target_num == 0) {
                    for (int i2 = 0; i2 < FloatingViewService.swipe_target.size(); i2++) {
                        FloatingViewService.swipe_target.remove(i2);
                    }
                    FloatingViewService.mPointerView_auto = null;
                    FloatingViewService.mPointerView_auto_swipe = null;
                }
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onLongClick(View view) {
                int i;
                SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                while (true) {
                    if (FloatingViewService.target_num == 0 || FloatingViewService.mPointerView_auto == null) {
                    } else {
                        FloatingViewService.target_num = FloatingViewService.settings.getInt("auto_target", 0);
                        Log.d("remove_btn  target_num : ", String.valueOf(FloatingViewService.target_num));
                        boolean z = false;
                        for (int i2 = 0; i2 < FloatingViewService.swipe_target.size(); i2++) {
                            if (FloatingViewService.target_num == ((Integer) FloatingViewService.swipe_target.get(i2)).intValue()) {
                                Log.d("target_num == swipe_target.get : ", String.valueOf(FloatingViewService.target_num));
                                FloatingViewService.swipe_target.remove(i2);
                                FloatingViewService.swipe_target.remove(i2 - 1);
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
                                    WindowManager windowManager = FloatingViewService.mWindowManager;
                                    Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("mPointerView_auto");
                                    sb3.append(String.valueOf(FloatingViewService.target_num));
                                    windowManager.removeView((View) map3.get(sb3.toString()));
                                    WindowManager windowManager2 = FloatingViewService.mWindowManager;
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
                                    WindowManager windowManager3 = FloatingViewService.mWindowManager;
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
                                    WindowManager windowManager4 = FloatingViewService.mWindowManager;
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
//                for (int i3 = 0; i3 < FloatingViewService.swipe_target.size(); i3++) {
//                    FloatingViewService.swipe_target.remove(i3);
//                }
//                for (i = 1; i < 48; i++) {
//                    StringBuilder sb11 = new StringBuilder();
//                    sb11.append("mPointer_Auto_Before");
//                    sb11.append(String.valueOf(i));
//                    edit.putInt(sb11.toString(), 0);
//                    StringBuilder sb12 = new StringBuilder();
//                    sb12.append("mPointer_Auto_After");
//                    sb12.append(String.valueOf(i));
//                    edit.putInt(sb12.toString(), 0);
//                }
//                edit.putInt("auto_target", 0);
//                edit.apply();
//                FloatingViewService.target_num = 0;
//                FloatingViewService.mPointerView_auto = null;
//                FloatingViewService.mPointerView_auto_swipe = null;
//                Toast.makeText(FloatingViewService.this.getApplicationContext(), "Initialized target", 0).show();
//                return false;
            }
        });

        times = (int) settings.getFloat("click_times", 30.0f);
        final ImageView imageView2 = (ImageView) mFloatingView.findViewById(R.id.play_btn);
        imageView2.setBackgroundResource(R.drawable.autoclick_play);
        imageView2.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (FloatingViewService.auto_click) {
                    Log.d("auto_click", "false");
                    FloatingViewService.auto_click = false;
                    imageView2.setBackgroundResource(R.drawable.autoclick_play);
                } else {
                    Log.d("auto_click", "true");
                    FloatingViewService.target_num = FloatingViewService.settings.getInt("auto_target", 0);
                    if (AutoClicker.instance != null) {
                        Log.d("AutoClicker.instance", "not null");
                        if (FloatingViewService.target_num == 0) {
                            FloatingViewService.auto_click = false;
                            return false;
                        }
                        FloatingViewService.auto_click = true;
                        imageView2.setBackgroundResource(0);
                        imageView2.setBackgroundResource(R.drawable.pause);
                        FloatingViewService.expandedView.setBackgroundColor(ContextCompat.getColor(FloatingViewService.this.getApplicationContext(), R.color.defaultcolor2));
                        final int[] iArr = new int[FloatingViewService.target_num];
                        final int[] iArr2 = new int[FloatingViewService.target_num];
                        Map<String, ViewPointerAuto> map = FloatingViewService.mPointerView_auto;
                        StringBuilder sb = new StringBuilder();
                        sb.append("mPointerView_auto");
                        sb.append(String.valueOf(FloatingViewService.target_num));
                        int width = ((ViewPointerAuto) map.get(sb.toString())).getWidth() / 2;
                        Map<String, ViewPointerAuto> map2 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("mPointerView_auto");
                        sb2.append(String.valueOf(FloatingViewService.target_num));
                        int height = ((ViewPointerAuto) map2.get(sb2.toString())).getHeight();
                        final int i = 1000 / AutoClickSpeed.clickpersecond;
                        for (int i2 = 1; i2 <= FloatingViewService.target_num; i2++) {
                            Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointerView_auto");
                            sb3.append(String.valueOf(i2));
                            if (((ViewPointerAuto) map3.get(sb3.toString())).isShown()) {
                                Map<String, LayoutParams> map4 = FloatingViewService.mParams_auto;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("mParams_auto");
                                sb4.append(String.valueOf(i2));
                                LayoutParams layoutParams = (LayoutParams) map4.get(sb4.toString());
                                layoutParams.flags |= 16;
                                WindowManager windowManager = FloatingViewService.mWindowManager;
                                Map<String, ViewPointerAuto> map5 = FloatingViewService.mPointerView_auto;
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("mPointerView_auto");
                                sb5.append(String.valueOf(i2));
                                View view2 = (View) map5.get(sb5.toString());
                                Map<String, LayoutParams> map6 = FloatingViewService.mParams_auto;
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("mParams_auto");
                                sb6.append(String.valueOf(i2));
                                windowManager.updateViewLayout(view2, (ViewGroup.LayoutParams) map6.get(sb6.toString()));
                            }
                            for (int i3 = 0; i3 < FloatingViewService.swipe_target.size(); i3++) {
                                Log.d("swipe target num : ", String.valueOf(FloatingViewService.swipe_target.get(i3)));
                                if (i2 == ((Integer) FloatingViewService.swipe_target.get(i3)).intValue()) {
                                    Log.d("swipe target num matched : ", String.valueOf(FloatingViewService.swipe_target.get(i3)));
                                    Map<String, ViewPointerAutoSwipe> map7 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append("mPointerView_auto_s");
                                    sb7.append(String.valueOf(FloatingViewService.swipe_target.get(i3)));
                                    if (map7.get(sb7.toString()) != null) {
                                        Map<String, ViewPointerAutoSwipe> map8 = FloatingViewService.mPointerView_auto_swipe;
                                        StringBuilder sb8 = new StringBuilder();
                                        sb8.append("mPointerView_auto_s");
                                        sb8.append(String.valueOf(FloatingViewService.swipe_target.get(i3)));
                                        ((ViewPointerAutoSwipe) map8.get(sb8.toString())).setPaint_Play();
                                        Map<String, ViewPointerAutoSwipe> map9 = FloatingViewService.mPointerView_auto_swipe;
                                        StringBuilder sb9 = new StringBuilder();
                                        sb9.append("mPointerView_auto_s");
                                        sb9.append(String.valueOf(FloatingViewService.swipe_target.get(i3)));
                                        ((ViewPointerAutoSwipe) map9.get(sb9.toString())).invalidate();
                                        WindowManager windowManager2 = FloatingViewService.mWindowManager;
                                        Map<String, ViewPointerAutoSwipe> map10 = FloatingViewService.mPointerView_auto_swipe;
                                        StringBuilder sb10 = new StringBuilder();
                                        sb10.append("mPointerView_auto_s");
                                        sb10.append(FloatingViewService.swipe_target.get(i3));
                                        View view3 = (View) map10.get(sb10.toString());
                                        Map<String, LayoutParams> map11 = FloatingViewService.mParams_auto;
                                        StringBuilder sb11 = new StringBuilder();
                                        sb11.append("mParams_auto_s");
                                        sb11.append(FloatingViewService.swipe_target.get(i3));
                                        windowManager2.updateViewLayout(view3, (ViewGroup.LayoutParams) map11.get(sb11.toString()));
                                    }
                                }
                            }
                            int i4 = i2 - 1;
                            Map<String, LayoutParams> map12 = FloatingViewService.mParams_auto;
                            StringBuilder sb12 = new StringBuilder();
                            sb12.append("mParams_auto");
                            sb12.append(String.valueOf(i2));
                            iArr[i4] = ((LayoutParams) map12.get(sb12.toString())).x + width;
                            Map<String, LayoutParams> map13 = FloatingViewService.mParams_auto;
                            StringBuilder sb13 = new StringBuilder();
                            sb13.append("mParams_auto");
                            sb13.append(String.valueOf(i2));
                            iArr2[i4] = ((LayoutParams) map13.get(sb13.toString())).y + height;
                        }
                        final Display defaultDisplay = ((WindowManager) FloatingViewService.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        AutoClickSpeed.test_flag = true;
                        AutoClickSpeed.start_time = (double) System.currentTimeMillis();
                        Handler handler = new Handler();
                        final Handler handler2 = handler;
                        Runnable r1 = new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            public void run() {
                                switch (defaultDisplay.getRotation()) {
                                    case 0:
                                        if (FloatingViewService.target_rotation > FloatingViewService.target_num) {
                                            FloatingViewService.target_rotation = 1;
                                            break;
                                        } else {
                                            boolean z = false;
                                            for (int i = 0; i < FloatingViewService.swipe_target.size(); i++) {
                                                if (FloatingViewService.target_rotation == ((Integer) FloatingViewService.swipe_target.get(i)).intValue()) {
                                                    z = true;
                                                }
                                            }
                                            if (!z) {
                                                AutoClicker.instance.click(iArr[FloatingViewService.target_rotation - 1], iArr2[FloatingViewService.target_rotation - 1]);
                                                FloatingViewService.target_rotation++;
                                                break;
                                            } else {
                                                Log.d("target_rotation-1", String.valueOf(FloatingViewService.target_rotation - 1));
                                                Log.d("target_x.length", String.valueOf(iArr.length));
                                                if (iArr.length - (FloatingViewService.target_rotation - 1) > 1) {
                                                    AutoClicker.instance.swipe(iArr[FloatingViewService.target_rotation - 1], iArr2[FloatingViewService.target_rotation - 1], iArr[FloatingViewService.target_rotation], iArr2[FloatingViewService.target_rotation]);
                                                    FloatingViewService.target_rotation++;
                                                    FloatingViewService.target_rotation++;
                                                    try {
                                                        Thread.sleep((long) AutoClickSpeed.swipe_duration_time);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    case 1:
                                        if (FloatingViewService.target_rotation > FloatingViewService.target_num) {
                                            FloatingViewService.target_rotation = 1;
                                            break;
                                        } else {
                                            boolean z2 = false;
                                            for (int i2 = 0; i2 < FloatingViewService.swipe_target.size(); i2++) {
                                                if (FloatingViewService.target_rotation == ((Integer) FloatingViewService.swipe_target.get(i2)).intValue()) {
                                                    z2 = true;
                                                }
                                            }
                                            if (!z2) {
                                                AutoClicker.instance.click(iArr[FloatingViewService.target_rotation - 1], iArr2[FloatingViewService.target_rotation - 1]);
                                                FloatingViewService.target_rotation++;
                                                break;
                                            } else {
                                                Log.d("ROTATION_90", "ROTATION_90");
                                                Log.d("target_rotation-1", String.valueOf(FloatingViewService.target_rotation - 1));
                                                Log.d("target_x.length", String.valueOf(iArr.length));
                                                if (iArr.length - (FloatingViewService.target_rotation - 1) > 1) {
                                                    AutoClicker.instance.swipe(iArr[FloatingViewService.target_rotation - 1], iArr2[FloatingViewService.target_rotation - 1], iArr[FloatingViewService.target_rotation], iArr2[FloatingViewService.target_rotation]);
                                                    FloatingViewService.target_rotation++;
                                                    FloatingViewService.target_rotation++;
                                                    try {
                                                        Thread.sleep((long) AutoClickSpeed.swipe_duration_time);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    case 3:
                                        if (FloatingViewService.target_rotation > FloatingViewService.target_num) {
                                            FloatingViewService.target_rotation = 1;
                                            break;
                                        } else {
                                            boolean z3 = false;
                                            for (int i3 = 0; i3 < FloatingViewService.swipe_target.size(); i3++) {
                                                if (FloatingViewService.target_rotation == ((Integer) FloatingViewService.swipe_target.get(i3)).intValue()) {
                                                    z3 = true;
                                                }
                                            }
                                            if (!z3) {
                                                AutoClicker.instance.click(iArr[FloatingViewService.target_rotation - 1], iArr2[FloatingViewService.target_rotation - 1]);
                                                FloatingViewService.target_rotation++;
                                                break;
                                            } else {
                                                Log.d("ROTATION_270", "ROTATION_270");
                                                Log.d("target_rotation-1", String.valueOf(FloatingViewService.target_rotation - 1));
                                                Log.d("target_x.length", String.valueOf(iArr.length));
                                                if (iArr.length - (FloatingViewService.target_rotation - 1) > 1) {
                                                    AutoClicker.instance.swipe(iArr[FloatingViewService.target_rotation - 1], iArr2[FloatingViewService.target_rotation - 1], iArr[FloatingViewService.target_rotation], iArr2[FloatingViewService.target_rotation]);
                                                    FloatingViewService.target_rotation++;
                                                    FloatingViewService.target_rotation++;
                                                    try {
                                                        Thread.sleep((long) AutoClickSpeed.swipe_duration_time);
                                                        break;
                                                    } catch (InterruptedException unused) {
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                }
                                if (FloatingViewService.auto_click) {
                                    AutoClickSpeed.interval = ((double) System.currentTimeMillis()) - AutoClickSpeed.start_time;
                                    if (AutoClickSpeed.test_flag && AutoClickSpeed.duration_time < 0.0d) {
                                        handler2.postDelayed(this, (long) (i / 2));
                                    } else if (!AutoClickSpeed.test_flag || AutoClickSpeed.interval >= AutoClickSpeed.duration_time) {
                                        FloatingViewService.auto_click = false;
                                        imageView2.setBackgroundResource(R.drawable.autoclick_play);
                                        FloatingViewService.target_num = FloatingViewService.settings.getInt("auto_target", 0);
                                        FloatingViewService.expandedView.setBackgroundColor(ContextCompat.getColor(FloatingViewService.this.getApplicationContext(), R.color.defaultcolor));
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                FloatingViewService.this.remove_flag_not_touchable();
                                            }
                                        }, 300);
                                    } else {
                                        handler2.postDelayed(this, (long) (i / 2));
                                    }
                                } else {
                                    Log.d("auto click", "end");
                                    imageView2.setBackgroundResource(R.drawable.autoclick_play);
                                    FloatingViewService.target_num = FloatingViewService.settings.getInt("auto_target", 0);
                                    FloatingViewService.expandedView.setBackgroundColor(ContextCompat.getColor(FloatingViewService.this.getApplicationContext(), R.color.defaultcolor));
                                    handler2.postDelayed(new Runnable() {
                                        public void run() {
                                            FloatingViewService.this.remove_flag_not_touchable();
                                        }
                                    }, 0);
//                                    if (new Random().nextInt(6) + 1 == 1 && !FloatingViewService.settings.getBoolean("rate", false)) {
//                                        FloatingViewService.this.startActivity(new Intent(FloatingViewService.this.getApplicationContext(), RateActivity.class));
//                                    }
                                }
                            }
                        };
                        handler.post(r1);
                    } else {
                        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                        intent.addFlags(FLAG_RECEIVER_FOREGROUND);
                        FloatingViewService.this.startActivity(intent);
                    }
                }
                return false;
            }
        });

        ((ImageView) mFloatingView.findViewById(R.id.btnCapture)).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                Intent intent = new Intent(FloatingViewService.this.getApplicationContext(), AllSettings.class);
                intent.setFlags(FLAG_RECEIVER_FOREGROUND);
                FloatingViewService.this.startActivity(intent);
            }
        });

        ((ImageView) mFloatingView.findViewById(R.id.close_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById.setVisibility(View.VISIBLE);
                FloatingViewService.expandedView.setVisibility(View.GONE);
                if (FloatingViewService.mPointerView_auto == null) {
                    FloatingViewService.mPointerView_auto = new HashMap();
                }
                for (int i = 1; i <= FloatingViewService.mPointerView_auto.size(); i++) {
                    Map<String, ViewPointerAuto> map = FloatingViewService.mPointerView_auto;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mPointerView_auto");
                    sb.append(String.valueOf(i));
                    if (map.get(sb.toString()) != null) {
                        Map<String, ViewPointerAuto> map2 = FloatingViewService.mPointerView_auto;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("mPointerView_auto");
                        sb2.append(String.valueOf(i));
                        if (((ViewPointerAuto) map2.get(sb2.toString())).isShown()) {
                            Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("mPointerView_auto");
                            sb3.append(String.valueOf(i));
                            ((ViewPointerAuto) map3.get(sb3.toString())).setVisibility(View.GONE);
                            for (int i2 = 0; i2 < FloatingViewService.swipe_target.size(); i2++) {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(String.valueOf(i));
                                sb4.append(" | ");
                                sb4.append(String.valueOf(FloatingViewService.swipe_target.get(i2)));
                                Log.d("i | swipe_target num", sb4.toString());
                                if (i == ((Integer) FloatingViewService.swipe_target.get(i2)).intValue()) {
                                    Map<String, ViewPointerAutoSwipe> map4 = FloatingViewService.mPointerView_auto_swipe;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("mPointerView_auto_s");
                                    sb5.append(String.valueOf(i));
                                    if (map4.get(sb5.toString()) != null) {
                                        Map<String, ViewPointerAutoSwipe> map5 = FloatingViewService.mPointerView_auto_swipe;
                                        StringBuilder sb6 = new StringBuilder();
                                        sb6.append("mPointerView_auto_s");
                                        sb6.append(String.valueOf(i));
                                        if (((ViewPointerAutoSwipe) map5.get(sb6.toString())).isShown()) {
                                            Map<String, ViewPointerAutoSwipe> map6 = FloatingViewService.mPointerView_auto_swipe;
                                            StringBuilder sb7 = new StringBuilder();
                                            sb7.append("mPointerView_auto_s");
                                            sb7.append(String.valueOf(i));
                                            ((ViewPointerAutoSwipe) map6.get(sb7.toString())).setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private float initialTouchX;
            private float initialTouchY;
            private int initialX;
            private int initialY;


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        this.initialX = FloatingViewService.params.x;
                        this.initialY = FloatingViewService.params.y;
                        this.initialTouchX = motionEvent.getRawX();
                        this.initialTouchY = motionEvent.getRawY();
                        return true;
                    case 1:
                        int rawX = (int) (motionEvent.getRawX() - this.initialTouchX);
                        int rawY = (int) (motionEvent.getRawY() - this.initialTouchY);
                        if (rawX < 10 && rawY < 10 && FloatingViewService.this.isViewCollapsed()) {
                            findViewById.setVisibility(View.GONE);
                            FloatingViewService.expandedView.setVisibility(View.VISIBLE);
                            for (int i = 1; i <= FloatingViewService.mPointerView_auto.size(); i++) {
                                Map<String, ViewPointerAuto> map = FloatingViewService.mPointerView_auto;
                                StringBuilder sb = new StringBuilder();
                                sb.append("mPointerView_auto");
                                sb.append(String.valueOf(i));
                                if (map.get(sb.toString()) != null) {
                                    Map<String, ViewPointerAuto> map2 = FloatingViewService.mPointerView_auto;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("mPointerView_auto");
                                    sb2.append(String.valueOf(i));
                                    if (!((ViewPointerAuto) map2.get(sb2.toString())).isShown()) {
                                        Map<String, ViewPointerAuto> map3 = FloatingViewService.mPointerView_auto;
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("mPointerView_auto");
                                        sb3.append(String.valueOf(i));
                                        ((ViewPointerAuto) map3.get(sb3.toString())).setVisibility(View.VISIBLE);
                                        for (int i2 = 0; i2 < FloatingViewService.swipe_target.size(); i2++) {
                                            if (i == ((Integer) FloatingViewService.swipe_target.get(i2)).intValue()) {
                                                Map<String, ViewPointerAutoSwipe> map4 = FloatingViewService.mPointerView_auto_swipe;
                                                StringBuilder sb4 = new StringBuilder();
                                                sb4.append("mPointerView_auto_s");
                                                sb4.append(String.valueOf(i));
                                                if (map4.get(sb4.toString()) != null) {
                                                    Map<String, ViewPointerAutoSwipe> map5 = FloatingViewService.mPointerView_auto_swipe;
                                                    StringBuilder sb5 = new StringBuilder();
                                                    sb5.append("mPointerView_auto_s");
                                                    sb5.append(String.valueOf(i));
                                                    if (!((ViewPointerAutoSwipe) map5.get(sb5.toString())).isShown()) {
                                                        Map<String, ViewPointerAutoSwipe> map6 = FloatingViewService.mPointerView_auto_swipe;
                                                        StringBuilder sb6 = new StringBuilder();
                                                        sb6.append("mPointerView_auto_s");
                                                        sb6.append(String.valueOf(i));
                                                        ((ViewPointerAutoSwipe) map6.get(sb6.toString())).setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return true;
                    case 2:
                        FloatingViewService.params.x = this.initialX + ((int) (motionEvent.getRawX() - this.initialTouchX));
                        FloatingViewService.params.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                        FloatingViewService.mWindowManager.updateViewLayout(FloatingViewService.mFloatingView, FloatingViewService.params);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @SuppressLint("LongLogTag")
    public static void displayMetrics2() throws RemoteException {
        Display defaultDisplay = mWindowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;
        deviceHorizontalCenter = (deviceWidth - mFloatingView.getWidth()) >> 1;
        deviceVerticalCenter = (deviceHeight - mFloatingView.getHeight()) >> 1;
        Log.d("deviceHorizontalCenter : ", String.valueOf(deviceHorizontalCenter));
        Log.d("deviceVerticalCenter : ", String.valueOf(deviceVerticalCenter));
    }

    @SuppressLint("LongLogTag")
    public void remove_flag_not_touchable() {
        for (int i = 1; i <= target_num; i++) {
            Map<String, ViewPointerAuto> map = mPointerView_auto;
            StringBuilder sb = new StringBuilder();
            sb.append("mPointerView_auto");
            sb.append(String.valueOf(i));
            if (((ViewPointerAuto) map.get(sb.toString())).isShown()) {
                Map<String, LayoutParams> map2 = mParams_auto;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("mParams_auto");
                sb2.append(String.valueOf(i));
                LayoutParams layoutParams = (LayoutParams) map2.get(sb2.toString());
                layoutParams.flags &= -17;
                WindowManager windowManager = mWindowManager;
                Map<String, ViewPointerAuto> map3 = mPointerView_auto;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("mPointerView_auto");
                sb3.append(String.valueOf(i));
                View view = (View) map3.get(sb3.toString());
                Map<String, LayoutParams> map4 = mParams_auto;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("mParams_auto");
                sb4.append(String.valueOf(i));
                windowManager.updateViewLayout(view, (ViewGroup.LayoutParams) map4.get(sb4.toString()));
            }
            for (int i2 = 0; i2 < swipe_target.size(); i2++) {
                Log.d("swipe target num : ", String.valueOf(swipe_target.get(i2)));
                if (i == ((Integer) swipe_target.get(i2)).intValue()) {
                    Log.d("swipe target num matched", String.valueOf(swipe_target.get(i2)));
                    Map<String, ViewPointerAutoSwipe> map5 = mPointerView_auto_swipe;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("mPointerView_auto_s");
                    sb5.append(String.valueOf(swipe_target.get(i2)));
                    if (map5.get(sb5.toString()) != null) {
                        Map<String, ViewPointerAutoSwipe> map6 = mPointerView_auto_swipe;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("mPointerView_auto_s");
                        sb6.append(String.valueOf(swipe_target.get(i2)));
                        ((ViewPointerAutoSwipe) map6.get(sb6.toString())).setPaint_Not_Play();
                        Map<String, ViewPointerAutoSwipe> map7 = mPointerView_auto_swipe;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("mPointerView_auto_s");
                        sb7.append(String.valueOf(swipe_target.get(i2)));
                        ((ViewPointerAutoSwipe) map7.get(sb7.toString())).invalidate();
                        WindowManager windowManager2 = mWindowManager;
                        Map<String, ViewPointerAutoSwipe> map8 = mPointerView_auto_swipe;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("mPointerView_auto_s");
                        sb8.append(swipe_target.get(i2));
                        View view2 = (View) map8.get(sb8.toString());
                        Map<String, LayoutParams> map9 = mParams_auto;
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("mParams_auto_s");
                        sb9.append(swipe_target.get(i2));
                        windowManager2.updateViewLayout(view2, (ViewGroup.LayoutParams) map9.get(sb9.toString()));
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private boolean isNetworkConnected() {
        return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
    }
}
