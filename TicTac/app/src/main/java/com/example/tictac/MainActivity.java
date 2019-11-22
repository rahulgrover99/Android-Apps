package com.example.tictac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int activePlayer = 0;
    boolean gameActive =true;
    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8}, {0,4,8}, {2,4,6}};
    public void drawIn(View view) {
        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());
        if(gameState[tappedCounter]==2 && gameActive){
            counter.setTranslationY(-1000f);
            if (activePlayer==0) {
                counter.setImageResource(R.drawable.yellow);
            }
            else {
                counter.setImageResource(R.drawable.red);
            }
            gameState[tappedCounter] = activePlayer;
            activePlayer = activePlayer^1;
            counter.animate().translationYBy(1000f).setDuration(300);

            for(int[] winningPosition : winningPositions) {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                    String color = "Yellow";
                    gameActive = false;
                    if (gameState[winningPosition[0]] == 1) {
                        color = "Red";
                    }
                    TextView winnerMessage = findViewById(R.id.winnerMessage);
                    winnerMessage.setText(color + " has won!!!");
                    LinearLayout layout = findViewById(R.id.playAgainLayout);
                    layout.setVisibility(View.VISIBLE);
                }

            }
            if (gameActive) {
                boolean isDraw = true;
                for (int counterState: gameState) {
                    if (counterState == 2)  isDraw = false;
                }
                if (isDraw) {
                    gameActive = false;
                    TextView winnerMessage = findViewById(R.id.winnerMessage);
                    winnerMessage.setText("It's a draw!");
                    LinearLayout layout = findViewById(R.id.playAgainLayout);
                    layout.setVisibility(View.VISIBLE);
                }
            }

        }
    }
    public void playAgain(View view){
        LinearLayout layout = findViewById(R.id.playAgainLayout);
        layout.setVisibility(View.INVISIBLE);
        activePlayer = 0;
        gameActive = true;
//        gameState = {2,2,2,2,2,2,2,2,2};
        for (int i = 0; i < gameState.length; i++) gameState[i] = 2;
        GridLayout gridLayout = findViewById(R.id.grid);
        for(int i = 0; i < gridLayout.getChildCount(); i++) {
            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
