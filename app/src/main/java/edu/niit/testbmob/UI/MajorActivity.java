package edu.niit.testbmob.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import edu.niit.testbmob.Adapter.EventAdapter;
import edu.niit.testbmob.R;
import edu.niit.testbmob.Utils.BaseActivity;
import edu.niit.testbmob.Utils.Event;
import edu.niit.testbmob.Utils.EventDiaLog;

/**
 * Created by 11094 on 2017/3/23.
 */

public class MajorActivity extends BaseActivity implements View.OnClickListener {
    private FloatingActionButton floatingActionButton_add;
    private FloatingActionButton floatingActionButton_about;
    private FloatingActionButton floatingActionButton_me;
    private FloatingActionsMenu multiple_actions_down;
    private CoordinatorLayout coordinator_layout;
    private TextView toolbar_title;
    private RecyclerView show_event;
    private ImageView no_event;

    EventAdapter adapter;
    SwipeRefreshLayout swipe_refresh;
    BmobUser user = BmobUser.getCurrentUser();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);
        initView();
        toolbarShow();
        getEvent();
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirst", false);
        editor.commit();
        Intent i = getIntent();
        String username = i.getStringExtra("userName");
        String password = i.getStringExtra("passWord");
        SharedPreferences preferences1 = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putString("userName", username);
        editor1.putString("passWord", password);
        editor1.commit();


    }

    private void initView() {
        multiple_actions_down = (FloatingActionsMenu) findViewById(R.id.multiple_actions_down);
        floatingActionButton_add = (FloatingActionButton) findViewById(R.id.button_add);
        floatingActionButton_about = (FloatingActionButton) findViewById(R.id.button_about);
        floatingActionButton_me = (FloatingActionButton) findViewById(R.id.button_me);
        coordinator_layout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        show_event = (RecyclerView) findViewById(R.id.show_event);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        no_event = (ImageView) findViewById(R.id.im_no_event);
        floatingActionButton_add.setOnClickListener(this);
        floatingActionButton_about.setOnClickListener(this);
        floatingActionButton_me.setOnClickListener(this);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvent();
                swipe_refresh.setRefreshing(false);
            }
        });
        show_event.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mScrollThreshold;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
                if (isSignificantDelta) {
                    if (dy > 0) {
                        onScrollUp();
                    } else {
                        onScrollDown();
                    }
                }

            }

        });

    }

    /**
     * 下滑监听
     */
    private void onScrollDown() {
        //下滑时要执行的代码
        multiple_actions_down.setVisibility(View.VISIBLE);
    }

    /**
     * 上滑监听
     */
    private void onScrollUp() {
        //上滑时要执行的代码
        multiple_actions_down.setVisibility(View.GONE);
    }

    private void toolbarShow() {
        toolbar_title.setText("欢迎使用");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_about:
                Snackbar.make(coordinator_layout, "正在开发中，尽情期待", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.button_me:
                Intent i = new Intent(MajorActivity.this, PersonalCenterActivity.class);
                startActivity(i);
                break;
            case R.id.button_add:
                Intent intent = new Intent(MajorActivity.this, AddEventActivity.class);
                startActivity(intent);
                break;

        }

    }

    public void getEvent() {
        BmobQuery<Event> query = new BmobQuery<Event>();
        query.addWhereEqualTo("userName", user.getUsername().toString());
        query.setLimit(50);
        query.findObjects(new FindListener<Event>() {
            @Override
            public void done(final List<Event> object, BmobException e) {
                if (e == null) {
                    //设置布局管理器
                    if (object.size() == 0) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(MajorActivity.this);
                        show_event.setLayoutManager(layoutManager);
                        adapter = new EventAdapter(object);
                        show_event.setAdapter(adapter);
                        no_event.setVisibility(View.VISIBLE);
                    } else {
                        no_event.setVisibility(View.GONE);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(MajorActivity.this);
                        show_event.setLayoutManager(layoutManager);
                        adapter = new EventAdapter(object);
                        show_event.setAdapter(adapter);
                        adapter.setOnRecyclerViewItemClickListener(new EventAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent i = new Intent(MajorActivity.this, DetailActivity.class);
                                i.putExtra("event", object.get(position));
                                startActivity(i);
                            }

                        });
                        adapter.setOnItemLongClickListener(new EventAdapter.OnItemLongClickListener() {
                            @Override
                            public void onItemLongClick(View view, final int position) {
                                final EventDiaLog eventDialog = new EventDiaLog(MajorActivity.this, object.get(position));
                                eventDialog.show();
                                eventDialog.setClicklistener(new EventDiaLog.ClickListenerInterface() {
                                    @Override
                                    public void doCharge() {
                                        eventDialog.dismiss();
                                    }

                                    @Override
                                    public void doDelete() {
                                        getEvent();
                                        eventDialog.dismiss();

                                    }
                                });
                            }
                        });
                    }

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());

                }
            }
        });
    }

    @Override
    protected void onRestart() {
        getEvent();
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return true;
        }
        return true;
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MajorActivity.this);
        builder.setMessage("退出?");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();

    }
}
