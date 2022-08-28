package com.mosamy.todo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos_Table")
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String content;
    private boolean isAccomplished;
    private boolean isStared;

    public Todo( String content, boolean isAccomplished, boolean isStared) {
        this.content = content;
        this.isAccomplished = isAccomplished;
        this.isStared = isStared;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAccomplished() {
        return isAccomplished;
    }

    public void setAccomplished(boolean accomplished) {
        isAccomplished = accomplished;
    }

    public boolean isStared() {
        return isStared;
    }

    public void setStared(boolean stared) {
        isStared = stared;
    }
}
