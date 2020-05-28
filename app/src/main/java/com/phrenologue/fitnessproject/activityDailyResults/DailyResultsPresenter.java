package com.phrenologue.fitnessproject.activityDailyResults;

import android.graphics.Color;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.phrenologue.fitnessproject.constants.Colors;
import com.phrenologue.fitnessproject.database.Daily;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.round;

public class DailyResultsPresenter {
    IDailyResultsView iDailyResultsView;

    public DailyResultsPresenter(IDailyResultsView iDailyResultsView) {
        this.iDailyResultsView = iDailyResultsView;
    }

    public void getPrevWeight(int primaryKey, boolean isNight) {
        if (isNight) {
            iDailyResultsView.getPrevNightWeight(primaryKey);
        } else {
            iDailyResultsView.getPrevMorningWeight(primaryKey);
        }
    }

    public void calculateWeightChange(Double prevWeight, Double currentWeight) {
        int c = (int) (((currentWeight - prevWeight) / prevWeight) * 100);
        iDailyResultsView.printWeightChange(c);
    }

    public void drawChart(List<Daily> dailyList, int position) {
        LineData lineData = new LineData();
        ArrayList<Entry> lineEntries = new ArrayList<>();
        ArrayList<Double> trendPoints = calculateTrendLinePoints(dailyList, position);
        for (int i = 0; i < trendPoints.size(); i++) {
            lineEntries.add(new Entry(i + 0.5f, trendPoints.get(i).floatValue()));
        }
        LineDataSet set = new LineDataSet(lineEntries, "Trend Line");
        set.setColor(Colors.russianViolet);
        set.setLineWidth(4f);
        set.setCircleRadius(0f);
        set.setCircleColor(Color.rgb(60,22,66));
        lineData.addDataSet(set);

        BarData barData = new BarData();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < dailyList.size(); i++) {
            if (position == 0){
                Double dailyAvg = dailyList.get(i).getDailyAvg();
                barEntries.add(new BarEntry(i + 0.5f, dailyAvg.floatValue()));
            } else if (position == 1) {
                Double morningWeight = dailyList.get(i).getDayWeight();
                barEntries.add(new BarEntry(i + 0.5f, morningWeight.floatValue()));
            } else if (position == 2) {
                Double nightWeight = dailyList.get(i).getDailyAvg();
                barEntries.add(new BarEntry(i + 0.5f, nightWeight.floatValue()));
            }

        }
        BarDataSet set2 = new BarDataSet(barEntries, "Daily Avg.");
        set2.setColor(Colors.blueSapphire);
        barData.addDataSet(set2);

        CombinedData data = new CombinedData();
        data.setData(lineData);
        data.setData(barData);
        iDailyResultsView.drawChart(data);
    }

    private ArrayList<Double> calculateTrendLinePoints(List<Daily> dailyList, int position) {
        List<Integer> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        for (int i = 0; i < dailyList.size(); i++) {
            x.add(i);
            if (position==0) {
                y.add(dailyList.get(i).getDailyAvg());
            } else if (position == 1) {
                y.add(dailyList.get(i).getDayWeight());
            } else if (position == 2) {
                y.add(dailyList.get(i).getNightWeight());
            }
        }
        double xAvg = calculateAvgOfIntList(x);
        double yAvg = calculateAvgOfDoubleList(y);

        List<Double> column1 = new ArrayList<>();
        List<Double> column2 = new ArrayList<>();
        List<Double> column3 = new ArrayList<>();
        List<Double> column4 = new ArrayList<>();

        for (Integer singleX : x) {
            Double difFromAvg = singleX.doubleValue() - xAvg;
            column1.add(difFromAvg);
        }

        for (Double singleY : y) {
            Double difFromAvg = singleY - yAvg;
            column2.add(difFromAvg);
        }

        for (int i = 0; i < column1.size(); i++) {
            column3.add(column1.get(i) * column2.get(i));
            column4.add(column1.get(i) * column1.get(i));
        }

        double trendSlope = calculateColumnSum(column3) / calculateColumnSum(column4);
        double b = yAvg - (trendSlope * xAvg);

        ArrayList<Double> trendLine = new ArrayList<>();
        for (int i = 0; i < x.size(); i++) {
            trendLine.add((trendSlope*x.get(i))+b);
        }

        return trendLine;
    }

    private double calculateColumnSum(List<Double> column) {
        Double sum = 0.0;
        if (!column.isEmpty()) {
            for (Double num : column) {
                sum += num;
            }
            return sum;
        }
        return sum;
    }

    private double calculateAvgOfIntList(List<Integer> list) {
        Integer sum = 0;
        if (!list.isEmpty()) {
            for (Integer num : list) {
                sum += num;
            }
            return sum.doubleValue() / list.size();
        }
        return sum;
    }

    private double calculateAvgOfDoubleList(List<Double> list) {
        Double sum = 0.0;
        if (!list.isEmpty()) {
            for (Double num : list) {
                sum += num;
            }
            return sum / list.size();
        }
        return sum;
    }

    public void checkIntentRood(boolean fromProfile){
        if (fromProfile) {
            iDailyResultsView.hideReports();
        }
    }

}
