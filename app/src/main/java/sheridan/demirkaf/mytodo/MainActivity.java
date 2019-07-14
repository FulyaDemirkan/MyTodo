package sheridan.demirkaf.mytodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sheridan.demirkaf.mytodo.database.TodoEntity;
import sheridan.demirkaf.mytodo.utilities.Constants;
import sheridan.demirkaf.mytodo.ui.TodoAdapter;
import sheridan.demirkaf.mytodo.viewmodel.AboutFragment;
import sheridan.demirkaf.mytodo.viewmodel.ConfirmFragment;
import sheridan.demirkaf.mytodo.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements ConfirmFragment.ConfirmListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab)
    void fabClickHandler()
    {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }
    private List<TodoEntity> mTodoData = new ArrayList<>();
    private TodoAdapter mTodoAdapter;

    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel() {

        final Observer<List<TodoEntity>> todoObserver = new Observer<List<TodoEntity>>() {
            @Override
            public void onChanged(List<TodoEntity> todoEntities) {
                mTodoData.clear();
                mTodoData.addAll(todoEntities);

                if(mTodoAdapter == null)
                {
                    mTodoAdapter = new TodoAdapter(mTodoData, MainActivity.this);
                    mRecyclerView.setAdapter(mTodoAdapter);
                }
                else
                {
                    mTodoAdapter.notifyDataSetChanged();
                }
            }
        };

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.mTodoData.observe(this, todoObserver);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        }
        else if(id == R.id.action_delete_all)
        {
            ConfirmFragment confirmFragment
                    = ConfirmFragment.newInstance(Constants.DELETE_All_DIALOG, getString(R.string.delete_all_confirmation));
            confirmFragment.show(getSupportFragmentManager(), Constants.CONFIRM_DELETE_All);
            return true;
        }
        else if(id == R.id.action_about)
        {
            AboutFragment aboutFragment = AboutFragment.newInstance();
            aboutFragment.show(getSupportFragmentManager(), Constants.MAIN_ABOUT_FRAGMENT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        mMainViewModel.deleteAll();
    }

    private void addSampleData() {
        mMainViewModel.addSampleData();
    }

    @Override
    public void onConfirmed(int dialogID) {
        if(dialogID == Constants.DELETE_All_DIALOG)
        {
            deleteAll();
        }
    }
}
