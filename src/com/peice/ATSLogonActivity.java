
package com.peice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ATSLogonActivity extends Activity {

    private Button logonButton;
    private LinearLayout ll_Hint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_logon_layout);

        ll_Hint = (LinearLayout) findViewById(R.id.linearLayoutHint);

        logonButton = (Button) findViewById(R.id.logon_button);
        logonButton.setOnClickListener(logonListener);
    }

    Button.OnClickListener logonListener = new Button.OnClickListener()
    {
        public void onClick(View v) {
            /*
            logonButton.setText(R.string.logon_logingin);
            showHintView();

            new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                        logonButton.setText(R.string.logon_logon);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            }.start();
            */
            enterNextActivity();
        }
    };

    private void enterNextActivity() {
        Intent intent = new Intent();

        intent.setClass(ATSLogonActivity.this, ATSFilloutInfoActivity.class);
        startActivity(intent);
        finish();
    }

    private void showHintView() {
        final TextView tv_hint = new TextView(this);

        ll_Hint.removeAllViews();
        ll_Hint.addView(tv_hint);
        tv_hint.setText(getString(R.string.logon_hint));
    }
}

