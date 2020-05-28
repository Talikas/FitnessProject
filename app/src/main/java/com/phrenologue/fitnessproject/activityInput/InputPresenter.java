package com.phrenologue.fitnessproject.activityInput;

import com.phrenologue.fitnessproject.constants.Date;

import java.util.Calendar;

public class InputPresenter {
    IInputView iInputView;
    public InputPresenter(IInputView iInputView){
        this.iInputView = iInputView;
    }

    public void checkInput(String time){
        if (iInputView.checkInput(time)){
            iInputView.onCompleteInput();
        } else {
            iInputView.onIncompleteInput();
        }
    }

    public void setTime(){
        Date date = getDate();
        if (date.isNight()){
            iInputView.setNight();
        } else {
            iInputView.setMorning(date.getDay(), date.getMonth(), date.getYear());
        }
    }

    public void setSelectedTime(int position) {
        if (position==0){
            iInputView.setMorning(getDate().getDay(), getDate().getMonth(), getDate().getYear());
        } else {
            iInputView.setNight();
        }
    }

    private Date getDate(){
        String now = Calendar.getInstance().getTime().toString();
        return new Date(now);
    }
}
