
package com.peice;

import com.peice.common.BaseActivity;
import com.peice.model.DataManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


public class ATSFilloutInfoActivity extends BaseActivity {

    private EditText filloutNameEdit;
    private EditText filloutEmailEdit;
    private RadioGroup filloutRadioGroup;
    private Button filloutButton;

    private Handler handle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_fillout_info_layout);

        filloutNameEdit = (EditText) findViewById(R.id.fillout_name_edit);
        filloutEmailEdit = (EditText) findViewById(R.id.fillout_name_email);

        filloutButton = (Button) findViewById(R.id.fillout_button);
        filloutButton.setOnClickListener(filloutListener);

        
        handle = new Handler() {
        	@Override
            public void handleMessage(Message msg) {
        		if (msg.what != DataManager.MSG_UPDATE) {
        			return ;
        		}
        		
        		if (msg.arg1 == 1) { //OK
                    Intent intent = new Intent();

                    intent.setClass(ATSFilloutInfoActivity.this, ATSInfoHintActivity.class);
                    startActivity(intent);
                    finish();
        		}
        		else {
        			//show error
        			Toast.makeText(ATSFilloutInfoActivity.this, (String)msg.obj, 4000).show();
        			filloutButton.setEnabled(true);
        		}
        	}
        	
        };
    }

    @Override
    public void onStart() {
        super.onStart();

        /*ActionBar ab = this.getActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        ab.setTitle(R.string.fillout_title);*/
        setTitle(R.string.fillout_title);
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
        	v.setEnabled(false);
        	DataManager.getInstance().updateCandidateInfo(
        				filloutNameEdit.getText().toString().trim(), 
        				filloutEmailEdit.getText().toString().trim(), 
        				true, //TODO 
        				handle);
        }
    };
}

