package com.peice.model;

public class TestAnswer {
	
	int mId; //Question id
	String mAnswer;
	
	public TestAnswer(int id) {
		mId = id;
	}
	
	public void setAnswer(String answer) {
		mAnswer = answer;
	}
	public String getAnswer() {
		return mAnswer;
	}
}
