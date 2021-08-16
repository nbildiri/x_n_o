package com.example.tictactoe;

//enum PLAYER_LETTER {X, O}


import com.google.gson.Gson;

import java.util.Arrays;

public class TicTacToe {

    private char[][] board;
    private char currentPlayer;

    private final char EMPTY = ' ';


    private final static int sPLAYER_COUNT = 2;
    private int startGame = 0;
    private int mNumberOfGamesPlayed = startGame;
    private final int[] mArrayPlayerWinCount;


    public TicTacToe() {
        this.board = new char[3][3];
        mArrayPlayerWinCount = new int[sPLAYER_COUNT];
        newGame();

    }

    public final void newGame() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = EMPTY;
            }
        }

        currentPlayer = 'X';
    }

    public boolean isSpaceAvailable(int col, int row) {
        return (board[col][row] == EMPTY);
    }

    public Character getCharacterAt(int col, int row) {
        return board[col][row];
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void takeSpace(int col, int row) {
        if (!isSpaceAvailable(col, row))
            throw new RuntimeException("Space " + col + ", " + " already taken.");
        else {
            board[col][row] = currentPlayer;
            currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
        }
    }

    public void updateWinnerAndGameCount(char team) {
        if ( team== 'X') {
            mArrayPlayerWinCount[0]++;
        } else if (team == 'O') {
            mArrayPlayerWinCount[1]++;
        }
        mNumberOfGamesPlayed++;
    }


    public char winner() {

        //Check each row
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') {
                return board[i][0];
            }
        }

        //Check each column
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == board[1][j] && board[1][j] == board[2][j] && board[0][j] != '-') {
                return board[0][j];
            }
        }

        //Check the diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '-') {
            return board[0][0];
        }
        if (board[2][0] == board[1][1] && board[1][1] == board[0][2] && board[2][0] != '-') {
            return board[2][0];
        }

        //Otherwise nobody has not won yet
        return EMPTY;

    }

    public boolean boardFull() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == EMPTY) {
                    // a move still exists - board is not full
                    return false;
                }
            }
        }
        return true;
    }


    public boolean isGameOver() {
        char win = winner();
        if (!(win == EMPTY)) {
            mNumberOfGamesPlayed++;
            return true;
        } else {
            return false;
        }
    }

    public void resetGameStats() {
        //reset number of games played
        mNumberOfGamesPlayed = startGame;
        //clear array
        Arrays.fill(mArrayPlayerWinCount, 0);
    }

    // save and restore purposes - turn the object into a String and back to an object
    public String getJSONFromThis() {
        return new Gson().toJson(this);
    }

    public static TicTacToe getGameFromString(String json) {
        return new Gson().fromJson(json, TicTacToe.class);
    }


    public int getNumberOfGamesPlayed() {
        return mNumberOfGamesPlayed;
    }

    /**
     * Reverses the game object's serialization as a String
     * back to a ThirteenStones game object
     *
     * @param json The serialized String of the game object
     * @return The game object
     */
    public static TicTacToe getGameFromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, TicTacToe.class);
    }

    /**
     * Serializes the game object to a JSON-formatted String
     *
     * @param obj Game Object to serialize
     * @return JSON-formatted String
     */
    public static String getJSONFromGame(TicTacToe obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);

    }

    public String getJSONFromCurrentGame()
    {
        return getJSONFromGame(this);
    }

    public int getWins(char team) {
        if (team == 'X') {
            return mArrayPlayerWinCount[0];
        }
        else if (team == 'O') {
            return mArrayPlayerWinCount[1];
        }
        return EMPTY;
    }
}