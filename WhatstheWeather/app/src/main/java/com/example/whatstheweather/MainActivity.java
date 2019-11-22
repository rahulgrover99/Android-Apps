package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextView;

    public void findWeather (View view){
        String city = cityName.getText().toString();
        Log.i("City Name: ", city);

        try {
            String encodedCityName = URLEncoder.encode(city, "UTF-8");
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName +"&appid="); // Add your APP ID
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find Weather !", Toast.LENGTH_LONG);
        }

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result =  "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int data = inputStreamReader.read();
                while(data!=-1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not find Weather !", Toast.LENGTH_LONG);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Log.i("Website Content : ", result);
            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather : ", weatherInfo);
                JSONArray jsonArray = new JSONArray(weatherInfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    String main = "";
                    String description = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if (main!="" && description!="") {
                        message += main + ": " + description + "\n";
                    }

                    Log.i("main : ", jsonPart.getString("main"));
                    Log.i("description : ", jsonPart.getString("description"));
                }

                if (message!="") {
                    resultTextView.setText(message);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Could not find Weather !", Toast.LENGTH_LONG);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Could not find Weather !", Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        resultTextView = findViewById(R.id.resultTextView);

    }
}
