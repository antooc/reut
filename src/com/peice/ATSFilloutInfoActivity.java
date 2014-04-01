
package com.peice;

import com.peice.common.BaseActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


public class ATSFilloutInfoActivity extends BaseActivity {

    private EditText filloutNameEdit;
    private EditText filloutEmailEdit;
    private RadioGroup filloutRadioGroup;
    private Button filloutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_fillout_info_layout);

        filloutNameEdit = (EditText) findViewById(R.id.fillout_name_edit);
        filloutEmailEdit = (EditText) findViewById(R.id.fillout_name_email);

        filloutButton = (Button) findViewById(R.id.fillout_button);
        filloutButton.setOnClickListener(filloutListener);

    }

    @Override
    public void onStart() {
        super.onStart();

        /*ActionBar ab = this.getActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        ab.setTitle(R.string.fillout_title);*/
        setTitle(R.string.fillout_title);
        showBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ATSLogonActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    Button.OnClickListener filloutListener = new Button.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();

            intent.setClass(ATSFilloutInfoActivity.this, ATSInfoHintActivity.class);
            startActivity(intent);
            finish();
        }
    };
}

