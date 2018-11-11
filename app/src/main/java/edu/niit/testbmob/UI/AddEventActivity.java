package edu.niit.testbmob.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import edu.niit.testbmob.R;
import edu.niit.testbmob.Utils.BaseActivity;
import edu.niit.testbmob.Utils.Event;


public class AddEventActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private MaterialEditText event_title;
    private MaterialEditText event_address;
    private EditText event_time;
    private EditText event_context;
    private long selectTime_start;
    private Timestamp startDate = null;
    Calendar start_calender = Calendar.getInstance();
    BmobUser user = BmobUser.getCurrentUser();
    Event event = new Event();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Bmob.initialize(this, "accbe3a7920d34d2c21e4cf1eacdfb9a");
        initView();
        showToolBar();


    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        event_context = (EditText) findViewById(R.id.et_context);
        event_time = (EditText) findViewById(R.id.event_time);
        event_title = (MaterialEditText) findViewById(R.id.event_title);
        event_address = (MaterialEditText) findViewById(R.id.event_address);
        event_time.setOnClickListener(this);
    }

    private void addEvent() {
        event.setUserName(user.getUsername().toString());
        event.setContext(event_context.getText().toString());
        event.setTitle(event_title.getText().toString());
        event.setTime(event_time.getText().toString());
        event.setAddress(event_address.getText().toString());
        event.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(AddEventActivity.this, "创建数据成功：" + objectId, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEventActivity.this, "失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void showToolBar() {
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_ok:
                        if (event_title.getText().toString().equals("")) {
                            Toast.makeText(AddEventActivity.this, "你输入的标题为空", Toast.LENGTH_SHORT).show();
                        } else if (event_time.getText().toString().equals("")) {
                            Toast.makeText(AddEventActivity.this, "你输入的日期为空", Toast.LENGTH_SHORT).show();
                        } else if (event_context.getText().toString().equals("")) {
                            Toast.makeText(AddEventActivity.this, "你输入的内容为空", Toast.LENGTH_SHORT).show();
                        } else if (event_address.getText().toString().equals("")) {
                            Toast.makeText(AddEventActivity.this, "你输入的地点为空", Toast.LENGTH_SHORT).show();
                        } else {
                            addEvent();
                        }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.event_time:
                new DatePickerDialog(AddEventActivity.this, new DatePickerDialog
                        .OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int
                            dayOfMonth) {
                        SimpleDateFormat df_start = new SimpleDateFormat("yyyy-MM-dd", Locale
                                .CHINA);
                        try {
                            selectTime_start = df_start.parse(year + "-" + (monthOfYear + 1) +
                                    "-" + dayOfMonth).getTime();
                            event_time.setText("日期：" + year + "-" + (monthOfYear + 1) + "-"
                                    + dayOfMonth + "");
                            startDate = new Timestamp(selectTime_start);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, start_calender.get(Calendar.YEAR), start_calender.get(Calendar.MONTH),
                        start_calender.get(Calendar
                                .DAY_OF_MONTH)).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
