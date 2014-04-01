
package com.peice;

import com.peice.common.BaseActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
// import android.widget.ImageButton;


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
    }

    @Override
    public void onStart() {
        super.onStart();

        /*ActionBar ab = this.getActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        ab.setTitle(R.string.infohint_title);*/
        setTitle(R.string.infohint_title);
        showBack(true);
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

