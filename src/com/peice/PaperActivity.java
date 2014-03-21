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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class PaperActivity extends Activity implements 
		ViewPager.OnPageChangeListener, 
		QuestionCard.Listner,
		AnswerCard.Listener{
	
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
	ImageButton mShowAnswerCard;
	
	QuestionCard mFrontQuestionCard;
	QuestionCard mBackQuestionCard;
	MyPagerAdapter mPagerAdapter;
	ViewPager    mContentView;
	CheckBox     mMark;
	
	static class PageViewManager<V extends View> {
		List<V> mCached;
		Map<Integer, V>  mUsing;
		V getCached() {
			if(mCached != null && mCached.size() > 0) {
				V v = mCached.get(0);
				mCached.remove(0);
				return v;
			}
			return null;
		}
		void putUsing(int pos, V v) {
			if(mUsing == null) {
				mUsing = new HashMap<Integer, V>();
			}
			mUsing.put(pos, v);
		}
		V remove(int pos) {
			if(mUsing == null)
				return null;
			V v = mUsing.get(pos);
			mUsing.remove(pos);
			if(mCached == null)
				mCached = new ArrayList<V>();
			mCached.add(v);
			return v;
		}
	}
	
	class MyPagerAdapter extends PagerAdapter {

		PageViewManager<QuestionCard>  mQuestionCardManager = new PageViewManager<QuestionCard>();
		PageViewManager<AnswerCard>    mAnswerCardManager = new PageViewManager<AnswerCard>();
		
		private QuestionCard  getQuestionCard(TestQuestion tq, int poistion) {
			QuestionCard card = mQuestionCardManager.getCached();
			if(card == null) {
				card = new QuestionCard(PaperActivity.this, null);
			}
			
			card.init(mPaper, PaperActivity.this);
			card.setQuestion(tq, poistion);
			mQuestionCardManager.putUsing(poistion, card);
			
			return card;
		}
		
		private AnswerCard getAnswerCard(int postion) {
			AnswerCard card = mAnswerCardManager.getCached();
			if(card == null) {
				card = new AnswerCard(PaperActivity.this, null);
			}
			card.setPaper(mPaper);
			card.setListener(PaperActivity.this);
			mAnswerCardManager.putUsing(postion, card);
			return card;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPaper != null ? mPaper.count() + 1 :  0;
		}

		@Override
		public boolean isViewFromObject(View view, Object data) {
			// TODO Auto-generated method stub
			if(view instanceof QuestionCard && ((QuestionCard)view).getQuestion() == data) {
				return true;
			}
			else if(view instanceof AnswerCard && data instanceof PaperAnswer) {
				return true;
			}
			return false;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			View view = null;
			if(position == mPaper.count()) //answer
			{
				view = mAnswerCardManager.remove(position);
			}
			else {
				QuestionCard card = mQuestionCardManager.remove(position);
				view = card;
			}
			((ViewGroup)container).removeView(view);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if(position == mPaper.count()) //answer
			{
				AnswerCard card = getAnswerCard(position);
				container.addView(card);
				return mPaper.getAnswers();
			}
			else {
				TestQuestion tq = mPaper.get(position);
				QuestionCard card = getQuestionCard(tq, position);
				container.addView(card);
				return tq;
			}
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
		
		mCurrentQuestion = 0;
		
		setContentView(R.layout.paper_frame);
		
		mContentView = (ViewPager)findViewById(R.id.viewpager);
		mPagerAdapter = new MyPagerAdapter();
		mContentView.setAdapter(mPagerAdapter);
		mContentView.setOnPageChangeListener(this);
		mTitle = (TextView)findViewById(R.id.title);
		mLeftTimes = (TextView)findViewById(R.id.timeleft);
		mAnswerProgress = (ProgressBar)findViewById(R.id.question_progress);
		mMark = (CheckBox)findViewById(R.id.mark);
		mMark.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMarkChanged(mMark.isChecked());
			}
		});
		mShowAnswerCard = (ImageButton)findViewById(R.id.questions);
		
		mShowAnswerCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mContentView.setCurrentItem(mPaper.count()+1);
			}
		});
		
		mLeftSeconds = 0;
		
		
		
		setTime(mPaper.getTime());
		startTimer();
		
		mAnswerProgress.setMax(mPaper.count());
		
		onPageSelected(0);
	}
	
	private void onMarkChanged(boolean checked) {
		Log.i("==DJJ", "onMarkChanged, currentQuestion="+mCurrentQuestion+",checked="+checked);
		if(mCurrentQuestion >= 0 && mCurrentQuestion < mPaper.count()) {
			TestQuestion tq = mPaper.get(mCurrentQuestion);
			mPaper.getAnswers().setQuestionMark(tq.getId(), checked);
		}
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == 1) {
			int qid = data.getIntExtra(QUESTION_ID, 0);
			if(qid == 0)
				return;
			
			gotoQuestion(qid);
		}
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	private void enableToolButtons(boolean b) {
		mShowAnswerCard.setEnabled(b);
		mMark.setEnabled(b);
	}

	@Override
	public void onPageSelected(int index) {
		mCurrentQuestion = index;
		enableToolButtons(index >= 0 && index < mPaper.count());
		if(index < mPaper.count()) //questions
		{
			TestQuestion tq = mPaper.get(index);
			mTitle.setText("");
			if(tq != null) {
				QuestionGroup qg = tq.getGroup();
				if(qg != null){
					mTitle.setText(qg.getTrunk());
				}
				mMark.setChecked(mPaper.getAnswers().isQuestionMark(tq.getId()));
			}
		}
		else {
			mTitle.setText("答案");
			mMark.setChecked(false);
		}
			
	}

	@Override
	public void onAnswer(TestQuestion tq, int idx) {
		mAnswerProgress.setProgress(mPaper.getAnswerCount());
	}

	@Override
	public void gotoQuestion(int index) {
		if(mPaper == null)
			return;
		mContentView.setCurrentItem(index);
	}

	@Override
	public void submitAnswer() {
		// TODO submit the answer
		
	}
	
	
}
