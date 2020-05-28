package com.phrenologue.fitnessproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phrenologue.fitnessproject.activityInput.InputView;
import com.phrenologue.fitnessproject.constants.UserProps;
import com.phrenologue.fitnessproject.databinding.ActivityFirstProfileBinding;
import com.phrenologue.fitnessproject.databinding.ActivitySetUpPropsBinding;

public class SetUpProps extends AppCompatActivity {
    private ActivitySetUpPropsBinding binding;
    private EditText currentW, goal, steps;
    private Button measurement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetUpPropsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        currentW = binding.currentW;
        goal = binding.goal;
        steps = binding.goal;

        measurement = binding.beginMeasurement;

        measurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double currentWDouble = Double.parseDouble(currentW.getText().toString());
                Double goalDouble = Double.parseDouble(goal.getText().toString());
                int stepsInt = Integer.parseInt(steps.getText().toString());
                UserProps props = new UserProps(getApplicationContext());
                props.setStartingW(currentWDouble);
                props.setGoal(goalDouble);
                props.setSteps(stepsInt);
                props.setStepLength();
                if (!currentW.getText().toString().equals("")
                && !goal.getText().toString().equals("")
                && !steps.getText().toString().equals("")){
                    Intent intent = new Intent(getApplicationContext(), InputView.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.fill_all, Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
