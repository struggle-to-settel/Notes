package com.learnandroid.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import static com.learnandroid.notes.fragments.NotesFragment.RESPONSE_CODE_UPDATE;
import static com.learnandroid.notes.fragments.NotesFragment.RESPONSE_CODE_INSERT;

public class AddNotesActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    EditText title;
    EditText description;
    int position = 0;
    MenuItem menuItem;
    boolean for_update = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        title = findViewById(R.id.title_add);
        title.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        description = findViewById(R.id.des_add);
        description.setImeOptions(EditorInfo.IME_ACTION_DONE);

        toolbar = findViewById(R.id.add_note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Add New Note");
        Intent intent = getIntent();
        if(intent.hasExtra("title_update")){
            for_update = true;
            title.setText(intent.getStringExtra("title_update"));
            description.setText(intent.getStringExtra("des_update"));
            position = intent.getIntExtra("position",0);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.add_note_ok){
                    String t = title.getText().toString();
                    if(t.length()<2){
                        Toast.makeText(AddNotesActivity.this, "Please Enter Title", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    String des = description.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("title",t);
                    intent.putExtra("des",des);
                    if(for_update){
                        intent.putExtra("position",position);
                        setResult(RESPONSE_CODE_UPDATE,intent);
                        for_update = false;
                    }else {
                        setResult(RESPONSE_CODE_INSERT, intent);
                    }
                    finish();
                }
                return false;
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()<5){
                        menuItem.setVisible(false);
                    }else{
                        menuItem.setVisible(true);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_activity_menu,menu);
        menuItem = menu.getItem(0);
        menuItem.setVisible(false);
        return true;
    }

}