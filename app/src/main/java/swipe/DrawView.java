package swipe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.wakeup.autoclick.R;

public class DrawView extends View {
    Paint paint = new Paint();

    /* renamed from: x1 */
    int f82x1;

    /* renamed from: x2 */
    int f83x2;

    /* renamed from: y1 */
    int f84y1;

    /* renamed from: y2 */
    int f85y2;

    public void assign(int i, int i2, int i3, int i4) {
        this.f82x1 = i;
        this.f84y1 = i2;
        this.f83x2 = i3;
        this.f85y2 = i4;
    }

    public DrawView(Context context) {
        super(context);
        this.paint.setColor(getContext().getColor(R.color.colorAccent));
        this.paint.setStrokeWidth(50.0f);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawLine((float) this.f82x1, (float) this.f84y1, (float) this.f83x2, (float) this.f85y2, this.paint);
    }
}
