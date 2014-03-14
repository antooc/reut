package com.peice.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Examinee {
	int mId;
	String mName;
	String mEmail;
	int mGender;
	String mPassword;
	String mUserName;
	
	Map<Integer, Course>  mCursors;
	Map<Integer, Paper>   mPapers;
	List<PaperAnswer>     mPaperAnswers;
	
	public static final int UNSET = 0;
	public static final int MALE = 1;
	public static final int FEMALE = 2;
	
	public Examinee(int id, String userName, String password) {
		mId = id;
		mUserName = userName;
		mPassword = password;
	}
	
	public List<Course> queryCourse() {
		if(mCursors == null) {
			loadCourse();
		}
		
		Collection<Course> courses = mCursors.values();
		
		Iterator<Course> it = courses.iterator();
		
		List<Course> l = new ArrayList<Course>();
		
		while(it.hasNext()) {
			l.add(it.next());
		}
		
		return l;
	}
	
	public Paper getPapder(int courseId) {
		if(mPapers == null)
			return loadPaper(courseId);
		
		Paper p = mPapers.get(courseId);
		if(p == null)
			return loadPaper(courseId);
		
		return p;
	}
	
	
	public int getId() {
		return mId;
	}
	
	
	private void loadCourse() {
		mCursors = new HashMap<Integer, Course>();
		mCursors.put(1, new Course(1, "培训评估表","19道题/20分钟"));
		mCursors.put(2, new Course(2, "销售知识管理","10道题/10分钟"));
	}
	
	private Paper loadPaper(int courseId) {
		if(mPapers == null)
			mPapers = new HashMap<Integer, Paper>();
		
		//TODO
		Paper p = Paper.getDefault(courseId);
		
		mPapers.put(courseId, p);
		return p;
	}
}
