package com.example.activity_recognition_pk_km;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    public enum CurrentActionEnum
    {
        NOTHING, RUNNING, WALKING, STAYING, GETTING_UP, SITTING_DOWN
    }

    CurrentActionEnum currentAction = CurrentActionEnum.NOTHING;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate = 0;
    DataSource datasource;

    public static int currentlySelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        datasource = new DataSource(getBaseContext());
        datasource.open();

//        List<DatabaseRecord> records = datasource.getAllGPS();
//        for (DatabaseRecord record :
//                records) {
//            Log.d("Record", record.getX() +", " + record.getY() +", " + record.getZ() + ", " + record.getTimestamp() + ", " + record.getActivity());
//        }


        Context ctx = this; // for Activity, or Service. Otherwise simply get the context.
        String dbname = "accelerometer_data.db";
        Log.d("Path", ctx.getDatabasePath(dbname).getPath());

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_nothing :
                        sensorManager.unregisterListener(MainActivity.this);
                        currentAction = CurrentActionEnum.NOTHING; break;
                    case R.id.radioButton_running :
                        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                        currentAction = CurrentActionEnum.RUNNING; break;
                    case R.id.radioButton_walking :
                        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                        currentAction = CurrentActionEnum.WALKING; break;
                    case R.id.radioButton_staying :
                        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                        currentAction = CurrentActionEnum.STAYING; break;
                    case R.id.radioButton_getting_up :
                        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                        currentAction = CurrentActionEnum.GETTING_UP; break;
                    case R.id.radioButton_sitting_down :
                        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                        currentAction = CurrentActionEnum.SITTING_DOWN; break;
                }
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

//                addAndSendToServer(x, y, z);
                Log.d("Sensor change", x + ", " + y +", " + z + ".\t" + currentAction.name() + "\t\t" + Long.toString(System.currentTimeMillis()));

                if(datasource != null)
                    datasource.createEntry(x, y, z, Long.toString(System.currentTimeMillis()), currentAction.name());
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

//    protected void onPause() {
//        super.onPause();
//        sensorManager.unregisterListener(this);
//    }
//
//    protected void onResume() {
//        super.onResume();
//        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//    }


    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(this);
        datasource.close();
        super.onDestroy();
    }
}
