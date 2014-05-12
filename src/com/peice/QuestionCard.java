package com.peice;

import com.peice.model.Candidate;
import com.peice.model.Question;
import com.peice.model.Test;

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
	Test mTest;
	int   mQuestionIndex;
	Question mQuestion;
	Candidate mCandidate;
	
	public static interface Listner {
		public void onAnswer(Question tq, int idx);
		public void onAnswerFinished(Question tq, int idx);
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
	
	public Question getQuestion() {
		return mQuestion;
	}
	
	public void setQuestion(Question q, int index) {
		mQuestionIndex = index;
		if(mQuestion == q)
			return;
		mQuestion = q;
		setupUI();
	}
	
	public void init(Candidate cand, Test test, Listner listner) {
		mCandidate = cand;
		mTest = test;
		mListner = listner;
	}
	
	private void updateQuestionCount() {
		SpannableStringBuilder text = new SpannableStringBuilder();
		String sindex = Integer.toString(mQuestionIndex+1);
		text.append(sindex);
		text.append("/");
		text.append(Integer.toString(mTest.getQuestionCount()));
		text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, sindex.length(),   
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text.setSpan(new AbsoluteSizeSpan(40), 0, sindex.length(), 0);
		
		mQuestionCount.setText(text);
	}
	
	private void setupUI() {

		mTitle.setText(mTest.getName());
		updateQuestionCount();
		
		
		//mTitle.setText(mQuestion.getGroup());
		mQuestionTrunk.setText(mQuestion.getTrunk());
		
		mQuestionAdapter = QuestionAdapter.getAdapter(mTest, mQuestion, this);
		mBranchesContainer.removeAllViews();
		Log.i("==DJJ", "setupUI question="+mQuestion + ",mQuestionAdapter="+mQuestionAdapter);
		if(mQuestionAdapter != null) {
			mQuestionAdapter.getBranchView(mBranchesContainer, LayoutInflater.from(getContext()));
			//mBranchesContainer.measure(mBranchesContainer.getMeasuredWidth(), mBranchesContainer.getMeasuredHeight());
			
			String answer = mTest.getAnswer(mQuestion.getId());
			if(answer != null) {
				mQuestionAdapter.updateAnswer(answer);
			}
		}
	}

	@Override
	public void onAnswerChanged(Question tq, String answer) {
		// TODO Auto-generated method stub
		mTest.setAnswer(mQuestion.getId(), answer);
		if(mListner != null) {
			mListner.onAnswer(mQuestion, mQuestionIndex);
		}
	}

	@Override
	public void onAnswerFinished(Question tq, String answer) {
		if(mListner != null) {
			mListner.onAnswerFinished(mQuestion, mQuestionIndex);
		}
	}
}
