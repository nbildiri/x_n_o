package com.example.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tictactoe.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.tictactoe.Utils.showInfoDialog;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button[][] mBoardView;
    private TicTacToe mGame;

    private boolean mUseAutoSave;
    private final String mKEY_GAME="Game";
    private String mKEY_AUTO_SAVE;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String json = mGame.getJSONFromThis();
        outState.putString("GAME", json);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String json = savedInstanceState.getString("GAME");
        mGame = TicTacToe.getGameFromString(json);
        restoreButtonTextFromModel();
    }

    private void restoreButtonTextFromModel() {
        for (Button[] buttonRow : mBoardView) {
            for (Button current : buttonRow) {
                String tag = (String) current.getTag();
                char row = tag.charAt(2);
                char col = tag.charAt(0);

                // convert from char to int
                int intCol = Character.getNumericValue(col);
                int intRow = Character.getNumericValue(row);

                current.setText(Character.toString(mGame.getCharacterAt(intCol, intRow)));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        initializeBoard();
        findViewById(R.id.reset).setOnClickListener(v -> newGame());
        mGame = new TicTacToe();
    }


    private void initializeBoard() {
        mBoardView = new Button[][]
                {{findViewById(R.id.button0), findViewById(R.id.button1), findViewById(R.id.button2)},
                        {findViewById(R.id.button3), findViewById(R.id.button4), findViewById(R.id.button5)},
                        {findViewById(R.id.button6), findViewById(R.id.button7), findViewById(R.id.button8)}};

        for (int i = 0; i < mBoardView.length; i++) {
            for (int j = 0; j < mBoardView[0].length; j++) {

                mBoardView[i][j].setTag(i + " " + j);
            }
        }

        //create listener for all buttons
        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle individual button clicked
                String tag = (String) v.getTag();
                char row = tag.charAt(2);
                char col = tag.charAt(0);
                // int index = Integer.parseInt((String) v.getTag());
                // Toast.makeText(getApplicationContext(), "Clicked on " + col + "," + row, Toast.LENGTH_SHORT).show();
                Button clickedButton = (Button) v;
                String clickedText = clickedButton.getText().toString();

                // convert from char to int
                int intCol = Character.getNumericValue(col);
                int intRow = Character.getNumericValue(row);

                if (mGame.isSpaceAvailable(intCol, intRow)) {
                    // update the View
                    clickedButton.setText(Character.toString(mGame.getCurrentPlayer()));

                    // update the Model
                    mGame.takeSpace(intCol, intRow);

                    if (mGame.boardFull())
                        Snackbar.make(v, "It's a tie! Would you like to play a new game?", Snackbar.LENGTH_INDEFINITE)
                                .setAction("New Game", v1 -> newGame())
                                .show();

                    if (mGame.isGameOver()) {
                        char winner = mGame.winner();
                        mGame.updateWinnerAndGameCount(winner);
                        Snackbar.make(v, "Winner: Team " + winner + "! \nWould you like to play again?", Snackbar.LENGTH_INDEFINITE)
                                .setAction("New Game", v1 -> newGame())
                                .show();
                    }
                } else {
                    Snackbar.make(v,
                            "Space " + tag + " already taken.",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        };

        for (Button[] currentRow : mBoardView) {
            for (Button current : currentRow) {
                current.setOnClickListener(buttonListener);
            }
        }

    }

    private void updateTeamScore() {
        TextView teamXScore = findViewById(R.id.playerOneScore);
        TextView teamOScore = findViewById(R.id.playerTwoScore);
        teamXScore.setText(String.valueOf(mGame.getWins('X')));
        teamOScore.setText(String.valueOf(mGame.getWins('O')));
    }

    private void newGame() {
        updateTeamScore();
        mGame.newGame();
        clearBoard();
    }

    private void clearBoard() {
        for (Button[] currentRow : mBoardView) {
            for (Button current : currentRow) {
                current.setText("");
            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_game: {
                newGame();
                return true;
            }
            case R.id.action_statistics: {
                showStatistics();
                return true;
            }
            case R.id.action_reset_stats: {
                mGame.resetGameStats();
                return true;
            }
            case R.id.action_settings: {
                showSettings();
                return true;
            }
            case R.id.action_about: {
                showAbout();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAbout() {
        showInfoDialog(MainActivity.this, "X n O",
                "This is a two player game where the two teams play against one another. The player " +
                        "who succeeds in placing three of their marks in a diagonal, horizontal, " +
                        "or vertical row is the winner\n" +
                        "\nAndroid game by Nina.");
    }

    private void showSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            restoreOrSetFromPreferences_AllAppAndGameSettings();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void restoreOrSetFromPreferences_AllAppAndGameSettings() {
        SharedPreferences sp = getDefaultSharedPreferences(this);
        mUseAutoSave = sp.getBoolean(mKEY_AUTO_SAVE, true);
    }

    private void showStatistics() {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        intent.putExtra("GAME", mGame.getJSONFromCurrentGame());
        startActivity(intent);
    }

    private void setupFields() {
        mKEY_AUTO_SAVE = getString(R.string.auto_save_key);
    }

}