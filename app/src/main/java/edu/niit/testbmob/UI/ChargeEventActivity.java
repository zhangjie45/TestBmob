package edu.niit.testbmob.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import edu.niit.testbmob.R;
import edu.niit.testbmob.Utils.BaseActivity;
import edu.niit.testbmob.Utils.Event;

/**
 * Created by 11094 on 2017/3/28.
 */

public class ChargeEventActivity extends BaseActivity implements View.OnClickListener {
    private MaterialEditText charge_event_title;
    private MaterialEditText charge_event_adress;
    private EditText charge_event_time;
    private EditText charge_event_context;
    private TextView charge_toolbar_title;
    private Toolbar charge_toolbar;
    String objectId;
    String title;
    private long selectTime_start;
    private Timestamp startDate = null;
    Calendar start_calender = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_charge);
        getData();
        initView();
        toolbarShow();


    }

    private void toolbarShow() {
        //  charge_toolbar_title.setText("修改");
        charge_toolbar.setTitle("");
        setSupportActionBar(charge_toolbar);
        charge_toolbar.setNavigationIcon(R.drawable.back);
        charge_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        charge_toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    private void initView() {
        //   charge_event_title = (MaterialEditText) findViewById(R.id.charge_event_title);
        charge_event_adress = (MaterialEditText) findViewById(R.id.charge_event_address);
        charge_event_time = (EditText) findViewById(R.id.charge_event_time);
        charge_event_context = (EditText) findViewById(R.id.charge_event_context);
        charge_toolbar = (Toolbar) findViewById(R.id.charge_toolbar);
        charge_toolbar_title = (TextView) findViewById(R.id.charge_toolbar_title);
        charge_event_time.setOnClickListener(this);
        charge_toolbar_title.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_charge, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.change_ok:
                    Event event = new Event();
                    event.setAddress(charge_event_adress.getText().toString());
                    event.setContext(charge_event_context.getText().toString());
                    event.setTime(charge_event_time.getText().toString());
                    event.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(ChargeEventActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChargeEventActivity.this, "修改失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
            }
            return true;
        }
    };
    public void getData() {
        Intent intent = this.getIntent();
        objectId = intent.getStringExtra("objectId");
        title = intent.getStringExtra("title");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.charge_event_time:
                new DatePickerDialog(ChargeEventActivity.this, new DatePickerDialog
                        .OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int
                            dayOfMonth) {
                        SimpleDateFormat df_start = new SimpleDateFormat("yyyy-MM-dd", Locale
                                .CHINA);
                        try {
                            selectTime_start = df_start.parse(year + "-" + (monthOfYear + 1) +
                                    "-" + dayOfMonth).getTime();
                            charge_event_time.setText("日期：" + year + "-" + (monthOfYear + 1) + "-"
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
}
