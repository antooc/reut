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
	public static Question create(JSONObject json, QuestionGroup defgroup) {
		try {
			Question q = new Question();
			q.mGroup = defgroup;
			
			q.mId = json.getString("quesid");
			q.mType = parseType(json.optString("questype"));
			q.mScore = json.getInt("score");
			q.mTrunk = json.getString("quesbody");
			
			if (q.mType == TYPE_SIGNLE_SELECT || q.mType == TYPE_MULTI_SELECT) {
				JSONObject choice = json.getJSONObject("choice");
				q.mBranches = new HashMap<String, String>();
				Iterator<String> it = (Iterator<String>)choice.keys();
				while (it.hasNext()) {
					String key = it.next();
					String value = choice.getString(key);
					q.mBranches.put(key, value);
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
		else if(t.equals("A")) {
			return TYPE_SHORT_ANSWER;
		}
		else if(t.equals("V")) {
			return TYPE_VACANCY;
		}
		else {
			return TYPE_SIGNLE_SELECT;
		}
	}
	
}
