package service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.wakeup.autoclick.FloatingViewService;

import servicecontrol.AutoClickSpeed;

public class AutoClicker extends AccessibilityService {
    public static AutoClicker instance;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    public static AutoClicker getAutoClickService() {
        return instance;
    }

    public static final void setAutoClickService(@Nullable AutoClicker autoClicker) {
        instance = autoClicker;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        setAutoClickService(this);
        Log.d("onServiceConnected : ", "Success");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public final void click (int i , int i2) {
        Path path = new Path();
        if (i < 0) {
            i = 0;
        }
        if (i > FloatingViewService.deviceWidth) {
            i = FloatingViewService.deviceWidth;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        if (i2 > FloatingViewService.deviceHeight) {
            i2 = FloatingViewService.deviceHeight;
        }
        Log.d("auto click target_num", String.valueOf(FloatingViewService.target_rotation));
        try {
            Log.d("Before Target Num", String.valueOf(FloatingViewService.target_rotation));
            SharedPreferences sharedPreferences = FloatingViewService.settings;
            StringBuilder sb = new StringBuilder();
            sb.append("mPointer_Auto_Before");
            sb.append(String.valueOf(FloatingViewService.target_rotation));
            Thread.sleep((long) sharedPreferences.getInt(sb.toString(), 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        path.moveTo((float) i, (float) i2);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription(path, 0, 10);
        dispatchGesture(builder.addStroke(strokeDescription).build(), null, null);
        try {
            Log.d("After Target Num", String.valueOf(FloatingViewService.target_rotation));
            SharedPreferences sharedPreferences2 = FloatingViewService.settings;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mPointer_Auto_After");
            sb2.append(String.valueOf(FloatingViewService.target_rotation));
            Thread.sleep((long) sharedPreferences2.getInt(sb2.toString(), 0));
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public final void swipe(int i, int i2, int i3, int i4) {
        Path path = new Path();
        if (i < 0) {
            i = 0;
        }
        if (i > FloatingViewService.deviceWidth) {
            i = FloatingViewService.deviceWidth;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        if (i2 > FloatingViewService.deviceHeight) {
            i2 = FloatingViewService.deviceHeight;
        }
        if (i3 < 0) {
            i3 = 0;
        }
        if (i3 > FloatingViewService.deviceWidth) {
            i3 = FloatingViewService.deviceWidth;
        }
        if (i4 < 0) {
            i4 = 0;
        }
        if (i4 > FloatingViewService.deviceHeight) {
            i4 = FloatingViewService.deviceHeight;
        }
        Log.d("auto click target_num", String.valueOf(FloatingViewService.target_rotation));
        try {
            Log.d("Before Target Num", String.valueOf(FloatingViewService.target_rotation));
            SharedPreferences sharedPreferences = FloatingViewService.settings;
            StringBuilder sb = new StringBuilder();
            sb.append("mPointer_Auto_Before");
            sb.append(String.valueOf(FloatingViewService.target_rotation));
            Thread.sleep((long) sharedPreferences.getInt(sb.toString(), 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        path.moveTo((float) i, (float) i2);
        path.lineTo((float) i3, (float) i4);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription(path, 0, (long) ((int) AutoClickSpeed.swipe_duration_time));
        dispatchGesture(builder.addStroke(strokeDescription).build(), null, null);
        try {
            Log.d("After Target Num", String.valueOf(FloatingViewService.target_rotation));
            SharedPreferences sharedPreferences2 = FloatingViewService.settings;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mPointer_Auto_After");
            sb2.append(String.valueOf(FloatingViewService.target_rotation));
            Thread.sleep((long) sharedPreferences2.getInt(sb2.toString(), 0));
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (instance != null) {
            instance = null;
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (instance != null) {
            stopService(new Intent(getApplicationContext(), AutoClicker.class));
        }
        super.onDestroy();
    }
}
