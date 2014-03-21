package com.peice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

public class AnswerView extends CheckedTextView {
	public int mState;
	
	private static final int ANSWER = 0x10;
	private static final int MARK = 0x20;
	private static final int RESULT_MASK = 0x0F;
	public static final int ANSWER_RIGHT = 1;
	public static final int ANSWER_WRONG = 2;
	public static final int ANSWER_UNKNOWN = 0;
	
	private Drawable mRightDrawable;
	private Drawable mWrongDrawable;
	
	public AnswerView(Context context, AttributeSet attr) {
		super(context, attr);
		setClickable(true);
		mRightDrawable = context.getResources().getDrawable(R.drawable.answerview_right);
		mWrongDrawable = context.getResources().getDrawable(R.drawable.answerview_wrong);
	}
	
	public void setAnswer(int answer) {
		if(ANSWER_RIGHT == answer) {
			mState = (mState & ~RESULT_MASK) | ANSWER_RIGHT;
		}
		else if(ANSWER_WRONG == answer){
			mState = (mState & ~RESULT_MASK) | ANSWER_WRONG;
		}
		else {
			mState = (mState & ~RESULT_MASK);
		}
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
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Drawable drawable = null;
		if(ANSWER_RIGHT == (mState & RESULT_MASK)) {
			drawable = mRightDrawable;
		}
		else if(ANSWER_WRONG == (mState & RESULT_MASK)) {
			drawable = mWrongDrawable;
		}
		
		if(drawable != null) {
			int d_width = drawable.getIntrinsicWidth();
			int d_height = drawable.getIntrinsicHeight();
			
			drawable.setBounds(getRight() - d_width, getBottom() - d_height, getRight(), getBottom());
			drawable.draw(canvas);
		}
	}
}
