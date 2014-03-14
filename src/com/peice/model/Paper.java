package com.peice.model;

public class Paper {
	int mCursorId;
	int mTime;
	
	QuestionGroup mQuestions;
	
	Paper(int cursorId, int time, QuestionGroup questions) {
		mCursorId = cursorId;
		mQuestions =  questions;
		mTime = time;
	}
	
	public int getCursorId() {
		return mCursorId;
	}
	
	public QuestionGroup getQuestions() {
		return mQuestions;
	}
	
	public int getTime() {
		return mTime;
	}
	
	
	//TODO
	static Paper _paper1;
	static Paper _paper2;
	static public Paper getDefault(int id) {
		if(id == 1 && _paper1 == null) {
			QuestionGroup group = new QuestionGroup();
			
			java.util.List<String> branches = new java.util.ArrayList<String>();
			branches.add("非常同意");
			branches.add("同意");
			branches.add("一般");
			branches.add("不同意");
			branches.add("非常不同意");
			
			group.setTrunk("培训评估表")
				.addQuestion(new QuestionGroup()
						.setTrunk("课程目标")
						.addQuestion(new TestQuestion(1, Question.TYPE_SIGNLE_SELECT, "这个课程目标与我工作的相关", branches))
						.addQuestion(new TestQuestion(2, Question.TYPE_SIGNLE_SELECT, "课程内容达到我的期望程度", branches))
				)
				.addQuestion(new QuestionGroup()
						.setTrunk("课程设计与安排")
						.addQuestion(new TestQuestion(3, Question.TYPE_SIGNLE_SELECT, "课程内容组织符合逻辑，易于学习", branches))
						.addQuestion(new TestQuestion(4, Question.TYPE_SIGNLE_SELECT, "课程的程序安排妥当", branches))
				)
				.addQuestion(new QuestionGroup()
						.setTrunk("课程设计与安排")
						.addQuestion(new TestQuestion(5, Question.TYPE_SHORT_ANSWER, "您觉得此次培训最有价值的三点是什么？", null))
						.addQuestion(new TestQuestion(5, Question.TYPE_SHORT_ANSWER, "您建议我们以后的同类培训作哪些调整？", null))
						.addQuestion(new TestQuestion(5, Question.TYPE_SHORT_ANSWER, "您期望再获得哪些方面的培训 ，请列三个", null))
				);	
			
			_paper1 = new Paper(id, 19*60, group);
		}
		
		if(id == 2 && _paper2 == null) {
			QuestionGroup group = new QuestionGroup();
			
			group.setTrunk("销售评估表")
				.addQuestion(new TestQuestion(1, Question.TYPE_SIGNLE_SELECT, "下面的插入语句是否正确")
						.addBranch("正确")
						.addBranch("错误")
				);
			_paper2 = new Paper(id, 600, group);
		}
		
		if(id == 1)
			return _paper1;
		if(id == 2)
			return _paper2;
		else
			return null;
	}
	
}
