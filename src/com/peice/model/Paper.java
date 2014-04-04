package com.peice.model;

import java.util.ArrayList;
import java.util.List;

public class Paper {
	int mCourseId;
	int mTime;
	boolean mAutoCheck = false; //是否自动判卷
	PaperAnswer mAnswers;
	
	List<TestQuestion> mQuestions = new ArrayList<TestQuestion>(); 
	
	Paper(int cursorId, int time) {
		mCourseId = cursorId;
		mTime = time;
	}
	
	public Paper setAutoCheck(boolean b) {
		mAutoCheck = b;
		return this;
	}
	public boolean getAutoCheck() {
		return mAutoCheck;
	}

	
	public int count() {
		return mQuestions.size();
	}
	
	public TestQuestion get(int idx) {
		return mQuestions.get(idx);
	}
	
	public Paper addQuestion(TestQuestion q) {
		mQuestions.add(q);
		return this;
	}
	
	public int getAnswerCount() {
		return mAnswers != null ? mAnswers.getCount() : 0;
	}
	
	public int getCourseId() {
		return mCourseId;
	}
	
	public PaperAnswer getAnswers() {
		if(mAnswers == null) {
			mAnswers = new PaperAnswer(mCourseId);
		}
		return mAnswers;
	}
	
	
	public int getTime() {
		return mTime;
	}
	
	
	//TODO
	static Paper _paper1;
	static Paper _paper2;
	static public Paper getDefault(int id) {
		if(id == 1 && _paper1 == null) {
			
			java.util.List<String> branches = new java.util.ArrayList<String>();
			branches.add("非常同意");
			branches.add("同意");
			branches.add("一般");
			branches.add("不同意");
			branches.add("非常不同意");
			
			_paper1 = new Paper(id, 19*60);
			
			QuestionGroup group1 = new QuestionGroup("课程目标");
			QuestionGroup group2 = new QuestionGroup("课程设计与安排");
			QuestionGroup group3 = new QuestionGroup("建议与意见");
			_paper1
				.addQuestion(new TestQuestion(1, TestQuestion.TYPE_SIGNLE_SELECT, "这个课程目标与我工作的相关", branches, group1))
				.addQuestion(new TestQuestion(2, TestQuestion.TYPE_SIGNLE_SELECT, "课程内容达到我的期望程度", branches, group1))
				.addQuestion(new TestQuestion(3, TestQuestion.TYPE_SIGNLE_SELECT, "课程内容组织符合逻辑，易于学习", branches, group2))
				.addQuestion(new TestQuestion(4, TestQuestion.TYPE_SIGNLE_SELECT, "课程的程序安排妥当", branches, group2))
				.addQuestion(new TestQuestion(5, TestQuestion.TYPE_SHORT_ANSWER, "您觉得此次培训最有价值的三点是什么？", null, group3))
				.addQuestion(new TestQuestion(6, TestQuestion.TYPE_SHORT_ANSWER, "您建议我们以后的同类培训作哪些调整？", null, group3))
				.addQuestion(new TestQuestion(7, TestQuestion.TYPE_SHORT_ANSWER, "您期望再获得哪些方面的培训 ，请列三个", null, group3))
				;	
			
			
		}
		
		if(id == 2 && _paper2 == null) {
			_paper2 = new Paper(id, 600);
			QuestionGroup group = new QuestionGroup("销售培训");
			
			_paper2
				.setAutoCheck(true)
				.addQuestion(new TestQuestion(1, TestQuestion.TYPE_SIGNLE_SELECT, "下面的插入语句是否正确")
						.addBranch("正确")
						.addBranch("错误")
						.setGroup(group)
						.setModelAnswer("A"))
				.addQuestion(new TestQuestion(2, TestQuestion.TYPE_SIGNLE_SELECT, "中国的首都是")
						.addBranch("上海")
						.addBranch("北京")
						.addBranch("南京")
						.addBranch("深圳")
						.setGroup(group)
						.setModelAnswer("B"))
				.addQuestion(new TestQuestion(3, TestQuestion.TYPE_MULTI_SELECT, "下列那些车是德国")
						.addBranch("大众")
						.addBranch("奥迪")
						.addBranch("别克")
						.addBranch("宝马")
						.setGroup(group)
						.setModelAnswer("ABD"));
		}
		
		if(id == 1)
			return _paper1;
		if(id == 2)
			return _paper2;
		else
			return null;
	}
	
}
