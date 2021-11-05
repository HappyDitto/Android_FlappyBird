package com.example.flappy_bird_basic;

import static utils.DatabaseCRUD.addUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import userInfo.User;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.flappy_bird_basic.listener.OnDialogChooseClickerListener;
import com.example.flappy_bird_basic.util.LocationUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginRegisterHomeActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private EditText usernameField;
    private Button loginBtn;
    private TextView resetPasswordBtn;
    private static FirebaseAuth authInLogReg;

    CircleImageView image_logo;
    private BottomSheetDialog dialog;
    private List<String> listInfos=new ArrayList<>();
    private int IMAGE_REQUEST_CODE=10,CAMERA_REQUEST_CODE=11,RESULT_REQUEST_CODE=12;
    private String photoPath,bitmapToString,mCurrentPhotoPath;
    private Bitmap bitmap;
    TextView text_adress;
    public static final int TAKE_CAMERA = 101;
    private Uri imageUri;
    private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_home);
        emailField =(EditText) findViewById(R.id.email_field);
        passwordField =(EditText) findViewById(R.id.password_field);
        usernameField=(EditText) findViewById(R.id.username_field);
        loginBtn=(Button) findViewById(R.id.login_btn);
        resetPasswordBtn=(TextView) findViewById(R.id.reset_password_btn);
        text_adress=findViewById(R.id.text_adress);//定位
        image_logo=findViewById(R.id.image_logo);//图像
        image_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //防止第一次进来没有权限，在获取一次定位
                    try{
                        Address adress=LocationUtils.getInstance(LoginRegisterHomeActivity.this).showLocation();
                        address = adress.getLocality() + ", " + adress.getCountryName();
                        text_adress.setText("Located address: "+adress.getCountryName()+"---"+adress.getAdminArea()+"---"+adress.getLocality());

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    showReplyDialog(new OnDialogChooseClickerListener() {
                        @Override
                        public void onTakePhoneItemClick(View view, BottomSheetDialog dialog) {
                            File photoFile = createImageFile();
                            imageUri=Uri.fromFile(photoFile);
                            //启动相机程序
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //MediaStore.ACTION_IMAGE_CAPTURE = android.media.action.IMAGE_CAPTURE
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                            dialog.dismiss();
                        }
                        @Override
                        public void onPhotoAlbumItemClick(View view, BottomSheetDialog dialog) {
                            //点击事件，而重定向到图片库
                            openPhones();
                            dialog.dismiss();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //注意6.0及以上版本需要在申请完权限后调用方法
        if(!checkPublishPermission()){
            try{
                Address adress=LocationUtils.getInstance(LoginRegisterHomeActivity.this).showLocation();
                address = adress.getLocality() + ", " + adress.getCountryName();
                text_adress.setText("Located address:"+adress.getCountryName()+"---"+adress.getAdminArea()+"---"+adress.getLocality());

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(10);
        //通过RequestOptions扩展功能
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        options.placeholder(R.mipmap.photo);
        options.error(R.mipmap.photo);
        options.fallback(R.mipmap.photo);
        //获取图片
        if (requestCode == IMAGE_REQUEST_CODE) {//相册

            if (data == null||"".equals(data)) {
                return;
            }
            imageUri= data.getData();
            //获取照片路径
            String[] filePathColumn = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            //上传服务器地址
            photoPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            Glide.with(LoginRegisterHomeActivity.this).load(photoPath).apply(options).into(image_logo);

        }else if (requestCode == CAMERA_REQUEST_CODE) {//相机
            Glide.with(LoginRegisterHomeActivity.this).load(mCurrentPhotoPath).apply(options).into(image_logo);
            //上传服务器地址
            photoPath=mCurrentPhotoPath;
        }

    }
    private File createImageFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = null;
        try {
            image = File.createTempFile(
                    generateFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public  String generateFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }
    //打开图库
    private void openPhones() {
        // 图库选择
        // 激活系统图库，选择一张图片
        Intent intent_gallery = new Intent(Intent.ACTION_PICK);
        intent_gallery.setType("image/*");
        startActivityForResult(intent_gallery,IMAGE_REQUEST_CODE);
    }

    public static void setUpAuth(FirebaseAuth authInMain) {
        authInLogReg=authInMain;
    }

    /**
     * by moos on 2018/04/20
     * func:弹出对话框
     */
    public void showReplyDialog(final OnDialogChooseClickerListener dislog){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.editor_dialog_layout,null);
        final LinearLayout line_take_phone= (LinearLayout) commentView.findViewById(R.id.line_take_phone);//照相
        final LinearLayout line_quxiao = (LinearLayout) commentView.findViewById(R.id.line_quxiao);//相册
        final LinearLayout line_remove = (LinearLayout) commentView.findViewById(R.id.line_remove);//删除
        final TextView text_xiangji = (TextView) commentView.findViewById(R.id.text_xiangji);//相机
        final TextView text_xiangce = (TextView) commentView.findViewById(R.id.text_xiangce);//相册
        dialog.setContentView(commentView);
        line_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        line_take_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislog.onTakePhoneItemClick(view,dialog);
            }
        });
        line_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislog.onPhotoAlbumItemClick(view,dialog);
            }
        });
        dialog.show();
    }
    //Build.VERSION.SDK_INT >= 23
    private boolean checkPublishPermission(){
        List<String> permissions = new ArrayList<>();
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(LoginRegisterHomeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(LoginRegisterHomeActivity.this, android.Manifest.permission.CAMERA)) {
            permissions.add(android.Manifest.permission.CAMERA);
        }
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(LoginRegisterHomeActivity.this, android.Manifest.permission.RECORD_AUDIO)) {
            permissions.add(android.Manifest.permission.RECORD_AUDIO);
        }
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(LoginRegisterHomeActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(LoginRegisterHomeActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissions.size() != 0) {
            ActivityCompat.requestPermissions(LoginRegisterHomeActivity.this,(String[]) permissions.toArray(new String[0]),100);
            return false;
        }
        return true;
    }

    // for login info verification
    public void registerEntry(View view) {
        String email_input=emailField.getText().toString().replaceAll("\\s","");
        String password_input=passwordField.getText().toString().replaceAll("\\s","");
        String username_input=usernameField.getText().toString().replaceAll("\\s","");
        if (email_input.isEmpty()||password_input.isEmpty()) {
            Toast.makeText(LoginRegisterHomeActivity.this, "Empty email or password", Toast.LENGTH_LONG).show();
        }else if (username_input.isEmpty()) {
            Toast.makeText(LoginRegisterHomeActivity.this, "Empty username", Toast.LENGTH_SHORT).show();
        } else if (password_input.length()<6) {
            Toast.makeText(LoginRegisterHomeActivity.this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
        } else {
            registerOnDemand(email_input,password_input,username_input);
        }
// possible discard TextUtils.isEmpty(email_input)||TextUtils.isEmpty(password_input)
    }

    // for register function
    private void registerOnDemand(String email_input, String password_input, String username_input) {
        authInLogReg.createUserWithEmailAndPassword(email_input,password_input).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username_input)
                            .setPhotoUri(Uri.parse(photoPath))                  //upload image logic needed, waiting to be changed
                            .build();
                    currUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(LoginRegisterHomeActivity.this, "Registered and Profile Updated Successfully, You can now Login", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.i("创建账户信息：","昵称："+authInLogReg.getCurrentUser().getDisplayName());
                    addUser(LoginRegisterHomeActivity.this, new User(authInLogReg.getCurrentUser().getUid(), address, authInLogReg.getCurrentUser().getDisplayName(), photoPath));
                }else {
                    Toast.makeText(LoginRegisterHomeActivity.this, "Failed Registering, Check your credentials and user name", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // for login info verification
    public void loginEntry(View view) {
        String email_input=emailField.getText().toString().replaceAll("\\s","");
        String password_input=passwordField.getText().toString().replaceAll("\\s","");
        if (email_input.isEmpty()||password_input.isEmpty()) {
            Toast.makeText(LoginRegisterHomeActivity.this, "Empty email or password", Toast.LENGTH_LONG).show();
        }else {
            loginOnDemand(email_input,password_input);
        }
    }

    // for login function
    private void loginOnDemand(String email_input, String password_input) {
        authInLogReg.signInWithEmailAndPassword(email_input,password_input).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginRegisterHomeActivity.this, "Successfully Logged-in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginRegisterHomeActivity.this,MainActivity.class));
                finish();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginRegisterHomeActivity.this, "Failed Login, Register or check your credentials", Toast.LENGTH_LONG).show();
            }
        });
    }

    // for password reset info collection
    public void resetPasswordEntry(View view) {
        AlertDialog.Builder resetDial = new AlertDialog.Builder(view.getContext());
        resetDial.setTitle("Forget Your Password?");
        resetDial.setMessage("Reset Via Your Email: ");
        EditText emailForResetPassword= new EditText(view.getContext());
        resetDial.setView(emailForResetPassword);

        resetDial.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //logic for cancel
            }
        });
        resetDial.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String resetEmail= emailForResetPassword.getText().toString();
                if (resetEmail.isEmpty()) {
                    Toast.makeText(LoginRegisterHomeActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                }else{
                    resetPassword(resetEmail);
                }
            }
        });

        resetDial.show();

    }

    // for password reset function
    private void resetPassword(String resetEmail) {
        authInLogReg.sendPasswordResetEmail(resetEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(LoginRegisterHomeActivity.this, "Check email to reset password", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginRegisterHomeActivity.this, "Something goes wrong sending reset email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}