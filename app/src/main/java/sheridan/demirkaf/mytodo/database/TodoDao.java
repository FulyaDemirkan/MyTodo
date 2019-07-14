package sheridan.demirkaf.mytodo.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodo(TodoEntity todoEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TodoEntity> todoEntity);

    @Delete
    void deleteTodo(TodoEntity todoEntity);

    @Query("SELECT * FROM todos WHERE id = :id")
    TodoEntity getTodoById(int id);

    @Query("SELECT * FROM todos ORDER BY dueDate DESC")
    LiveData<List<TodoEntity>> getAll();

    @Query("DELETE FROM todos")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM todos")
    int getCount();
}
