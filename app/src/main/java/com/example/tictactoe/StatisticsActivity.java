package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tictactoe.R;
import com.example.tictactoe.TicTacToe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvDataGamesPlayed,
            tvDataPlayer1Wins, tvDataPlayer1WinsPercent,
            tvDataPlayer2Wins, tvDataPlayer2WinsPercent;

    private TicTacToe mCurrentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setupToolbar();
        setupFAB();
        setupViews();
        getIncomingData();
        processAndOutputIncomingData();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setupViews() {
        tvDataGamesPlayed = findViewById(R.id.tv_data_games_played);
        tvDataPlayer1Wins = findViewById(R.id.tv_data_player1_wins);
        tvDataPlayer1WinsPercent = findViewById(R.id.tv_data_player1_win_percent);
        tvDataPlayer2Wins = findViewById(R.id.tv_data_player2_wins);
        tvDataPlayer2WinsPercent = findViewById(R.id.tv_data_player2_win_percent);
    }

    private void getIncomingData() {
        Intent intent = getIntent();
        String gameJSON = intent.getStringExtra("GAME");
        mCurrentGame = TicTacToe.getGameFromJSON(gameJSON);
    }

    private void processAndOutputIncomingData() {
        final String FORMAT_STRING = "%2.1f%%", N_A = "N/A";
        int numberOfGamesPlayed = mCurrentGame.getNumberOfGamesPlayed();
        if (!mCurrentGame.isGameOver() && numberOfGamesPlayed > 0)
            numberOfGamesPlayed--;
        int p1Wins = mCurrentGame.getWins('X');
        int p2Wins = mCurrentGame.getWins('O');
        String p1WinPct = numberOfGamesPlayed == 0 ? N_A :
                String.format(Locale.US, FORMAT_STRING, (p1Wins / (double) numberOfGamesPlayed) * 100);
        String p2WinPct = numberOfGamesPlayed == 0 ? N_A :
                String.format(Locale.US, FORMAT_STRING, (p2Wins / (double) numberOfGamesPlayed) * 100);
        tvDataGamesPlayed.setText(String.valueOf(numberOfGamesPlayed));     // don't forget String.valueOf()
        tvDataPlayer1Wins.setText(String.valueOf(p1Wins));
        tvDataPlayer2Wins.setText(String.valueOf(p2Wins));
        tvDataPlayer1WinsPercent.setText(p1WinPct);
        tvDataPlayer2WinsPercent.setText(p2WinPct);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}

