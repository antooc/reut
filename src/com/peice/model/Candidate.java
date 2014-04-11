package com.peice.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Candidate {
	String mCandId;
	String mCandName;
	String mProjId;
	String mProjName;
	String mNotice;
	String mLoginResult;
	int   mLoginStatus;
	public final static int STATUS_OK = 1;
	public final static int STATUS_FAIL = 0;
	public final static String NEWNAME = "NEWNAME";
	List<Test> mTests = new ArrayList<Test>();
	
	
	//解析json字符串生成对象
	Candidate(String json) {
		parse(json);
	}
	
	public String getId() {
		return mCandId;
	}
	
	public String getName() {
		return mCandName;
	}
	
	public void setName(String name) {
		mCandName = name;
	}
	
	public int getLoginStatus() {
		return mLoginStatus;
	}
	public String getLoginResult() {
		return mLoginResult;
	}
	
	public String getProjectId() {
		return mProjId;
	}
	public String getProjectName() {
		return mProjName;
	}
	
	public List<Test> getTests() {
		return mTests;
	}
	
	public String getNotice() {
		return mNotice;
	}
	
	public Test  getTest(String id) {
		for(Test test : mTests) {
			if(test.getId().equals(id)) {
				return test;
			}
		}
		return null;
	}
	
	private void parse(String json) {
		try {
			parse(new JSONObject(json));
		}catch(JSONException e) {
			Log.i("Candidate", "Parse Json error:"+e);
		}
	}
	
	/*
	 * m_login_json:
		输入参数： loginname=<..>&loginpw=<..>
		输出： {"status":"OK", 
			"result":"登陆成功", 
			"candid":"1000044802",
			"candname":"NEWNAME",
			"projid":"P100190",
			"projname":"民建经销商经营决策沙盘",
			"notice":"本次测试的成绩关系到各位的学分获取",
			"tests":[
				{"testid":"T100001","testname":"培训评估表","timelength":"20","testtype":"S"},
				{"testid":"T100162","testname":"民建经销商经营决策沙盘","timelength":"40","timetype":"E"}
			]}
		
		说明：
			1. 登陆成功后，直接返回信息。如果candname是NEWNAME，直接返回NEWNAME，客户端自动检测
			2. status 返回状态码。只需要返回OK,FAIL即可。 result字段返回错误信息。如果status为OK，那么result字段被忽略，如果是FAIL，将自动显示一个页面，页面的提示信息为result字段内容；
			3. candid, candname, projname, notice，请用数据库内对应字段填充，请不要直接把sql查询结果返回
			4. tests字段返回一个数组，数组的元素是每项测试表
			5. tests元素包含testid, testname, timelength和testtype，请用数据库对应字段填充，不要直接返回sql查询结果。
	 */
	private void parse(JSONObject json) {
		//parse status
		try {
			String ret = json.getString("status");
			if(ret == null) {
				mLoginStatus = STATUS_FAIL;
				mLoginResult = "Parse JSON Failed!";
				return;
			}
			mLoginResult = json.optString("result");
			
			mCandId    = json.getString("candid");
			mCandName  = json.getString("candname");
			mProjId    = json.getString("projid");
			mProjName  = json.getString("projname");
			mNotice    = json.getString("notice");
			
			JSONArray tests = json.getJSONArray("tests");
			for(int i = 0; i < tests.length(); i++) {
				Test test = Test.create((JSONObject)tests.get(i));
				if (test != null) {
					mTests.add(test);
				}
			}
			
		}catch(JSONException e) {
			Log.e("Candidate", "Cannot parse json:" + e);
		}
	}
	
}
