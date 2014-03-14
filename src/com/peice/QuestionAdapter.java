package com.peice;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.peice.model.Question;
import com.peice.model.TestQuestion;

public abstract class QuestionAdapter {

	protected TestQuestion mQuestion;
	
	private String mAnswer;
	
	public QuestionAdapter(TestQuestion question) {
		mQuestion = question;
	}
	
	abstract public void getBranchView(ViewGroup parent, LayoutInflater inflate);
	abstract protected int getResId();
	
	protected void setAnswer(String answer) {
		mAnswer = answer;
	}
	
	public String getAnswer() {
		return mAnswer;
	}
	
	public static QuestionAdapter getAdapter(TestQuestion q) {
		
		switch(q.getType()) {
		case Question.TYPE_SIGNLE_SELECT:
			return new SingleSelectQuestionAdapter(q);
		case Question.TYPE_MULTI_SELECT:
			return new SelectQuestionAdapter(q);
		case Question.TYPE_SHORT_ANSWER:
			return new ShortAnswerQuestionAdapter(q);
		}
		return null;
	}
}
