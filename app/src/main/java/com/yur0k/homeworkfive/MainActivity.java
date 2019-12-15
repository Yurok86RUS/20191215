package com.yur0k.homeworkfive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private TextView textViewCityName;
    private Button buttonOk;
    private TextView textViewTemperatureNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCityName = findViewById(R.id.textViewCityNameNow);
        textViewCityName.setText("Moscow");

        textViewTemperatureNow= findViewById(R.id.temperatureNow);
        final WeatherModel weatherModel = new WeatherModel();

        buttonOk = findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //getWeather(textViewCityName.getText().toString());
                    WeatherModel model = getWeather("294021");
                    //textViewTemperatureNow.setText(weatherModel.getTemperature().toString());
                    //textViewTemperatureNow.setText(Double.toString(weatherModel.getTemperature().getValue()));
                   textViewTemperatureNow.setText(Double.toString(model.getTemperature().getValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    interface AccuWeather{
        @GET
        Call<WeatherModel> getWeather(@Path("currentconditions/v1") @Query("/") String q, @Query("apikey") String key);
    }

    private WeatherModel getWeather(String cityName) throws Exception {


        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://dataservice.accuweather.com").
                addConverterFactory(GsonConverterFactory.create()).build();
        Call<WeatherModel> call = retrofit.create(AccuWeather.class).
                getWeather(cityName, "dNCJ5Bk0LjZE75BNfUFBgAdOFj6I7bjI" + "language=en&details=false");

            Response<WeatherModel> response = call.execute();

            if (response.isSuccessful())
                return response.body();
            else
                throw new Exception(response.errorBody().string(),null);

        //так запрашиваем погоду текущую
       //"http://dataservice.accuweather.com/currentconditions/v1/294021?apikey=dNCJ5Bk0LjZE75BNfUFBgAdOFj6I7bjI&language=en&details=false"
        //так ищется город
        //"http://dataservice.accuweather.com/locations/v1/cities/search?apikey=dNCJ5Bk0LjZE75BNfUFBgAdOFj6I7bjI&q=Moscow&language=en&details=false"

    }

}
