package edu.niit.testbmob.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import edu.niit.testbmob.R;



public class SendEmailActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialEditText et_email;
    private Button btn_send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        Bmob.initialize(this, "accbe3a7920d34d2c21e4cf1eacdfb9a");
        initView();


    }

    private void sendEmail() {
        if (et_email.getText().toString().equals("")) {
            Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
        } else {
            final String email = et_email.getText().toString();
            BmobUser.resetPasswordByEmail(email, new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(SendEmailActivity.this, "重置密码请求成功，请到" + email + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SendEmailActivity.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void initView() {
        et_email = (MaterialEditText) findViewById(R.id.et_email);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                sendEmail();
        }
    }
}
