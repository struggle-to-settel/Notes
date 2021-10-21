package com.learnandroid.notes.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    public void insert(TaskModel taskModel);

    @Delete
    public void delete(TaskModel taskModel);

    @Update
    public void update(TaskModel taskModel);

    @Query("delete from task_table")
    public void deleteAll();

    @Query("update task_table set isChecked=1 where id=:TaskId")
    public void setChecked(int TaskId);

    @Query("select * from task_table where isChecked=1")
    public LiveData<List<TaskModel>> getAllChecked();

    @Query("select * from task_table")
    public LiveData<List<TaskModel>> getAll();
}
