package sheridan.demirkaf.mytodo;

import android.content.Context;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import sheridan.demirkaf.mytodo.database.AppDatabase;
import sheridan.demirkaf.mytodo.database.TodoDao;
import sheridan.demirkaf.mytodo.database.TodoEntity;
import sheridan.demirkaf.mytodo.utilities.SampleData;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    public static final String TAG = "Junit";
    private AppDatabase mDb;
    private TodoDao mDao;

    @Before
    public void createDb()
    {
        Context context = InstrumentationRegistry.getContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();

        mDao = mDb.todoDao();

        Log.i(TAG, "createDb");
    }

    @After
    public void closeDb()
    {
        mDb.close();
        Log.i(TAG, "closeDb");
    }

    @Test
    public void createAndRetrieveTodos()
    {
        mDao.insertAll(SampleData.getTodoList());
        int count = mDao.getCount();

        Log.i(TAG, "createAndRetrieveTodos: count = " + count);

        assertEquals(SampleData.getTodoList().size(), count);
    }

    @Test
    public void compareStrings()
    {
        mDao.insertAll(SampleData.getTodoList());
        TodoEntity original = SampleData.getTodoList().get(0);
        TodoEntity fromDb = mDao.getTodoById(1);
        assertEquals(original.getTitle(), fromDb.getTitle());
        assertEquals(1, fromDb.getId());
    }
}
