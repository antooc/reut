package com.peice;

import com.peice.model.PaperAnswer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class AnswerCard extends FrameLayout {
	PaperAnswer mAnswer;
	public AnswerCard(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public PaperAnswer getAnswer() {
		return mAnswer;
	}
	public void setAnswer(PaperAnswer answer) {
		mAnswer = answer;
	}
}
