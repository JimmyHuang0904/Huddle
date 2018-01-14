package io.left.hellomesh;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ConnectedActivity extends FragmentActivity{

    MyGroupFragment myGroupFragment;
    ConnectedHandleFragment connectedHandleFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idlinglist);

        connectedHandleFragment = new ConnectedHandleFragment();
        myGroupFragment = new MyGroupFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.idlinglist, connectedHandleFragment).commit();
    }
}
