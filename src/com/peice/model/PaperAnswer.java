package com.peice.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PaperAnswer {
	Map<Integer, TestAnswer> mAnswers;
	int mCourseId;
	HashSet<Integer> mMarkedQuestions = new HashSet<Integer>();
	
	public static interface OnAnswerChanged {
		public void onAnswerChanged(int q_id, String answer);
		public void onMarkChanged(int q_id, boolean bmarked);
	}
	
	OnAnswerChanged mOnAnswerChanged;
	
	public PaperAnswer(int cursorId) {
		mCourseId = cursorId;
		mAnswers = new HashMap<Integer, TestAnswer>();
	}
	
	public int getCount() {
		return mAnswers != null ? mAnswers.size() : 0;
	}
	
	public void setListener(OnAnswerChanged on) {
		mOnAnswerChanged = on;
	}
	
	public void setAnswer(int questionId, String answer) {
		TestAnswer ta = mAnswers.get(questionId);
		if(ta == null) {
			ta = new TestAnswer(questionId);
			mAnswers.put(questionId, ta);
		}
		
		ta.setAnswer(answer);
		if(mOnAnswerChanged != null)
			mOnAnswerChanged.onAnswerChanged(questionId, answer);
	}
	public String getAnswer(int questionId) {
		TestAnswer ta = mAnswers.get(questionId);
		return ta == null ? null : ta.getAnswer();
	}
	
	public void setQuestionMark(int questionId, boolean bmarked) {
		if(bmarked) {
			mMarkedQuestions.add(questionId);
		}
		else {
			mMarkedQuestions.remove(questionId);
		}
		if(mOnAnswerChanged != null) {
			mOnAnswerChanged.onMarkChanged(questionId, bmarked);
		}
	}
	public boolean isQuestionMark(int questionId) {
		return mMarkedQuestions.contains(questionId);
	}
}
