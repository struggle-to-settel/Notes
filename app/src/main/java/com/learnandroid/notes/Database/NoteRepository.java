package com.learnandroid.notes.Database;

import android.app.Application;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private DAO dao;
    private LiveData<List<NoteModel>> listLiveData;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.newInstance(application);
        this.dao = database.dao();
        this.listLiveData = dao.getAll();
    }


    public void insert(NoteModel noteModel){
        new insertAsync(dao).execute(noteModel);
    }
    public void delete(NoteModel noteModel){
        new deleteAsync(dao).execute(noteModel);
    }
    public void update(NoteModel noteModel){
        new updateAsync(dao).execute(noteModel);
    }
    public void deleteAll(){
        new deleteAllAsync(dao).execute();
    }

    public LiveData<List<NoteModel>> getAll(){
        return this.listLiveData;
    }

    public class insertAsync extends AsyncTask<NoteModel, Void, Void> {
        private DAO dao;

        insertAsync(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NoteModel... noteModels) {
            dao.insert(noteModels[0]);
            return null;
        }
    }
    public class updateAsync extends AsyncTask<NoteModel, Void, Void> {
        private DAO dao;

        updateAsync(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NoteModel... noteModels) {
            dao.update(noteModels[0]);
            return null;
        }
    }
    public class deleteAsync extends AsyncTask<NoteModel, Void, Void> {
        private DAO dao;

        deleteAsync(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NoteModel... noteModels) {
            dao.delete(noteModels[0]);
            return null;
        }
    }
    public class deleteAllAsync extends AsyncTask<Void, Void, Void> {
        private DAO dao;

        deleteAllAsync(DAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
}
