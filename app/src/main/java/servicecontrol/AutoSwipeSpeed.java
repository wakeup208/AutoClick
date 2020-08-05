package servicecontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.wakeup.autoclick.FloatingViewService;
import com.wakeup.autoclick.R;

import java.util.ArrayList;

public class AutoSwipeSpeed extends AppCompatActivity {
    static Context context;
    public static double duration_time;
    /* access modifiers changed from: private */
    public EditText Duration_Edt;
    private Button Reset_Btn;
    Bitmap bitmap;
    Button button;
    Canvas canvas;
    private int[] loc = new int[2];
    public WindowManager mWindowManager;
    Paint paint;
    Path path2;
    RelativeLayout relativeLayout;
    View view;

    public class DrawingClass {
        Paint DrawingClassPaint;
        Path DrawingClassPath;

        public DrawingClass() {
        }

        public Path getPath() {
            return this.DrawingClassPath;
        }

        public void setPath(Path path) {
            this.DrawingClassPath = path;
        }

        public Paint getPaint() {
            return this.DrawingClassPaint;
        }

        public void setPaint(Paint paint) {
            this.DrawingClassPaint = paint;
        }
    }

    class SketchSheetView extends View {
        public ArrayList<DrawingClass> DrawingClassArrayList = new ArrayList<>();

        public SketchSheetView(Context context) {
            super(context);
            AutoSwipeSpeed.this.bitmap = Bitmap.createBitmap(820, 480, Bitmap.Config.ARGB_4444);
            AutoSwipeSpeed.this.canvas = new Canvas(AutoSwipeSpeed.this.bitmap);
            setBackgroundColor(-1);
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            DrawingClass drawingClass = new DrawingClass();
            AutoSwipeSpeed.this.canvas.drawPath(AutoSwipeSpeed.this.path2, AutoSwipeSpeed.this.paint);
            if (motionEvent.getAction() == 0) {
                AutoSwipeSpeed.this.path2.moveTo(motionEvent.getX(), motionEvent.getY());
                AutoSwipeSpeed.this.path2.lineTo(motionEvent.getX(), motionEvent.getY());
            } else if (motionEvent.getAction() == 2) {
                AutoSwipeSpeed.this.path2.lineTo(motionEvent.getX(), motionEvent.getY());
                drawingClass.setPath(AutoSwipeSpeed.this.path2);
                drawingClass.setPaint(AutoSwipeSpeed.this.paint);
                this.DrawingClassArrayList.add(drawingClass);
            }
            invalidate();
            return true;
        }

        public void remove_DrawingClassArrayList() {
            this.DrawingClassArrayList = null;
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (this.DrawingClassArrayList.size() > 0) {
                canvas.drawPath(((DrawingClass) this.DrawingClassArrayList.get(this.DrawingClassArrayList.size() - 1)).getPath(), ((DrawingClass) this.DrawingClassArrayList.get(this.DrawingClassArrayList.size() - 1)).getPaint());
            }
        }
    }

    public AutoSwipeSpeed() {
    }

    public AutoSwipeSpeed(Context context2) {
        context = context2;
    }

@Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_autoswipespeed);

        widget();
    }

    /* access modifiers changed from: 0000 */
    public double ParseDouble(String str) {
        if (str == null || str.length() <= 0) {
            return 0.0d;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception unused) {
            return -1.0d;
        }
    }

    /* access modifiers changed from: 0000 */
    public int ParseInt(String str) {
        if (str == null || str.length() <= 0) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return -1;
        }
    }

    private void widget() {
        this.relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout1);
        this.button = (Button) findViewById(R.id.button);
        this.view = new SketchSheetView(this);
        this.paint = new Paint();
        this.path2 = new Path();
        this.relativeLayout.addView(this.view, new LinearLayout.LayoutParams(-1, -1));
        this.paint.setDither(true);
        this.paint.setColor(Color.parseColor("#000000"));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeWidth(10.0f);

        this.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AutoSwipeSpeed.this.relativeLayout = (RelativeLayout) AutoSwipeSpeed.this.findViewById(R.id.relativelayout1);
                AutoSwipeSpeed.this.view = new SketchSheetView(AutoSwipeSpeed.this);
                AutoSwipeSpeed.this.paint = new Paint();
                AutoSwipeSpeed.this.path2 = new Path();
                AutoSwipeSpeed.this.relativeLayout.addView(AutoSwipeSpeed.this.view, new LinearLayout.LayoutParams(-1, -1));
                AutoSwipeSpeed.this.paint.setDither(true);
                AutoSwipeSpeed.this.paint.setColor(Color.parseColor("#000000"));
                AutoSwipeSpeed.this.paint.setStyle(Paint.Style.STROKE);
                AutoSwipeSpeed.this.paint.setStrokeJoin(Paint.Join.ROUND);
                AutoSwipeSpeed.this.paint.setStrokeCap(Paint.Cap.ROUND);
                AutoSwipeSpeed.this.paint.setStrokeWidth(10.0f);
            }
        });

        this.Duration_Edt = (EditText) findViewById(R.id.swipe_duration_edt);
        this.Duration_Edt.setText(String.valueOf(FloatingViewService.settings.getInt("swipe_duration", 300)));
        AutoClickSpeed.swipe_duration_time = (double) FloatingViewService.settings.getInt("swipe_duration", 300);
        this.Duration_Edt.setSelection(this.Duration_Edt.getText().length());
        this.Duration_Edt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (AutoSwipeSpeed.this.ParseDouble(AutoSwipeSpeed.this.Duration_Edt.getText().toString()) > 0.0d && AutoSwipeSpeed.this.ParseDouble(AutoSwipeSpeed.this.Duration_Edt.getText().toString()) < 1000000.0d) {
                    AutoClickSpeed.swipe_duration_time = Double.parseDouble(AutoSwipeSpeed.this.Duration_Edt.getText().toString());
                    SharedPreferences.Editor edit = FloatingViewService.settings.edit();
                    edit.putInt("swipe_duration", Integer.parseInt(AutoSwipeSpeed.this.Duration_Edt.getText().toString()));
                    edit.apply();
                } else if (AutoSwipeSpeed.this.ParseDouble(AutoSwipeSpeed.this.Duration_Edt.getText().toString()) > 1000000.0d) {
                    AutoSwipeSpeed.this.Duration_Edt.setText("1000000");
                    AutoClickSpeed.swipe_duration_time = 1000000.0d;
                    SharedPreferences.Editor edit2 = FloatingViewService.settings.edit();
                    edit2.putInt("swipe_duration", 1000000);
                    edit2.apply();
                    Toast.makeText(AutoSwipeSpeed.this.getApplicationContext(), "Max swipe duration seconds : 1000000 ", Toast.LENGTH_SHORT).show();
                } else {
                    AutoClickSpeed.swipe_duration_time = 300.0d;
                    SharedPreferences.Editor edit3 = FloatingViewService.settings.edit();
                    edit3.putInt("swipe_duration", 300);
                    edit3.apply();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
