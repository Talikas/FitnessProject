package com.phrenologue.fitnessproject.activityInput;

public interface IInputView {
    void onIncompleteInput();
    void onCompleteInput();
    boolean checkInput(String time);
    void setMorning(int day, int month, int year);
    void setNight();
}
