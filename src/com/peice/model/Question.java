package com.peice.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Question {
	public static final int TYPE_SIGNLE_SELECT = 1;
	public static final int TYPE_MULTI_SELECT = 2;
	public static final int TYPE_SHORT_ANSWER = 3;
	public static final int TYPE_VACANCY = 4;
	
	QuestionGroup mGroup;
	String  mId;
	int     mType;
	int     mScore;
	String  mTrunk;
	int     mBranchCount; //1 for short answer
	Map<String, String> mBranches; //for select
	String  mModelAnswer;
	
	public String getId() {
		return mId;
	}
	public int getType() {
		return mType;
	}
	public int getScore() {
		return mScore;
	}
	public String getTrunk() {
		return mTrunk;
	}
	public int getBranchCount() {
		return mBranchCount;
	}
	
	public Map<String, String> getBranches() {
		return mBranches;
	}
	
	public boolean isObjectiveQuestion() {
		return mType == TYPE_SIGNLE_SELECT || mType == TYPE_MULTI_SELECT;
	}
	
	public String getModelAnswer() {
		return mModelAnswer;
	}
	
	static final String choice_list_key[] = {
		"choice_a",
		"choice_b",
		"choice_c",
		"choice_d",
		"choice_e",
		"choice_f",
		"choice_g",
		"choice_h"
	};
	static final String choice_list_id[] = {
		"A","B","C","D","E","F"
	};
	
	/*
	 {"paperid":"T100154P001",
	 	"quesid":"Q102158",
	 	"testid":"T100154"
	 	"qeustype":"T",
	 	"score":"1"
	 	"ordercode":"10",
      	"difficulty":"",
	 	"distinction":"",
         "quesbody":"2014年，新员工在办理入职手续时，人力资源中心会为每一位新员工发放《新员工导师手册》。",
	 	"quesmedia":"",
	 	"choice": {"A":"对","B":"错"}
	  }
	 */
	public static Question create(JSONObject json, QuestionGroup defgroup, Map<String, String> branches) {
		try {
			Question q = new Question();
			q.mGroup = defgroup;
			
			q.mId = json.getString("quesid");
			q.mType = parseType(json.optString("questype"));
			q.mScore = json.optInt("score");
			q.mTrunk = json.getString("quesbody");
			
			if (q.mType == TYPE_SIGNLE_SELECT || q.mType == TYPE_MULTI_SELECT) {
				if (branches == null) {
					q.mBranches = new HashMap<String, String>();
					for (int i = 0; i < choice_list_key.length; i ++) {
						try {
							String value = json.getString(choice_list_key[i]);
							if (value == null || value.length() == 0) {
								break;
							}
							
							q.mBranches.put(choice_list_id[i], value);
							
						}catch(Exception e) {
							break;
						}
					}
					
				}
				else {
					q.mBranches = branches;
				}
			}
			
			return q;
		}catch(JSONException e) {
			Log.e("Question", "Cannot parse json:" + e);
			return null;
		}
	}  
	
	private static int parseType(String t) {
		if (t == null) {
			return TYPE_SIGNLE_SELECT;
		}
		if (t.equals("T") || t.equals("S")) {
			return TYPE_SIGNLE_SELECT;
		}
		else if(t.equals("M")) {
			return TYPE_MULTI_SELECT;
		}
		else if(t.equals("Q")) {
			return TYPE_SHORT_ANSWER;
		}
		else if(t.equals("F")) {
			return TYPE_VACANCY;
		}
		else {
			return TYPE_SIGNLE_SELECT;
		}
	}
	
}
