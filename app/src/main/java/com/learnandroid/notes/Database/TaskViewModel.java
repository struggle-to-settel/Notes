package com.learnandroid.notes.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public TaskRepository repository;
    public LiveData<List<TaskModel>> taskList;
    private LiveData<List<TaskModel>> taskCheckedList;
    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        taskList = repository.getAll();
        taskCheckedList = repository.getAllChecked();
    }

    public void insert(TaskModel taskModel){
        repository.insert(taskModel);
    }
    public void delete(TaskModel taskModel){
        repository.delete(taskModel);
    }
    public void update(TaskModel taskModel){
        repository.update(taskModel);
    }
    public void deleteAll(){
        repository.deleteAll();
    }
    public void setChecked(int id){
        repository.setChecked(id);
    }
    public LiveData<List<TaskModel>> getAllChecked(){
        return this.taskCheckedList;
    }
    public LiveData<List<TaskModel>> getAll(){
        return repository.getAll();
    }
}
