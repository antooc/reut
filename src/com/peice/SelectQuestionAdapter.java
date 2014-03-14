package com.peice;

import java.util.List;

import com.peice.model.TestQuestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SelectQuestionAdapter extends QuestionAdapter{

	protected CompoundButton[] mBtnBranches;
	
	public SelectQuestionAdapter(TestQuestion tq) {
		super(tq);
	}

	@Override
	public void getBranchView(ViewGroup parent, LayoutInflater inflater) {
		if(mQuestion == null)
			return ;
		List<String> branches = mQuestion.getBranches();
		
		if(branches == null || branches.size() <= 0)
			return ;
		
		mBtnBranches = new CompoundButton[branches.size()];
		
		for(int i = 0; i < branches.size(); i ++) {
			View view = inflater.inflate(getResId(), null);
			if(view == null)
				continue;
			
			if(view instanceof TextView) {
				((TextView)view).setText(branches.get(i));
			}
			
			//add listener
			mBtnBranches[i] = (CompoundButton)view;
			
			mBtnBranches[i].setOnCheckedChangeListener(mOnCheckedChanged);
			
			parent.addView(view);
		}
		
		
	} 
	
	@Override
	protected int getResId() {
		return R.layout.multi_select_item;
	}
	
	private CompoundButton.OnCheckedChangeListener mOnCheckedChanged= new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton btn, boolean checked) {
			onCheckedChanged(btn, checked);
		}
		
	};
	
	protected void onCheckedChanged(CompoundButton btn, boolean checked) {
		StringBuilder answer = new StringBuilder();
		for(int i = 0; i < mBtnBranches.length; i++) {
			if(mBtnBranches[i].isChecked()) {
				answer.append('A' + i);
			}
		}
		setAnswer(answer.toString());
	}
	
}
