package com.example.flappy_bird_basic;

import static utils.DatabaseCRUD.addUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import userInfo.User;

public class LoginRegisterHomeActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;
    private FirebaseAuth authInLogReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_home);
        emailField =(EditText) findViewById(R.id.email_field);
        passwordField =(EditText) findViewById(R.id.password_field);
        loginBtn=(Button) findViewById(R.id.login_btn);
        authInLogReg = FirebaseAuth.getInstance();

    }

    public void registerEntry(View view) {
        String email_input=emailField.getText().toString();
        String password_input=passwordField.getText().toString();
        if (TextUtils.isEmpty(email_input)||TextUtils.isEmpty(password_input)) {
            Toast.makeText(LoginRegisterHomeActivity.this, "Empty email or password", Toast.LENGTH_SHORT).show();
        }else {
            registerOnDemand(email_input,password_input);
        }
        //register logic here


    }

    private void registerOnDemand(String email_input, String password_input) {
        authInLogReg.createUserWithEmailAndPassword(email_input,password_input).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginRegisterHomeActivity.this, "Successfully Registered, You can now Login", Toast.LENGTH_SHORT).show();
                    addUser(LoginRegisterHomeActivity.this, new User(authInLogReg.getCurrentUser().getUid(),authInLogReg.getCurrentUser().getDisplayName(), authInLogReg.getCurrentUser().getPhotoUrl()));
                }else {
                    Toast.makeText(LoginRegisterHomeActivity.this, "Failed Registering", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void loginEntry(View view) {
        //login logic here
        String email_input=emailField.getText().toString();
        String password_input=passwordField.getText().toString();
        if (TextUtils.isEmpty(email_input)||TextUtils.isEmpty(password_input)) {
            Toast.makeText(LoginRegisterHomeActivity.this, "Empty email or password", Toast.LENGTH_SHORT).show();
        }else {
            loginOnDemand(email_input,password_input);
        }
    }

    private void loginOnDemand(String email_input, String password_input) {
        authInLogReg.signInWithEmailAndPassword(email_input,password_input).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginRegisterHomeActivity.this, "Successfully Logined", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginRegisterHomeActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}