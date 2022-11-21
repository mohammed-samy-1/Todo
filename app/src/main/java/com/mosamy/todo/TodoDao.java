package com.mosamy.todo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    String TODOS_TABLE = "todos_Table";
    String IS_ACCOMPLISHED = "isAccomplished";
    String IS_STARED = "isStared";
    String ID = "id";

    @Insert
    void insert(Todo todo);
    @Update
    void update(Todo todo);
    @Delete
    void delete(Todo todo);

    @Query("delete from " + TODOS_TABLE)
    void deleteAll();

    @Query("select * from " + TODOS_TABLE + " where " + IS_ACCOMPLISHED + " = 0 order by " + ID)
    LiveData<List<Todo>> getAllById();
    @Query("select * from " + TODOS_TABLE + " where " + IS_ACCOMPLISHED + " = 1  order by " + ID)
    LiveData<List<Todo>> getAllByIdComplete();
    @Query("select * from " + TODOS_TABLE + " where " + IS_STARED + " = 1  order by " + ID)
    LiveData<List<Todo>> getAllByIdStared();
}
