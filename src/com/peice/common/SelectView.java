package com.peice.common;

import com.peice.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class SelectView extends LinearLayout
	implements Checkable , View.OnClickListener{
	CheckedTextView mIdView;
	TextView mContentView;
	String   mId;
	OnCheckedChangeListener mCheckedChangedListener;

	public interface OnCheckedChangeListener {
		public void onCheckedChanged(SelectView view, boolean ischecked);
	}
	
	public SelectView(Context context, AttributeSet attr) {
		super(context, attr);
		setClickable(true);
		setOnClickListener(this);
	}
	
	public void onFinishInflate() {
		mIdView = (CheckedTextView)findViewById(R.id.branch_id);
		mContentView = (TextView)findViewById(R.id.branch_content);
	}
	
	public void setId(String id) {
		mId = id;
		mIdView.setText(mId);
	}
	public void setContent(String content) {
		mContentView.setText(content);
	}

	@Override
	public void onClick(View v) {

		mIdView.toggle();
		if(mCheckedChangedListener != null) {
			mCheckedChangedListener.onCheckedChanged(this, isChecked());
		}
	}

	@Override
	public boolean isChecked() {
		return mIdView.isChecked();
	}
	
	protected boolean autoToggle() {
		return false;
	}

	@Override
	public void setChecked(boolean arg0) {
		boolean b = isChecked();
		
		if(b == arg0)
			return ;
		
		mIdView.setChecked(arg0);
		if(mCheckedChangedListener != null) {
			mCheckedChangedListener.onCheckedChanged(this, isChecked());
		}
	}

	@Override
	public void toggle() {
		mIdView.toggle();
	}
	
	public void  setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mCheckedChangedListener = listener;
	}
	
	
}
