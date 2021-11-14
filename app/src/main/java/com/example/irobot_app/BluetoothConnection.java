package com.example.irobot_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothConnection extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;

    Switch mOnOffBluetooth;
    ArrayList deviceList = new ArrayList();
    public static BluetoothAdapter mBlueAdapter;
    ListView mBluetoothDevices;
    ListAdapter aAdapter;
    public static String selectedDeviceAddress;
    public static String selectedDeviceName;


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

        checkBluetooth_atStart();

        //On Button click
        mOnOffBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBluetoothState();
            }
        });

        //Choose device with click on listview
        mBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Get the device MAC address, which is the last 17 chars in the View
                String info = ((TextView) v).getText().toString();
                String infoArray[] = info.split("\\r?\\n");
                selectedDeviceName = infoArray[0];
                selectedDeviceAddress = infoArray[1];

                String ConnectionMessage = ("Connecté à " + selectedDeviceName);
                Toast.makeText(getApplicationContext(), (String) ConnectionMessage, Toast.LENGTH_SHORT).show();

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
                        ViewBluetoothDevices();
                        break;

                    case BluetoothAdapter.STATE_OFF:
                        mOnOffBluetooth.setChecked(false);
                        mOnOffBluetooth.setText("Bluetooth OFF");
                        ClearListView();
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
    public void checkBluetooth_atStart(){
        if (mBlueAdapter.isEnabled()){
            mOnOffBluetooth.setChecked(true);
            ViewBluetoothDevices();
        }
        else{
            mOnOffBluetooth.setChecked(false);
            ClearListView();
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

    private void ViewBluetoothDevices(){
        if(mBlueAdapter==null){
            Toast.makeText(getApplicationContext(),"Bluetooth not supported ",Toast.LENGTH_SHORT).show();
        }
        else{
            Set<BluetoothDevice> pairedDevices = mBlueAdapter.getBondedDevices();
            if(pairedDevices.size()>0){
                for(BluetoothDevice device: pairedDevices){
                    String deviceName = device.getName();
                    String macAddress = device.getAddress();
                    deviceList.add(deviceName + "\n"+macAddress);
                }
                mBluetoothDevices = (ListView) findViewById(R.id.PairedDevicesList);
                aAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceList);
                mBluetoothDevices.setAdapter(aAdapter);
            }
        }
    }

    public void ClearListView(){
        deviceList.clear();
        mBluetoothDevices = (ListView) findViewById(R.id.PairedDevicesList);
        aAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceList);
        mBluetoothDevices.setAdapter(aAdapter);
    }

}
