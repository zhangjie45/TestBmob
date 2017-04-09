package edu.niit.testbmob.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_new_user;
    private Button btn_login;
    private EditText et_userEmail;
    private EditText et_userPassWd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "accbe3a7920d34d2c21e4cf1eacdfb9a");
        initView();
        btn_new_user.setOnClickListener(this);
        btn_login.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        Boolean orFirst = preferences.getBoolean("isFirst", false);
        SharedPreferences preferences1 = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (orFirst == false && preferences1.getString("userName", "") != "") {
            Toast.makeText(this, "正在登录。。。", Toast.LENGTH_SHORT).show();
            String username = preferences1.getString("userName", "");
            String password = preferences1.getString("passWord", "");
            BmobUser bu2 = new BmobUser();
            bu2.setUsername(username);
            bu2.setPassword(password);
            bu2.login(new SaveListener<BmobUser>() {

                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if (e == null) {
                        toast(LoginActivity.this, "登录成功");
                        Intent i = new Intent(LoginActivity.this, MajorActivity.class);
                        startActivity(i);
                        finish();
                        //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                        //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                    } else {
                        toast(LoginActivity.this, e.getMessage());
                    }
                }
            });

        }
    }

    private void initView() {
        btn_new_user = (Button) findViewById(R.id.btn_new_user);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_userEmail = (EditText) findViewById(R.id.et_userEmail);
        et_userPassWd = (EditText) findViewById(R.id.et_userPasswd);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new_user:
                Intent i = new Intent(LoginActivity.this, NewUserActivity.class);
                startActivity(i);
                break;
            case R.id.btn_login:
                BmobUser bu2 = new BmobUser();
                bu2.setUsername(et_userEmail.getText().toString());
                bu2.setPassword(et_userPassWd.getText().toString());
                bu2.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            toast(LoginActivity.this, "登录成功");
                            Intent i = new Intent(LoginActivity.this, MajorActivity.class);
                            i.putExtra("userName", et_userEmail.getText().toString());
                            i.putExtra("passWord", et_userPassWd.getText().toString());
                            startActivity(i);
                            finish();
                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                        } else {
                            toast(LoginActivity.this, e.getMessage());
                        }
                    }
                });
                break;


        }
    }
}
