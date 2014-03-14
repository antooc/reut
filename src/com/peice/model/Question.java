package com.peice.model;

public abstract class Question {
	public static final int TYPE_SIGNLE_SELECT = 1;
	public static final int TYPE_MULTI_SELECT = 2;
	public static final int TYPE_SHORT_ANSWER = 3;
	public static final int TYPE_GROUP = 4;
	
	public abstract int getType();
	public abstract String getTrunk();
	
}
