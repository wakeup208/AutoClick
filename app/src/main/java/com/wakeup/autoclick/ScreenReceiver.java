package com.wakeup.autoclick;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
    public static boolean wasScreenOn = true;

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            Log.d("abcd", " Off");
            if (FloatingViewService.auto_click) {
                FloatingViewService.touch_first = true;
                FloatingViewService.auto_click = false;
                Log.d("Screen Off & auto_click false ", " success");
            }
            wasScreenOn = false;
        } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
            wasScreenOn = true;
        }
    }
}
