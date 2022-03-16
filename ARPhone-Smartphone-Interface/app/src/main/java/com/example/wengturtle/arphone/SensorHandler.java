package com.example.wengturtle.arphone;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorHandler {

    private Sensor gyroscopeSensor;
    private Sensor orientationSensor;


    public SensorHandler(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(new GyroscopeSenserListener(), gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);

        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(new OrientationSenserListener(), orientationSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private class GyroscopeSenserListener implements  SensorEventListener {

        private float ROTATE_THRESHOLD = 7.0f;

        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            if(z < -ROTATE_THRESHOLD) {
                Log.d("YueTing","right-rotate");
                Net.getInstance().sendString("right-rotate");
            } else if(z > ROTATE_THRESHOLD) {
                Log.d("YueTing","left-rotate");
                Net.getInstance().sendString("left-rotate");
            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class OrientationSenserListener implements SensorEventListener {

        private boolean toggleNavigation = false;
        private float THRESHOLD = 100;

        @Override
        public void onSensorChanged(SensorEvent event) {
            float y = event.values[1];


            if(y < -THRESHOLD) {
                if(!toggleNavigation) {
                    toggleNavigation = true;
                    Log.d("YueTing","nav");
                    //Net.getInstance().sendString("nav");
                }

            } else {
                if(toggleNavigation) {
                    toggleNavigation = false;
                    Log.d("YueTing","un-nav");
                    //Net.getInstance().sendString("un-nav");
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
