package com.dberserker.execisehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final int DEFAULT_REPETITION_REST_TIME = 30;
    private static final int DEFAULT_SERIE_REST_TIME = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button buttonRepetition = findViewById(R.id.buttonRepetition);
        buttonRepetition.setOnClickListener( eventButtonRepetition());
    }

    private View.OnClickListener eventButtonRepetition(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        };
    }
}
