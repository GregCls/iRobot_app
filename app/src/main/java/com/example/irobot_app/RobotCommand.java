package com.example.irobot_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.chip.Chip;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class RobotCommand extends BluetoothConnection {

    Chip mDB9connected;
    String DB9name = "X9 PLUS";
    private ImageButton HomeButton;

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
}
