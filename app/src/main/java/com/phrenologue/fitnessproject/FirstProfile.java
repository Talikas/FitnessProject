package com.phrenologue.fitnessproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.phrenologue.fitnessproject.activityInput.InputView;
import com.phrenologue.fitnessproject.activityProfile.ProfileView;
import com.phrenologue.fitnessproject.databinding.ActivityFirstProfileBinding;

public class FirstProfile extends AppCompatActivity {
    private ActivityFirstProfileBinding binding;
    private Button start, measurement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        SharedPreferences sp = getSharedPreferences("welcome", MODE_PRIVATE);
        boolean welcomed = sp.getBoolean("welcomed", false);
        if (welcomed) {
            Intent intent = new Intent(getApplicationContext(), ProfileView.class);
            startActivity(intent);
            finish();
        }
        sp.edit().putBoolean("welcomed", true).apply();
        start = binding.start;
        measurement = binding.measurement;

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetUpProps.class);
                startActivity(intent);
                finish();
            }
        });

        measurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InputView.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
