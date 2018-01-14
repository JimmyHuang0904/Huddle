package io.left.hellomesh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

import org.kocakosm.pitaya.io.Resource;


public class LoginActivity extends Activity {

    EditText username;

    public void signIn(View view)
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
