package com.mosamy.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText et;
    RecyclerView r, rC;
    TodoAdapter adapter, adapterC;
    private TodoViewModel todoViewModel;

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = findViewById(R.id.r);
        rC = findViewById(R.id.rComplete);
        iv = findViewById(R.id.imageView);
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        adapter = new TodoAdapter(MainActivity.this, (position, checked) -> {
            Todo todo = adapter.getCurrentList().get(position);
            todo.setAccomplished(checked);
            todoViewModel.update(todo);
        }, (position, checked) -> {
            Todo todo = adapter.getCurrentList().get(position);
            todo.setStared(checked);
            todoViewModel.update(todo);
        });
        adapterC = new TodoAdapter(MainActivity.this, (position, checked) -> {
            Todo todo = adapterC.getCurrentList().get(position);
            todo.setAccomplished(checked);
            todoViewModel.update(todo);
        }, (position, checked) -> {
            Todo todo = adapterC.getCurrentList().get(position);
            todo.setStared(checked);
            todoViewModel.update(todo);
        });
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);

        r.setLayoutManager(llm);
        rC.setLayoutManager(new LinearLayoutManager(this));
        r.setAdapter(adapter);
        rC.setAdapter(adapterC);


        todoViewModel.getAllById().observe(this, todos -> {
            List<Todo> todos1 = new ArrayList<>();
            List<Todo> todosC = new ArrayList<>();
            for (Todo t : todos) {
                if (t.isAccomplished()) {
                    todosC.add(0, t);
                } else {
                    todos1.add(0, t);
                }
            }
            adapter.submitList(todos1);
            adapterC.submitList(todosC);
        });
        et = findViewById(R.id.editTextTextPersonName);
        iv.setOnClickListener(view -> {
            if (!et.getText().toString().isEmpty()) {
                todoViewModel.insert(new Todo(et.getText().toString(), false, false));
                et.setText("");
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                todoViewModel.delete(adapter.getTodo(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(r);

        et.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                    i == KeyEvent.KEYCODE_ENTER && !et.getText().toString().trim().isEmpty()) {
                todoViewModel.insert(new Todo(et.getText().toString(), false, false));
                et.setText("");
                et.requestFocus();
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_all, menu);
        return true;

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                todoViewModel.deleteAll();
                break;
            case R.id.stared:
                startActivity(new Intent(MainActivity.this, StaredItemsActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}