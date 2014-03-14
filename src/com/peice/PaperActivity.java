package com.peice;

import java.util.ArrayList;
import java.util.List;

import com.peice.model.Examinee;
import com.peice.model.ExamineeManager;
import com.peice.model.Paper;
import com.peice.model.Question;
import com.peice.model.QuestionGroup;
import com.peice.model.TestQuestion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

public class PaperActivity extends BaseActivity {
	
	static final String TAG="PaperActivity";
	
	static public final String COURSE_ID = "course_id";
	static public final String PAPER_ID = "paper_id";
	static public final String EXAMINEE_ID = "examinee_id";
	
	TextView mTitle;
	TextView mLeftTimes;
	ViewGroup mPaperContent;
	ViewGroup mBranchesContainer;
	TextView mQuestionTrunk;
	Paper    mPaper;
	QuestionAdapter mQuestionAdapter;
	
	Button mBtnPrev;
	Button mBtnNext;
	Button mBtnQuestions; //
	Button mBtnSubmit;
	
	static class StackElement {
		QuestionGroup group;
		int current;
		StackElement(QuestionGroup g) {
			group = g;
			current = 0;
		}
	}
	
	StackElement  mQuestionStack[] = new StackElement[10];
	int  mStackTop = 0;
	
	private void push(QuestionGroup g) {
		if(g.getQuestions() == null || g.getQuestions().size() <= 0)
			return;
		StackElement e = new StackElement(g);
		mQuestionStack[mStackTop++] = e;
		Question q = g.getQuestions().get(0);
		if(q != null && q instanceof QuestionGroup) {
			push((QuestionGroup)q);
		}
	}
	private void push_with_last(QuestionGroup g){
		if(g.getQuestions() == null || g.getQuestions().size() <= 0)
			return;
		StackElement e = new StackElement(g);
		mQuestionStack[mStackTop++] = e;
		e.current = g.getQuestions().size() - 1;
		Question q = g.getQuestions().get(e.current);
		if(q != null && q instanceof QuestionGroup) {
			push_with_last((QuestionGroup)q);
		}
	}
	private void pop() {
		mStackTop --;
	}
	private StackElement top() {
		if(mStackTop > 0)
			return mQuestionStack[mStackTop - 1];
		return null;
	}
	private boolean isStackEmpty() {
		return mStackTop <= 0;
	}
	
	
	int     mLeftSeconds;
	int     mTotalSeconds;
	long    mLastTimeMillise;
	int     mTimerState;
	static final int TIMER_IDLE = 0;
	static final int TIMER_RUNNING = 0;
	static final int TIMER_PAUSED = 0;
	static final int TIMER_FINISH = 0;
	Handler mTimerHandler = new Handler();
	Runnable mTimerRunable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mTimerState != TIMER_RUNNING)
				return;
			
			if(mLastTimeMillise <= 0)  {
				mLastTimeMillise = System.currentTimeMillis();
			}
			else {
				long cur = System.currentTimeMillis();
				int seconds = (int)((cur - mLastTimeMillise) / 1000);
				mLastTimeMillise = cur;
				mLeftSeconds -= seconds;
				if(mLeftSeconds <= 0) {
					//时间到
					timeCome();
					mTimerState = TIMER_FINISH;
				}
			}
			
			updateTime();
			
			mTimerHandler.postDelayed(this, 1000);
		}
	};
	
	private void updateUI() {
		updateQuestion();
		mBtnPrev.setEnabled(hasPrev());
		mBtnNext.setEnabled(hasNext());
	}
	
	private void updateQuestion() {
		StackElement e = top();
		if(e == null)
			return;
		
		if(e.group != null) {
			mTitle.setText(e.group.getTrunk());
		}
		
		Question q = e.group.getQuestions().get(e.current);
		
		if(q instanceof TestQuestion) {
			TestQuestion tq = (TestQuestion)q;
			mQuestionTrunk.setText(tq.getTrunk());
			setQuestionBranches(tq);
		}
	}
	
	private void setQuestionBranches(TestQuestion tq) {
		mQuestionAdapter = QuestionAdapter.getAdapter(tq);
		mBranchesContainer.removeAllViews();
		if(mQuestionAdapter != null) {
			mQuestionAdapter.getBranchView(mBranchesContainer, getLayoutInflater());
		}
		
	}
	
	
	
	private void nextQuestion() {
		
		StackElement e = top();
		if(e == null) {
			//TODO Last
			return ;
		}
		e.current ++;
		Log.i("==DJJ", "current="+e.current+",size="+e.group.getQuestions().size());
		if(e.current >= e.group.getQuestions().size()) {
			pop();
			nextQuestion();
		}
		else {
			Question q = e.group.getQuestions().get(e.current);
			Log.i("==DJJ", "Question="+q);
			if(q instanceof QuestionGroup) {
				push((QuestionGroup)q);
			}
		}
		
		updateUI();
	}
	

	
	private void prevQuestion() {
		StackElement e = top();
		if(e == null) {
			//TODO first
			return;
		}
		
		e.current --;
		if(e.current < 0) {
			pop();
			prevQuestion();
		}
		else {
			Question q = e.group.getQuestions().get(e.current);
			if(q instanceof QuestionGroup) {
				push_with_last((QuestionGroup)q);
			}
		}
		updateUI();
	}
	
	private boolean isEmptyQuestionGroup(QuestionGroup g) {
		if(g.getQuestions().size() <= 0)
			return true;
		
		for(Question q : g.getQuestions()) {
			if(q instanceof QuestionGroup) {
				if(!isEmptyQuestionGroup((QuestionGroup)q))
					return false;
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	private boolean hasPrev(StackElement e) {
		int i = e.current - 1;
		while(i >= 0) {
			Question q = e.group.getQuestions().get(i);
			if(q instanceof QuestionGroup) {
				if(!isEmptyQuestionGroup((QuestionGroup)q)) {
					return true;
				}
			}
			else {
				return true;
			}
			i --;
		}
		return false;
	}
	
	public boolean hasPrev() {
		int i = mStackTop -1;
		while(i >= 0) {
			if(hasPrev(mQuestionStack[i]))
				return true;
			i --;
		}
		return false;
	}
	
	private boolean hasNext(StackElement e) {
		int i = e.current + 1;
		Log.i("==DJJ", "hasNext current="+e.current +",group="+e.group);
		while(i < e.group.getQuestions().size()) {
			Question q = e.group.getQuestions().get(i);
			if(q instanceof QuestionGroup) {
				if(!isEmptyQuestionGroup((QuestionGroup)q)) {
					return true;
				}
			}
			else {
				return true;
			}
			i ++;
		}
		return false;
	}
	
	public boolean hasNext() {
		int i = mStackTop -1;
		Log.i("==DJJ", "hasNext mStackTop="+mStackTop);
		while(i >= 0) {
			if(hasNext(mQuestionStack[i]))
				return true;
			i --;
		}
		return false;	
	}
	
	@Override
	public void onCreate(Bundle savedInstance) {
		
		Intent intent = getIntent();
		
		int courseId = intent.getIntExtra(COURSE_ID, 0);
		int examineeId = intent.getIntExtra(EXAMINEE_ID, 0);
		Examinee ex = ExamineeManager.getManager().get(examineeId);

		if(ex == null) {
			Log.e(TAG, "Cannot Get the Examinee by Id=" + examineeId);
			return;
		}
		mPaper = ex.getPapder(courseId);
		if(mPaper != null)
			push(mPaper.getQuestions());
		else 
			Log.e(TAG, "Cannot Find the Paper for Course:"+courseId);
		
		super.onCreate(savedInstance);
	}
	
	@Override
    protected void inflateContentView(ViewGroup content, LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.paperlayout, content);
		
		mTitle = (TextView)view.findViewById(R.id.title);
		mLeftTimes = (TextView)view.findViewById(R.id.timeleft);
		mPaperContent = (ViewGroup)view.findViewById(R.id.paper_content);
		mQuestionTrunk = (TextView)view.findViewById(R.id.trunk);
		mBranchesContainer = (ViewGroup)view.findViewById(R.id.branches);
		
		mLeftSeconds = 0;
		
		setTime(mPaper.getTime());
		updateUI();
		startTimer();
	}
	
	public void setTitle(String title) {
		mTitle.setText(title);
	}
	public void setTitle(int resid) {
		String str = getResources().getString(resid);
		mTitle.setText(str);
	}
	
	public void setTime(int seconds) {
		stopTimer();
		mTotalSeconds = seconds;
	}
	
	public void stopTimer() {
		mTimerState = TIMER_FINISH;
	}
	
	public void pauseTimer() {
		mTimerState = TIMER_PAUSED;
	}
	
	public void resumeTimer() {
		if(mTimerState == TIMER_PAUSED || mTimerState == TIMER_IDLE);
		mTimerState = TIMER_RUNNING;
		mTimerHandler.post(mTimerRunable);
		mLastTimeMillise = System.currentTimeMillis();
	}
	
	public void startTimer() {
		mTimerState = TIMER_IDLE;
		mLeftSeconds = mTotalSeconds;
		resumeTimer();
	}
	
	
	private void timeCome() {
		//时间到
		
	}
	
	private void updateTime() {
		mLeftTimes.setText("剩余"+ (mLeftSeconds/60) + "分" + (mLeftSeconds%60) + "秒");
	}
	
	
	@Override
	protected void inflateToolbarView(ViewGroup toolbar, LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.paper_toolbar, toolbar);
		mBtnPrev = (Button)view.findViewById(R.id.prev);
		mBtnNext = (Button)view.findViewById(R.id.next);
		mBtnQuestions = (Button)view.findViewById(R.id.questions);
		mBtnSubmit = (Button)view.findViewById(R.id.submit);
	}

	@Override
	protected void onToolbarClick(View view, int id) {
		switch(id) {
		case R.id.next:
			nextQuestion();
			break;
		case R.id.prev:
			prevQuestion();
			break;
		case R.id.submit: //提交
			break;
		case R.id.questions: //选题
			break;
		}
	}
	
}
