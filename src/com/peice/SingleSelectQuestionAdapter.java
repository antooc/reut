package com.peice;

import android.widget.CompoundButton;

import com.peice.QuestionAdapter.OnAnswerChanged;
import com.peice.common.SelectView;
import com.peice.model.TestQuestion;

public class SingleSelectQuestionAdapter extends SelectQuestionAdapter {
	
	public SingleSelectQuestionAdapter(TestQuestion tq, OnAnswerChanged onAnswerChanged) {
		super(tq, onAnswerChanged);
	}
	
	protected void onBrancheSelectChanged(SelectView btn, boolean checked){
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
		onAnswerFinished();
	}
	
	@Override
	protected int getResId() {
		return R.layout.single_select_item;
	}
	
}
