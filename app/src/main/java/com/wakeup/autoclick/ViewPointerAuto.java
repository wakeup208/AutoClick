package com.wakeup.autoclick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ViewPointerAuto extends View {
    public static int pointerSize = 15;
    Paint paint = new Paint();

    /* renamed from: x1 */
    int f41x1;

    /* renamed from: x2 */
    int f42x2;

    /* renamed from: y1 */
    int f43y1;

    /* renamed from: y2 */
    int f44y2;

    public ViewPointerAuto(Context context) {
        super(context);
    }

    public ViewPointerAuto(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPointerAuto(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewPointerAuto(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i2);
        if (!(mode == Integer.MIN_VALUE || mode == 0 || mode != 1073741824)) {
            MeasureSpec.getSize(i2);
        }
        int mode2 = MeasureSpec.getMode(i);
        if (!(mode2 == Integer.MIN_VALUE || mode2 == 0 || mode2 != 1073741824)) {
            MeasureSpec.getSize(i);
        }
        setMeasuredDimension(getContext().getApplicationContext().getResources().getDisplayMetrics().heightPixels / pointerSize, getContext().getApplicationContext().getResources().getDisplayMetrics().heightPixels / pointerSize);
    }

    public void assign(int i, int i2, int i3, int i4) {
        this.f41x1 = i;
        this.f43y1 = i2;
        this.f42x2 = i3;
        this.f44y2 = i4;
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(this.f41x1));
        sb.append(" ");
        sb.append(String.valueOf(this.f43y1));
        sb.append(" ");
        sb.append(String.valueOf(this.f42x2));
        sb.append(" ");
        sb.append(String.valueOf(this.f44y2));
        Log.d("x1 y1 x2 y2", sb.toString());
    }

    public void onDraw(Canvas canvas) {
        Log.w("onDraw", "onDraw()");
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }
}
