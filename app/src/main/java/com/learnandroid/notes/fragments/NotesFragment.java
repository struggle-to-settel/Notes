package com.learnandroid.notes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.learnandroid.notes.AddNotesActivity;
import com.learnandroid.notes.Database.NoteModel;
import com.learnandroid.notes.R;
import com.learnandroid.notes.Database.ViewModel;
import com.learnandroid.notes.adapter.Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class NotesFragment extends Fragment implements Adapter.onItemClickListener {
    public static final int REQUEST_CODE_INSERT = 1;
    public static final int REQUEST_CODE_UPDATE = 3;
    public static final int RESPONSE_CODE_INSERT = 2;
    public static final int RESPONSE_CODE_UPDATE = 4;
    FloatingActionButton fab;
    ViewModel viewModel;
    Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    int span = 2;
    NoteModel deletedNote;
    SearchView searchView;
    String grid = "Grid View";
    String list = "List View";
    TextView emptyList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


                                // ON_CREATE_VIEW

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        setHasOptionsMenu(true);

        //Search View logic
        SearchView searchView = view.findViewById(R.id.search);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getNoteFilter().filter(newText);
                return false;
            }
        });

        // Floating action button logic
        fab = view.findViewById(R.id.f_button_notes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNotesActivity.class);
                startActivityForResult(intent,REQUEST_CODE_INSERT);

            }
        });

        //RecyclerView logic
        List<NoteModel> list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewNotes);
        adapter = new Adapter(getContext(),list);
        adapter.setOnItemClickListener(this::onItemClick);
        manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        emptyList = view.findViewById(R.id.empty_view);

        //Connectivity with RoomDatabase
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(ViewModel.class);

        viewModel.getAll().observe(getActivity(),new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(List<NoteModel> list) {
                adapter.changeList(list);
                if(list.isEmpty()){
                    emptyList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    emptyList.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deletedNote = adapter.getItemAt(position);
                viewModel.delete(adapter.getItemAt(position));
                showSnackBar();
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }

    private void showSnackBar(){
        Snackbar snackbar = Snackbar.make( (((Activity) getContext()).findViewById(android.R.id.content)), R.string.snack_bar_text,
                Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.snackBar));
        snackbar.setTextColor(getResources().getColor(R.color.white));
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        }).setActionTextColor(getResources().getColor(R.color.white))
        .setBackgroundTint(getResources().getColor(R.color.snackBar));
        snackbar.show();
    }

    private void undoDelete() {
        viewModel.insert(deletedNote);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESPONSE_CODE_INSERT){

            String title  = data.getStringExtra("title");
            String des = data.getStringExtra("des");
            NoteModel noteModel = new NoteModel(title,des);
            viewModel.insert(noteModel);
        }
        else if(resultCode==RESPONSE_CODE_UPDATE){

            String title = data.getStringExtra("title");
            String des = data.getStringExtra("des");
            int position = data.getIntExtra("position",0);
            NoteModel noteModel = adapter.getItemAt(position);
            noteModel.setTitle(title);
            noteModel.setNote(des);
            viewModel.update(noteModel);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), AddNotesActivity.class);
        String title = adapter.getItemAt(position).getTitle();
        String description = adapter.getItemAt(position).getNote();
        intent.putExtra("title_update",title);
        intent.putExtra("des_update",description);
        intent.putExtra("position",position);
        startActivityForResult(intent,REQUEST_CODE_UPDATE);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.note_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.view_type:
                if(grid.equalsIgnoreCase(item.getTitle().toString())){
                    span = 2;
                    RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(span,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    item.setTitle(list);
                }else{
                    span = 1;
                    RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(span,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    item.setTitle(grid);
                }
                break;
            case R.id.delete_all_note:
                viewModel.deleteAll();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }
}