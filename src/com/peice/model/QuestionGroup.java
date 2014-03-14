package com.peice.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionGroup extends Question {
	
	String mTrunk;
	List<Question>  mQuestions;
	
	@Override
	public int getType() {
		return TYPE_GROUP;
	}
	
	@Override
	public String getTrunk() {
		return mTrunk;
	}
	
	public QuestionGroup setTrunk(String trunk) {
		mTrunk = trunk;
		return this;
	}
	
	public QuestionGroup addQuestion(Question q) {
		if(mQuestions == null)
			mQuestions = new ArrayList<Question>();
		mQuestions.add(q);
		return this;
	}
	
	public List<Question> getQuestions() {
		return mQuestions;
	}
	
	public int getTotalQuestionCount() {
		if(mQuestions == null)
			return 0;
		
		int count = 0;
		for(Question q : mQuestions) {
			if(q instanceof QuestionGroup) {
				count += ((QuestionGroup)q).getTotalQuestionCount();
			}
			else {
				count ++;
			}
		}
		return count;
	}
	
}
