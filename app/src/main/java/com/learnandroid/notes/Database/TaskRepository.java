package com.learnandroid.notes.Database;

import android.app.Application;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private TaskDAO taskDAO;
    private LiveData<List<TaskModel>> taskList;
    private LiveData<List<TaskModel>> taskCheckedList;

    public TaskRepository(Application application){
        NoteDatabase database = NoteDatabase.newInstance(application);
        taskDAO = database.taskDAO();
        taskList = taskDAO.getAll();
        taskCheckedList = taskDAO.getAllChecked();
    }

    public void insert(TaskModel taskModel){
        new insertAsync(taskDAO).execute(taskModel);
    }
    public void update(TaskModel taskModel){
        new updateAsync(taskDAO).execute(taskModel);
    }
    public void delete(TaskModel taskModel){
        new deleteAsync(taskDAO).execute(taskModel);
    }
    public void deleteAll(){
        new deleteAllAsync(taskDAO).execute();
    }
    public void setChecked(int id){
        new setCheckedAsync(taskDAO).execute(id);
    }
    public LiveData<List<TaskModel>> getAll(){
        return this.taskList;
    }
    public LiveData<List<TaskModel>> getAllChecked(){
        return this.taskCheckedList;
    }


    public class insertAsync extends AsyncTask<TaskModel,Void,Void>{

        private TaskDAO taskDAO;

        public insertAsync(TaskDAO taskDAO){
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(TaskModel... taskModels) {
            taskDAO.insert(taskModels[0]);
            return null;
        }
    }
    public class deleteAsync extends AsyncTask<TaskModel,Void,Void>{

        private TaskDAO taskDAO;

        public deleteAsync(TaskDAO taskDAO){
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(TaskModel... taskModels) {
            taskDAO.delete(taskModels[0]);
            return null;
        }
    }
    public class updateAsync extends AsyncTask<TaskModel,Void,Void>{

        private TaskDAO taskDAO;

        public updateAsync(TaskDAO taskDAO){
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(TaskModel... taskModels) {
            taskDAO.update(taskModels[0]);
            return null;
        }
    }
    public class deleteAllAsync extends AsyncTask<Void,Void,Void>{

        private TaskDAO taskDAO;

        public deleteAllAsync(TaskDAO taskDAO){
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDAO.deleteAll();
            return null;
        }
    }
    public class setCheckedAsync extends AsyncTask<Integer,Void,Void>{

        private TaskDAO taskDAO;

        public setCheckedAsync(TaskDAO taskDAO){
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            taskDAO.setChecked(integers[0]);
            return null;
        }
    }

}
