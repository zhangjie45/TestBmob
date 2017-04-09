package edu.niit.testbmob.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import edu.niit.testbmob.R;
import edu.niit.testbmob.Utils.BaseActivity;
import edu.niit.testbmob.Utils.User;

public class NewUserActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_new_user;
    private EditText et_new_userEmail;
    private EditText et_new_userPassWd;
    private EditText et_new_userPassWdAgain;
    private Button btn_new_userOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);
        Bmob.initialize(this, "accbe3a7920d34d2c21e4cf1eacdfb9a");
        initView();
    }

    private void initView() {
        et_new_user = (EditText) findViewById(R.id.et_new_user);
        et_new_userEmail = (EditText) findViewById(R.id.et_new_userEmail);
        et_new_userPassWd = (EditText) findViewById(R.id.et_new_userPassWd);
        et_new_userPassWdAgain = (EditText) findViewById(R.id.et_new_userPassWdAgain);
        btn_new_userOk = (Button) findViewById(R.id.btn_new_userOk);
        btn_new_userOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new_userOk:
                BmobUser bu = new BmobUser();
                bu.setUsername(et_new_user.getText().toString());
                bu.setPassword(et_new_userPassWdAgain.getText().toString());
                bu.setEmail(et_new_userEmail.getText().toString());
                if (et_new_userPassWd.getText().toString().equals("")) {
                    Toast.makeText(NewUserActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (et_new_user.length() >= 6 && et_new_user.getText().toString().equals("")) {
                    Toast.makeText(NewUserActivity.this, "用户名不能超过6位或者为空", Toast.LENGTH_SHORT).show();
                } else if (null != et_new_userPassWdAgain && et_new_userPassWd.getText().toString().equals(et_new_userPassWdAgain.getText().toString())) {
                    BmobUser user = new BmobUser();
                    user.setUsername(et_new_user.getText().toString());
                    user.setPassword(et_new_userPassWdAgain.getText().toString());
                    user.setEmail(et_new_userEmail.getText().toString());
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                Toast.makeText(NewUserActivity.this, "注册成功:" + user.toString(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(NewUserActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(NewUserActivity.this, e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        }
    }
}
