package com.example.irobot_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button DeployButton;
    private ImageButton BluetoothButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeployButton = (Button) findViewById(R.id.DeployButton);
        BluetoothButton = (ImageButton) findViewById(R.id.BluetoothButton);


        DeployButton.setOnClickListener(v -> {
            openRobotCommand();
        });

        BluetoothButton.setOnClickListener(v -> {
            openBluetoothMenu();
        });
    }

    public void openRobotCommand(){
        Intent intent = new Intent(this, RobotCommand.class);
        startActivity(intent);
    }

    public void openBluetoothMenu(){
        Intent intent = new Intent(this, BluetoothConnection.class);
        startActivity(intent);
    }
}