package com.example.flappy_bird_basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.flappy_bird_basic.MainActivity.bestscore;

import static utils.DatabaseCRUD.setUserBestScore;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import utils.DatabaseCRUD;

public class ProfileLogoutActivity extends AppCompatActivity {
    private static FirebaseAuth authInProfile;
    private ImageView profileImage;
    private EditText usernameProfileSetup;
    private Button updateProfileBtn;
    private Button logoutBtn;
    private TextView userScoreText;
    private int databaseScore = 0;


    public static void setUpAuth(FirebaseAuth authInMain) {
        authInProfile=authInMain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_logout);

        profileImage=(ImageView) findViewById(R.id.profile_image_view);
        usernameProfileSetup=(EditText) findViewById(R.id.username_profile_setup);
        updateProfileBtn=(Button) findViewById(R.id.update_profile_btn);
        logoutBtn=(Button) findViewById(R.id.logout_btn);
        userScoreText=(TextView) findViewById(R.id.userScore);

        profileSetupEntry();
    }


    private void profileSetupEntry() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            profileImage.setImageURI(photoUrl);  //read from local, need more work
            usernameProfileSetup.setText(name);  //read from remote

            //zpy update score part
            String thisuid = user.getUid();
            Log.i("我的ID：", thisuid);
            setUserBestScore(thisuid,34);
//            int thisScore = 0;
            DatabaseCRUD.getUserBestScore(thisuid).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot scoreData = (DataSnapshot) task.getResult();
                    Log.i("最好成绩：", scoreData.getValue().toString());
                    userScoreText.setText(String.valueOf(scoreData.getValue()));
                }
            });
//            userScoreText.setText(String.valueOf(2222));
//            userScoreText.setText(String.valueOf(thisScore));

        }
    }

// asynchronous need to be solved
    public void logoutEntry(View view) {
        FirebaseUser currentUser = authInProfile.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(ProfileLogoutActivity.this, "User Logined", Toast.LENGTH_SHORT).show();
        }
        authInProfile.signOut();
        if(currentUser != null){
            Toast.makeText(ProfileLogoutActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(ProfileLogoutActivity.this, "logout after", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileLogoutActivity.this,MainActivity.class));
        }
    }

    public void updateProfileEntry(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            //zpy update score part
            String thisuid = user.getUid();

            DatabaseCRUD.getUserBestScore(thisuid).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    DataSnapshot scoreData = (DataSnapshot) task.getResult();
                    Log.i("最好成绩：", scoreData.getValue().toString());
                    databaseScore = new Long((Long) scoreData.getValue()).intValue();
                    if (bestscore > databaseScore){
                        setUserBestScore(thisuid,bestscore);
                        userScoreText.setText(String.valueOf(bestscore));
                    }else{
                        userScoreText.setText(String.valueOf(scoreData.getValue()));
                    }

                }
            });

//            int thisScore = DatabaseCRUD.getUserBestScore(thisuid);
//            DatabaseCRUD.getUserBestScore(thisuid).addOnCompleteListener(new OnCompleteListener() {
//                @Override
//                public void onComplete(@NonNull Task task) {
//                    DataSnapshot scoreData = (DataSnapshot) task.getResult();
//                    Log.i("最好成绩：", scoreData.getValue().toString());
//                    userScoreText.setText(String.valueOf(scoreData.getValue()));
//                }
//            });
        }

    }

    public void setUpProfileImage(View view) {

    }
// possible implementation

//    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//if (user != null) {
//        // Name, email address, and profile photo Url
//        String name = user.getDisplayName();
//        String email = user.getEmail();
//        Uri photoUrl = user.getPhotoUrl();
//
//        // Check if user's email is verified
//        boolean emailVerified = user.isEmailVerified();
//
//        // The user's ID, unique to the Firebase project. Do NOT use this value to
//        // authenticate with your backend server, if you have one. Use
//        // FirebaseUser.getIdToken() instead.
//        String uid = user.getUid();
//    }

}