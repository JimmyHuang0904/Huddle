package io.left.hellomesh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

import org.kocakosm.pitaya.io.Resource;


public class LoginActivity extends Activity {

    EditText username;

    // createGroup and joinGroup could be refactored later
    public void createGroup(View view)
    {
        username = (EditText)findViewById(R.id.username);

        Intent intent = new Intent(this, UsersListActivity.class);
        intent.putExtra("username", username.getText().toString());

        startActivity(intent);
    }

    public void joinGroup(View view)
    {
        username = (EditText)findViewById(R.id.username);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username.getText().toString());

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
