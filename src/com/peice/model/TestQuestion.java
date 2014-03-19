package com.peice.model;

import java.util.ArrayList;
import java.util.List;

public class TestQuestion {
	public static final int TYPE_SIGNLE_SELECT = 1;
	public static final int TYPE_MULTI_SELECT = 2;
	public static final int TYPE_SHORT_ANSWER = 3;
	public static final int TYPE_FILL_VACANCY  = 4;
	
	String mTrunk; //题干
	List<String>  mBranches; //题支
	int   mType; //题型
	int   mId; //试题的id
	QuestionGroup mGroup;

	TestQuestion(int id, int type, String trunk, List<String> branches, QuestionGroup group) {
		mId = id;
		mType = type;
		mTrunk = trunk;
		mBranches = branches;
		mGroup = group;
	}
	TestQuestion(int id, int type, String trunk) {
		this(id, type, trunk, null, null);
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
	public TestQuestion setGroup(QuestionGroup g) {
		mGroup = g;
		return this;
	}
	
	public QuestionGroup getGroup() {
		return mGroup;
	}
}
