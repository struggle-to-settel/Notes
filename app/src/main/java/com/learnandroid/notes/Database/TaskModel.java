package com.learnandroid.notes.Database;

import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class TaskModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;
    private boolean isChecked;

    public TaskModel(String text,boolean isChecked){
        this.text = text;
        this.isChecked = isChecked;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
