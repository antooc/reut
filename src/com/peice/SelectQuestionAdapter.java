package com.peice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.peice.common.SelectView;
import com.peice.model.Question;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectQuestionAdapter extends QuestionAdapter{

	protected Map<String, SelectView> mBtnBranches;
	
	public SelectQuestionAdapter(Question tq, OnAnswerChanged onAnswerChanged) {
		super(tq, onAnswerChanged);
	}

	@Override
	public void getBranchView(ViewGroup parent, LayoutInflater inflater) {
		if(mQuestion == null)
			return ;
		Map<String, String> branches = mQuestion.getBranches();
		
		if(branches == null || branches.size() <= 0)
			return ;
		
		mBtnBranches = new HashMap<String, SelectView>();
		
		Iterator<String> keyit = branches.keySet().iterator();
		while (keyit.hasNext()) {

			SelectView view = (SelectView)inflater.inflate(getResId(), null);
			if(view == null)
				continue;
			
			String key = keyit.next();
			String branch = branches.get(key);
			
			mBtnBranches.put(key, view);
			view.setContent(branch);
			view.setId(key);
			
			view.setOnCheckedChangeListener(mOnCheckedChanged);
			view.setVisibility(View.VISIBLE);
			parent.addView(view);
		}
		
		
		onAnswerUpdated();
	} 
	
	@Override
	public void onAnswerUpdated() {
		if(mBtnBranches == null || getAnswer() == null)
			return;
		String answers = getAnswer();
		if(answers == null || answers.length() == 0)
			return;
		
		for(int i = 0; i < answers.length(); i++) {
			String key = answers.substring(i, i+1);
			SelectView view = mBtnBranches.get(key);
			if (view != null) {
				view.setChecked(true);
			}
		}
	}
	
	@Override
	protected int getResId() {
		return R.layout.multi_select_item;
	}
	
	private SelectView.OnCheckedChangeListener mOnCheckedChanged= new SelectView.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(SelectView btn, boolean checked) {
			onBrancheSelectChanged(btn, checked);
		}
		
	};
	
	protected void onBrancheSelectChanged(SelectView btn, boolean checked) {
		StringBuilder answer = new StringBuilder();
		Iterator<String> keyit = mBtnBranches.keySet().iterator();
		while (keyit.hasNext()) {
			String key = keyit.next();
			SelectView view = mBtnBranches.get(key);
			if (view.isChecked()) {
				answer.append(key);
			}
		}
		setAnswer(answer.toString());
		onAnswerChanged();
	}
	
	
}
