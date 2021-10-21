package com.learnandroid.notes.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learnandroid.notes.R;
import com.learnandroid.notes.Database.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private static final String TAG = "TaskAdapter";
    Context context;
    List<TaskModel> list;
    private List<TaskModel> searchList = new ArrayList<>();
    public OnItemClickListener listener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void itemClickListener(int position);
    }

    public TaskAdapter(Context context, List<TaskModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<TaskModel> list) {
        this.list.clear();
        searchList.clear();
        this.list.addAll(list);
        this.searchList.addAll(list);
        notifyDataSetChanged();
    }

    public TaskModel getItemAt(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskModel task = list.get(position);
        holder.checkBox.setText(task.getText());
        holder.checkBox.setChecked(task.isChecked());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<TaskModel> getList() {
        return this.list;
    }

    public Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TaskModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(searchList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TaskModel taskModel : searchList) {
                    if (taskModel.getText().toLowerCase().contains(filterPattern)) {
                        filteredList.add(taskModel);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getTaskFilter() {
        return taskFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Position: "+getAdapterPosition());
                        listener.itemClickListener(getAdapterPosition());
                }
            });
        }

    }
}
