package com.example.flappy_bird_basic;

import static utils.DatabaseCRUD.*;
import static utils.Utils.*;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.Service;
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
import android.speech.RecognizerIntent;


import java.util.ArrayList;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
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

        authInMain=FirebaseAuth.getInstance();

        // light sensor
        textView = (TextView) findViewById(R.id.textView);
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // databases
        playDirectlyBtn = findViewById(R.id.startDirectlyBtn);
        loginBtn = findViewById(R.id.goLoginBtn);

    }

    /***
     * Implementation for light sensors
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

//    public void goStart(View view){
////        Intent intent = new Intent(this, StartGame.class);
////        Intent intent = new Intent(this, StartPlayWithAI.class);
//        Intent intent = new Intent(this, StartTrain.class);
//        startActivity(intent);
//    }

    /***
     * Firebase Runtime Database Connection Testing
     */
    private void dataBaseInitTrying(){
        // Database Init Test
        // Write a message to the database
        DatabaseReference myRef = getFirebaseRef();

        //监听到数据库有变化取出来打印
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.i("监听到变化:", value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("监听到错误:", error.toException().toString());
            }
        });

    }

    //possible discard of button
    public void accountLoginRegister(View view) {
//        ((Button)findViewById(R.id.goLoginBtn)).setEnabled(false);
        Intent intentForLoginRegister= new Intent(this,LoginRegisterHomeActivity.class);
        startActivity(intentForLoginRegister);
    }


    // change to graphical account entry
    public void userAccountEntry(View view) {
        //need logic for login register and user profile
        startActivity(new Intent(MainActivity.this,ProfileLogoutActivity.class));
    }
}