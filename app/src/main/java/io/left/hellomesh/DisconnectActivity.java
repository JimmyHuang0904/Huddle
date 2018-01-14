package io.left.hellomesh;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class DisconnectActivity extends AppCompatActivity {

    Uri notification;
    Ringtone r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnect);

        String message = getIntent().getStringExtra("message");

        final TextView textViewToChange = (TextView) findViewById(R.id.textViewDisconnect);
        textViewToChange.setText(message);

        startAlarm();
    }

    public void onDismiss(View view){
        r.stop();
        finish();
    }

    private void startAlarm() {
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        // Alarm sound
        r.play();
        shakeItBaby();
    }

    // Vibrate for 1000 milliseconds
    private void shakeItBaby() {
        int milliSeconds = 1000;
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(milliSeconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(milliSeconds);
        }
    }
}
