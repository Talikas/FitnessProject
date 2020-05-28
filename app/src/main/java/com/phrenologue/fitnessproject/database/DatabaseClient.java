package com.phrenologue.fitnessproject.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context context;
    private static DatabaseClient instance;
    private Database database;

    private DatabaseClient(Context context){
        this.context = context;
        database = Room.databaseBuilder(context, Database.class, "Fitness").build();
    }

    public static synchronized DatabaseClient getInstance(Context context){
        if (instance == null){
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public Database getDatabase(){
        return database;
    }
}
