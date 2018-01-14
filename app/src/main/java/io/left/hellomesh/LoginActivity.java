package io.left.hellomesh;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.kocakosm.pitaya.io.Resource;


public class LoginActivity extends Activity {

    private EditText username;
    private String groupName;

    // createGroup and joinGroup could be refactored later
    public void createGroup(View view) {
        username = (EditText) findViewById(R.id.username);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("group_name", groupName);

        startActivity(intent);
    }

    public void joinGroup(View view) {
        username = (EditText) findViewById(R.id.username);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username.getText().toString());

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button) findViewById(R.id.button1);
        //resultText = (TextView) findViewById(R.id.result);
        //groupName = (TextView) findViewById(R.id.groupName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(LoginActivity.this);
                final View mView = layoutInflaterAndroid.inflate(R.layout.prompts, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(LoginActivity.this, R.style.UserPrompt);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                groupName = userInputDialogEditText.getText().toString();

                                createGroup(mView);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });
    }
}
