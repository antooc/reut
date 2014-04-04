package com.peice;

import com.peice.model.Course;
import com.peice.model.Examinee;
import com.peice.model.Paper;
import com.peice.model.TestQuestion;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class QuestionCard extends FrameLayout implements QuestionAdapter.OnAnswerChanged{
	Paper mPaper;
	int   mQuestionIndex;
	TestQuestion mQuestion;
	Examinee mExaminee;
	
	public static interface Listner {
		public void onAnswer(TestQuestion tq, int idx);
		public void onAnswerFinished(TestQuestion tq, int idx);
	}
	
	////////////////////////
	ViewGroup mBranchesContainer;
	TextView mQuestionTrunk;
	QuestionAdapter mQuestionAdapter;
	TextView mTitle;
	TextView mQuestionCount;
	Listner mListner;
	
	public QuestionCard(Context context, AttributeSet attr) {
		super(context, attr);
		
		loadContentView(context);
	}
	
	private void loadContentView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.paperlayout, this);
		
		mTitle = (TextView)view.findViewById(R.id.course_title);
		mQuestionCount = (TextView)view.findViewById(R.id.question_count);
		mQuestionTrunk = (TextView)view.findViewById(R.id.trunk);
		mBranchesContainer = (ViewGroup)view.findViewById(R.id.branches);
		
	}
	
	@Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		Log.i("==DJJ", "QuestionCard Layout="+l+","+t+","+r+","+b);
	}
	
	public TestQuestion getQuestion() {
		return mQuestion;
	}
	
	public void setQuestion(TestQuestion q, int index) {
		mQuestionIndex = index;
		if(mQuestion == q)
			return;
		mQuestion = q;
		setupUI();
	}
	
	public void init(Examinee examinee, Paper paper, Listner listner) {
		mExaminee = examinee;
		mPaper = paper;
		mListner = listner;
	}
	
	private void updateQuestionCount() {
		SpannableStringBuilder text = new SpannableStringBuilder();
		String sindex = Integer.toString(mQuestionIndex+1);
		text.append(sindex);
		text.append("/");
		text.append(Integer.toString(mPaper.count()));
		text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, sindex.length(),   
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text.setSpan(new AbsoluteSizeSpan(40), 0, sindex.length(), 0);
		
		mQuestionCount.setText(text);
	}
	
	private void setupUI() {

		Course course = mExaminee.getCourse(mPaper.getCourseId());
		mTitle.setText(course.name);
		updateQuestionCount();
		
		
		//mTitle.setText(mQuestion.getGroup());
		mQuestionTrunk.setText(mQuestion.getTrunk());
		
		mQuestionAdapter = QuestionAdapter.getAdapter(mQuestion, this);
		mBranchesContainer.removeAllViews();
		Log.i("==DJJ", "setupUI question="+mQuestion + ",mQuestionAdapter="+mQuestionAdapter);
		if(mQuestionAdapter != null) {
			mQuestionAdapter.getBranchView(mBranchesContainer, LayoutInflater.from(getContext()));
			//mBranchesContainer.measure(mBranchesContainer.getMeasuredWidth(), mBranchesContainer.getMeasuredHeight());
			
			String answer = mPaper.getAnswers().getAnswer(mQuestion.getId());
			if(answer != null) {
				mQuestionAdapter.updateAnswer(answer);
			}
		}
	}

	@Override
	public void onAnswerChanged(TestQuestion tq, String answer) {
		// TODO Auto-generated method stub
		mPaper.getAnswers().setAnswer(mQuestion.getId(), answer);
		if(mListner != null) {
			mListner.onAnswer(mQuestion, mQuestionIndex);
		}
	}

	@Override
	public void onAnswerFinished(TestQuestion tq, String answer) {
		if(mListner != null) {
			mListner.onAnswerFinished(mQuestion, mQuestionIndex);
		}
	}
}
