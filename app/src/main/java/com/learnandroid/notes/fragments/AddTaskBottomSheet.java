package com.learnandroid.notes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.learnandroid.notes.R;

public class AddTaskBottomSheet extends BottomSheetDialogFragment {
    EditText add_task_text;
    Button cancel,ok;
    bottomSheetListener listener;

    public void setListener(bottomSheetListener listener){
        this.listener = listener;
    }
    public interface bottomSheetListener{
        public void setCheckBox(String text);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_task,container,false);
        add_task_text = view.findViewById(R.id.edit_add_task);
        add_task_text.setImeOptions(EditorInfo.IME_ACTION_DONE);
        cancel = view.findViewById(R.id.btn_cancel);
        ok = view.findViewById(R.id.btn_add_task);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_task_text.getText().length()>1) {
                    listener.setCheckBox(add_task_text.getText().toString());
                    dismiss();
                }else{
                    Toast.makeText(getContext(), "Please Enter Title", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (bottomSheetListener) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
