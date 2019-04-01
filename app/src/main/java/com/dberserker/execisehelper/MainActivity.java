package com.dberserker.execisehelper;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int DEFAULT_REPETITION_REST_TIME = 5;
    private static final int DEFAULT_SERIE_REST_TIME = 10;
    private Handler handler;
    private Runnable currentTask;
    private TextView timerTextView;
    private boolean taskRunning = false;
    private int currentTimerValue;
    private int currentRepetitions = 0;
    private int currentSeries = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button buttonRepetition = findViewById(R.id.buttonRepetition);
        final Button buttonSerie = findViewById(R.id.buttonSerie);
        final Button buttonReset = findViewById(R.id.buttonReset);
        buttonRepetition.setOnClickListener( eventButtonRepetition());
        buttonSerie.setOnClickListener( eventButtonSerie() );
        buttonReset.setOnClickListener( eventButtonReset() );
        handler = new Handler();
        timerTextView = findViewById(R.id.textViewTimer);
    }

    private View.OnClickListener eventButtonRepetition(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( !taskRunning) {
                    taskRunning = true;
                    currentTask = taskRepetition();
                    currentTimerValue = DEFAULT_REPETITION_REST_TIME;
                    timerTextView.setText(timeToString(currentTimerValue));
                    handler.removeCallbacks(currentTask);
                    handler.postDelayed(currentTask, 1000);
                }
            }
        };
    }

    private View.OnClickListener eventButtonSerie(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( !taskRunning) {
                    taskRunning = true;
                    currentTask = taskSerie();
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
                }
            }
        };
    }

    private Runnable taskRepetition(){
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
                    repetitionTextView.setText( String.valueOf(currentRepetitions) );
                    handler.removeCallbacks(this);
                }else
                    handler.postDelayed(this, 1000);
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
                    timerTextView.setText( timeToString(currentTimerValue) );
                    currentRepetitions = 0;
                    currentSeries++;
                    taskRunning = false;
                    TextView repetitionTextView = findViewById(R.id.textViewRepetitions);
                    TextView serieTextView = findViewById(R.id.textViewSeries);
                    repetitionTextView.setText( String.valueOf(currentRepetitions) );
                    serieTextView.setText( String.valueOf(currentSeries) );
                    handler.removeCallbacks(this);
                }else
                    handler.postDelayed(this, 1000);
            }
        };
    }

    private String timeToString(int seconds) {
        long minutes = TimeUnit.SECONDS.toMinutes(Long.valueOf(seconds));
        long remainSeconds = seconds - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d", minutes, remainSeconds);
    }
}
