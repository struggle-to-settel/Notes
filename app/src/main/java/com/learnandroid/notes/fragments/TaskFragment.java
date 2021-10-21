package com.learnandroid.notes.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.learnandroid.notes.Database.TaskModel;
import com.learnandroid.notes.Database.TaskViewModel;
import com.learnandroid.notes.R;
import com.learnandroid.notes.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment implements AddTaskBottomSheet.bottomSheetListener, TaskAdapter.OnItemClickListener {
    private static final String TAG = "TaskFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TaskViewModel viewModel;
    TaskAdapter taskAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView,recyclerViewCompleted;
    List<TaskModel> list = new ArrayList<>();
    TaskModel deletedListItem;
    TextView empty_text,todo,completed;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task, container, false);
        setHasOptionsMenu(true);


        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskBottomSheet taskBottomSheet = new AddTaskBottomSheet();
                taskBottomSheet.setListener(TaskFragment.this::setCheckBox);
                taskBottomSheet.show(getChildFragmentManager(),"addTaskBottomSheet");
            }
        });

        recyclerView = view.findViewById(R.id.task_recycler);
        taskAdapter = new TaskAdapter(getContext(),list);
        taskAdapter.setOnItemClickListener(this::itemClickListener);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(taskAdapter);

        viewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication()))
                .get(TaskViewModel.class);

        empty_text = view.findViewById(R.id.empty_view_task);
        todo = view.findViewById(R.id.todo);
        viewModel.getAll().observe(getActivity(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> taskModels) {
                taskAdapter.setList(taskModels);
                if(taskModels.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    todo.setVisibility(View.GONE);
                    empty_text.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    todo.setVisibility(View.VISIBLE);
                    empty_text.setVisibility(View.GONE);
                }
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deletedListItem = taskAdapter.getItemAt(position);
                viewModel.delete(taskAdapter.getItemAt(position));
                showSnackBar();
            }
        }).attachToRecyclerView(recyclerView);


        //Search View
        SearchView searchView = view.findViewById(R.id.search_task);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                taskAdapter.getTaskFilter().filter(newText);
                return false;
            }
        });

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
        viewModel.insert(deletedListItem);
    }
    @Override
    public void setCheckBox(String text) {
        TaskModel task = new TaskModel(text,false);
        viewModel.insert(task);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.task_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_task) {
            viewModel.deleteAll();
        }

        return true;
    }
    @Override
    public void itemClickListener(int position) {
        TaskModel taskModel = taskAdapter.getItemAt(position);
        Log.d(TAG, "itemClickListener: Checked: "+taskModel.isChecked());
        if(taskModel.isChecked()){
            taskModel.setChecked(false);
        }else{
            taskModel.setChecked(true);
        }
        viewModel.update(taskModel);
    }
}