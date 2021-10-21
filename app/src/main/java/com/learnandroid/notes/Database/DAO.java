package com.learnandroid.notes.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    public void insert(NoteModel note);

    @Delete
    public void delete(NoteModel note);

    @Update
    public void update(NoteModel note);

    @Query("select * from notes")
    public LiveData<List<NoteModel>> getAll();

    @Query("delete from notes")
    public void deleteAll();
}
