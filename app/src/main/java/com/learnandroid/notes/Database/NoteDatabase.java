package com.learnandroid.notes.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NoteModel.class,TaskModel.class},version = 2)
abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract DAO dao();
    public abstract TaskDAO taskDAO();

    public static synchronized NoteDatabase newInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "NoteDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
