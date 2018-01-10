package org.scorelab.soundcom;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.camera2.params.BlackLevelPattern;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import android.graphics.Color;

import com.john.waveview.WaveView;

public class MainActivity extends AppCompatActivity {
    private ImageView startBtn;
    TextView textView4;
    private int seconds = 0;
    private boolean startRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            startRun = savedInstanceState.getBoolean("startRun");
        }

        startBtn = (ImageView) findViewById(R.id.button1);
        startBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent svc = new Intent(getBaseContext(), RecordingService.class);
                startService(svc);

                Timer();
                startRun = true;


                //  finish(); //because I don't want to close the UI after service started
            }
        });


        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        TextView soundcom_text;
        soundcom_text = (TextView) findViewById(R.id.textView4);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.alpha_disappear);
        soundcom_text.startAnimation(myanim);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        myanim = AnimationUtils.loadAnimation(this, R.anim.alpha_appear);
        soundcom_text.startAnimation(myanim);

        WaveView parent;
        parent = (WaveView) findViewById(R.id.wave_view) ;
        ColorDrawable[] color = {new ColorDrawable(Color.rgb(66,66,66)), new ColorDrawable(Color.LTGRAY)};
        TransitionDrawable trans = new TransitionDrawable(color);
        parent.setBackground(trans);
        trans.startTransition(1500);

    }

    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putInt("seconds", seconds);
        saveInstanceState.putBoolean("startRun", startRun);
    }

    private void Timer() {
        final TextView timeView = (TextView) findViewById(R.id.textView4);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format("%d:%02d:%02d", hours, minutes, secs);

                timeView.setText(time);

                if (startRun) {
                    seconds++;
                }

                handler.postDelayed(this, 100);
            }
        });

    }
}
