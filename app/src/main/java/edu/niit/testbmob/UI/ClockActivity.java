package edu.niit.testbmob.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import edu.niit.testbmob.Utils.BaseActivity;



public class ClockActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
        AlarmManager alarmService = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 8);//小时
        instance.set(Calendar.MINUTE, 0);//分钟
        instance.set(Calendar.SECOND, 0);//秒
        Intent alarmIntent = new Intent(this, AlarmclockReceive.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmService.setRepeating(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), INTERVAL, broadcast);//这里一定要注意，一定要相对时间对应相对时间，
    }


    @Override
    public void onClick(View view) {

    }
}
