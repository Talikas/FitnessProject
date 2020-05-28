package com.phrenologue.fitnessproject.database;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Daily.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract DailyDao dailyDao();
}
