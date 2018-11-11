package edu.niit.testbmob.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import edu.niit.testbmob.R;
import edu.niit.testbmob.Utils.BaseActivity;


public class AlarmclockReceive extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.clock_dailog);
        Button btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
               // Intent i = new Intent(this, AlarmclockReceive.class);
//                PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//                manager.cancel(pi);
            }
        });

    }
}
