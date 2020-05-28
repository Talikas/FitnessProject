package com.phrenologue.fitnessproject.activityDailyResults;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;

public interface IDailyResultsView {
    void getPrevMorningWeight(int primaryKey);
    void getPrevNightWeight(int primaryKey);
    void printWeightChange(int change);
    void drawChart(CombinedData data);
    void hideReports();
}
