package com.peice.model;

import java.util.HashMap;
import java.util.Map;

public class PaperAnswer {
	Map<Integer, TestAnswer> mAnswers;
	int mCourseId;
	
	public PaperAnswer(int cursorId) {
		mCourseId = cursorId;
		mAnswers = new HashMap<Integer, TestAnswer>();
	}
	
	public int getCount() {
		return mAnswers != null ? mAnswers.size() : 0;
	}
	
	public void setAnswer(int questionId, String answer) {
		TestAnswer ta = mAnswers.get(questionId);
		if(ta == null) {
			ta = new TestAnswer(questionId);
			mAnswers.put(questionId, ta);
		}
		
		ta.setAnswer(answer);
	}
	public String getAnswer(int questionId) {
		TestAnswer ta = mAnswers.get(questionId);
		return ta == null ? null : ta.getAnswer();
	}
}
