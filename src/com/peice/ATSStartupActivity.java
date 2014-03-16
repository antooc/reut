
package com.peice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class ATSStartupActivity extends Activity {

    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();

            intent.setClass(ATSStartupActivity.this, CourseActivity.class); // enter login activity
            startActivity(intent);
            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_startup_layout);

        /* 1 second after close ATSStartupActivity, enter CourseActivity. */
        new Handler().postDelayed(r,1000);
    }
}

