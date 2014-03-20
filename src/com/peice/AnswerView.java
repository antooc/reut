package com.peice;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

public class AnswerView extends CheckedTextView {
	public int mState;
	
	private static final int ANSWER = 0x1;
	private static final int MARK = 0x2;
	
	public AnswerView(Context context, AttributeSet attr) {
		super(context, attr);
		setClickable(true);
	}
	
	public void setHasAnswer(boolean b) {
		if(b)
			mState |= ANSWER;
		else
			mState &= ~ANSWER;
		updateState();
	}
	public void setMark(boolean b) {
		if(b)
			mState |= MARK;
		else
			mState &= ~MARK;
		updateState();
	}
	
	private void updateState() {
		setChecked((mState & ANSWER) == ANSWER);
		setSelected((mState & MARK) == MARK);
	}
}
