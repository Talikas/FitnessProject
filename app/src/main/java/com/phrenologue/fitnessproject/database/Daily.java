package com.phrenologue.fitnessproject.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Daily")
public class Daily {
    @PrimaryKey(autoGenerate = true)
    private
    int dayNo;

    @ColumnInfo
    private
    double dayWeight;

    @ColumnInfo
    private
    double nightWeight = 0.0;

    @ColumnInfo
    private
    double dailyAvg = 0.0;

    @ColumnInfo
    private
    int day;

    @ColumnInfo
    private
    int month;

    @ColumnInfo
    private
    int year;

    public int getDayNo() {
        return dayNo;
    }

    public void setDayNo(int dayNo) {
        this.dayNo = dayNo;
    }

    public double getDayWeight() {
        return dayWeight;
    }

    public void setDayWeight(double dayWeight) {
        this.dayWeight = dayWeight;
    }

    public double getNightWeight() {
        return nightWeight;
    }

    public void setNightWeight(double nightWeight) {
        this.nightWeight = nightWeight;
    }

    public double getDailyAvg() {
        return dailyAvg;
    }

    public void setDailyAvg(double dailyAvg) {
        this.dailyAvg = dailyAvg;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
