package com.phrenologue.fitnessproject.activityProfile;

import android.content.SharedPreferences;

import com.phrenologue.fitnessproject.constants.Date;
import com.phrenologue.fitnessproject.database.Daily;

import java.util.Calendar;

public class ProfilePresenter {
    IProfileView iProfileView;

    public ProfilePresenter(IProfileView iProfileView) {
        this.iProfileView = iProfileView;
    }

    public void setWeight(SharedPreferences sp) {
        boolean firstDone = sp.getBoolean("firstDone", false);
        if (firstDone) {
            iProfileView.getLastWeight();
        } else {
            iProfileView.showCurrentWeight();
        }
    }

    public int getLastWeight(Daily last) {
        double nightW = last.getNightWeight();
        if (nightW > 0) {
            return (int) nightW;
        } else {
            return (int) last.getDayWeight();
        }
    }

    public double getLastWeightDouble(Daily last) {
        double nightW = last.getNightWeight();
        if (nightW > 0) {
            return nightW;
        } else {
            return last.getDayWeight();
        }
    }


}
