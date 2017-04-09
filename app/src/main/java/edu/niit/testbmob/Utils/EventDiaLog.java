package edu.niit.testbmob.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import edu.niit.testbmob.R;
import edu.niit.testbmob.UI.ChargeEventActivity;

/**
 * Created by 11094 on 2017/3/27.
 */

public class EventDiaLog extends Dialog {
    private Context context;
    private Event str;
    private ClickListenerInterface eventClickListenerInterface;

    public interface ClickListenerInterface {
        //修改
        void doCharge();

        //取消
        void doDelete();
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.eventClickListenerInterface = clickListenerInterface;
    }


    public EventDiaLog(Context context, Event str) {
        super(context, R.style.dialog_custom);
        this.context = context;
        this.str = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }


    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_dialog, null);
        setContentView(view);
        Button btn_charge = (Button) findViewById(R.id.btn_charge);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);

        btn_charge.setOnClickListener(new eventClick());
        btn_delete.setOnClickListener(new eventClick());
    }

    class eventClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_charge:
                    eventClickListenerInterface.doCharge();
                    Intent i = new Intent(context, ChargeEventActivity.class);
                  //  Toast.makeText(context, str.getObjectId().toString(), Toast.LENGTH_SHORT).show();
                    i.putExtra("objectId", str.getObjectId().toString());
                    i.putExtra("title",str.getTitle().toString());
                    context.startActivity(i);
                    break;
                case R.id.btn_delete:
                    Event event = new Event();
                    event.setObjectId(str.getObjectId());
                    event.delete(new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {

                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "删除失败" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    eventClickListenerInterface.doDelete();
                    break;
            }
        }
    }

}
