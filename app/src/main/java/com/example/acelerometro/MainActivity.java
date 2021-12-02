package com.example.acelerometro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final String MY_ACTION = "android.com.example.broadcastreceiver.action.MYACTION";

    /**
     * Constants for sensors
     */
    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;

    private long shakeTime = 0;
    //private long mRotationTime = 0;

    /**
     * Sensors
     */
    private SensorManager sensorManager;
    private Sensor accelerometer;
   // private long mShakeTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the sensors to use
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

      /*  if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }*/

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            detectShake(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void detectShake(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - shakeTime) > SHAKE_WAIT_TIME_MS) {
            shakeTime = now;

            //values[i]: aceleración en el eje i
            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement
            double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD) {
                Intent intent = new Intent();
                // Establecer la propiedad de acción del objeto Intent
                intent.setAction(MY_ACTION);
                // Agregar información adicional al objeto Intent
                intent.putExtra("msg", "Enviar prueba de transmisión.....");
                //Publicar transmisión
                sendBroadcast(intent);
            }
        }
    }


    // Receptor de transmisión, responde a la operación de envío de transmisión
    public static class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            //Obtiene los datos en el objeto Intent
            String msg = "shake detected: " + intent.getStringExtra("msg");
            //muestra el mensaje
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }


}















