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
import android.widget.TextView;

public class AnswerCard extends FrameLayout implements PaperAnswer.OnAnswerChanged {
	PaperAnswer mAnswer;
	Paper       mPaper;
	
	GridView  mGridView;
	Button    mSubmit;
	Adapter   mAdapter;
	Map<Integer, AnswerView> mAnswerViews = new HashMap<Integer, AnswerView>();
	
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
			AnswerView view = null;
			if(convert != null) {
				view = (AnswerView)convert;
			}
			else {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				view = (AnswerView)inflater.inflate(R.layout.answerview, null);
			}
			
			TestQuestion tq = mPaper.get(postion);
			view.setText(Integer.toString(postion+1));
			view.setHasAnswer(mAnswer.getAnswer(tq.getId()) != null);
			view.setLayoutParams(new GridView.LayoutParams(80, 80));
			android.util.Log.i("==DJJ", "Grid getView mark=" + mAnswer.isQuestionMark(tq.getId()));
			view.setMark(mAnswer.isQuestionMark(tq.getId()));
			
			mAnswerViews.put(tq.getId(), view);
			return view;
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
	
	public PaperAnswer getAnswer() {
		return mAnswer;
	}
	
	
	private void onSubmit() {
		
	}

	@Override
	public void onAnswerChanged(int q_id, String answer) {
		// TODO Auto-generated method stub
		/*AnswerView view = mAnswerViews.get(q_id);
		if(view != null)
			view.setHasAnswer(true);*/
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onMarkChanged(int q_id, boolean bmarked) {
		// TODO Auto-generated method stub
		/*AnswerView view = mAnswerViews.get(q_id);
		if(view != null) {
			view.setMark(bmarked);
		}*/
		mAdapter.notifyDataSetChanged();
	}
}
