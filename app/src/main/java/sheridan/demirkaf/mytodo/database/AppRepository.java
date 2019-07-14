package sheridan.demirkaf.mytodo.database;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import sheridan.demirkaf.mytodo.utilities.SampleData;

public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<TodoEntity>> mTodoData;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if(ourInstance == null)
        {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mTodoData = getAllData();
    }

    private LiveData<List<TodoEntity>> getAllData() {
        return mDb.todoDao().getAll();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.todoDao().insertAll(SampleData.getTodoList());
            }
        });
    }

    public void deleteAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.todoDao().deleteAll();
            }
        });
    }

    public TodoEntity getTodoById(int todoId) {
        return mDb.todoDao().getTodoById(todoId);
    }

    public void insertTodo(TodoEntity todo) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.todoDao().insertTodo(todo);
            }
        });
    }

    public void deleteTodo(TodoEntity todo) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.todoDao().deleteTodo(todo);
            }
        });
    }
}
