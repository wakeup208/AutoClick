package com.wakeup.autoclick;

import android.content.SharedPreferences.Editor;

public class Utils {
    public static void set_editor_int(String str, int i) {
        Editor edit = FloatingViewService.settings.edit();
        edit.putInt(str, i);
        edit.apply();
    }
}
