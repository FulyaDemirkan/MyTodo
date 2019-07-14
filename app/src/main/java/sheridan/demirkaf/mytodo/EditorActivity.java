package sheridan.demirkaf.mytodo;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import sheridan.demirkaf.mytodo.database.TodoEntity;
import sheridan.demirkaf.mytodo.utilities.Constants;
import sheridan.demirkaf.mytodo.viewmodel.AboutFragment;
import sheridan.demirkaf.mytodo.viewmodel.ConfirmFragment;
import sheridan.demirkaf.mytodo.viewmodel.DatePickerFragment;
import sheridan.demirkaf.mytodo.viewmodel.EditorViewModel;

public class EditorActivity extends AppCompatActivity implements DatePickerFragment.DateSetListener, ConfirmFragment.ConfirmListener{

    @BindView(R.id.txt_title)
    TextView mTextTitle;

    @BindView(R.id.txt_description)
    TextView mTextDescription;

    @BindView(R.id.txt_date)
    TextView mTextDate;

    Date mDate;
    ImageButton mEditDateButton;

    private EditorViewModel mEditorViewModel;
    private boolean mNewTodo, mEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndReturn();
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if(savedInstanceState != null)
        {
            mEditing = savedInstanceState.getBoolean(Constants.EDITING_KEY);
            mDate = (Date) savedInstanceState.getSerializable(Constants.DATE_KEY);
        }
        else
        {
            mDate = new Date();
        }

        mTextDate = findViewById(R.id.txt_date);
        mTextDate.setText(DateFormat.getLongDateFormat(this).format(mDate));

        mEditDateButton = findViewById(R.id.edit_date_button);
        mEditDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = DatePickerFragment.getInstance(mDate);
                fragment.show(getSupportFragmentManager(), Constants.DATE_PICKER_FRAGMENT);
            }
        });

        initViewModel();
    }

    private void initViewModel() {
        mEditorViewModel = ViewModelProviders.of(this).get(EditorViewModel.class);

        mEditorViewModel.mLiveTodo.observe(this, new Observer<TodoEntity>() {
            @Override
            public void onChanged(TodoEntity todoEntity) {
                if(todoEntity != null && !mEditing)
                {
                    mTextTitle.setText(todoEntity.getTitle());
                    mTextDescription.setText(todoEntity.getDescription());
                    mTextDate.setText(DateFormat.getLongDateFormat(EditorActivity.this).format(todoEntity.getDueDate()));
                    mDate = todoEntity.getDueDate();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null)
        {
            setTitle(R.string.new_todo);
            mNewTodo = true;
        }
        else
        {
            setTitle(R.string.edit_todo);
            int todoId = extras.getInt(Constants.TODO_ID_KEY);
            mEditorViewModel.loadData(todoId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(!mNewTodo)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_delete)
        {
            ConfirmFragment confirmFragment
                    = ConfirmFragment.newInstance(Constants.DELETE_ONE_DIALOG, getString(R.string.delete_one_confirmation));
            confirmFragment.show(getSupportFragmentManager(), Constants.CONFIRM_DELETE_ONE);
            return true;
        }
        else if(item.getItemId() == R.id.action_about)
        {
            AboutFragment aboutFragment = AboutFragment.newInstance();
            aboutFragment.show(getSupportFragmentManager(), Constants.DETAILS_ABOUT_FRAGMENT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndReturn() {
        mEditorViewModel.saveTodo(mTextTitle.getText().toString(), mTextDescription.getText().toString(), mDate);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(Constants.EDITING_KEY, true);
        outState.putSerializable(Constants.DATE_KEY, mDate);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(year, month, day, hour, minute);
        mDate = calendar.getTime();
        mTextDate.setText(DateFormat.getLongDateFormat(this).format(mDate));
    }

    @Override
    public void onConfirmed(int dialogID) {
        if(dialogID == Constants.DELETE_ONE_DIALOG)
        {
            mEditorViewModel.deleteTodo();
            finish();
        }
    }
}
