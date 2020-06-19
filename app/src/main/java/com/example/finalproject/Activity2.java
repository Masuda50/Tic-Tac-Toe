package com.example.finalproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import android.content.Intent;


public class Activity2 extends AppCompatActivity implements View.OnClickListener {

    private Button[][] tiles = new Button[3][3];
    private int roundCount;
    private int p1wins;
    private int p2wins;
    private int playerTurn = 1;
    private String textOne;
    private String textTwo;


    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent intent = getIntent();
        textOne = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        if (textOne.compareTo("X")==0){
            textTwo = "O";
        }
        else{
            textTwo = "X";
        }


        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        //assigning all the tiles to a place in the two dimensional array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                tiles[i][j] = findViewById(resID);
                tiles[i][j].setOnClickListener(this);
            }
        }

        //reset button
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                resetGame();
            }
        });

    }

    //the artificial intelligence the player one goes up against
    public void AI1() {


        playerTurn = 2;

        Random rand = new Random();
        int p1 = rand.nextInt(3);
        int p2 = rand.nextInt(3);

        //if chosen tile is already occupied
        if (!((tiles[p1][p2]).getText().toString().equals(""))){
            AI1();
        }
        else{tiles[p1][p2].setText(textTwo);}
    }


    //on click of one of the tiles
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            Toast.makeText(this, "Invalid Choice!", Toast.LENGTH_SHORT).show();
            return;
        }

        ((Button) v).setText(textOne);

        playerTurn = 1;

        roundCount++;

        if (checkForWin()) {
            player1Wins();

        } else if (roundCount == 9) {
            draw();
        }

        AI1();

        roundCount++;

        if (checkForWin()) {
            player2Wins();

        } else if (roundCount == 9) {
            draw();
        }

    }

    private boolean checkForWin() {
        //putting all the button text into an array
        String[][] place = new String[3][3];

        //check cols, rows, and then diagonals
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                place[i][j] = tiles[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (place[i][0].equals(place[i][1])
                    && place[i][0].equals(place[i][2])
                    && !place[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (place[0][i].equals(place[1][i])
                    && place[0][i].equals(place[2][i])
                    && !place[0][i].equals("")) {
                return true;
            }
        }

        if (place[0][0].equals(place[1][1])
                && place[0][0].equals(place[2][2])
                && !place[0][0].equals("")) {
            return true;
        }

        if (place[0][2].equals(place[1][1])
                && place[0][2].equals(place[2][0])
                && !place[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        p1wins++;
        Toast.makeText(this, "Player 1 WINS!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins() {
        p2wins++;
        Toast.makeText(this, "Player 2 WINS!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        Toast.makeText(this, "DRAW!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + p1wins);
        textViewPlayer2.setText("Player 2: " + p2wins);
    }

    private void resetBoard() {
        roundCount = 0;
        playerTurn = 1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tiles[i][j].setText("");
            }
        }
    }

    private void resetGame() {
        p1wins = 0;
        p2wins = 0;
        updatePointsText();
        resetBoard();
    }

    //to allow for the game to be flipped
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt("player1Points", p1wins);
        outState.putInt("player2Points", p2wins);
        outState.putInt("roundCount", roundCount);
        outState.putInt("playerTurn", playerTurn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        p1wins = savedInstanceState.getInt("p1wins");
        p2wins = savedInstanceState.getInt("p2wins");
        playerTurn = savedInstanceState.getInt("playerTurn");
        roundCount = savedInstanceState.getInt("roundCount");

    }
}