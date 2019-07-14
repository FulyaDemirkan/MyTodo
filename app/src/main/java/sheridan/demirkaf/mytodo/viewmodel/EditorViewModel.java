package sheridan.demirkaf.mytodo.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import sheridan.demirkaf.mytodo.database.AppRepository;
import sheridan.demirkaf.mytodo.database.TodoEntity;

public class EditorViewModel extends AndroidViewModel {

    public MutableLiveData<TodoEntity> mLiveTodo = new MutableLiveData<>();

    private AppRepository mRepository;
    private Executor executer = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(int todoId) {
        executer.execute(new Runnable() {
            @Override
            public void run() {
                TodoEntity todo = mRepository.getTodoById(todoId);
                mLiveTodo.postValue(todo);
            }
        });
    }

    public void saveTodo(String title, String description, Date dueDate) {
        TodoEntity todo = mLiveTodo.getValue();

        if(todo == null)
        {
            if(TextUtils.isEmpty(title.trim()) || TextUtils.isEmpty(description.trim()))
            {
                return;
            }
            else
            {
                todo = new TodoEntity(title.trim(), description.trim(), dueDate);
            }
        }
        else
        {
            todo.setTitle(title);
            todo.setDescription(description);
            todo.setDueDate(dueDate);
        }
        mRepository.insertTodo(todo);
    }

    public void deleteTodo() {
        mRepository.deleteTodo(mLiveTodo.getValue());
    }
}
