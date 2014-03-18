
package com.peice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;


public class ATSFilloutInfoActivity extends Activity {

    private Button filloutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_fillout_info_layout);

        filloutButton = (Button) findViewById(R.id.fillout_button);
        filloutButton.setOnClickListener(filloutListener);
    }

    Button.OnClickListener filloutListener = new Button.OnClickListener()
    {
        public void onClick(View v) {
            Intent intent = new Intent();

            intent.setClass(ATSFilloutInfoActivity.this, CourseActivity.class);
            startActivity(intent);
            finish();
        }
    };
}

