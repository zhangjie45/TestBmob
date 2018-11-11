package edu.niit.testbmob.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;

import edu.niit.testbmob.R;

public class AlarmActivity extends Activity {
    MediaPlayer alarmMusic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmMusic = MediaPlayer.create(this, R.raw.clock);
        alarmMusic.setLooping(true);
        alarmMusic.start();
        new AlertDialog.Builder(AlarmActivity.this).setTitle("提醒：").setMessage("您的闹钟时间到了")
                .setPositiveButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarmMusic.stop();
                        // 结束Activity
                        AlarmActivity.this.finish();

                    }
                }).show();
    }
}