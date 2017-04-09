package edu.niit.testbmob.Utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import edu.niit.testbmob.R;

/**
 * Created by 11094 on 2017/3/24.
 */

public class ConfirmDialog extends android.app.Dialog {
    private Context context;
    private String charge_name;
    private String charge_email;
    private String charge_phNum;
    private String confirmButtonText;
    private String cancelButtonText;
    TextView tvName;
    TextView tvEmail;
    TextView tvPhNum;
    private ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {
        //确定
        public void doConfirm();

        //取消
        public void doCancel();
    }

    public ConfirmDialog(Context context, String charge_name,
                         String charge_email, String charge_phNum,
                         String confirmButtonText, String cancelButtonText) {
        super(context, R.style.dialog_custom);
        this.context = context;
        this.charge_name = charge_name;
        this.charge_email = charge_email;
        this.charge_phNum = charge_phNum;
        this.confirmButtonText = confirmButtonText;
        this.cancelButtonText = cancelButtonText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog, null);
        setContentView(view);
        tvName = (TextView) view.findViewById(R.id.charge_userName);
        tvEmail = (TextView) view.findViewById(R.id.charge_userEmail);
        tvPhNum = (TextView) view.findViewById(R.id.charge_userPhNum);
        Button btnOK = (Button) view.findViewById(R.id.btn_charge_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_charge_cancel);
        tvName.setText(charge_name);
        tvEmail.setText(charge_email);
        tvPhNum.setText(charge_phNum);
        btnOK.setText(confirmButtonText);
        btnCancel.setText(cancelButtonText);
        btnOK.setOnClickListener(new clickListener());
        btnCancel.setOnClickListener(new clickListener());

    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_charge_ok:
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    bmobUser.setUsername(tvName.getText().toString());
                    bmobUser.setEmail(tvEmail.getText().toString());
                    bmobUser.setMobilePhoneNumber(tvPhNum.getText().toString());
                    bmobUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(context, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.btn_charge_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }
    }
}
