package com.example.weatherapptestapi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText countryName, countryCode;
    Button getData;
    TextView weatherData;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String apikey = "050c5842ad337300be088fb38145da42";
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryCode = findViewById(R.id.countryCode);
        countryName = findViewById(R.id.countryName);
        getData = findViewById(R.id.getData);
        weatherData = findViewById(R.id.weatherData);
    }

    public void getWeatherData(View view) {
        String tempurl;
        String cityName = countryName.getText().toString().trim();
        String cityCode = countryCode.getText().toString().trim();
        if (cityName.isEmpty()) {
            Toast.makeText(this, "Tên thành phố không được để trống !!!", Toast.LENGTH_SHORT).show();
        } else {
            if (!cityCode.isEmpty()) {
                tempurl = url + "?q=" + cityName + "," + cityCode + "&appid=" + apikey;
            } else {
                tempurl = url + "?q=" + cityName + "&appid=" + apikey;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.d("response", response);
                    String result = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectCloud = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectCloud.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
//                        weatherData.setTextColor(Color.rgb(68, 134, 199));
                        result += " Thời tiết hiện tại ở " + cityName + "(" + countryName + ")"
                                + "\n Nhiệt độ : " + df.format(temp) + "°C"
                                + "\n Nhiệt độ cảm nhận : " + df.format(feelsLike) + "°C"
                                + "\n Độ ẩm : " + humidity + "%"
                                + "\n Mô tả : " + description
                                + "\n Gió : " + wind + " m/s"
                                + "\n Mây mù : " + clouds + "%"
                                + "\n Áp suất : " + pressure + " hPa";
                        weatherData.setText(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}