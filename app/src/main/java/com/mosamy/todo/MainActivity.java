package com.mosamy.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;



public class MainActivity extends AppCompatActivity {
    private EditText et;
    private TextView tvCompleted;
    private RecyclerView r, rC;
    private TodoAdapter adapter, adapterC;
    private TodoViewModel todoViewModel;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        initViews();
        initRVs();
        initData();

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


    private void initViews() {
        r = findViewById(R.id.r);
        rC = findViewById(R.id.rComplete);
        iv = findViewById(R.id.imageView);
        tvCompleted = findViewById(R.id.textView2);
        et = findViewById(R.id.editTextTextPersonName);
    }

    private void initRVs() {
        adapter = new TodoAdapter((position, checked) -> {
            if (adapter.getItemCount() > position && position >= 0) {
                Todo todo = adapter.getCurrentList().get(position);
                todo.setAccomplished(checked);
                todoViewModel.update(todo);
            } else {
                adapterC.notifyItemRemoved(position);
            }
        }, (position, checked) -> {
            Todo todo = adapter.getCurrentList().get(position);
            todo.setStared(checked);
            todoViewModel.update(todo);
        });
        adapterC = new TodoAdapter((position, checked) -> {
            if (adapterC.getItemCount() > position && position >= 0) {
                Todo todo = adapterC.getTodo(position);
                todo.setAccomplished(checked);
                todoViewModel.update(todo);
            }
        }, (position, checked) -> {
            Todo todo = adapterC.getTodo(position);
            todo.setStared(checked);
            todoViewModel.update(todo);
        });
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);

        r.setLayoutManager(llm);
        rC.setLayoutManager(new LinearLayoutManager(this));
        r.setAdapter(adapter);
        rC.setAdapter(adapterC);
    }

    private void initData() {
        todoViewModel.getAllById().observe(this, todos -> adapter.submitList(todos));
        todoViewModel.getAllByIdComplete().observe(this , todos -> {
            if (!todos.isEmpty()) tvCompleted.setVisibility(View.VISIBLE);
            else tvCompleted.setVisibility(View.INVISIBLE);
            adapterC.submitList(todos);
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
                @SuppressLint("UseCompatLoadingForDrawables") AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                        .setTitle("Delete all Todos")
                        .setMessage("Are you sure you want to delete all your todos ?")
                        .setPositiveButton(R.string.yes, (dialogInterface, i) -> todoViewModel.deleteAll()).setNegativeButton(R.string.no,
                                (dialogInterface, i) -> {})
                        .setBackground(getDrawable(R.drawable.dark_actionbar)).create();
                dialog.show();
                break;
            case R.id.stared:
                startActivity(new Intent(MainActivity.this, StaredItemsActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        todoViewModel.getAllById().removeObservers(this);
    }
}