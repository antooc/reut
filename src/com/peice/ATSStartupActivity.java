
package com.peice;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class ATSStartupActivity extends Activity {

    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();

            intent.setClass(ATSStartupActivity.this, ATSLogonActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_startup_layout);

        ActionBar ab = this.getActionBar();
        ab.hide();

        /* 1 second after close ATSStartupActivity, enter CourseActivity. */
        new Handler().postDelayed(r,1000);
    }
}

