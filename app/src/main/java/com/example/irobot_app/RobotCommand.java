package com.example.irobot_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.core.view.GestureDetectorCompat;

import com.google.android.material.chip.Chip;

import java.util.HashMap;
import java.util.Map;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class RobotCommand extends BluetoothConnection implements MyLoggerInterface {

    Chip mDB9connected;
    String DB9name = "X9 PLUS";
    private ImageButton HomeButton;

    //Variables pour la detection du SWIPE
    final private int DIRECTION_RIGHT = 1;
    final private int DIRECTION_LEFT = 2;
    final private int DIRECTION_UP = 3;
    final private int DIRECTION_DOWN = 4;
    public Map<Integer, String> directionsMap;
    private int curDir = DIRECTION_RIGHT;
    private String TAG = "coming up...";
    //gesture detector
    private GestureDetectorCompat gDetect;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_robot_command);

        HomeButton = (ImageButton) findViewById(R.id.homebutton);
        mDB9connected = findViewById(R.id.chipDB9);

        AdaptDB9chip();
        AdaptDB9chipColor();

        HomeButton.setOnClickListener(v -> {
            openMainActivity();
        });


        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                // do whatever you want
            }
        });

        //Detection du Swipe
        directionsMap = new HashMap<>();
        directionsMap.put(DIRECTION_RIGHT, "right");
        directionsMap.put(DIRECTION_LEFT, "left");
        directionsMap.put(DIRECTION_UP, "up");
        directionsMap.put(DIRECTION_DOWN, "down");

        TAG = getString(R.string.app_name);

        gDetect = new GestureDetectorCompat(this, new GestureListener(this));
    }

    public void AdaptDB9chip(){
        //Verifie la connection au Robot DB9
        if (selectedDeviceName != null && selectedDeviceName.equals(DB9name)){
            mDB9connected.setChecked(true);
        }
        else {
            mDB9connected.setChecked(false);
        }
    }

    public void AdaptDB9chipColor(){
        if (mDB9connected.isChecked()){
            mDB9connected.setChipBackgroundColorResource(R.color.green);
        }
        else {
            mDB9connected.setChipBackgroundColorResource(R.color.red);
        }
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    // FOLD GestureListener for seeing how it works in MainActivity
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private float flingMin = 100;
        private float velocityMin = 100;
        private MyLoggerInterface ml;

        public GestureListener(MyLoggerInterface ml) {
            this.ml = ml;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            //calculate the change in X position within the fling gesture
            float horizontalDiff = e2.getX() - e1.getX();
            //calculate the change in Y position within the fling gesture
            float verticalDiff = e2.getY() - e1.getY();

            float absHDiff = Math.abs(horizontalDiff);
            float absVDiff = Math.abs(verticalDiff);
            float absVelocityX = Math.abs(velocityX);
            float absVelocityY = Math.abs(velocityY);

            if (absHDiff > absVDiff && absHDiff > flingMin && absVelocityX > velocityMin) {
                //move forward or backward
                if (horizontalDiff > 0) {
                    curDir= DIRECTION_RIGHT;

                } else {
                    curDir = DIRECTION_LEFT;
                }
            } else if (absVDiff > flingMin && absVelocityY > velocityMin) {
                if (verticalDiff > 0) {
                    curDir = DIRECTION_DOWN;
                } else {
                    curDir = DIRECTION_UP;
                }
            }
            ml.onLog(directionsMap.get(curDir));
            return true;
        }
    }


    @Override public boolean onTouchEvent(MotionEvent event) {
        gDetect.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    @Override
    public void onLog(String s)   {
        if (s != null && s.equals("left")) {
            setContentView(R.layout.layout_robot_explications);
        }
        else if (s!= null && s.equals("right")){
            Intent intent = new Intent(this, RobotCommand.class);
            startActivity(intent);
        }
    }

}
