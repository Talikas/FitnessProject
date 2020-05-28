package com.phrenologue.fitnessproject.constants;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class UserProps {
    private SharedPreferences sharedPreferences;

    public UserProps(Context context) {
        this.sharedPreferences = context.getSharedPreferences("props", Context.MODE_PRIVATE);
    }

    public double getStartingW() {
        return sharedPreferences.getFloat("startingW", 0f);
    }

    public void setStartingW(Double startingW) {
        sharedPreferences.edit().putFloat("startingW", startingW.floatValue()).apply();
    }

    public double getCurrentWeight() {
        return sharedPreferences.getFloat("currentW", 0f);
    }

    public void setCurrentWeight(Double currentW) {
        sharedPreferences.edit().putFloat("currentW", currentW.floatValue()).apply();
        setCurrentStep();
    }

    public double getGoal() {
        return sharedPreferences.getFloat("goal", 0);
    }

    public void setGoal(Double goal) {
        sharedPreferences.edit().putFloat("goal", goal.floatValue()).apply();
    }

    public double getStepLength() {
        return sharedPreferences.getFloat("stepLength", 0);
    }

    public void setStepLength() {
        Double goal = getGoal();
        Double current = getStartingW();
        int steps = getSteps();
        Double stepLength = (goal - current) / steps;
        sharedPreferences.edit().putFloat("stepLength", stepLength.floatValue()).apply();
    }

    public int getSteps() {
        return sharedPreferences.getInt("steps", 0);
    }

    public void setSteps(int steps) {
        sharedPreferences.edit().putInt("steps", steps).apply();
    }

    public void setCurrentStep() {
        sharedPreferences.edit().putInt("stepsMade", (int) ((getStartingW() - getCurrentWeight())
                / getStepLength())).apply();
        Double progress = (getStartingW() - getCurrentWeight()) % getStepLength();
        sharedPreferences.edit().putFloat("progress", progress.floatValue()).apply();
    }

    public int getCurrentStep() {
        return sharedPreferences.getInt("stepsMade", 0);
    }

    public double getCurrentProgress() {
        return  sharedPreferences.getFloat("progress", 0f);
    }

}
