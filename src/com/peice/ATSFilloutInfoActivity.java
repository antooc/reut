
package com.peice;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
// import android.widget.ImageButton;


public class ATSFilloutInfoActivity extends Activity {

    private Button filloutButton;
    // private ImageButton filloutImageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_fillout_info_layout);

        filloutButton = (Button) findViewById(R.id.fillout_button);
        filloutButton.setOnClickListener(filloutListener);

        // filloutImageButton = (ImageButton) findViewById(R.id.imageButtonArrow);
        // filloutImageButton.setOnClickListener(filloutArrowListener);

    }

    @Override
    public void onStart() {
        super.onStart();

        ActionBar ab = this.getActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        ab.setTitle(R.string.fillout_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
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

    // ImageButton.OnClickListener filloutArrowListener = new ImageButton.OnClickListener() {
    //     public void onClick(View v) {
    //         Intent intent = new Intent();

    //         intent.setClass(ATSFilloutInfoActivity.this, ATSLogonActivity.class);
    //         startActivity(intent);
    //         finish();
    //     }
    // };
}

