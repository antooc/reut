package com.peice;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peice.model.TestQuestion;

public class ShortAnswerQuestionAdapter extends QuestionAdapter{
	
	public ShortAnswerQuestionAdapter(TestQuestion tq) {
		super(tq);
	}
	
	@Override
	protected int getResId() {
		return R.layout.short_answer_item;
	}
	
	@Override
	public void getBranchView(ViewGroup parent, LayoutInflater inflater) {
		TextView ev = (TextView)inflater.inflate(getResId(), null);
		ev.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				StringBuffer buf = new StringBuffer();
				buf.append(arg0);
				setAnswer(buf.toString());
			}
			
		});
		parent.addView(ev);
	
	}
	
	
}
