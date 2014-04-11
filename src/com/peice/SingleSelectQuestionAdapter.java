package com.peice;

import java.util.Iterator;

import android.widget.CompoundButton;

import com.peice.QuestionAdapter.OnAnswerChanged;
import com.peice.common.SelectView;
import com.peice.model.Question;

public class SingleSelectQuestionAdapter extends SelectQuestionAdapter {
	
	public SingleSelectQuestionAdapter(Question tq, OnAnswerChanged onAnswerChanged) {
		super(tq, onAnswerChanged);
	}
	
	protected void onBrancheSelectChanged(SelectView btn, boolean checked){
		if(!checked)
			return;
		
		Iterator<String> keyit = mBtnBranches.keySet().iterator();
		while (keyit.hasNext()) {
			String key = keyit.next();
			SelectView view = mBtnBranches.get(key);
			
			if (view == btn) {
				setAnswer(key);
				onAnswerChanged();
			}
			else {
				view.setChecked(false);
			}
		}
		
		onAnswerFinished();
	}
	
	@Override
	protected int getResId() {
		return R.layout.single_select_item;
	}
	
}
