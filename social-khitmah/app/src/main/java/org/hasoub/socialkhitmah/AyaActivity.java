package org.hasoub.socialkhitmah;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by personal on 3/28/15.
 */
public class AyaActivity extends Activity {
    public static final String AYA_TEXT = "aya_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aya_activity);

        TextView tv = (TextView) findViewById(R.id.aya_text);
        tv.setText(getIntent().getStringExtra(AYA_TEXT));

        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "me_quran_volt_newmet.ttf");
        tv.setTypeface(tf);

    }
}
