package com.peice.model;

import java.util.HashMap;
import java.util.Map;

public class ExamineeManager {
	Map<Integer, Examinee> mExaminees;
	static ExamineeManager _manager;
	
	public static ExamineeManager getManager() {
		if(_manager == null)
			_manager = new ExamineeManager();
		return _manager;
	}
	
	
	public Examinee login(String username, String password) {
		
		//TODO
		if("doon".equals(username) && "1234".equals(password)) {
			Examinee ex = new Examinee(1, username, password);
			mExaminees.put(1, ex);
			return ex;
		}
		return null;
	}
	
	public Examinee get(int id) {
		return mExaminees != null ? mExaminees.get(id) : null;
	}
	
	ExamineeManager() {
		mExaminees = new HashMap<Integer, Examinee>();
	}
	
}
