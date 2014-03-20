
package com.peice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class ATSInfoHintActivity extends Activity {

    private Button infohintButton;
    private ImageButton infohintImageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_info_hint_layout);

        infohintButton = (Button) findViewById(R.id.infohint_button);
        infohintButton.setOnClickListener(infohintListener);

        infohintImageButton = (ImageButton) findViewById(R.id.infohintImageButtonArrow);
        infohintImageButton.setOnClickListener(infohintArrowListener);

    }

    Button.OnClickListener infohintListener = new Button.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();

            intent.setClass(ATSInfoHintActivity.this, CourseActivity.class);
            startActivity(intent);
            finish();
        }
    };

    ImageButton.OnClickListener infohintArrowListener = new ImageButton.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();

            intent.setClass(ATSInfoHintActivity.this, ATSFilloutInfoActivity.class);
            startActivity(intent);
            finish();
        }
    };
}

