package com.peice;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.peice.common.BaseActivity;
import com.peice.model.Candidate;
import com.peice.model.DataManager;
import com.peice.model.Test;

public class CourseActivity extends BaseActivity {
	private ListView mCourseList;
	private Adapter mAdapter;
	private Candidate mCandidate;
	
	static private class ViewHolder {
		TextView name;
		TextView description;
		CheckBox complete;
	}
	
	class Adapter extends BaseAdapter {
		private List<Test> mList;
		public Adapter(List<Test> course) {
			mList = course;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
		}
		
		@Override
		 public Object getItem(int position) {
			return mList == null ? null : mList.get(position);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			if(convertView != null) {
				view = convertView;
				holder = (ViewHolder)view.getTag();
			}
			else {
				view = getLayoutInflater().inflate(R.layout.course_item, null);
				holder = new ViewHolder();
				holder.name = (TextView)view.findViewById(R.id.title);
				holder.description = (TextView)view.findViewById(R.id.summary);
				holder.complete = (CheckBox)view.findViewById(R.id.complete);
				view.setTag(holder);
			}
			
			Test test = (Test)getItem(position);
			
			holder.name.setText(test.getName());
			holder.description.setText(test.getDescription());
			holder.complete.setChecked(test.isComplete());
			
			return view;
		}
	}
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	setContentView(R.layout.course_content);
    	mCourseList = (ListView)findViewById(R.id.course_list);
        
        mCandidate = DataManager.getInstance().getCandidate();
        
        
        mAdapter = new Adapter(getTests());
        mCourseList.setAdapter(mAdapter);
        mCourseList.setOnItemClickListener(new OnItemClickListener() {
        	@Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        		onCourseClicked(arg2);
        	}
        });
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	setTitle(getCaption());
    }
    
    private String getCaption() {
    	return "【"+  mCandidate.getName() + "】" + mCandidate.getProjectName();
    }
    
    private void onCourseClicked(int pos) {
    	final Test test = (Test)mAdapter.getItem(pos);
    	
    	if(test == null)
    		return;
    	
    	//Show Alert
    	Dialog alertDialog = null;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	builder.setTitle(getCaption());
    	
    	LayoutInflater inflater = LayoutInflater.from(this);
    	final View view = inflater.inflate(R.layout.course_info_dialog, null);
    	final TextView course_name = (TextView)view.findViewById(R.id.course_name);
    	final TextView course_desc = (TextView)view.findViewById(R.id.course_desc);
    	course_name.setText(test.getName());
    	course_desc.setText(test.getDescription());
    	builder.setView(view);
    	
    	if(test.isComplete()) {
    		builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
    		     @Override
    		     public void onClick(DialogInterface dialog, int which) {
    		    	 dialog.dismiss();
    		     }
    		    });
    	}
    	else {
    		builder.setPositiveButton("开始答题", new DialogInterface.OnClickListener() {
   		     @Override
   		     public void onClick(DialogInterface dialog, int which) {
   		    	 dialog.dismiss();
   		    	 startCourse(test);
   		     }
   		    });
    		builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
   		     @Override
   		     public void onClick(DialogInterface dialog, int which) {
   		    	 dialog.dismiss();
   		     }
   		    });
    	}
    	
    	alertDialog = builder.create();
    	alertDialog.show();
    }
    
    private void startCourse(Test test) {
    	Intent intent = new Intent();
    	intent.setClass(this, PaperActivity.class);
    	intent.putExtra(PaperActivity.TEST_ID, test.getId());
    	startActivity(intent);
    }


    
    
    private List<Test> getTests() {
    	return mCandidate.getTests();
    }
    
}
