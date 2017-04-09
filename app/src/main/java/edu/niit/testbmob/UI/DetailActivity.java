package edu.niit.testbmob.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import edu.niit.testbmob.R;
import edu.niit.testbmob.Utils.BaseActivity;
import edu.niit.testbmob.Utils.Event;

/**
 * Created by 11094 on 2017/3/28.
 */

public class DetailActivity extends BaseActivity {
    private Toolbar detailToolbar;
    private TextView detailTitle;
    private TextView detailAddress;
    private TextView detailTime;
    private TextView detailContext;
    String title, context, address, time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        initView();
        getData();
        showDetail();

    }

    private void showDetail() {
        detailTitle.setText(title);
        detailAddress.setText("地址" + address);
        detailContext.setText("内容：" + context);
        detailTime.setText(time);
    }

    private void getData() {
        Intent intent = this.getIntent();
        Event event = (Event) intent.getSerializableExtra("event");
        title = event.getTitle();
        context = event.getContext();
        address = event.getAddress();
        time = event.getTime();
    }

    private void initView() {
        detailToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        detailTitle = (TextView) findViewById(R.id.detail_toolbar_title);
        detailTime = (TextView) findViewById(R.id.detail_time);
        detailAddress = (TextView) findViewById(R.id.detail_address);
        detailContext = (TextView) findViewById(R.id.detail_context);
        detailToolbar.setNavigationIcon(R.drawable.back);
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
