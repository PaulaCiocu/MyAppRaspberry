package com.example.myapplication;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView textHumidity, textLightDetected, textLightLed, textTemperature, textWaterDetected;
    private DatabaseReference lightLedRef;
    private static final String CHANNEL_ID = "my_channel";
    private static final CharSequence CHANNEL_NAME = "My Notification Channel";
    private static final int notificationId = 3;

    private void showInAppNotification(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        // Initialize TextViews
        textHumidity = findViewById(R.id.text_humidity);
        textLightLed = findViewById(R.id.text_light_led);
        textLightDetected = findViewById(R.id.text_light_detected);
        textTemperature = findViewById(R.id.text_temperature);
        textWaterDetected = findViewById(R.id.text_water_detected);

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference humidityRef = database.getReference("Humidity");
        lightLedRef = database.getReference("Light_LED");
        DatabaseReference lightDetectedRef = database.getReference("Light_detected");
        DatabaseReference temperatureRef = database.getReference("Temp");
        DatabaseReference waterDetectedRef = database.getReference("Water_detected");

        // Read from the database
        humidityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String humidityValue = String.valueOf(dataSnapshot.getValue());
                textHumidity.setText("Humidity: " + humidityValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read humidity value.", error.toException());
            }
        });

        lightLedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean lightLedValue = dataSnapshot.getValue(Boolean.class);
                textLightLed.setText("Light LED: " + lightLedValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read light LED value.", error.toException());
            }
        });


        lightDetectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lightDetectedValue = String.valueOf(dataSnapshot.getValue());
                textLightDetected.setText("Light Detected: " + lightDetectedValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read light detected value.", error.toException());
            }
        });


        temperatureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temperatureValue = String.valueOf(dataSnapshot.getValue());
                textTemperature.setText("Temperature: " + temperatureValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read temperature value.", error.toException());
            }
        });

        waterDetectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double waterDetectedValue = dataSnapshot.getValue(Double.class);
                textWaterDetected.setText("Water Detected: " + waterDetectedValue);
                if(waterDetectedValue !=null &&  waterDetectedValue < 1.5 ){
                    Log.w(TAG,"Needs notification");
                    showInAppNotification("Water level is below 1.5 - Plant needs water");
                    sendNotification("Water level is below 1.5", "Plant needs water", MainActivity.this);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read water detected value.", error.toException());
            }
        });

    }

    private void createNotificationChannel() {
        // Create the notification channel if device is running Android Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                );
                // Optionally configure the channel's properties
                channel.setDescription("My Notification Channel");
                channel.enableLights(true);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void sendNotification(String messageBody, String title, Context context) {
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Get the NotificationManager and issue the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(notificationId, builder.build());
            Log.w(TAG,"Notification successfull");
        }
    }

    public void toggleLightLED(View view) {
        lightLedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean currentLightLedValue = dataSnapshot.getValue(Boolean.class);
                // Toggle the value (invert boolean)
                lightLedRef.setValue(!currentLightLedValue);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read Light_LED value.", error.toException());
            }
        });
    }


}
