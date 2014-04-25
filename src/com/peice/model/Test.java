package com.peice.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

public class Test {
	public final static char TYPE_EVA = 'S'; //评估表
	public final static char TYPE_NORMAL = 'E'; 
	
	String mId;
	String mName;
	int   mLength;
	char  mType;
	boolean mIsComplete;
	boolean mQuestionLoaded;
	boolean mAutoCheck;
	
	public static interface OnAnswerChanged {
		public void onAnswerChanged(String q_id, String answer);
		public void onMarkChanged(String q_id, boolean bmarked);
	}
	
	OnAnswerChanged mOnAnswerChanged;
	
	List<Question> mQuestions = new ArrayList<Question>(); 
	Map<String, String> mAnswers;
	HashSet<String> mMarkedQuestions = new HashSet<String>();
	
	
	Test() {
		
	}
	
	public String getId() {
		return mId;
	}
	
	public String getName() {
		return mName;
	}
	
	public int getTimeLength() {
		return mLength;
	}
	
	public int getType() {
		return mType;
	}
	
	public String getDescription() {
		return Integer.toString(mLength) + "分钟";
	}
	
	public boolean isComplete() {
		return mIsComplete;
	}
	
	public void setComplete(boolean b) {
		mIsComplete = b;
	}
	
	public boolean isQuestionLoaded() {
		return mQuestionLoaded;
	}
	
	public int getQuestionCount() {
		return mQuestions.size();
	}
	
	public List<Question> getQuestions() {
		return mQuestions;
	}
	
	public Question getQuestion(int idx) {
		return mQuestions.get(idx);
	}
	
	public Question getQuestion(String qesuid) {
		for (Question q : mQuestions) {
			if (q.getId().equals(qesuid)) {
				return q;
			}
		}
		return null;
	}
	
	public void setAnswer(String qid, String answer) {
		if (mAnswers == null) {
			mAnswers = new HashMap<String, String>();
		}
		mAnswers.put(qid, answer);
		if (mOnAnswerChanged != null) {
			mOnAnswerChanged.onAnswerChanged(qid, answer);
		}
	}
	public String getAnswer(String qid) {
		if (mAnswers == null) {
			return null;
		}
		
		return mAnswers.get(qid);
	}
	
	/*
	 * {"testid":"T100001","testname":"培训评估表","timelength":"20","testtype":"S"},
	 */
	public static Test create(JSONObject json) {
		try {
			Test test = new Test();
			test.mId = json.getString("testid");
			test.mName = json.getString("testname");
			test.mLength = json.getInt("timelength");
			String type = json.getString("testtype");
			test.mType = type.toCharArray()[0];
			
			return test;
		}catch(JSONException e) {
			Log.e("Test", "Cannot parse json:" + e);
			return null;
		}
	}
	
	/*
	 * m_test_json.php 
	输入：projid=<...>&candid=<...>&testid=<...>
	输出：如果是评估表，请直接返回xml文件
		如果是试卷，请返回一个json字符串，如下
		{"status":"TEST_IMCOMPLET",
		 "Questions":[
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
			....
		]}
	说明:
		1. 如果有些字段不是必需的，请不要放在其中，如ordercode,difficulty,distinction这些字段
		2. questype取值有那些？代表那些含义？
	 */
	public void loadQuestions(JSONArray questions, Map<String, String> branches) {		
		QuestionGroup group = new QuestionGroup(mName);
		try {
			for(int i = 0; i < questions.length(); i++) {
				Question tq = Question.create(questions.getJSONObject(i), group, branches);
				if (tq != null) {
					mQuestions.add(tq);
				}
			}
			
			mQuestionLoaded = true;
		}catch(JSONException e) {
			Log.e("Test", "load questions error:"+e);
		}
	}
	
	public void loadQuestion(Document doc) {
		if (mType != TYPE_EVA) {
			Log.e("Test", "incorrect question type, need Evaluation!");
			return ;
		}
		
		Element rootElement = doc.getDocumentElement();
		
		if (!rootElement.getNodeName().equals("evaluation")) {
			Log.e("Test", "parse Evaluation error: incorrect root element!");
			return ;
		}
		
		QuestionGroup group = new QuestionGroup(mName);
		
		NodeList children = rootElement.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			Element e = (Element)node;
			if(e.getNodeName().equals("index")) {
				loadIndex(e, group);
			}
			else if(e.getNodeName().equals("question")) {
				loadQuestion(e, group);
			}
			
		}

		mQuestionLoaded = true;
	}
	
	private void loadIndex(Element e, QuestionGroup defgroup) {
		//get the levels
		Map<String, String> branches = new HashMap<String, String>();
		branches.put("A", e.getAttribute("level5"));
		branches.put("B", e.getAttribute("level4"));
		branches.put("C", e.getAttribute("level3"));
		branches.put("D", e.getAttribute("level2"));
		branches.put("E", e.getAttribute("level1"));
		
		//load statements
		NodeList statements = e.getElementsByTagName("statement");
		for (int i = 0; i < statements.getLength(); i ++) {
			Element statement = (Element) statements.item(i);
			//create group
			QuestionGroup group = new QuestionGroup(statement.getAttribute("content"));
			
			//get items and create questions
			NodeList items = statement.getElementsByTagName("item");
			for (int j = 0; j < items.getLength(); j ++) {
				loadItem((Element)items.item(i), group, branches, Question.TYPE_SIGNLE_SELECT);
			}
		}
		
	}
	
	private void loadQuestion(Element e, QuestionGroup defgroup) {
		NodeList items = e.getElementsByTagName("item");
		for (int i = 0; i < items.getLength(); i ++) {
			loadItem((Element)items.item(i), defgroup, null, Question.TYPE_SHORT_ANSWER);
		}
	}
	
	private void loadItem(Element e, QuestionGroup group, Map<String, String> branches, int type) {
		Question q = new Question();
		q.mId = e.getAttribute("id");
		q.mGroup = group;
		q.mTrunk = e.getAttribute("content");
		q.mType = type;
		q.mBranches = branches;
		mQuestions.add(q);
	}
	
	public void setQuestionMark(String questionId, boolean bmarked) {
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
	public boolean isQuestionMark(String questionId) {
		return mMarkedQuestions.contains(questionId);
	}
	
	public void setListener(OnAnswerChanged on) {
		mOnAnswerChanged = on;
	}
	
	public boolean getAutoCheck() {
		return mAutoCheck;
	}
}
