package com.peice;

import android.widget.CompoundButton;

import com.peice.QuestionAdapter.OnAnswerChanged;
import com.peice.model.TestQuestion;

public class SingleSelectQuestionAdapter extends SelectQuestionAdapter {
	
	public SingleSelectQuestionAdapter(TestQuestion tq, OnAnswerChanged onAnswerChanged) {
		super(tq, onAnswerChanged);
	}
	
	protected void onBrancheSelectChanged(CompoundButton btn, boolean checked){
		if(!checked)
			return;
		
		for(int i = 0; i < mBtnBranches.length; i++) {
			if(mBtnBranches[i] != btn)
				mBtnBranches[i].setChecked(false);
			else {
				StringBuilder b = new StringBuilder();
				b.append(IdxToAnswer(i));
				setAnswer(b.toString());
				onAnswerChanged();
			}
		}
		
	}
	
	@Override
	protected int getResId() {
		return R.layout.single_select_item;
	}
	
}
