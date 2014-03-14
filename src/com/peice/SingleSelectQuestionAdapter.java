package com.peice;

import android.widget.CompoundButton;

import com.peice.model.TestQuestion;

public class SingleSelectQuestionAdapter extends SelectQuestionAdapter {
	
	public SingleSelectQuestionAdapter(TestQuestion tq) {
		super(tq);
	}
	
	protected void onCheckedChanged(CompoundButton btn, boolean checked){
		if(!checked)
			return;
		
		for(int i = 0; i < mBtnBranches.length; i++) {
			if(mBtnBranches[i] != btn)
				mBtnBranches[i].setChecked(false);
			else {
				StringBuilder b = new StringBuilder();
				setAnswer(b.toString());
			}
		}
		
	}
	
	@Override
	protected int getResId() {
		return R.layout.single_select_item;
	}
	
}
