package com.dberserker.execisehelper;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int DEFAULT_REPETITION_REST_TIME = 30;
    private static final int DEFAULT_SERIE_REST_TIME = 60;
    private Handler handler;
    private Runnable currentTask;
    private TextView timerTextView;
    private TextView startTimeTextView;
    private TextView lastExcerciseTimeTextView;
    private SoundPool soundPool;
    private DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private boolean taskRunning = false;
    private boolean started = false;
    private int currentTimerValue;
    private int currentRepetitions = 0;
    private int currentSeries = 0;
    private int idNotificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button buttonSerie = findViewById(R.id.buttonRepetition);
        final Button buttonExercise = findViewById(R.id.buttonSerie);
        final Button buttonReset = findViewById(R.id.buttonReset);
        buttonSerie.setOnClickListener( eventButtonSerie());
        buttonExercise.setOnClickListener( eventButtonExercise() );
        buttonReset.setOnClickListener( eventButtonReset() );
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        idNotificacion = soundPool.load(getApplicationContext(), R.raw.notification, 0);
        handler = new Handler();
        timerTextView = findViewById(R.id.textViewTimer);
        startTimeTextView = findViewById(R.id.textViewStartTime);
        lastExcerciseTimeTextView = findViewById(R.id.textViewLastExercise);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app, menu);
        return true;
    }

    private View.OnClickListener eventButtonSerie(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( !taskRunning) {
                    if(!started) {
                        started = true;
                        startTimeTextView.setText(timeFormatter.format(new Date()));
                        lastExcerciseTimeTextView.setText(timeFormatter.format(new Date()));
                    }
                    lastExcerciseTimeTextView.setText(timeFormatter.format(new Date()));
                    taskRunning = true;
                    currentTask = taskSerie();
                    currentTimerValue = DEFAULT_REPETITION_REST_TIME;
                    timerTextView.setText(timeToString(currentTimerValue));
                    handler.removeCallbacks(currentTask);
                    handler.postDelayed(currentTask, 1000);
                }
            }
        };
    }

    private View.OnClickListener eventButtonExercise(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( !taskRunning) {
                    lastExcerciseTimeTextView.setText(timeFormatter.format(new Date()));
                    currentRepetitions++;
                    TextView repetitionTextView = findViewById(R.id.textViewRepetitions);
                    repetitionTextView.setText( String.valueOf(currentRepetitions) );
                    taskRunning = true;
                    currentTask = taskExercise();
                    currentTimerValue = DEFAULT_SERIE_REST_TIME;
                    timerTextView.setText(timeToString(currentTimerValue));
                    handler.removeCallbacks(currentTask);
                    handler.postDelayed(currentTask, 1000);
                }
            }
        };
    }

    private View.OnClickListener eventButtonReset(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( !taskRunning) {
                    TextView repetitionTextView = findViewById(R.id.textViewRepetitions);
                    TextView serieTextView = findViewById(R.id.textViewSeries);
                    repetitionTextView.setText("0");
                    serieTextView.setText("0");
                    currentRepetitions = 0;
                    currentSeries = 0;
                    started = false;
                    startTimeTextView.setText("00:00");
                    lastExcerciseTimeTextView.setText("00:00");
                }
            }
        };
    }

    private Runnable taskSerie(){
        return new Runnable() {
            @Override
            public void run() {
                currentTimerValue --;
                timerTextView.setText( timeToString(currentTimerValue) );
                if(currentTimerValue == 0){
                    currentRepetitions++;
                    taskRunning = false;
                    timerTextView.setText( timeToString(currentTimerValue) );
                    TextView repetitionTextView = findViewById(R.id.textViewRepetitions);
                    soundPool.play(idNotificacion, 1, 1, 1, 0, 1);
                    repetitionTextView.setText( String.valueOf(currentRepetitions) );
                    handler.removeCallbacks(this);
                }else {
                    handler.postDelayed(this, 1000);
                }
            }
        };
    }

    private Runnable taskExercise(){
        return new Runnable() {
            @Override
            public void run() {
                currentTimerValue --;
                timerTextView.setText( timeToString(currentTimerValue) );
                if(currentTimerValue == 0){
                    timerTextView.setText( timeToString(currentTimerValue) );
                    currentRepetitions = 0;
                    currentSeries++;
                    taskRunning = false;
                    TextView repetitionTextView = findViewById(R.id.textViewRepetitions);
                    TextView serieTextView = findViewById(R.id.textViewSeries);
                    repetitionTextView.setText( String.valueOf(currentRepetitions) );
                    serieTextView.setText( String.valueOf(currentSeries) );
                    soundPool.play(idNotificacion, 1, 1, 1, 0, 1);
                    handler.removeCallbacks(this);
                }else {
                    handler.postDelayed(this, 1000);
                }
            }
        };
    }

    private String timeToString(int seconds) {
        long minutes = TimeUnit.SECONDS.toMinutes(Long.valueOf(seconds));
        long remainSeconds = seconds - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d", minutes, remainSeconds);
    }
}
