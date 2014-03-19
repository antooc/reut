package com.peice.model;


public class QuestionGroup {
	
	String mTrunk;
	
	public QuestionGroup(String trunk) {
		mTrunk = trunk;
	}
	
	public String getTrunk() {
		return mTrunk;
	}
	
	public QuestionGroup setTrunk(String trunk) {
		mTrunk = trunk;
		return this;
	}
	
	
}
