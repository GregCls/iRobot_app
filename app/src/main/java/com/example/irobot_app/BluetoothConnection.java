package com.example.irobot_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;

public class BluetoothConnection extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch mOnOffBluetooth;
    BluetoothAdapter mBlueAdapter;
    ListView mBluetoothDevicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);

        //OnOff Bluetooth Button
        mOnOffBluetooth = findViewById(R.id.OnOffBluetooth);
        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        // Register for broadcasts on BluetoothAdapter state change
        IntentFilter btFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBlueReceiver, btFilter);

        checkBluetoothState();

        //On Button click
        mOnOffBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBluetoothState();
            }
        });
    }
    //Bluetooth State change broadcast receiver
    private final BroadcastReceiver mBlueReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        mOnOffBluetooth.setChecked(true);
                        mOnOffBluetooth.setText("Bluetooth ON");
                        mBluetoothDevicesList.setVisibility(View.VISIBLE);
                        break;

                    case BluetoothAdapter.STATE_OFF:
                        mOnOffBluetooth.setChecked(false);
                        mOnOffBluetooth.setText("Bluetooth OFF");
                        mBluetoothDevicesList.setVisibility(View.GONE);
                        break;

                    case BluetoothAdapter.STATE_TURNING_ON:
                        mOnOffBluetooth.setText("Bluetooth turning ON");
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        mOnOffBluetooth.setText("Bluetooth turning OFF");
                        break;
                }
            }
        }
    };

    //Check bluetooth State at the activity Start
    public void checkBluetoothState(){
        if (mBlueAdapter.isEnabled()){
            mOnOffBluetooth.setChecked(true);
            mBluetoothDevicesList.setVisibility(View.VISIBLE);
        }
        else{
            mOnOffBluetooth.setChecked(false);
            mBluetoothDevicesList.setVisibility(View.GONE);
        }
    }

    //change bluetooth State if user click on switch
    private void changeBluetoothState(){
        if(mBlueAdapter.isEnabled()) {
            mBlueAdapter.disable();
        }
        else{
            mBlueAdapter.enable();
        }
    }


}
