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
import com.peice.model.Course;
import com.peice.model.ExamineeManager;
import com.peice.model.Examinee;

public class CourseActivity extends BaseActivity {
	private ListView mCourseList;
	private Adapter mAdapter;
	private Examinee mExaminee;
	
	static private class ViewHolder {
		TextView name;
		TextView description;
		CheckBox complete;
	}
	
	class Adapter extends BaseAdapter {
		private List<Course> mList;
		public Adapter(List<Course> course) {
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
			
			Course course = (Course)getItem(position);
			
			holder.name.setText(course.name);
			holder.description.setText(course.description);
			holder.complete.setChecked(course.isComplete);
			
			return view;
		}
	}
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	setContentView(R.layout.course_content);
    	mCourseList = (ListView)findViewById(R.id.course_list);
        
        //TODO 
        mExaminee = ExamineeManager.getManager().login("doon", "1234");
        
        
        mAdapter = new Adapter(getCourse());
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
    	setTitle(mExaminee.getCaption());
    }
    
    private void onCourseClicked(int pos) {
    	final Course course = (Course)mAdapter.getItem(pos);
    	
    	if(course == null)
    		return;
    	
    	//Show Alert
    	Dialog alertDialog = null;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	builder.setTitle("课程信息");
    	String info = "课程名称：" + course.name +"\n"
    			+ "课程描述：" + course.description +"\n";
    	if(course.isComplete) {
    		info += "您已经完成该课程";
    	}
    	else {
    		info += "您尚未完成该课程";
    	}
    	builder.setMessage(info);
    	
    	if(course.isComplete) {
    		builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
    		     @Override
    		     public void onClick(DialogInterface dialog, int which) {
    		    	 dialog.dismiss();
    		     }
    		    });
    	}
    	else {
    		builder.setPositiveButton("开始课程", new DialogInterface.OnClickListener() {
   		     @Override
   		     public void onClick(DialogInterface dialog, int which) {
   		    	 dialog.dismiss();
   		    	 startCourse(course);
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
    
    private void startCourse(Course course) {
    	Intent intent = new Intent();
    	intent.setClass(this, PaperActivity.class);
    	intent.putExtra(PaperActivity.EXAMINEE_ID, mExaminee.getId());
    	intent.putExtra(PaperActivity.COURSE_ID, course.id);
    	startActivity(intent);
    }


    
    
    private List<Course> getCourse() {
    	return mExaminee.queryCourse();
    }
    
}
