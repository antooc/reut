
package com.peice;

import java.util.List;

import com.peice.common.BaseActivity;
import com.peice.model.Candidate;
import com.peice.model.DataManager;
import com.peice.model.Test;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
// import android.widget.ImageButton;
import android.widget.TextView;


public class ATSInfoHintActivity extends BaseActivity {

    private Button infohintButton;
    // private ImageButton infohintImageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_info_hint_layout);

        infohintButton = (Button) findViewById(R.id.infohint_button);
        infohintButton.setOnClickListener(infohintListener);

        // infohintImageButton = (ImageButton) findViewById(R.id.infohintImageButtonArrow);
        // infohintImageButton.setOnClickListener(infohintArrowListener);

        Candidate cand = DataManager.getInstance().getCandidate();
        if (cand != null) {
        	setInfo(R.id.tv_infohint_itemcontent, cand.getName());
        	setInfo(R.id.tv_infohint_itemcontent, cand.getProjectName());
        	setInfo(R.id.tv_infohint_hintcontent, cand.getNotice());
        	
        	List<Test> tests = cand.getTests();
        	int total_len = 0;
        	StringBuilder testlist = new StringBuilder();
        	for (int i = 0; i < tests.size(); i ++) {
        		total_len += tests.get(i).getTimeLength();
        		if (i != 0) {
        			testlist.append("\n");
        		}
        		testlist.append(Integer.toString(i+1) + ". ");
        		testlist.append(tests.get(i).getName());
        	}
        	setInfo(R.id.tv_infohint_timelongcontent, Integer.toString(total_len) + "min");
        	setInfo(R.id.tv_infohint_subject1, testlist.toString());
        }
        
    }
    
    private void setInfo(int id, String str) {
    	TextView textview = (TextView)findViewById(id);
    	if (textview != null) {
    		textview.setText(str);
    	}
    }

    @Override
    public void onStart() {
        super.onStart();

        /*ActionBar ab = this.getActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        ab.setTitle(R.string.infohint_title);*/
        setTitle(R.string.infohint_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            Intent intent = new Intent(this, ATSFilloutInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    Button.OnClickListener infohintListener = new Button.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();

            intent.setClass(ATSInfoHintActivity.this, CourseActivity.class);
            startActivity(intent);
            finish();
        }
    };

    // ImageButton.OnClickListener infohintArrowListener = new ImageButton.OnClickListener() {
    //     public void onClick(View v) {
    //         Intent intent = new Intent();

    //         intent.setClass(ATSInfoHintActivity.this, ATSFilloutInfoActivity.class);
    //         startActivity(intent);
    //         finish();
    //     }
    // };
}

