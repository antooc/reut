package com.peice;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.peice.model.TestQuestion;

public abstract class QuestionAdapter {
	
	private static final String[] ANSWSER_LIST = {"A","B","C","D","E","F","G","H", "I", "J","K","M","L","N"};
	public static String IdxToAnswer(int idx) {
		return ANSWSER_LIST[idx];
	}
	
	
	public interface OnAnswerChanged {
		public void onAnswerChanged(TestQuestion tq, String answer);
		public void onAnswerFinished(TestQuestion tq, String answer);
	}

	private   OnAnswerChanged mOnAnswerChanged;
	protected TestQuestion mQuestion;
	
	private String mAnswer;
	
	public QuestionAdapter(TestQuestion question, OnAnswerChanged onAnswerChanged) {
		mQuestion = question;
		mOnAnswerChanged = onAnswerChanged;
	}
	
	abstract public void getBranchView(ViewGroup parent, LayoutInflater inflate);
	abstract protected int getResId();
	abstract public void onAnswerUpdated();
	
	protected void onAnswerChanged() {
		if(mOnAnswerChanged != null) {
			mOnAnswerChanged.onAnswerChanged(mQuestion, mAnswer);
		}
	}
	
	protected void onAnswerFinished() {
		if(mOnAnswerChanged != null) {
			mOnAnswerChanged.onAnswerFinished(mQuestion, mAnswer);
		}
	}
	
	public void updateAnswer(String answer) {
		setAnswer(answer);
		onAnswerUpdated();
	}
	
	protected void setAnswer(String answer) {
		mAnswer = answer;
	}
	
	public String getAnswer() {
		return mAnswer;
	}
	
	public static QuestionAdapter getAdapter(TestQuestion q, OnAnswerChanged onac) {
		
		switch(q.getType()) {
		case TestQuestion.TYPE_SIGNLE_SELECT:
			return new SingleSelectQuestionAdapter(q,onac);
		case TestQuestion.TYPE_MULTI_SELECT:
			return new SelectQuestionAdapter(q,onac);
		case TestQuestion.TYPE_SHORT_ANSWER:
			return new ShortAnswerQuestionAdapter(q,onac);
		}
		return null;
	}
}
