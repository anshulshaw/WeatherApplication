package com.example.weather;

import android.app.Activity;
import android.content.Context;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

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
import java.util.Random;


public class MainActivity extends Activity {

    EditText cityName;
    TextView resultTextView;
    TextView resultTextView2;
    ImageView imageView;
    Button button;
    Button button2;


    public void findWeather(View view) {

        Log.i("cityName", cityName.getText().toString());

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        try {
            String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");

            DownloadTask task = new DownloadTask();
            task.execute("https://openweathermap.org/data/2.5/weather?q="+ encodedCityName +"&appid=439d4b804bc8187953eb36d2a8c26a02");


        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();


        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cityName = (EditText) findViewById(R.id.cityName);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        resultTextView2 = (TextView) findViewById(R.id.resultTextView2);
        button = (Button) findViewById(R.id.button2);
        button2 = (Button) findViewById(R.id.button3);

        findViewById(R.id.button2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    resultTextView.setBackgroundColor(0xffffff);
                    resultTextView2.setBackgroundColor(0xffffff);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                }
                return false;
            }
        });

        findViewById(R.id.button3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    resultTextView.setBackgroundColor(0xffffff);
                    resultTextView2.setBackgroundColor(0xffffff);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    resultTextView.setBackgroundColor(0xfff00000);
                    resultTextView2.setBackgroundColor(0xfff00000);

                }
                return false;
            }
        });

    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();

            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                String message = "";
                String message2 = "";

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");
                String mainTemperature = jsonObject.getString("main");
                String sysInfo = jsonObject.getString("sys");
                String humidity ;
                String country ;
                double temp;
                String maxTemp ;
                String minTemp ;
                String feelsLike = "" ;
                String visi = jsonObject.getString("visibility");
                int visiInKm = Integer.parseInt(visi) / 1000 ;

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = "";
                    String description = "";
                    String temperature = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    JSONObject mainPart = new JSONObject(mainTemperature);
                    temperature = mainPart.getString("temp");

                    JSONObject humidityPart = new JSONObject(mainTemperature);
                    humidity = humidityPart.getString("humidity");

                    JSONObject maxTempPart = new JSONObject(mainTemperature);
                    maxTemp = maxTempPart.getString("temp_max");

                    JSONObject minTempPart = new JSONObject(mainTemperature);
                    minTemp = minTempPart.getString("temp_min");

                    JSONObject feelsPart = new JSONObject(mainTemperature);
                    feelsLike = feelsPart.getString("feels_like");

                    JSONObject countryPart = new JSONObject((sysInfo));
                    country = countryPart.getString("country");


                    if (main != "" && description != "") {

                        message += "\n\n"+ main + " : " + description +
                                "\n  Temperature : " + temperature + "°C" +
                                "\n  Feels like : " + feelsLike + "°C" ;

                        message2 += "\n\n" +
                                "Max temp : " + maxTemp + "°C" +
                                "\n  Min temp : " + minTemp + "°C" +
                                "\n  Humidity : " + humidity + "%" +
                                "\n  Visibility : " + visiInKm + " Km" +
                                "\n  Country code : " + country ;

                        //For Haze weather
                        if(main.equals("Haze")){

                            imageView = (ImageView) findViewById(R.id.imageView);

                            int[] myImageList = new int[]{R.drawable.haze1,R.drawable.haze2,R.drawable.haze3,
                                    R.drawable.haze4,R.drawable.haze5,R.drawable.haze6,
                                    R.drawable.haze7,R.drawable.haze8,R.drawable.haze9,
                                    R.drawable.haze10,R.drawable.haze11,R.drawable.haze};
                            Random random = new Random();

                            Integer rand = random.nextInt(myImageList.length - 1) + 0;

                            imageView.animate().alpha(1f).setDuration(2000);
                            imageView.setImageResource(myImageList[rand]);


                        }

                        //For Clear Weather
                        if(main.equals("Clear")){

                            imageView = (ImageView) findViewById(R.id.imageView);

                            int[] myImageList = new int[]{R.drawable.clear,R.drawable.clear1,R.drawable.clear2,R.drawable.clear3,
                                                            R.drawable.clear4,R.drawable.clear5,R.drawable.clear6,
                                                                R.drawable.clear7,R.drawable.clear8,R.drawable.clear9};
                            Random random = new Random();

                            Integer rand = random.nextInt(myImageList.length - 1) + 0;

                            imageView.animate().alpha(1f).setDuration(3000);
                            imageView.setImageResource(myImageList[rand]);


                        }

                        //For Overcast weather
                        if(main.equals("Clouds")){

                            temp = Double.parseDouble(temperature);

                            if(Double.compare(temp,5) < 0 ){

                                imageView = (ImageView) findViewById(R.id.imageView);

                                int[] myImageList = new int[]{R.drawable.snow,R.drawable.snow2,R.drawable.snow3,
                                        R.drawable.snow4,R.drawable.snow5,R.drawable.snow6,R.drawable.snow7};
                                Random random = new Random();

                                Integer rand = random.nextInt(myImageList.length - 1) + 0;

                                imageView.animate().alpha(1f).setDuration(3000);
                                imageView.setImageResource(myImageList[rand]);

                            }
                                else
                                 {
                                    imageView = (ImageView) findViewById(R.id.imageView);

                                    int[] myImageList = new int[]{R.drawable.cloud1,
                                                                    R.drawable.cloud4,R.drawable.cloud5,R.drawable.cloud6, R.drawable.cloud7,
                                                                        R.drawable.cloud8,R.drawable.cloud9,
                                                                            R.drawable.cloud12,R.drawable.cloud13,
                                                                                R.drawable.cloud14,R.drawable.cloud,R.drawable.cloud11};
                                    Random random = new Random();

                                    Integer rand = random.nextInt(myImageList.length - 1) + 0;

                                    imageView.animate().alpha(1f).setDuration(3000);
                                    imageView.setImageResource(myImageList[rand]);
                                 }

                        }

                        //For Rainy Weather
                        if(main.equals("Rain")){

                            temp = Double.parseDouble(temperature);

                            if(Double.compare(temp,4) < 0 ){

                                imageView = (ImageView) findViewById(R.id.imageView);

                                int[] myImageList = new int[]{R.drawable.snow,R.drawable.snow2,R.drawable.snow3,
                                        R.drawable.snow4,R.drawable.snow5,R.drawable.snow6,R.drawable.snow7};
                                Random random = new Random();

                                Integer rand = random.nextInt(myImageList.length - 1) + 0;

                                imageView.animate().alpha(1f).setDuration(3000);
                                imageView.setImageResource(myImageList[rand]);

                            }

                            else {

                                imageView = (ImageView) findViewById(R.id.imageView);

                                int[] myImageList = new int[]{R.drawable.rain2, R.drawable.rain3, R.drawable.rain4, R.drawable.rain5,
                                        R.drawable.rain6, R.drawable.rain7, R.drawable.rain8};
                                Random random = new Random();

                                Integer rand = random.nextInt(myImageList.length - 1) + 0;

                                imageView.animate().alpha(1f).setDuration(3000);
                                imageView.setImageResource(myImageList[rand]);
                            }


                        }

                        //For Snowy Weather
                        if(main.equals("Snow")){

                            imageView = (ImageView) findViewById(R.id.imageView);

                            int[] myImageList = new int[]{R.drawable.snow,R.drawable.snow2,R.drawable.snow3,R.drawable.snow4,R.drawable.snow5,R.drawable.snow6,R.drawable.snow7};
                            Random random = new Random();

                            Integer rand = random.nextInt(myImageList.length - 1) + 0;

                            imageView.animate().alpha(1f).setDuration(2000);
                            imageView.setImageResource(myImageList[rand]);


                        }

                        if(main.equals("Drizzle")){

                            imageView = (ImageView) findViewById(R.id.imageView);

                            int[] myImageList = new int[]{R.drawable.drizzle,R.drawable.drizzle1,R.drawable.drizzle3,R.drawable.drizzle4,R.drawable.drizzle5};
                            Random random = new Random();

                            Integer rand = random.nextInt(myImageList.length - 1) + 0;

                            imageView.animate().alpha(1f).setDuration(3200);
                            imageView.setImageResource(myImageList[rand]);


                        }

                        if(main.equals("Thunderstorm")){

                            imageView = (ImageView) findViewById(R.id.imageView);

                            int[] myImageList = new int[]{R.drawable.thunder1,R.drawable.thunder2,R.drawable.thunder3,R.drawable.thunder4,R.drawable.thunder5,R.drawable.thunder6,R.drawable.thunder8,R.drawable.thunder7};
                            Random random = new Random();

                            Integer rand = random.nextInt(myImageList.length - 1) + 0;

                            imageView.animate().alpha(1f).setDuration(3200);
                            imageView.setImageResource(myImageList[rand]);


                        }

                        if(main.equals("Mist")){

                            imageView = (ImageView) findViewById(R.id.imageView);

                            int[] myImageList = new int[]{R.drawable.mist1,R.drawable.mist2,R.drawable.mist4,
                                    R.drawable.mist5,R.drawable.mist6,R.drawable.mist7,R.drawable.mist8,
                                    R.drawable.mist9};
                            Random random = new Random();

                            Integer rand = random.nextInt(myImageList.length - 1) + 0;

                            imageView.animate().alpha(1f).setDuration(2000);
                            imageView.setImageResource(myImageList[rand]);


                        }


                    }

                }


                if (message != "" && message2 !="") {

                    resultTextView.setText(message);
                    resultTextView2.setText(message2);

                }
                else {

                    Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();

                }



            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();

            }

        }
    }

    public void click(View view)
    {

        imageView = findViewById(R.id.imageView);
        imageView.animate().alpha(0f).setDuration(7000);
        resultTextView = findViewById(R.id.resultTextView);
        resultTextView2 = findViewById(R.id.resultTextView2);
        resultTextView.setText("\n\n\nJust name another" + "\ncity");
        resultTextView2.setText("\n\n\nAnd I will find the" + "\nweather for  you");
    }

}