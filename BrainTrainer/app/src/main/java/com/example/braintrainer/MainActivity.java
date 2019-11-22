package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button playAgainButton;

    TextView timerTextView;
    TextView resultTextView;
    TextView pointsTextView;
    TextView sumTextView;

    GridLayout gridLayout;

    CountDownTimer countDownTimer;

    ArrayList<Integer> answers;
    int locationOfCorrectAnswer;
    int score, a, b, i;
    int total;
    public void start(View view){
        startButton.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.VISIBLE);
        pointsTextView.setVisibility(View.VISIBLE);
        sumTextView.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.VISIBLE);
        countDownTimer.start();
    }



    public void generateQuestion() {
        Random rand = new Random();
        answers.clear();
        a = rand.nextInt(21);
        b = rand.nextInt(21);


        sumTextView.setText(Integer.toString(a) + " + " + Integer.toString(b));



        locationOfCorrectAnswer = rand.nextInt(4);
        for (i = 0; i < 4; i++) {
            if(i == locationOfCorrectAnswer) {
                answers.add(a+b);
            }
            else{
                int incorrectAnswer = rand.nextInt(41);
                while(incorrectAnswer == a+b) incorrectAnswer = rand.nextInt(41);
                answers.add(incorrectAnswer);
            }
        }
        Log.i("Loc : ", Integer.toString(locationOfCorrectAnswer));

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));

    }

    public void playAgain (View view) {
        score = 0;
        total = 0;
        playAgainButton.setVisibility(View.INVISIBLE);
        pointsTextView.setText(Integer.toString(score) + "/"+Integer.toString(total));
        resultTextView.setText("");
        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        generateQuestion();
        new CountDownTimer(31000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(Integer.toString((int)millisUntilFinished/1000) + "s");
            }

            @Override
            public void onFinish() {
                // resultTextView.setText("Done");
                timerTextView.setText("0s");
                resultTextView.setText("Your score is : " +Integer.toString(score) + "/"+Integer.toString(total));
                playAgainButton.setVisibility(View.VISIBLE);
                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
            }

        }.start();

    }

    public void chooseAnswer(View view) {
        total++;
        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){
            resultTextView.setText("CORRECT!");
            score++;

        }
        else {
            resultTextView.setText("WRONG!");
        }
        pointsTextView.setText(Integer.toString(score) + "/"+Integer.toString(total));


        generateQuestion();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = 0;
        total = 0;

        answers = new ArrayList<Integer>();

        gridLayout = findViewById(R.id.gridLayout2);

        timerTextView = findViewById(R.id.timerTextView);
        resultTextView = findViewById(R.id.resultTextView);
        pointsTextView = findViewById(R.id.pointsTextView);
        sumTextView = findViewById(R.id.sumTextView);
        final TextView timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        playAgainButton = findViewById(R.id.playAgainButton);


        generateQuestion();

         countDownTimer = new CountDownTimer(31000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(Integer.toString((int)millisUntilFinished/1000) + "s");
            }

            @Override
            public void onFinish() {
                // resultTextView.setText("Done");
                resultTextView.setText("Your score is : " +Integer.toString(score) + "/"+Integer.toString(total));
                playAgainButton.setVisibility(View.VISIBLE);
                timerTextView.setText("0s");
                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
            }
        };


    }
}
