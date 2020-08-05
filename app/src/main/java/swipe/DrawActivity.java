package swipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wakeup.autoclick.R;

public class DrawActivity extends Activity {
    DrawView drawView;
    String[] pass;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        this.pass = new String[4];
        this.pass = intent.getStringArrayExtra("coordinates");
        Log.i("NOTES", this.pass[0]);
        int parseInt = Integer.parseInt(this.pass[0]);
        int parseInt2 = Integer.parseInt(this.pass[1]);
        int parseInt3 = Integer.parseInt(this.pass[2]);
        int parseInt4 = Integer.parseInt(this.pass[3]);
        this.drawView = new DrawView(this);
        this.drawView.assign(parseInt, parseInt2, parseInt3, parseInt4);
        this.drawView.setBackgroundColor(getColor(R.color.colorAccent));
    }
}
