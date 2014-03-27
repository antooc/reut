
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peice.model.ATSHttpUtil;

public class ATSLogonActivity extends Activity {

    private EditText logonNameEdit;
    private EditText logonPasswordEdit;
    private Button logonButton;
    private LinearLayout ll_Hint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ats_logon_layout);

        ll_Hint = (LinearLayout) findViewById(R.id.logonLinearLayoutHint);

        logonNameEdit = (EditText) findViewById(R.id.logon_name_edit);
        logonPasswordEdit = (EditText) findViewById(R.id.logon_password_edit);

        logonButton = (Button) findViewById(R.id.logon_button);
        logonButton.setOnClickListener(logonListener);
    }

    @Override
    public void onStart() {
        super.onStart();

        ActionBar ab = this.getActionBar();
        ab.hide();
    }

    Button.OnClickListener logonListener = new Button.OnClickListener()
    {
        public void onClick(View v) {
            logonButton.setText(R.string.logon_logingin);

            if (valudate()) {
                if (loginProcess()) {
                    if (validTestStatus()) {
                        enterNextActivity(ATSInfoHintActivity.class);
                    } else {
                        enterNextActivity(ATSFilloutInfoActivity.class);
                    }
                } else {
                    showHintView();
                }
            }
        }
    };

    private void enterNextActivity(Class<?> cls) {
        Intent intent = new Intent();

        intent.setClass(ATSLogonActivity.this, cls);
        startActivity(intent);
        finish();
    }

    private void showHintView() {
        final TextView tv_hint = new TextView(this);

        ll_Hint.removeAllViews();
        ll_Hint.addView(tv_hint);
        tv_hint.setText(getString(R.string.logon_hint));
        tv_hint.setTextColor(Color.RED);
    }

    // 检查考试状态
    private boolean validTestStatus() {
        return false;
    }

    private boolean loginProcess() {
        String userName = logonNameEdit.getText().toString();
        String password = MD5(logonPasswordEdit.getText().toString());
        JSONObject jsonObj;

        try {
            jsonObj = query(userName, password);
            if (jsonObj.getInt("userId") > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
        // return false;
    }

    private boolean valudate() {
        String userName = logonNameEdit.getText().toString().trim();
        if (userName.equals("")) {
            showDialog("用户名是必填项！");
            return false;
        }

        String password = logonPasswordEdit.getText().toString().trim();
        if (password.equals("")) {
            showDialog("密码是必填项！");
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

    private JSONObject query(String userName, String password) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("candname", userName);
        map.put("pass", password);

        String url = ATSHttpUtil.BASE_URL + "tbl_proj_cand";
        return new JSONObject(ATSHttpUtil.postRequest(url, map));
    }

    // 根据用户名称密码查询
    private String queryC(String account, String password) throws Exception {
        // 查询参数
        String queryString = "account=" + account + "&password=" + password;
        // url
        String url = ATSHttpUtil.BASE_URL + "servlet/tbl_proj_cand?" + queryString;
        // 查询返回结果
        return ATSHttpUtil.queryStringForPost(url);
    }

    // MD5加密，32位 
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
     
    // 可逆的加密算法 
    public static String encryptMD5(String str) { 
        char[] a = str.toCharArray(); 

        for (int i = 0; i < a.length; i++) { 
            a[i] = (char) (a[i] ^ 'l'); 
        } 

        String s = new String(a); 

        return s; 
    }
}

