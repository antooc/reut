
package com.peice;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peice.common.BaseActivity;
import com.peice.model.ATSHttpUtil;
import com.peice.model.Candidate;
import com.peice.model.DataManager;

public class ATSLogonActivity extends BaseActivity {

    private EditText logonNameEdit;
    private EditText logonPasswordEdit;
    private Button logonButton;
    private LinearLayout ll_Hint;
    private Handler handle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_logon_layout);

        ll_Hint = (LinearLayout) findViewById(R.id.logonLinearLayoutHint);

        logonNameEdit = (EditText) findViewById(R.id.logon_name_edit);
        logonPasswordEdit = (EditText) findViewById(R.id.logon_password_edit);

        logonButton = (Button) findViewById(R.id.logon_button);
        logonButton.setOnClickListener(logonListener);
        
        handle = new Handler() {
        	@Override
            public void handleMessage(Message msg) {
        		if (msg.what != DataManager.MSG_LOGIN) {
        			return ;
        		}
        		onLogon((Candidate)msg.obj);
        	}
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        showBack(false);

        setTitle(R.string.logon_title);
    }

    Button.OnClickListener logonListener = new Button.OnClickListener()
    {
        public void onClick(View v) {
            logonButton.setText(R.string.logon_logingin);

            if (valudate()) {
            	loginProcess();
            }
        }
    };
    
    private void onLogon(Candidate cand) {
    	if (cand != null) {
    		if(cand.getLoginStatus() == Candidate.STATUS_OK) {
    			if (cand.getName().equals(Candidate.NEWNAME)) {
    				enterNextActivity(ATSFilloutInfoActivity.class);
    			}
    			else {
    				enterNextActivity(ATSInfoHintActivity.class);
    			}
    			return;
    		}
    		showHintView(cand.getLoginResult());
    	}
    	else {
    		showHintView(null);
    	}
    }

    private void enterNextActivity(Class<?> cls) {
        Intent intent = new Intent();

        intent.setClass(ATSLogonActivity.this, cls);
        startActivity(intent);
        finish();
    }

    private void showHintView(String result) {
        final TextView tv_hint = new TextView(this);

        ll_Hint.removeAllViews();
        ll_Hint.addView(tv_hint);
        tv_hint.setText(result == null ? getString(R.string.logon_hint) : result);
        tv_hint.setTextColor(Color.RED);
    }

    // ��鿼��״̬
    /*private boolean validTestStatus() {
        return false;
    }*/

    private void loginProcess() {
    	
        DataManager.getInstance().login(
        			logonNameEdit.getText().toString().trim(), 
        			logonPasswordEdit.getText().toString().trim(), 
        			handle);
    }

    private boolean valudate() {
        String userName = logonNameEdit.getText().toString().trim();
        if (userName.equals("")) {
            showDialog("用户名不能为空");
            return false;
        }

        String password = logonPasswordEdit.getText().toString().trim();
        if (password.equals("")) {
            showDialog("密码不能为空");
            return false;
        }

        return true;
    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg) .setCancelable(false)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    logonButton.setText(R.string.logon_logon);
                }
            });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*private JSONObject query(String userName, String password) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("candname", userName);
        map.put("pass", password);

        String url = ATSHttpUtil.BASE_URL + "tbl_proj_cand";
        return new JSONObject(ATSHttpUtil.postRequest(url, map));
    }

    // ����û���������ѯ
    private String queryC(String account, String password) throws Exception {
        // ��ѯ����
        String queryString = "account=" + account + "&password=" + password;
        // url
        String url = ATSHttpUtil.BASE_URL + "servlet/tbl_proj_cand?" + queryString;
        // ��ѯ���ؽ��
        return ATSHttpUtil.queryStringForPost(url);
    }

    // MD5���ܣ�32λ 
    public static String MD5(String str) { 
        MessageDigest md5 = null; 
        try { 
            md5 = MessageDigest.getInstance("MD5"); 
        }
        catch (Exception e) { 
            e.printStackTrace(); 
            return ""; 
        } 
         
        char[] charArray = str.toCharArray(); 
        byte[] byteArray = new byte[charArray.length]; 
         
        for (int i = 0; i < charArray.length; i++) { 
            byteArray[i] = (byte) charArray[i]; 
        } 

        byte[] md5Bytes = md5.digest(byteArray); 
        StringBuffer hexValue = new StringBuffer(); 

        for (int i = 0; i < md5Bytes.length; i++) { 
            int val = ((int) md5Bytes[i]) & 0xff; 
            if (val < 16) { 
                hexValue.append("0"); 
            } 
            hexValue.append(Integer.toHexString(val)); 
        }

        return hexValue.toString(); 
    } 
     
    // ����ļ����㷨 
    public static String encryptMD5(String str) { 
        char[] a = str.toCharArray(); 

        for (int i = 0; i < a.length; i++) { 
            a[i] = (char) (a[i] ^ 'l'); 
        } 

        String s = new String(a); 

        return s; 
    }*/
}

