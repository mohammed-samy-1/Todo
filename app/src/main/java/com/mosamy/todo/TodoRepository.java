package com.mosamy.todo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoRepository {
    private TodoDao dao;
    private LiveData<List<Todo>> allTodos;

    public TodoRepository(Application application){
        TodoDatabase todoDatabase = TodoDatabase.getInstance(application);
        dao = todoDatabase.todoDao();
        allTodos = dao.getAllById();

    }


    void insert(Todo todo){
        new insertTodoAsyncTask(dao).execute(todo);

    }
    void update(Todo todo){
        new updateTodoAsyncTask(dao).execute(todo);
    }
    void delete(Todo todo){
        new deleteTodoAsyncTask(dao).execute(todo);
    }
    void deleteAll(){
       new deleteAllTodoAsyncTask(dao).execute();
    }
    LiveData<List<Todo>> getAllById(){
        return allTodos;
    }
    private static class insertTodoAsyncTask extends AsyncTask<Todo, Void,Void>{

        private TodoDao todoDao ;

        public insertTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.insert(todos[0]);
            return null;
        }
    }

    private static class updateTodoAsyncTask extends AsyncTask<Todo, Void,Void>{

        private TodoDao todoDao ;

        public updateTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.update(todos[0]);
            return null;
        }
    }

    private static class deleteTodoAsyncTask extends AsyncTask<Todo, Void,Void>{

        private TodoDao todoDao ;

        public deleteTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.delete(todos[0]);
            return null;
        }
    }

    private static class deleteAllTodoAsyncTask extends AsyncTask<Void, Void,Void>{

        private TodoDao todoDao ;

        public deleteAllTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            todoDao.deleteAll();
            return null;
        }
    }
}
