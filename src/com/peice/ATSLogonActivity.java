
package com.peice;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peice.model.ATSDialogUtil;

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

    @Override
    public void onResume() {
        super.onResume();

        logonButton.setText(R.string.logon_logon);
    }

    Button.OnClickListener logonListener = new Button.OnClickListener()
    {
        public void onClick(View v) {
            logonButton.setText(R.string.logon_logingin);

            if (valudate()) {
                if (logonProcess()) {
                    enterNextActivity();
                }
                else {
                    showHintView();
                }
            }
        }
    };

    private void enterNextActivity() {
        Intent intent = new Intent();

        intent.setClass(ATSLogonActivity.this, ATSFilloutInfoActivity.class);
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

    private boolean logonProcess() {
        String userName = logonNameEdit.getText().toString();
        String password = logonPasswordEdit.getText().toString();
        JSONObject jsonObj;

        try {
            jsonObj = query(userName, password);
            if (jsonObj.getInt("userId") > 0) {
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
        // return false;
    }

    private boolean valudate() {
        String userName = logonNameEdit.getText().toString().trim();
        if (userName.equals("")) {
            ATSDialogUtil.showDialog(this, "用户名是必填项！", false);
            return false;
        }

        String password = logonPasswordEdit.getText().toString().trim();
        if (password.equals("")) {
            ATSDialogUtil.showDialog(this, "密码是必填项！", false);
            return false;
        }

        return true;
    }

    private JSONObject query(String userName, String password) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user", userName);
        map.put("pass", password);

        // String url = HttpUtil.BASE_URL + "login.jsp";
        // return new JSONObject(HttpUtil.postRequest(url, map));
        return null;
    }
}

