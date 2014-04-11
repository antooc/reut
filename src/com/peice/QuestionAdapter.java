package com.peice;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.peice.model.Question;

public abstract class QuestionAdapter {
	
	private static final String[] ANSWSER_LIST = {"A","B","C","D","E","F","G","H", "I", "J","K","M","L","N"};
	public static String IdxToAnswer(int idx) {
		return ANSWSER_LIST[idx];
	}
	
	
	public interface OnAnswerChanged {
		public void onAnswerChanged(Question tq, String answer);
		public void onAnswerFinished(Question tq, String answer);
	}

	private   OnAnswerChanged mOnAnswerChanged;
	protected Question mQuestion;
	
	private String mAnswer;
	
	public QuestionAdapter(Question question, OnAnswerChanged onAnswerChanged) {
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
	
	public static QuestionAdapter getAdapter(Question q, OnAnswerChanged onac) {
		
		switch(q.getType()) {
		case Question.TYPE_SIGNLE_SELECT:
			return new SingleSelectQuestionAdapter(q,onac);
		case Question.TYPE_MULTI_SELECT:
			return new SelectQuestionAdapter(q,onac);
		case Question.TYPE_VACANCY:
			return new VacancyQuestionAdapter(q, onac);
		case Question.TYPE_SHORT_ANSWER:
			return new ShortAnswerQuestionAdapter(q,onac);
		}
		return null;
	}
}
