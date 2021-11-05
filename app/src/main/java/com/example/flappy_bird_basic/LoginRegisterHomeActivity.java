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

public class LoginRegisterHomeActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private EditText usernameField;
    private Button loginBtn;
    private TextView resetPasswordBtn;
    private static FirebaseAuth authInLogReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_home);
        emailField =(EditText) findViewById(R.id.email_field);
        passwordField =(EditText) findViewById(R.id.password_field);
        usernameField=(EditText) findViewById(R.id.username_field);
        loginBtn=(Button) findViewById(R.id.login_btn);
        resetPasswordBtn=(TextView) findViewById(R.id.reset_password_btn);

    }

    public static void setUpAuth(FirebaseAuth authInMain) {
        authInLogReg=authInMain;
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
                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))                  //upload image logic needed, waiting to be changed
                            .build();
                    currUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(LoginRegisterHomeActivity.this, "Registered and Profile Updated Successfully, You can now Login", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.i("创建账户信息：","昵称："+authInLogReg.getCurrentUser().getDisplayName());
                    addUser(LoginRegisterHomeActivity.this, new User(authInLogReg.getCurrentUser().getUid(),authInLogReg.getCurrentUser().getDisplayName()));
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