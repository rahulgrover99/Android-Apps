package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    ArrayList<String> celebURLs;
    ArrayList<String> celebNames;
    int chosenCeleb;
    ImageView imageView;
    int locationOfCorrectAnswer;
    String[] answers;
    Button button0;
    Button button1;
    Button button2;
    Button button3;

    public void celebChosen(View view) {
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){
            Toast.makeText(getApplicationContext(), "Correct Answer!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Wrong! It was "+ celebNames.get(chosenCeleb), Toast.LENGTH_LONG).show();
        }
        createNewQuestion();
    }

    public void createNewQuestion() {
        Random random = new Random();
        chosenCeleb = random.nextInt(celebNames.size());
        ImageDownloader imageDownloader = new ImageDownloader();
        Bitmap celebImage = null;
        try {
            celebImage = imageDownloader.execute(celebURLs.get(chosenCeleb)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(celebImage);
        int incorrectLoc;
        locationOfCorrectAnswer = random.nextInt(4);
        Set<Integer> hash_Set = new HashSet<Integer>();
        for (int i = 0; i < 4; i++) {
            if (i==locationOfCorrectAnswer) {
                answers[i] = celebNames.get(chosenCeleb);
                hash_Set.add(chosenCeleb);
            }
            else {
                incorrectLoc = random.nextInt(celebNames.size());
                while(incorrectLoc==chosenCeleb || hash_Set.contains(incorrectLoc)==true) incorrectLoc = random.nextInt(celebNames.size());
                hash_Set.add(incorrectLoc);
                answers[i] = celebNames.get(incorrectLoc);
            }
        }
        button0.setText(answers[0]);
        button1.setText(answers[1]);
        button2.setText(answers[2]);
        button3.setText(answers[3]);
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class Downloadtask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;
            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("Bakchodi", "TRUE");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Bakchodi", "TRUE");
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Downloadtask downloadtask = new Downloadtask();
        String result;
        celebURLs = new ArrayList<>();
        celebNames = new ArrayList<>();
        imageView = findViewById(R.id.imageView);
        answers = new String[4];
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        try {
            result = downloadtask.execute("http://www.posh24.se/kandisar/").get();

            String[] splitResult = result.split("<div class=\"sidebarInnerContainer\">");

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);
            while(m.find()) {
                celebURLs.add(m.group(1));
                Log.i("Group : ", m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);

            while(m.find()) {
                celebNames.add(m.group(1));
                Log.i("Group : ", m.group(1));
            }

//            Log.i("Content : ", " "+result);
            createNewQuestion();


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
