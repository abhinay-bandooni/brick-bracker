package com.example.abhinay.brickbraker;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements Runnable{

    private EditText ip;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Socket skt;
    private DataOutputStream dos;
    private Thread t1;
    private boolean register;

    private AccelerometerListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText) findViewById(R.id.editText);
        ip.setText("192.168.0.100");
        Button connect = (Button) findViewById(R.id.button);

        register = false;

        t1 = new Thread(this);

        connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!t1.isAlive())
                {
                    t1 = new Thread(MainActivity.this);
                    t1.start();
                }
            }
        });

    }
    @Override
    public void run()
    {
        String ipadd = ip.getText().toString();

        try {
            skt = new Socket(ipadd,1024);
            dos = new DataOutputStream(skt.getOutputStream());
            Log.e("DataOutputStream obj",dos.toString());

            listener = new AccelerometerListener(dos);

            if(register==false)
            {
                sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI);
                Log.e("SensorManager", "Listener Registered");
                register=true;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e("Exception","UnknownHostException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception","IOException");
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if(skt!=null)
                skt.close();
            if(dos!=null)
                dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(listener!=null)
        {
            sensorManager.unregisterListener(listener);
            register=false;
            Log.e("SensorManager","Listener Unregistered");
        }

    }

}
