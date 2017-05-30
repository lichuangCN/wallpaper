package com.example.cnlcnn.email;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.utils.mail.EmailUtils;
import com.example.cnlcnn.wallpaper.R;

import java.security.GeneralSecurityException;

import javax.mail.MessagingException;


/**
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间： 2017/5/27.
 *  描述：    意见反馈，发送邮件
 */

public class SendToEmail extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText emailText;
    private Button sendbtn;
    private String title, emailTxt;
    private String[] emailReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        initViews();

        Intent intent = getIntent();
        title= intent.getStringExtra(Constants.TYPE_NAME);

        if (!TextUtils.isEmpty(title)) {
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        sendbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailTxt = emailText.getText().toString();
                Thread sendThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            new EmailUtils(emailTxt).sendMail();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        }
                    }
                };
                sendThread.start();
                emailText.setText("");
                Toast.makeText(SendToEmail.this, "反馈成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * toolbar里面菜单的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {//返回上一个界面
                SendToEmail.this.finish();
            }
            break;
            default:
        }
        return true;
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        emailText = (EditText) findViewById(R.id.emailTxt);
        sendbtn = (Button) findViewById(R.id.sendBtn);
    }
}
