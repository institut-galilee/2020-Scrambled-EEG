package com.scrambled_eeg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button graph;
    private Button reveil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        this.graph = (Button) findViewById(R.id.button_graph);
        this.reveil = (Button) findViewById(R.id.button_Reveil);

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent graphActivity = new Intent(getApplicationContext(), GraphActivity.class);
                startActivity(graphActivity);
                finish();
            }
        });

        reveil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer medp = MediaPlayer.create(getApplicationContext(), R.raw.beep);
                medp.setVolume(100f,100f);
                medp.start();
            }
        });

    }
}