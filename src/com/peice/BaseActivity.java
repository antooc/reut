package com.peice;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class BaseActivity extends Activity {

	private FrameLayout mContent;
	private FrameLayout mToolbar;
	private View.OnClickListener mOnClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onToolbarClick(v, v.getId());
		}
		
	};

	@Override
	public void onCreate(Bundle onSaveInstance) {
		super.onCreate(onSaveInstance);
		
		setContentView(R.layout.baselayout);
		
		mContent = (FrameLayout)findViewById(R.id.content);
		mToolbar = (FrameLayout)findViewById(R.id.toolbar);
		
		inflateToolbarView(mToolbar, getLayoutInflater());
		inflateContentView(mContent, getLayoutInflater());
		
		setupToolbarListener(mToolbar);
	}
	
	protected void inflateContentView(ViewGroup content, LayoutInflater inflater) {
		
	}
	
	protected void inflateToolbarView(ViewGroup toolbar, LayoutInflater inflater) {
		
	}
	
	private void setupToolbarListener(ViewGroup vg) {
		int count = vg.getChildCount();
		for(int i = 0; i < count; i ++) {
			View view = vg.getChildAt(i);
			if(view instanceof ViewGroup) {
				setupToolbarListener((ViewGroup)view);
			}
			else {
				view.setOnClickListener(mOnClick);
			}
		}
	}
	
	protected void onToolbarClick(View view, int id) {
		
	}
	
}
