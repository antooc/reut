package com.peice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.peice.model.Examinee;
import com.peice.model.ExamineeManager;
import com.peice.model.Paper;
import com.peice.model.PaperAnswer;
import com.peice.model.QuestionGroup;
import com.peice.model.TestQuestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class PaperActivity extends Activity implements ViewPager.OnPageChangeListener, QuestionCard.Listner {
	
	static final String TAG="PaperActivity";
	
	static public final String COURSE_ID = "course_id";
	static public final String PAPER_ID = "paper_id";
	static public final String EXAMINEE_ID = "examinee_id";
	static public final String QUESTION_ID = "question_id";
	
	TextView mLeftTimes;
	TextView mTitle;
	Paper    mPaper;
	Examinee mExaminee;
	int      mCurrentQuestion;
	ProgressBar mAnswerProgress;
	
	QuestionCard mFrontQuestionCard;
	QuestionCard mBackQuestionCard;
	MyPagerAdapter mPagerAdapter;
	ViewPager    mContentView;
	
	class MyPagerAdapter extends PagerAdapter {

		List<QuestionCard>  mQuestionCardRemovedViews;
		Map<Integer,QuestionCard>  mQuestionUsingViews;
		
		private QuestionCard  getQuestionCard(TestQuestion tq, int poistion) {
			QuestionCard card = null;
			if(mQuestionCardRemovedViews != null && mQuestionCardRemovedViews.size() > 0) {
				card = mQuestionCardRemovedViews.get(0);
				mQuestionCardRemovedViews.remove(0);
			}
			else {
				card = new QuestionCard(PaperActivity.this, null);
			}
			
			card.init(mPaper, PaperActivity.this);
			card.setQuestion(tq, poistion);
			if(mQuestionUsingViews == null)
				mQuestionUsingViews = new HashMap<Integer, QuestionCard>();
			mQuestionUsingViews.put(poistion, card);
			
			return card;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPaper != null ? mPaper.count() :  0;
		}

		@Override
		public boolean isViewFromObject(View view, Object data) {
			// TODO Auto-generated method stub
			if(view instanceof QuestionCard && ((QuestionCard)view).getQuestion() == data) {
				return true;
			}
			return false;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			QuestionCard card = null;
			if(mQuestionUsingViews != null)
			{
				card = mQuestionUsingViews.get(position);
				mQuestionUsingViews.remove(position);
			}
			if(card != null) {
				if(mQuestionCardRemovedViews == null)
					mQuestionCardRemovedViews = new ArrayList<QuestionCard>();
				mQuestionCardRemovedViews.add(card);
				((ViewGroup)container).removeView(card);
			}
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TestQuestion tq = mPaper.get(position);
			QuestionCard card = getQuestionCard(tq, position);
			container.addView(card);
			return tq;
		}
		
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
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		Intent intent = getIntent();
		
		int courseId = intent.getIntExtra(COURSE_ID, 0);
		int examineeId = intent.getIntExtra(EXAMINEE_ID, 0);
		mExaminee = ExamineeManager.getManager().get(examineeId);

		if(mExaminee == null) {
			Log.e(TAG, "Cannot Get the Examinee by Id=" + examineeId);
			return;
		}
		mPaper = mExaminee.getPapder(courseId);
		if(mPaper == null) {
			Log.e(TAG, "Cannot Find the Paper for Course:"+courseId);
			return;
		}
		
		setContentView(R.layout.paper_frame);
		
		mContentView = (ViewPager)findViewById(R.id.viewpager);
		mPagerAdapter = new MyPagerAdapter();
		mContentView.setAdapter(mPagerAdapter);
		mContentView.setOnPageChangeListener(this);
		mTitle = (TextView)findViewById(R.id.title);
		mLeftTimes = (TextView)findViewById(R.id.timeleft);
		mAnswerProgress = (ProgressBar)findViewById(R.id.question_progress);
			
		mLeftSeconds = 0;
		
		setTime(mPaper.getTime());
		startTimer();
		
		mAnswerProgress.setMax(mPaper.count());
		
		onPageSelected(0);
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
		if(mLeftTimes != null)
			mLeftTimes.setText("剩余"+ (mLeftSeconds/60) + "分" + (mLeftSeconds%60) + "秒");
	}
	
	
	private void selectQuestion() {
		if(mPaper == null || mExaminee == null)
			return;
		
		Intent intent = new Intent();
		//intent.setClass(this, SelectQuestionActivity.class);
		intent.putExtra(COURSE_ID, mPaper.getCursorId());
		intent.putExtra(EXAMINEE_ID, mExaminee.getId());
		
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == 1) {
			int qid = data.getIntExtra(QUESTION_ID, 0);
			if(qid == 0)
				return;
			
			gotoQuestion(qid);
		}
	}
	
	private void gotoQuestion(int qid) {
		if(mPaper == null)
			return;
		
	
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int index) {
		TestQuestion tq = mPaper.get(index);
		mTitle.setText("");
		if(tq != null) {
			QuestionGroup qg = tq.getGroup();
			if(qg != null){
				mTitle.setText(qg.getTrunk());
			}
		}
		mAnswerProgress.setProgress(mPaper.getAnswerCount());
	}

	@Override
	public void onAnswer(TestQuestion tq, int idx) {
		
	}
	
	
}
