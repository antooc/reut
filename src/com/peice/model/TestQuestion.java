package com.peice.model;

import java.util.ArrayList;
import java.util.List;

public class TestQuestion extends Question {
	
	String mTrunk; //题干
	List<String>  mBranches; //题支
	int   mType; //题型
	int   mId; //试题的id

	TestQuestion(int id, int type, String trunk, List<String> branches) {
		mId = id;
		mType = type;
		mTrunk = trunk;
		mBranches = branches;
	}
	TestQuestion(int id, int type, String trunk) {
		this(id, type, trunk, null);
	}
	
	public int getId() {
		return mId;
	}
	
	public int getType() {
		return mType;
	}
	
	public String getTrunk() {
		return mTrunk;
	}
	
	public List<String> getBranches() {
		return mBranches;
	}
	
	public TestQuestion setTrunk(String trunk) {
		mTrunk = trunk;
		return this;
	}
	public TestQuestion addBranch(String branch) {
		if(mBranches == null)
			mBranches = new ArrayList<String>();
		mBranches.add(branch);
		return this;
	}
}
