package com.learnandroid.notes.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.learnandroid.notes.Database.NoteModel;
import com.learnandroid.notes.Database.NoteRepository;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<NoteModel>> listLiveData;


    public ViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        this.listLiveData = repository.getAll();
    }

    public void insert(NoteModel noteModel){
        repository.insert(noteModel);
    }
    public void update(NoteModel noteModel){
        repository.update(noteModel);
    }
    public void delete(NoteModel noteModel){
        repository.delete(noteModel);
    }
    public void deleteAll(){
        repository.deleteAll();
    }
    public LiveData<List<NoteModel>> getAll(){
        return repository.getAll();
    }
}
