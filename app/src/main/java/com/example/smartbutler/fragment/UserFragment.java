package com.example.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbutler.R;
import com.example.smartbutler.entity.MyUser;
import com.example.smartbutler.ui.CourierActivity;
import com.example.smartbutler.ui.LoginActivity;
import com.example.smartbutler.ui.PhoneActivity;
import com.example.smartbutler.utils.L;
import com.example.smartbutler.utils.ShareUtils;
import com.example.smartbutler.utils.UtilTools;
import com.example.smartbutler.view.CustomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2019/5/3 0003.
 * 个人中心
 */

public class UserFragment extends Fragment implements View.OnClickListener{
    private Button btn_exit_user;
    private TextView edit_user;
    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;
    private Button btn_update_ok;
    //圆形头像
    private CircleImageView profile_image;
    private CustomDialog dialog;
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    private TextView tv_courier;
    private TextView tv_phone;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        btn_exit_user=view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        edit_user=view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);

        et_username=view.findViewById(R.id.et_username);
        et_sex=view.findViewById(R.id.et_sex);
        et_age=view.findViewById(R.id.et_age);
        et_desc=view.findViewById(R.id.et_desc);
        //默认不可输入的，不可点击的
        setEnabled(false);
        //获取服务端数据，设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getUsername());
        //String类型的，而age是int类型
        et_age.setText(userInfo.getAge()+"");
        et_sex.setText(userInfo.isSex()?"男":"女");
        et_desc.setText(userInfo.getDesc());
        //更新修改按钮
        btn_update_ok=view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);

        profile_image=view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        UtilTools.getImageToShare(getActivity(),profile_image);

        dialog=new CustomDialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT,R.layout.dialog_photo,R.style.Theme_dialog, Gravity.BOTTOM,0);
        dialog.setCanceledOnTouchOutside(false);
        btn_camera=dialog.findViewById(R.id.btn_camera);
        btn_picture=dialog.findViewById(R.id.btn_picture);
        btn_cancel=dialog.findViewById(R.id.btn_cancel);
        btn_camera.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        tv_courier=view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);

        tv_phone=view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);

    }
    private void setEnabled(boolean b){
        et_username.setEnabled(b);
        et_sex.setEnabled(b);
        et_age.setEnabled(b);
        et_desc.setEnabled(b);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //退出登陆
            case R.id.btn_exit_user:
                // 清除缓存用户对象
                // 现在的currentUser是null了
                MyUser.logOut();
                BmobUser currentUser =  MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            break;
            case R.id.edit_user:
                setEnabled(true);
                btn_update_ok.setVisibility(View.VISIBLE);
                break;
                //点击更新
            case R.id.btn_update_ok:
                //拿到输入框的值
                String username=et_username.getText().toString().trim();
                String age=et_age.getText().toString().trim();
                String sex=et_sex.getText().toString().trim();
                String desc=et_desc.getText().toString().trim();
                if(!TextUtils.isEmpty(username)&!TextUtils.isEmpty(age)&!TextUtils.isEmpty(sex)) {
                    //更新属性
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setAge(Integer.parseInt(age));
                    if(sex.equals("男")){
                        user.setSex(true);
                    }else if(sex.equals("女")){
                        user.setSex(false);
                    }else{
                        Toast.makeText(getActivity(),"输入有误，默认性别为男",Toast.LENGTH_SHORT).show();
                        user.setSex(true);
                        et_sex.setText("男");
                    }
                    if(!TextUtils.isEmpty(desc)){
                        user.setDesc(desc);
                    }else{
                        user.setDesc(getResources().getString(R.string.desc_text));
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                setEnabled(false);
                                btn_update_ok.setVisibility(View.GONE);
                                Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getActivity(),"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;
        }
    }
    public static final String PHOTO_IMAGE_FILE_NAME="fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE=100;
    public static final int IMAGE_REQUEST_CODE=101;
    public static final int RESULT_REQUEST_CODE=102;
    private File tempFile=null;
    //跳转相机
    private void toCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可以用,可以用的话进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }
    //跳转相册
    private void toPicture() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        //全部图片
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=getActivity().RESULT_CANCELED){//请求成功
            switch(requestCode){
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());//点击选择图片(data.getData())
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    //获取到相机拍摄的图片
                    tempFile=new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍去
                    if(data!=null){
                        //拿到图片设置
                        setImagToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if(tempFile!=null){
                            tempFile.delete();
                        }
                    }
                    break;

            }
        }
    }

    private void setImagToView(Intent data) {
        Bundle bundle=data.getExtras();
        if(bundle!=null){
            Bitmap bitmap=bundle.getParcelable("data");
            //设置图片
            profile_image.setImageBitmap(bitmap);
        }
    }

    private void startPhotoZoom(Uri uri){
        if(uri==null){
            L.e("uri==null");
            return;
        }
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");//设置数据类型
        //设置裁剪
        intent.putExtra("crop",true);
        //裁剪宽高比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //裁剪图片的质量(分辨率)
        intent.putExtra("outputX",320);
        intent.putExtra("outputY",320);
        //发送数据
        intent.putExtra("return-data",true);
        startActivityForResult(intent,RESULT_REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UtilTools.putImageToShare(getActivity(),profile_image);
    }
}
