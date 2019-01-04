package com.example.abhinay.brickbraker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

public class AccelerometerListener implements SensorEventListener {

    private DataOutputStream dos;

    public AccelerometerListener(DataOutputStream d)
    {
        dos = d;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        try{
            dos.writeFloat(x);
            dos.writeFloat(y);
            dos.writeFloat(z);

        }catch(IOException e)
        {
            Log.e("IOException","at onSensorChanged()");
            Log.e("IOException" ,e.getMessage());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
