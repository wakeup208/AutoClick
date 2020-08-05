package com.wakeup.autoclick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class ViewPointerAutoSwipe extends View {
    public static boolean draw_flag = false;
    public static int pointerSize = 15;
    public Paint paint = new Paint();

    /* renamed from: x1 */
    int f45x1;

    /* renamed from: x2 */
    int f46x2;

    /* renamed from: y1 */
    int f47y1;

    /* renamed from: y2 */
    int f48y2;

    public ViewPointerAutoSwipe(Context context) {
        super(context);
        invalidate();
        this.paint.setColor(-16776961);
        this.paint.setAlpha(100);
        this.paint.setStrokeWidth(50.0f);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
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
        this.f45x1 = i;
        this.f47y1 = i2;
        this.f46x2 = i3;
        this.f48y2 = i4;
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(this.f45x1));
        sb.append(" ");
        sb.append(String.valueOf(this.f47y1));
        sb.append(" ");
        sb.append(String.valueOf(this.f46x2));
        sb.append(" ");
        sb.append(String.valueOf(this.f48y2));
        Log.d("x1 y1 x2 y2", sb.toString());
    }

    public void onDraw(Canvas canvas) {
        Log.w("onDraw", "onDraw()");
        if (draw_flag) {
            canvas.save();
            canvas.drawLine((float) this.f45x1, (float) this.f47y1, (float) this.f46x2, (float) this.f48y2, this.paint);
            canvas.restore();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    public void setPaint_Play() {
        this.paint.setColor(-16711936);
        this.paint.setAlpha(100);
        this.paint.setStrokeWidth(50.0f);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setPaint_Not_Play() {
        this.paint.setColor(-16776961);
        this.paint.setAlpha(100);
        this.paint.setStrokeWidth(50.0f);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
    }
}
