package com.mosamy.todo;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class TodoAdapter extends ListAdapter<Todo, TodoAdapter.TodoHolder> {
    DoneCheckedListener doneCheckedListener;
    StaredCheckedListener staredCheckedListener;


    public interface DoneCheckedListener {
        void onChecked(int position, boolean checked);
    }

    public interface StaredCheckedListener {
        void onChecked(int position, boolean checked);
    }
    public TodoAdapter( DoneCheckedListener doneCheckedListener, StaredCheckedListener staredCheckedListener) {
        super(DIFF_CALLBACK);
        this.doneCheckedListener = doneCheckedListener;
        this.staredCheckedListener = staredCheckedListener;
    }

    private static final DiffUtil.ItemCallback<Todo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Todo>() {
                @Override
                public boolean areItemsTheSame(@NonNull @NotNull Todo oldItem, @NonNull @NotNull Todo newItem) {
                    return oldItem.getId() == oldItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull @NotNull Todo oldItem, @NonNull @NotNull Todo newItem) {
                    return  oldItem.getContent().equals(newItem.getContent())
                            && oldItem.isStared() == newItem.isStared()
                            && oldItem.isAccomplished() == newItem.isAccomplished();
                }
            };


    public Todo getTodo(int position) {
        return getItem(position);
    }


    @NonNull
    @NotNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new TodoHolder(v , doneCheckedListener, staredCheckedListener);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull TodoHolder holder, int position) {
        holder.title.setText(getItem(position).getContent());
        holder.stared.setChecked(getItem(position).isStared());
        if (getItem(position).isAccomplished()) {
            SpannableString ss = new SpannableString(getItem(position).getContent());
            StrikethroughSpan sts = new StrikethroughSpan();
            ss.setSpan(sts,
                    0,
                    getItem(position).getContent().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.title.setText(ss);
        }
        try {
            holder.done.setChecked(getItem(position).isAccomplished());
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.stared.setOnClickListener(view -> staredCheckedListener.onChecked(position, holder.stared.isChecked()));

    }

    protected static class TodoHolder extends RecyclerView.ViewHolder {
        CheckBox done, stared;
        TextView title;
        CardView cv;

        public TodoHolder(@NonNull @NotNull View itemView
                , DoneCheckedListener doneCheckedListener
                , StaredCheckedListener staredCheckedListener
        ) {
            super(itemView);
            done = itemView.findViewById(R.id.c2);
            stared = itemView.findViewById(R.id.c1);
            title = itemView.findViewById(R.id.tv);
            cv = itemView.findViewById(R.id.cv);
            cv.setOnClickListener(view -> doneCheckedListener.onChecked(getAdapterPosition(), !done.isChecked()) );
            done.setOnClickListener(view -> doneCheckedListener.onChecked(getAdapterPosition(), done.isChecked()) );
            stared.setOnClickListener(view -> staredCheckedListener.onChecked(getAdapterPosition(), stared.isChecked()));
        }

    }
}
