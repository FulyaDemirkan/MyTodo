package sheridan.demirkaf.mytodo.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import sheridan.demirkaf.mytodo.database.AppRepository;
import sheridan.demirkaf.mytodo.database.TodoEntity;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<TodoEntity>> mTodoData;
    private AppRepository mRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mTodoData = mRepository.mTodoData;
    }

    public void addSampleData() {
        mRepository.addSampleData();
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
