package com.peice.common;

import com.peice.R;
import com.peice.R.id;
import com.peice.R.layout;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BaseActivity extends Activity {

	private TextView mTitle;
	
	@Override
	public void onStart() {
		super.onStart();
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.baselayout);
		mTitle = (TextView)actionBar.getCustomView().findViewById(R.id.title);
	}
	
	public void showBack(boolean b) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	public void setTitle(String title) {
		mTitle.setText(title);
	}
	
	public void setTitle(int res_id) {
		mTitle.setText(res_id);
	}
	

}
