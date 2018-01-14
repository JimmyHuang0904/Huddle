package io.left.hellomesh;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


/**
 * Activity to list all users and groups. User attempts to join one group upon clicking a user or group.
 */
public class UsersListActivity extends ListActivity {

    private ListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ListAdapter(this);

        // Populate lists with groups and names here
        for (int i = 1; i < 30; i++) {
            mAdapter.addItem("Row Item #" + i);
            if (i % 4 == 0) {
                mAdapter.addSectionHeaderItem("Section #" + i);
            }
        }
        setListAdapter(mAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //String item = mAdapter.getItem(position);

        // Check if string is a user or group name.

        // Launch join process

        //Toast.makeText(this, "Clicked row " + position, Toast.LENGTH_SHORT).show();

        final ProgressDialog progressDialog = new ProgressDialog(UsersListActivity.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Joining... Please wait");
        progressDialog.show();

        // Perform joining logic here
    }
}
