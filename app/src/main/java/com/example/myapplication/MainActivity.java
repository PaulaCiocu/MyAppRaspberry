package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.myapplication.R;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView textHumidity, textLightLed, textLightDetected, textTemperature, textWaterDetected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews
        textHumidity = findViewById(R.id.text_humidity);
       // textLightLed = findViewById(R.id.text_light_led);
        textLightDetected = findViewById(R.id.text_light_detected);
        textTemperature = findViewById(R.id.text_temperature);
        textWaterDetected = findViewById(R.id.text_water_detected);

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference humidityRef = database.getReference("Humidity");
        DatabaseReference lightLedRef = database.getReference("Light_LED");
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
                String lightLedValue = String.valueOf(dataSnapshot.getValue());
                //textLightLed.setText("Light LED: " + lightLedValue);
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
                String waterDetectedValue = String.valueOf(dataSnapshot.getValue());
                textWaterDetected.setText("Water Detected: " + waterDetectedValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read water detected value.", error.toException());
            }
        });
    }
}
