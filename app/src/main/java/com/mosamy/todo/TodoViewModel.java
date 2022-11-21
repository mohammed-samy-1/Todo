package com.mosamy.todo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private final TodoRepository repository;

    public TodoViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new TodoRepository(application);
    }

    public void insert(Todo todo){
        repository.insert(todo);
    }
    public void update(Todo todo){
        repository.update(todo);
    }
    public void delete(Todo todo){
        repository.delete(todo);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public LiveData<List<Todo>> getAllById(){
        return repository.getAllById();
    }
    public LiveData<List<Todo>> getAllByIdComplete(){
        return repository.getAllTodosComplete();
    }
    public LiveData<List<Todo>>getAllTodosStared(){
        return repository.getAllTodosStared();
    }

}
