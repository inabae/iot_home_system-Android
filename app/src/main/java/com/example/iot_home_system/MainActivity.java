package com.example.iot_home_system;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity{
    TextView mTvReceiveData;
    TextView mtxtAlramTime;
    TextView mtxtRedLight;
    TextView mtxtGreenLight;
    TextView mtxtBlueLight;
    Button mBtnBluetoothOn;
    Button mBtnBluetoothOff;
    Button mBtnConnect;
    Button mBtnLedOn;
    Button mBtnBellOn;
    Button mBtnFanOn;
    Button mBtnBoilerOn;
    Button mBtnKitchenLedOn;
    Button mBtnLivingRoomLedOn;
    Button mBtnBedRoomLedOn;
    Button mBtnDressRoomLedOn;
    Button mBtnAlramOff;
    Button mBtnFanOff;
    Button mBtnBoilerOff;
    Button mBtnImageRotation;
    Button mBtnAlramTime;
    SeekBar mSBRed;
    SeekBar mSBBlue;
    SeekBar mSBGreen;
    ImageView mImageView;
    ImageView mImageView2;
    TimePicker mTimePicker;

    int mDegree = 0;
    int _hour = 0;
    int _minute = 0;
    float RedLightNum = 0;
    float GreenLightNum = 0;
    float BlueLightNum = 0;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    ConnectedBluetoothThread mThreadConnectedBluetooth1;
    BluetoothDevice mBluetoothDevice;
    BluetoothDevice mBluetoothDevice1;
    BluetoothSocket mBluetoothSocket;
    BluetoothSocket mBluetoothSocket1;

    int BT_ONE_CONNECTING_STATUS = 0;
    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvReceiveData = (TextView)findViewById(R.id.tvReceiveData);
        mBtnBluetoothOn = (Button)findViewById(R.id.btnBluetoothOn);
        mBtnBluetoothOff = (Button)findViewById(R.id.btnBluetoothOff);
        mBtnConnect = (Button)findViewById(R.id.btnConnect);
        mBtnLedOn = (Button)findViewById(R.id.btnLedOn);
        mBtnBellOn = (Button)findViewById(R.id.btnBellOn);
        mBtnFanOn = (Button)findViewById(R.id.btnFanOn);
        mBtnBoilerOn = (Button)findViewById(R.id.btnBoilerOn);
        mBtnKitchenLedOn = (Button)findViewById(R.id.btnKitchenLedOn);
        mBtnLivingRoomLedOn = (Button)findViewById(R.id.btnLivingRoomLedOn);
        mBtnBedRoomLedOn = (Button)findViewById(R.id.btnBedRoomLedOn);
        mBtnDressRoomLedOn = (Button)findViewById(R.id.btnDressRoomLedOn);
        mBtnAlramOff = (Button)findViewById(R.id.btnAlramOff);
        mBtnFanOff = (Button)findViewById(R.id.btnFanOFF);
        mBtnBoilerOff = (Button)findViewById(R.id.btnBoilerOFF);
        mBtnImageRotation = (Button)findViewById(R.id.btnImagerotation);
        mBtnAlramTime = (Button)findViewById(R.id.btnAlramTime);
        mImageView = (ImageView)findViewById(R.id.imgRotate);
        mImageView2 = (ImageView)findViewById(R.id.imgRotate2);
        mTimePicker = (TimePicker)findViewById(R.id.timePicker);
        mtxtAlramTime = (TextView)findViewById(R.id.txtalramTime);
        mtxtRedLight = (TextView)findViewById(R.id.txtseekbarRed);
        mtxtGreenLight = (TextView)findViewById(R.id.txtseekbarGreen);
        mtxtBlueLight = (TextView)findViewById(R.id.txtseekbarBlue);
        mSBRed = (SeekBar)findViewById(R.id.seekBarRed);
        mSBGreen = (SeekBar)findViewById(R.id.seekBarGreen);
        mSBBlue = (SeekBar)findViewById(R.id.seekBarBlue);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mTimePicker.setIs24HourView(true);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                _hour = hour;
                _minute = minute;
                mtxtAlramTime.setText(_hour + " : " + _minute);
            }
        });

        mBtnBluetoothOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOn();
            }
        });
        mBtnBluetoothOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOff();
            }
        });
        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });
        mBtnLedOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("1\n");
                }
            }
        });
        mBtnBellOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("2\n");
                    mTvReceiveData.setText("");
                }
            }
        });
        mBtnFanOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("3\n");
                }
            }
        });
        mBtnBoilerOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("4\n");
                }
            }
        });
        mBtnFanOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("7\n");
                }
            }
        });
        mBtnBoilerOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("8\n");
                }
            }
        });
        mBtnKitchenLedOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("5\n");
                }
            }
        });
        mBtnAlramOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("6\n");
                }
            }
        });
        mBtnLivingRoomLedOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("9\n");
                }
            }
        });
        mBtnBedRoomLedOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("0\n");
                }
            }
        });
        mBtnDressRoomLedOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("D\n");
                }
            }
        });
        mBtnImageRotation.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mDegree = mDegree + 30;
                mImageView.setRotationY(mDegree);
                mImageView2.setRotationY(mDegree * (-1));
                if(mDegree > 40){
                    mDegree = -30;
                }
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("S\n");
                }
            }
        });
        mBtnAlramTime.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("T" + String.valueOf(_hour * 100 + _minute)+ "\n");
                }
            }
        });
        mSBRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                RedLightNum = mSBRed.getProgress();
                mtxtRedLight.setText(String.valueOf(RedLightNum));
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("R" + String.valueOf(RedLightNum)+ "\n");
                }
            }
        });
        mSBGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GreenLightNum = mSBGreen.getProgress();
                mtxtGreenLight.setText(String.valueOf(GreenLightNum));
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("G" + String.valueOf(GreenLightNum)+ "\n");
                }
            }
        });
        mSBBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BlueLightNum = mSBBlue.getProgress();
                mtxtBlueLight.setText(String.valueOf(BlueLightNum));
                if(mThreadConnectedBluetooth1 != null) {
                    mThreadConnectedBluetooth1.write("B" + String.valueOf(BlueLightNum)+ "\n");
                }
            }
        });
        mBluetoothHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == BT_MESSAGE_READ){
                    String readMessage = null;
                    mTvReceiveData.setText(readMessage);
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mTvReceiveData.setText(readMessage);
                }
            }
        };
    }
    void bluetoothOn() {
        if(mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
                //mTvBluetoothStatus.setText("활성화");
            }
            else {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            }
        }
    }
    void bluetoothOff() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                    //mTvBluetoothStatus.setText("활성화");
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                    //mListPairedDevices.add(device.getName() + "\n" + device.getAddress());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(BT_ONE_CONNECTING_STATUS == 0) {
                            connectSelectedDevice(items[item].toString());
                        }
                        else if(BT_ONE_CONNECTING_STATUS == 1){
                            connectSelectedDevice1(items[item].toString());
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    void connectSelectedDevice(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
            BT_ONE_CONNECTING_STATUS++;
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }
    void connectSelectedDevice1(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice1 = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket1 = mBluetoothDevice1.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket1.connect();
            mThreadConnectedBluetooth1 = new ConnectedBluetoothThread(mBluetoothSocket1);
            mThreadConnectedBluetooth1.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
            BT_ONE_CONNECTING_STATUS--;
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
