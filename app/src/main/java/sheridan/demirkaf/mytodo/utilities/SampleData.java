package sheridan.demirkaf.mytodo.utilities;

import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import sheridan.demirkaf.mytodo.database.TodoEntity;

public class SampleData {

    private static Date getDate(int diff)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, diff);
        return cal.getTime();
    }

    public static List<TodoEntity> getTodoList()
    {
        List<TodoEntity> todoList = new ArrayList<>();
        todoList.add(new TodoEntity("Finish Android Assignment","-ask for professor feedback\n-import each constant or use Constants. ?", getDate(0)));
        todoList.add(new TodoEntity("Implement next features of the Capstone", "-Facebook & Twitter login\n-QR Reader\n-How to generate QR Code?\n##Also, check .validators of Firestore", getDate(0)));
        todoList.add(new TodoEntity("Go to RBC Branch", "Need to switch account to employee account.",getDate(0)));

        return todoList;
    }
}
