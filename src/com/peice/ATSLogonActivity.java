
package com.peice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;


public class ATSLogonActivity extends Activity {

    private Button logonButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_logon_layout);

        logonButton = (Button) findViewById(R.id.logon_button);
        logonButton.setOnClickListener(logonListener);
    }

    Button.OnClickListener logonListener = new Button.OnClickListener()
    {
        public void onClick(View v) {
            logonButton.setText(R.string.logon_logingin);

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
        }
    };

}

