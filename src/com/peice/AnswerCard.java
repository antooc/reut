package com.peice;

import java.util.HashMap;
import java.util.Map;

import com.peice.model.Paper;
import com.peice.model.PaperAnswer;
import com.peice.model.TestQuestion;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;

public class AnswerCard extends FrameLayout implements PaperAnswer.OnAnswerChanged {
	PaperAnswer mAnswer;
	Paper       mPaper;
	
	GridView  mGridView;
	Button    mSubmit;
	Adapter   mAdapter;
	
	View.OnClickListener mQuestionOnclicked = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mListener != null) {
				mListener.gotoQuestion((Integer)v.getTag());
			}
		}
	};
	
	public static interface Listener {
		public void gotoQuestion(int index);
		public void submitAnswer();
	}
	
	Listener mListener;
	
	class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPaper.count();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mPaper.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int postion, View convert, ViewGroup parent) {
			ViewGroup vg = null;
			AnswerView view = null;
			if(convert != null) {
				vg = (ViewGroup)convert;
			}
			else {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				vg = (ViewGroup)inflater.inflate(R.layout.answerview, null);
			}
			vg.setLayoutParams(new GridView.LayoutParams(80, 80));
			
			view = (AnswerView)vg.findViewById(R.id.answerview);
			TestQuestion tq = mPaper.get(postion);
			view.setText(Integer.toString(postion+1));
			view.setHasAnswer(mAnswer.getAnswer(tq.getId()) != null);
			android.util.Log.i("==DJJ", "Grid getView id=" + tq.getId() + ",index="+postion +", mark=" + mAnswer.isQuestionMark(tq.getId()));
			view.setMark(mAnswer.isQuestionMark(tq.getId()));
			view.setTag(postion);
			view.setOnClickListener(mQuestionOnclicked);
			
			android.util.Log.i("==DJJ", "User answer="+mAnswer.getAnswer(tq.getId()) + ", ModelAnswer="+tq.getModelAnswer());
			if(mPaper.getAutoCheck() && tq.isObjectiveQuestion() ) {
				String answer = mAnswer.getAnswer(tq.getId());
				if(answer != null) {
					view.setAnswer(answer.equalsIgnoreCase(tq.getModelAnswer()) ? AnswerView.ANSWER_RIGHT: AnswerView.ANSWER_WRONG);
				}
				else {
					view.setAnswer(AnswerView.ANSWER_UNKNOWN);
				}
			}
			else {
				view.setAnswer(AnswerView.ANSWER_UNKNOWN);
			}
			
			return vg;
		}
		
	}
	
	
	public AnswerCard(Context context, AttributeSet attr) {
		super(context, attr);
		
		loadContentView(context);
		
	}
	
	private void loadContentView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.answer_card, this);
		
		mGridView = (GridView)view.findViewById(R.id.answer_gridview);
		
		mSubmit = (Button)view.findViewById(R.id.submit);
		
		mSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSubmit();
			}
			
		});
		
	}
	
	public void setPaper(Paper paper) {
		mPaper = paper;
		mAnswer = mPaper.getAnswers();
		mAnswer.setListener(this);
		if(mAdapter == null) {
			mAdapter = new Adapter();
			mGridView.setAdapter(mAdapter);
		}
	}
	
	public void setListener(Listener listener) {
		mListener = listener;
	}
	
	public PaperAnswer getAnswer() {
		return mAnswer;
	}
	
	
	private void onSubmit() {
		if(mListener != null) {
			mListener.submitAnswer();
		}
	}

	@Override
	public void onAnswerChanged(int q_id, String answer) {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onMarkChanged(int q_id, boolean bmarked) {
		mAdapter.notifyDataSetChanged();
	}
}
