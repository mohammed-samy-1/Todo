package com.mosamy.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StaredItemsActivity extends AppCompatActivity {

    private TodoViewModel todoViewModel;
    private TodoAdapter adapter ;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stared_items);
        recyclerView = findViewById(R.id.rStared);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.stared);
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        adapter = new TodoAdapter((position, checked) -> {
            Todo todo = adapter.getCurrentList().get(position);
            todo.setAccomplished(checked);
            todoViewModel.update(todo);
        }, (position, checked) -> {
            Todo todo = adapter.getCurrentList().get(position);
            todo.setStared(checked);
            todoViewModel.update(todo);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        todoViewModel.getAllById().observe(this, todos -> {
            List<Todo> todos1 = new ArrayList<>();
            for (Todo t : todos){
                if (t.isStared()){
                    todos1.add(0,t);
                }
            }
            adapter.submitList(todos1);

        });
    }
}