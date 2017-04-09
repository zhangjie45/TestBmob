package edu.niit.testbmob.UI;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import edu.niit.testbmob.R;
import edu.niit.testbmob.Utils.BaseActivity;
import edu.niit.testbmob.Utils.ConfirmDialog;
import edu.niit.testbmob.Utils.ImageTools;


/**
 * Created by 11094 on 2017/3/23.
 */

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener {
    private TextView userName;
    private TextView userEmail;
    private TextView userPhoNum;
    private TextView userFlag;
    private Toolbar toolbarPersonal;
    private TextView tv_personal;
    private String phoneNum;
    ImageView user_image = null;
    BmobUser user = BmobUser.getCurrentUser();
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int SCALE = 3;//照片缩小比例


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        Bmob.initialize(this, "accbe3a7920d34d2c21e4cf1eacdfb9a");
        initView();
        getData();
        showToolBar();
    }

    private void showToolBar() {
        tv_personal.setText("个人资料");
        toolbarPersonal.setTitle("");
        setSupportActionBar(toolbarPersonal);
        toolbarPersonal.setNavigationIcon(R.drawable.back);
        toolbarPersonal.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarPersonal.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    private void initView() {
        tv_personal = (TextView) findViewById(R.id.tv_personal);
        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userPhoNum = (TextView) findViewById(R.id.userPhoNum);
        userFlag = (TextView) findViewById(R.id.userFlag);
        toolbarPersonal = (Toolbar) findViewById(R.id.personal_toolbar);
        user_image = (ImageView) findViewById(R.id.user_image);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicturePicker(PersonalCenterActivity.this);
            }
        });


    }

    public void showPicturePicker(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;

                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;

                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();
                    //将处理过的图片显示在界面上，并保存到本地
                    user_image.setImageBitmap(newBitmap);
                    ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();
                            user_image.setImageBitmap(smallBitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }

    }

    public void getData() {
        if (null == user.getMobilePhoneNumber()) {
            userPhoNum.setText("你还没有设置过手机号");
            phoneNum = "你还没有设置过手机号";
        } else {
            userPhoNum.setText(user.getMobilePhoneNumber().toString());
            phoneNum = user.getMobilePhoneNumber().toString();
        }
        if (user.getEmailVerified() == null) {
            userFlag.setText("邮箱未激活（可以点击激活，激活之后可以使用邮箱登录）");
            userFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEmailMsg();

                }


            });

        } else {
            userFlag.setText("邮箱已激活");
        }
        userEmail.setText(user.getEmail().toString());
        userName.setText(user.getUsername().toString());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.change:
                    final ConfirmDialog confirmDialog = new ConfirmDialog(PersonalCenterActivity.this,
                            user.getUsername().toString(), user.getEmail().toString(), phoneNum, "确定", "取消");
                    confirmDialog.show();
                    confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {

                            confirmDialog.dismiss();
                        }

                        @Override
                        public void doCancel() {
                            confirmDialog.dismiss();
                        }
                    });
                    break;
            }
            return true;
        }
    };

    private void sendEmailMsg() {
        final String email = user.getEmail().toString();
        BmobUser.requestEmailVerify(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(PersonalCenterActivity.this, "请求验证邮件成功，请到" + email + "邮箱中进行激活。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalCenterActivity.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
