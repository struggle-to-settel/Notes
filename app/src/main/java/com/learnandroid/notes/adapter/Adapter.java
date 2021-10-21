package com.learnandroid.notes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learnandroid.notes.Database.NoteModel;
import com.learnandroid.notes.Database.TaskModel;
import com.learnandroid.notes.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<NoteModel> list;
    Context context;
    List<NoteModel> searchList = new ArrayList<>();
    onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        public void onItemClick(int position);
    }

    public Adapter(Context context, List<NoteModel> list) {
        this.context = context;
        this.list = list;
    }

    public void changeList(List<NoteModel> list) {
        this.list.clear();
        searchList.clear();
        this.list = list;
        searchList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteModel current = list.get(position);
        holder.title.setText(current.getTitle());
        holder.description.setText(current.getNote());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public NoteModel getItemAt(int position) {
        return list.get(position);
    }

    public Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NoteModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(searchList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (NoteModel noteModel : searchList) {
                    if (noteModel.getTitle()
                            .toLowerCase()
                            .contains(filterPattern)){
                        filteredList.add(noteModel);
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

    public Filter getNoteFilter(){
        return noteFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.card_note);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

}
