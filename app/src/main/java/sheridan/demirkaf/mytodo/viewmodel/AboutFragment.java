package sheridan.demirkaf.mytodo.viewmodel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import sheridan.demirkaf.mytodo.R;

public class AboutFragment extends DialogFragment {
    public AboutFragment() {
    }

    public static AboutFragment newInstance(){
        return new AboutFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // create a new AlertDialog Builder
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.my_todo_project));
        builder.setMessage(getString(R.string.author));

        builder.setPositiveButton(android.R.string.ok, null);

        return builder.create();
    }
}
