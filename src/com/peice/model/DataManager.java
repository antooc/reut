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
	public final static int MSG_SUBMIT_TEST = 4;
	public final static int MSG_SUBMIT_ALL = 4;
	public final static int MSG_TEST_LIST_UPDATED = 5;
	public final static int OK = 1;
	public final static int FAIL = 0;
	
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
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginname", name);
		params.put("loginpw", passwrod);
		
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
	
	public void getTestListStates(final Handler handle) {
		if (mCandidate == null || mCandidate.getLoginStatus() != Candidate.STATUS_OK) {
			return ;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("candid", mCandidate.getId());
		params.put("projid", mCandidate.getProjectId());
		
		final NetClient.Reciever reciever = new NetClient.Reciever() {
			
			@Override
			public void onStringReceived(String buffer) {
				String result = "OK";
				boolean ok = true;
				try {
					JSONArray json = new JSONArray(buffer);
					for (int i = 0; i < json.length(); i ++) {
						JSONObject jtest = json.getJSONObject(i);
						String id = jtest.getString("testid");
						Test test = mCandidate.getTest(id);
						if (test != null) {
							test.update(jtest);
						}
					}	
				} catch (JSONException e) {
					result = "Faield:" + e;
					ok = false;
				}
				
				Message msg = handle.obtainMessage();
				msg.what = MSG_TEST_LIST_UPDATED;
				msg.arg1 = ok ? OK : FAIL;
				msg.obj = result;
				handle.sendMessage(msg);
			}
		};
		
		mNetClient.post("json/m_list_json.php", params, reciever);
	}
	
	static String level_keys[] = {
		"level1",
		"level2",
		"level3",
		"level4",
		"level5",
		"level6",
		"level7",
		"level8",
		"level9",
	};
	static String level_values[] = { "1","2","3","4","5","6","7","8","9"};
	static String getLevelValue(String level) {
		for (int i = 0; i < level_keys.length; i ++) {
			if (level.equals(level_keys[i])) {
				return level_values[i];
			}
		}
		return level;
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
			msg.arg1 = OK;
			msg.obj = test;
			handle.sendMessage(msg);
			return ;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("projid", mCandidate.getProjectId());
		params.put("candid", mCandidate.getId());
		params.put("testid", test.getId());
		params.put("testtype",test.getTypeString());
		
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
							String result = json.optString("result");
							if (result == null) {
								result = "Unkown Error!";
							}
							Message msg = handle.obtainMessage();
							msg.what = MSG_QUERY_TEST;
							msg.arg1 = FAIL;
							msg.obj = result;
							handle.sendMessage(msg);
							
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
								branches.put(getLevelValue(key), value);
							}
						}
						
						JSONArray questions = json.getJSONArray("queslist");
						//reget the paper id
						test.refreash(json);
					
						test.loadQuestions(questions, branches);
					}catch(JSONException e) {
						Log.e("DataManager", "Get Test failed:"+e);
						e.printStackTrace();
						return ;
					}
				//}
				
				Message msg = handle.obtainMessage();
				msg.what = MSG_QUERY_TEST;
				msg.arg1 = OK;
				msg.obj = test;
				handle.sendMessage(msg);
				return ;
			}
		};
		
		mNetClient.post("json/m_test_json.php", params, reciever);
	}
	
	public void submitTest(Test test, String begintime, final Handler handle) {
		if(mCandidate == null) {
			return ;
		}
		
		Map<String, String> answers = test.getAnswers();
		if (answers == null) {
			return ;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("candid", mCandidate.getId());
		params.put("projid", mCandidate.getProjectId());
		params.put("testid", test.getId());
		params.put("paperid", test.getPaperId());
		params.put("testtype", test.getTypeString());
		//TODO begin time yymmddhhmm
		params.put("begintime", begintime);
		
		Iterator<String> iterator = answers.keySet().iterator();
		
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = answers.get(key);
			params.put(key, value);
		}
		
		//Submit
		final NetClient.Reciever reciever = new NetClient.Reciever() {

			@Override
			public void onStringReceived(String buffer) {
				//1. 提交成功 {"status":"OK","result":"进交完成！"}
				//2. 提交失败 {"status":"FAIL","result":"提交失败，不能完成提交！"}
				try {
					JSONObject json = new JSONObject(buffer);
					String status = json.getString("status");
					int istatus = FAIL;
					if (status.equalsIgnoreCase("OK")) {
						istatus = OK;
					}
					String errmsg = json.optString("result");
					
					Message msg = handle.obtainMessage();
					msg.what = MSG_SUBMIT_TEST;
					msg.arg1 = istatus;
					msg.obj = errmsg;
					handle.sendMessage(msg);
				}catch (Exception e) {
					Log.e("DataManager", "Submit failed:"+e);
					e.printStackTrace();
				}
			}
		};
		
		mNetClient.post("json/m_test_submit_json.php", params, reciever);
	}
	
	public void submitAll(final Handler handle) {
		if(mCandidate == null) {
			return ;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("candid", mCandidate.getId());
		params.put("projid", mCandidate.getProjectId());
		
		//Submit
				final NetClient.Reciever reciever = new NetClient.Reciever() {

					@Override
					public void onStringReceived(String buffer) {
						//1. 提交成功 {"status":"OK","result":"进交完成！"}
						//2. 提交失败 {"status":"FAIL","result":"提交失败，不能完成提交！"}
						String errmsg = "";
						int istatus = FAIL;
						try {
							JSONObject json = new JSONObject(buffer);
							String status = json.getString("status");
							if (status.equalsIgnoreCase("OK")) {
								istatus = OK;
							}
							errmsg = json.optString("result");
							
						}catch (Exception e) {
							Log.e("DataManager", "Submit failed:"+e);
							e.printStackTrace();
							istatus = FAIL;
							errmsg = "Submit failed:" + e;
						}
						
						Message msg = handle.obtainMessage();
						msg.what = MSG_SUBMIT_ALL;
						msg.arg1 = istatus;
						msg.obj = errmsg;
						handle.sendMessage(msg);
					}
				};
				
				mNetClient.post("json/m_submit_json.php", params, reciever);
	}
	
	public Candidate getCandidate() {
		return mCandidate;
	}
	
}
