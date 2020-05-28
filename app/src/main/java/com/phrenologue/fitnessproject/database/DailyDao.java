package com.phrenologue.fitnessproject.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DailyDao {
    // Post daily weight
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void postDay(Daily daily);

    // Post nightly weight and daily avg.
    @Update
    void postNight(Daily daily);

    // Get all daily Avg.
    @Query("SELECT dailyAvg FROM Daily")
    List<Double> getDailyAvgs();

    // Get Today's Morning weight
    @Query("SELECT dayWeight FROM Daily ORDER BY dayNo DESC LIMIT 1")
    double getTodayWeight();

    //  Get previous morning weight
    @Query("SELECT dayWeight FROM Daily WHERE dayNo=:dayNo")
    double getPrevDayWeight(int dayNo);

    // Get previous night weight
    @Query("SELECT nightWeight FROM Daily WHERE dayNo=:dayNo")
    double getPrevNightWeight(int dayNo);

    // Get all days
    @Query("SELECT * FROM Daily")
    List<Daily> getAllDays();

    // Get previous daily Avg.
    @Query("SELECT dailyAvg FROM Daily WHERE dayNo=:dayNo")
    double getPrevDailyAvg(int dayNo);

    // Get previous day row
    @Query("SELECT * FROM Daily ORDER BY dayNo DESC LIMIT 1")
    Daily getPrevDay();

    // Get Today's row

    // Get previous day number.
    @Query("SELECT dayNo FROM Daily ORDER BY dayNo DESC LIMIT 1")
    int getPrevDayNo();

    @Delete
    void delDay(Daily daily);


}
