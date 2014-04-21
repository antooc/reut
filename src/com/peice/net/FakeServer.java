package com.peice.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

public class FakeServer {
	String mHost;
	Context mContext;
	
	static FakeServer _instance;
	
	public static void init(String host) {
		getInstance().mHost = host;
	}
	
	public static FakeServer getInstance() {
		if (_instance == null) {
			_instance = new FakeServer();
		}
		return _instance;
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public String getBuffer(String url, String params) {
		//read 
		try {
			String ret = "";
			InputStream in = mContext.getAssets().open("fake_server.txt");
		
			if (in == null) {
				return "";
			}
			
			String suburl = url.substring(mHost.length() + 1);
			
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			
			String line="";
			while ((line = br.readLine()) != null) {
				if(line.equals(suburl)) {
					break;
				}
			}
			
			if (line == null) {
				in.close();
				return ret;
			}
			
			Map<String,String> mparams = null;
			if (params.length() > 0) {
				 mparams = new HashMap<String, String>();
				 String[] key_values = params.split("&");
				 for (int i = 0; i < key_values.length; i ++) {
					 String [] kv = key_values[i].split("=");
					 mparams.put(kv[0], kv[1]);
				 }
			}
			
			while (line != null) {
				Map<String,String> ps = null;
				if (mparams != null) {
					ps = new HashMap<String, String>();
				}
				//get the params;
				while ((line = br.readLine()) != null) {
					if (line.equals("==START==")) {
						break;
					}
					if (ps != null) {
						String[] arr = line.split("=");
						ps.put(arr[0], arr[1]);
					}
				}
				
				if (ps != null && ps.size() > 0) {
					Iterator<String> it = ps.keySet().iterator();
					boolean matched = true;
					while (it.hasNext()) {
						String key = it.next();
						String value = ps.get(key);
						String mvalue = mparams.get(key);
						if (!(mvalue != null && mvalue.equals(value))) {
							matched = false;
							break;
						}
					}
					if (matched) {
						break;
					}
				}
			}
			
			if (line == null) {
				in.close();
				return "";
			}
			
			//get the buffer
			while ((line = br.readLine()) != null) {
				if (line.equals("==END==")) {
					break;
				}
				ret += line;
			}
			
			in.close();
			return ret;
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
		/*
		
		if (url.equals(mHost + "/m_login_json.php")) {
			return "{\"status\":\"OK\","
				+ "\"result\":\"登陆成功\"," 
				+ "\"candid\":\"1000044802\","
				+ "\"candname\":\"NEWNAME\","
				+ "\"projid\":\"P100190\","
				+ "\"projname\":\"民建经销商经营决策沙盘\","
				+ "\"notice\":\"本次测试的成绩关系到各位的学分获取\","
				+ "\"tests\":["
				+ "	{\"testid\":\"T100001\",\"testname\":\"培训评估表\",\"timelength\":\"20\",\"testtype\":\"S\"},"
				+ "	{\"testid\":\"T100162\",\"testname\":\"民建经销商经营决策沙盘\",\"timelength\":\"40\",\"timetype\":\"E\"}"
				+ "]}";
		}
		
		return "";*/
	}
	
}
