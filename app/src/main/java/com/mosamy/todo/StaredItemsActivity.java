package com.mosamy.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class StaredItemsActivity extends AppCompatActivity {

    private TodoViewModel todoViewModel;
    private TodoAdapter adapter ;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stared_items);
        recyclerView = findViewById(R.id.rStared);
        getSupportActionBar().setTitle("Stared");
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        adapter = new TodoAdapter(this, new TodoAdapter.DoneCheckedListener() {
            @Override
            public void onChecked(int position, boolean checked) {
                Todo todo = adapter.getCurrentList().get(position);
                todo.setAccomplished(checked);
                todoViewModel.update(todo);
            }
        }, new TodoAdapter.StaredCheckedListener() {
            @Override
            public void onChecked(int position, boolean checked) {
                Todo todo = adapter.getCurrentList().get(position);
                todo.setStared(checked);
                todoViewModel.update(todo);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        todoViewModel.getAllById().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                List<Todo> todos1 = new ArrayList<>();
                for (Todo t : todos){
                    if (t.isStared()){
                        todos1.add(0,t);
                    }
                }
                adapter.submitList(todos1);

            }
        });
    }
}