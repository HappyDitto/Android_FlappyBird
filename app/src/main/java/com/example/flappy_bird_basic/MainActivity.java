package com.example.flappy_bird_basic;

import static utils.DatabaseCRUD.*;
import static utils.Utils.*;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import userInfo.User;
public class MainActivity extends Activity implements SensorEventListener {
    private Button playDirectlyBtn;
    private Button loginBtn;
    private FirebaseAuth authInMain;

    // define variable for light sensors
    TextView textView;
    SensorManager sensorManager;
    Sensor sensor;

    // we can use this to pass light intensity to GameView
    public static int light_intensity = 0;

    public static int bestscore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase authentication setup
        authInMain=FirebaseAuth.getInstance();
        LoginRegisterHomeActivity.setUpAuth(authInMain);
        ProfileLogoutActivity.setUpAuth(authInMain);


        // light sensor
        textView = (TextView) findViewById(R.id.textView);
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // databases
        playDirectlyBtn = findViewById(R.id.startDirectlyBtn);
        loginBtn = findViewById(R.id.goLoginBtn);

        bestscore = getIntInfo("best_score");
    }

    /***
     * Light Sensor implementation section
     */

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            // show on screen the bird jump
            textView.setText(""+"Light Intensity: "+sensorEvent.values[0] + "\n"
                    + "Bird Jump Strength: " + 30 * ( 1 + (sensorEvent.values[0]/1000)) + "Pixels");
            light_intensity = (int)sensorEvent.values[0];
        }
    }

    /***
     * start games
     */

    public void goStart(View view){
        Intent intent = new Intent(this, StartPlayWithAI.class);
        intent.putExtra("Mode", StartPlayWithAI.SOLO_MODE);
        startActivity(intent);
    }

    public void goPlayWithAI(View view){
        Intent intent = new Intent(this, StartPlayWithAI.class);
        intent.putExtra("Mode", StartPlayWithAI.AI_MODE);
        startActivity(intent);
    }

    /***
     * Firebase Runtime Database Connection Testing
     */
    private void dataBaseInitTrying(){
        // Database Init Test
        // Write a message to the database
        DatabaseReference myRef = getFirebaseRef();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.i("monitor change:", value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("monitor error:", error.toException().toString());
            }
        });

    }

    //must set to invisible if logged-in
    public void accountLoginRegister(View view) {
        Intent intentForLoginRegister= new Intent(this,LoginRegisterHomeActivity.class);
        startActivity(intentForLoginRegister);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        authStateLog();
    }

    private void authStateLog() {
        FirebaseUser currUser= authInMain.getCurrentUser();
        if(currUser != null){
            ((Button) findViewById(R.id.goLoginBtn)).setEnabled(false);
            ((Button) findViewById(R.id.goLoginBtn)).setVisibility(View.GONE);
        }else {
            ((Button) findViewById(R.id.goLoginBtn)).setEnabled(true);
            ((Button) findViewById(R.id.goLoginBtn)).setVisibility(View.VISIBLE);
        }
    }


    //graphical account entry after logged-in
    public void userAccountEntry(View view) {
        //need logic for login register and user profile
        FirebaseUser currUser= authInMain.getCurrentUser();
        if (currUser!=null) {
            startActivity(new Intent(MainActivity.this,ProfileLogoutActivity.class));
        }else {
            Toast.makeText(MainActivity.this, "Login to check your profile", Toast.LENGTH_LONG).show();
        }
    }

    public int getIntInfo(String key) {
        SharedPreferences userInfo = getSharedPreferences("BEST_SCORE", MODE_PRIVATE);
        return userInfo.getInt(key, 0);
    }
}