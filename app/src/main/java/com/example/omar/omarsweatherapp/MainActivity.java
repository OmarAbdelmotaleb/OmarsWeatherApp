package com.example.omar.omarsweatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    Button buttonZip;
    EditText editTextZip;
    String weatherReq;

    ImageView weatherImage;
    TextView  currentTemp;
    TextView  quote;
    TextView  high1,     high2,      high3,      high4,      high5;
    ImageView weather1,  weather2,   weather3,   weather4,   weather5;
    TextView  low1,      low2,       low3,       low4,       low5;
    TextView  time1,     time2,      time3,      time4,      time5;




    //** AsyncTask stuff **//

    URL                 weatherURL;
    URLConnection       connect;
    InputStream         input;
    InputStreamReader   inputReader;
    BufferedReader      reader;

    String     weatherLocation;
    String     weatherText;
    String     weatherTime;
    double     weatherTemp;
    JSONObject weather;
    JSONObject weatherCurrent;
    JSONObject weatherMain;
    JSONArray  weatherList;
    JSONArray  weatherArray;


    double[] minTemp = new double[5];
    double[] maxTemp = new double[5];

    String[] desc    = new String[5];
    String[] date    = new String[5];

    String[] quotes  = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonZip       = (Button)      findViewById(R.id.id_buttonEnter);
        editTextZip     = (EditText)    findViewById(R.id.id_editTextZIP);

        currentTemp     = (TextView)    findViewById(R.id.id_weather);
        quote           = (TextView)    findViewById(R.id.id_quote);
        weatherImage    = (ImageView)   findViewById(R.id.id_imageWeather);

        weather1        = (ImageView)   findViewById(R.id.id_weather1);
        weather2        = (ImageView)   findViewById(R.id.id_weather2);
        weather3        = (ImageView)   findViewById(R.id.id_weather3);
        weather4        = (ImageView)   findViewById(R.id.id_weather4);
        weather5        = (ImageView)   findViewById(R.id.id_weather5);

        low1            = (TextView)    findViewById(R.id.id_low1);
        low2            = (TextView)    findViewById(R.id.id_low2);
        low3            = (TextView)    findViewById(R.id.id_low3);
        low4            = (TextView)    findViewById(R.id.id_low4);
        low5            = (TextView)    findViewById(R.id.id_low5);

        high1           = (TextView)    findViewById(R.id.id_high1);
        high2           = (TextView)    findViewById(R.id.id_high2);
        high3           = (TextView)    findViewById(R.id.id_high3);
        high4           = (TextView)    findViewById(R.id.id_high4);
        high5           = (TextView)    findViewById(R.id.id_high5);

        time1           = (TextView)    findViewById(R.id.id_time1);
        time2           = (TextView)    findViewById(R.id.id_time2);
        time3           = (TextView)    findViewById(R.id.id_time3);
        time4           = (TextView)    findViewById(R.id.id_time4);
        time5           = (TextView)    findViewById(R.id.id_time5);


        buttonZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editTextZip.getText().length() < 5){
                    // editTextZip.setText("Please enter a valid Zip Code");
                    Toast.makeText(MainActivity.this, "Please Enter a valid ZIP Code.", Toast.LENGTH_SHORT).show();
                }
                else{

                    weatherReq = "http://api.openweathermap.org/data/2.5/forecast?zip="
                            + editTextZip.getText() + ",us&APPID=e739b901319d0d224131ee1f86d3d914";
                    WeatherTask order66 = new WeatherTask();
                    order66.execute();

                }

            }
        });


    }

    public class WeatherTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                weatherURL    = new URL(weatherReq);
                connect       = weatherURL.openConnection();
                input         = connect.getInputStream();
                inputReader   = new InputStreamReader(input);
                reader        = new BufferedReader(inputReader);

                String weatherText = reader.readLine();
                JSONObject weather = new JSONObject(weatherText);
                JSONObject weatherCurrent;
                JSONObject weatherMain;

                JSONArray weatherList = weather.getJSONArray("list");

                for(int i = 0; i < 5; i++) {
                    weatherCurrent  = weatherList.getJSONObject(i);
                    weatherArray    = weatherCurrent.getJSONArray("weather");
                    weatherMain     = weatherCurrent.getJSONObject("main");

                    minTemp[i]  = weatherMain.getDouble("temp_min");
                    maxTemp[i]  = weatherMain.getDouble("temp_max");
                    desc[i]     = weatherArray.getJSONObject(0).getString("description");

                    weatherTime = weatherCurrent.getString("dt_txt").split(" ")[1];
                    TimeZone timezone = TimeZone.getTimeZone("EST");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                    simpleDateFormat.setTimeZone(timezone);

                    SimpleDateFormat weatherFormat = new SimpleDateFormat("hh:mm:ss");
                    weatherFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                    try{
                        Date weatherDate = weatherFormat.parse(weatherTime);
                        date[i] = simpleDateFormat.format(weatherDate);
                    }catch(ParseException e){ e.printStackTrace(); }

                }

                JSONObject weatherObject = weatherList.getJSONObject(0);
                JSONObject main          = weatherObject.getJSONObject("main");
                weatherTemp              = main.getDouble("temp");

                JSONObject weatherCity   = weather.getJSONObject("city");
                weatherLocation          = weatherCity.getString("name");


            }catch( JSONException | IOException e ){ e.printStackTrace(); };

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            low1.setText(  Math.round(fahrenheit(minTemp[0])) + "°F" );
            low2.setText(  Math.round(fahrenheit(minTemp[1])) + "°F" );
            low3.setText(  Math.round(fahrenheit(minTemp[2])) + "°F" );
            low4.setText(  Math.round(fahrenheit(minTemp[3])) + "°F" );
            low5.setText(  Math.round(fahrenheit(minTemp[4])) + "°F" );

            high1.setText( Math.round(fahrenheit(maxTemp[0])) + "°F" );
            high2.setText( Math.round(fahrenheit(maxTemp[1])) + "°F" );
            high3.setText( Math.round(fahrenheit(maxTemp[2])) + "°F" );
            high4.setText( Math.round(fahrenheit(maxTemp[3])) + "°F" );
            high5.setText( Math.round(fahrenheit(maxTemp[4])) + "°F" );

            if(date[0].substring(0,1).equals("0")) time1.setText(date[0].substring(1)); else time1.setText(date[0]);
            if(date[1].substring(0,1).equals("0")) time2.setText(date[1].substring(1)); else time2.setText(date[1]);
            if(date[2].substring(0,1).equals("0")) time3.setText(date[2].substring(1)); else time3.setText(date[2]);
            if(date[3].substring(0,1).equals("0")) time4.setText(date[3].substring(1)); else time4.setText(date[3]);
            if(date[4].substring(0,1).equals("12:00:00")) time5.setText("7:00");
            if(date[4].substring(0,1).equals("0")) time5.setText(date[4].substring(1)); else time1.setText(date[4]);

            currentTemp.setText( Math.round(fahrenheit(weatherTemp)) + "°F" );

            for(int i = 0; i < 5; i++){

                if(desc[0].contains("clear sky")){
                    weather1.setImageResource(R.drawable.clearsky);
                    weatherImage.setImageResource(R.drawable.clearsky);
                }
                if(desc[0].contains("clouds")){
                    weather1.setImageResource(R.drawable.fewclouds);
                    weatherImage.setImageResource(R.drawable.fewclouds);
                }
                if(desc[0].contains("rain")){
                    weather1.setImageResource(R.drawable.rain);
                    weatherImage.setImageResource(R.drawable.rain);
                }
                if(desc[0].equals("scattered clouds")){
                    weather1.setImageResource(R.drawable.scatteredclouds);
                    weatherImage.setImageResource(R.drawable.scatteredclouds);
                }
                if(desc[0].equals("shower rain")){
                    weather1.setImageResource(R.drawable.showerrain);
                    weatherImage.setImageResource(R.drawable.showerrain);
                }
                if(desc[0].equals("snow")){
                    weather1.setImageResource(R.drawable.snow);
                    weatherImage.setImageResource(R.drawable.snow);
                }
                if(desc[0].contains("light snow")){
                    weather1.setImageResource(R.drawable.snow);
                    weatherImage.setImageResource(R.drawable.snow);
                }

            }

            for(int i = 0; i < 5; i++){

                if(desc[1].contains("clear sky"))       weather2.setImageResource(R.drawable.clearsky);
                if(desc[1].contains("clouds"))          weather2.setImageResource(R.drawable.fewclouds);
                if(desc[1].contains("rain"))            weather2.setImageResource(R.drawable.rain);
                if(desc[1].equals("scattered clouds"))  weather2.setImageResource(R.drawable.scatteredclouds);
                if(desc[1].equals("shower rain"))       weather2.setImageResource(R.drawable.showerrain);
                if(desc[1].equals("snow"))              weather2.setImageResource(R.drawable.snow);
                if(desc[1].contains("light snow"))      weather2.setImageResource(R.drawable.snow);

            }

            for(int i = 0; i < 5; i++){

                if(desc[2].contains("clear sky"))       weather3.setImageResource(R.drawable.clearsky);
                if(desc[2].contains("clouds"))          weather3.setImageResource(R.drawable.fewclouds);
                if(desc[2].contains("rain"))            weather3.setImageResource(R.drawable.rain);
                if(desc[2].equals("scattered clouds"))  weather3.setImageResource(R.drawable.scatteredclouds);
                if(desc[2].equals("shower rain"))       weather3.setImageResource(R.drawable.showerrain);
                if(desc[2].equals("snow"))              weather3.setImageResource(R.drawable.snow);
                if(desc[2].contains("light snow"))      weather3.setImageResource(R.drawable.snow);

            }

            for(int i = 0; i < 5; i++){

                if(desc[3].contains("clear sky"))       weather4.setImageResource(R.drawable.clearsky);
                if(desc[3].contains("clouds"))          weather4.setImageResource(R.drawable.fewclouds);
                if(desc[3].contains("rain"))            weather4.setImageResource(R.drawable.rain);
                if(desc[3].equals("scattered clouds"))  weather4.setImageResource(R.drawable.scatteredclouds);
                if(desc[3].equals("shower rain"))       weather4.setImageResource(R.drawable.showerrain);
                if(desc[3].equals("snow"))              weather4.setImageResource(R.drawable.snow);
                if(desc[3].contains("light snow"))      weather4.setImageResource(R.drawable.snow);

            }

            for(int i = 0; i < 5; i++){

                if(desc[4].contains("clear sky"))       weather5.setImageResource(R.drawable.clearsky);
                if(desc[4].contains("clouds"))          weather5.setImageResource(R.drawable.fewclouds);
                if(desc[4].contains("rain"))            weather5.setImageResource(R.drawable.rain);
                if(desc[4].equals("scattered clouds"))  weather5.setImageResource(R.drawable.scatteredclouds);
                if(desc[4].equals("shower rain"))       weather5.setImageResource(R.drawable.showerrain);
                if(desc[4].equals("snow"))              weather5.setImageResource(R.drawable.snow);
                if(desc[4].contains("light snow"))      weather5.setImageResource(R.drawable.snow);

            }

            quotes[0] = "“Master Yoda says I should be mindful of the future… But not at the expense of the moment.” – Qui Gon Jinn";
            quotes[1] = "“Death is a natural part of life. Rejoice for those around you who transform into the Force.” – Yoda";
            quotes[2] = "“Do. Or do not. There is no try.”- Yoda";
            quotes[3] = "“Who’s more foolish? The fool, or the fool who follows him?” – Ben Obi-Wan Kenobi";
            quotes[4] = "“The ability to speak does not make you intelligent.” – Qui Gon Jinn";

            int random = (int)(Math.random() * 5);

            quote.setText(quotes[random]);

        }

        double kelvin;
        double celsius;
        double fahrenheit;

        public double fahrenheit(double k){
            kelvin = k;
            celsius = kelvin - 273;
            fahrenheit = ( celsius * (9.0/5) ) + 32;

            return fahrenheit;

        }
    }
}