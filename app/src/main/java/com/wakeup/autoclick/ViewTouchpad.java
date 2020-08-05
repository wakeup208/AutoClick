package com.wakeup.autoclick;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.ViewCompat;

public class ViewTouchpad extends View {
    public static float size;
    public static int width;
    private int backgroundColor = ViewCompat.MEASURED_STATE_MASK;
    private String tempText;
    private String text = null;


    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
    }

    public ViewTouchpad(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setClickable(true);
        Log.w("LOG", "onFinishInflate()");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (!(mode == Integer.MIN_VALUE || mode == 0 || mode != 1073741824)) {
            MeasureSpec.getSize(heightMeasureSpec);
        }
        int mode2 = MeasureSpec.getMode(widthMeasureSpec);
        if (!(mode2 == Integer.MIN_VALUE || mode2 == 0 || mode2 != 1073741824)) {
            MeasureSpec.getSize(widthMeasureSpec);
        }
        setMeasuredDimension((int) (((float) getContext().getApplicationContext().getResources().getDisplayMetrics().heightPixels) / size), (int) (((float) getContext().getApplicationContext().getResources().getDisplayMetrics().heightPixels) / size));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("onTouchEvent(");
        sb.append(event);
        sb.append(")");
        Log.w("LOG", sb.toString());
        switch (event.getAction()) {
            case 0:
                this.backgroundColor = ViewCompat.MEASURED_STATE_MASK;
                this.tempText = this.text;
                this.text = "Clicked!";
                break;
            case 1:
                this.backgroundColor = ViewCompat.MEASURED_STATE_MASK;
                this.text = this.tempText;
                break;
            case 2:
                this.backgroundColor = -16776961;
                this.text = "Moved!";
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }
}
