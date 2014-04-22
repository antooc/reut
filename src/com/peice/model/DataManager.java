package com.peice.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.peice.net.NetClient;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DataManager {
	private static DataManager _manager;
	
	public final static int MSG_LOGIN = 1; //
	public final static int MSG_UPDATE = 2;
	public final static int MSG_QUERY_TEST = 3;
	
	NetClient  mNetClient;
	Candidate mCandidate;
	
	public static DataManager getInstance() {
		if (_manager == null) {
			_manager = new DataManager();
		}
		return _manager;
	}
	
	private DataManager() {
		mNetClient = new NetClient("http://www.peice.com.cn");
	}
	
	public void login(String name, String passwrod, final Handler handle) {
		if(mCandidate != null) {
			Message msg = handle.obtainMessage();
			msg.what = MSG_LOGIN;
			msg.obj = mCandidate;
			handle.sendMessage(msg);
			return ;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginname", name);
		params.put("loginpw", name);
		
		final NetClient.Reciever reciever = new NetClient.Reciever() {

			@Override
			public void onStringReceived(String buffer) {
				mCandidate = new Candidate(buffer);
				Message msg = handle.obtainMessage();
				msg.what = MSG_LOGIN;
				msg.obj = mCandidate;
				handle.sendMessage(msg);
			}
		};
		
		mNetClient.post("json/m_login_json.php", params, reciever);
	}
	
	public void updateCandidateInfo(String name, String email, boolean bmale, final Handler handle) {
		if (mCandidate == null || mCandidate.getLoginStatus() != Candidate.STATUS_OK) {
			return ;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("candname", name);
		params.put("candsex", bmale ? "M" : "F");
		params.put("candemail", email);
		params.put("candid", mCandidate.getId());
		mCandidate.setName(name);
		
		final NetClient.Reciever reciever = new NetClient.Reciever() {

			@Override
			public void onStringReceived(String buffer) {
				boolean bok = false;
				String result = "OK";
				try {
					JSONObject json = new JSONObject(buffer);
					
					String status = json.getString("status");
					bok = status.equals("OK");
					
					result = json.optString("result");
					
				}catch(JSONException e) {
					result = "Faield:" + e;
				}
				
				Message msg = handle.obtainMessage();
				msg.what = MSG_UPDATE;
				msg.arg1 = bok ? 1 : 0;
				msg.obj = result;
				
				handle.sendMessage(msg);
			}
		};

		mNetClient.post("json/m_inputinfo_json.php", params, reciever);
	}
	
	public void queryTest(String testId, final Handler handle) {
		if(mCandidate == null) {
			return ;
		}
		
		final Test test = mCandidate.getTest(testId);
		if (test == null) {
			Log.e("DataManager", "invalidate test id:"+testId);
			return;
		}
		
		if (test.isQuestionLoaded()) {
			//send
			Message msg = handle.obtainMessage();
			msg.what = MSG_QUERY_TEST;
			msg.obj = test;
			handle.sendMessage(msg);
			return ;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("projid", mCandidate.getProjectId());
		params.put("candid", mCandidate.getId());
		params.put("testid", test.getId());
		
		final NetClient.Reciever reciever = new NetClient.Reciever() {

			@Override
			public void onStringReceived(String buffer) {
				/*if (test.getType() == Test.TYPE_EVA) { //xml 格式的评估表
					try {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder builder = factory.newDocumentBuilder();
						InputStream inStream = new ByteArrayInputStream(buffer.getBytes());
						Document dom = builder.parse(inStream);
						test.loadQuestion(dom);
						inStream.close();
					}catch(Exception e) {
						Log.e("DataManager", "Parse XML String Failed:" + e);
						return ;
					}
                    
				}
				else if(test.getType() == Test.TYPE_NORMAL) {*/ //json格式
					
					try {
						Map<String,String> branches = null;
						JSONObject json = new JSONObject(buffer);
						String status = json.getString("status");
						if (!status.equalsIgnoreCase("OK")) {
							Log.e("DataManager", "Get the Test " + test.getId() + " failed!");
							return ;
						}
						
						//try get level
						if (test.getType() == Test.TYPE_EVA) {
							branches = new HashMap<String, String>();
							JSONObject jlevel = json.getJSONObject("level");
							Iterator<String> it = (Iterator<String>)jlevel.keys();
							while (it.hasNext()) {
								String key = it.next();
								String value = jlevel.getString(key);
								branches.put(key, value);
							}
						}
						
						JSONArray questions = json.getJSONArray("testquestions");
					
						test.loadQuestions(questions, branches);
					}catch(JSONException e) {
						Log.e("DataManager", "Get Test failed:"+e);
						e.printStackTrace();
						return ;
					}
				//}
				
				Message msg = handle.obtainMessage();
				msg.what = MSG_QUERY_TEST;
				msg.obj = test;
				handle.sendMessage(msg);
				return ;
			}
		};
		
		mNetClient.post("json/m_test_json.php", params, reciever);
	}
	
	public Candidate getCandidate() {
		return mCandidate;
	}
	
}
