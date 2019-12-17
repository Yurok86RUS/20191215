package com.yur0k.homeworkfive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
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

        ImageView imageViewCity = findViewById(R.id.imageViewCity);

        Log.i("CLICK", textViewCityName.getText().toString());

        Picasso.with(this).
                load("https://static.mk.ru/upload/entities/2019/10/06/09/articles/detailPicture/ba/fb/40/fa/98920cfb019dcd386a568be49f14c8b0.jpg").
                into(imageViewCity);

        buttonOk = findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i("CLICK2", textViewCityName.getText().toString());
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
        @GET("currentconditions/v1")
        Call<WeatherModel> getWeather(@Query("/") String q, @Query("apikey") String key);
    }

    private WeatherModel getWeather(String cityName) throws Exception {


        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://dataservice.accuweather.com/").
                addConverterFactory(GsonConverterFactory.create()).build();
                Call<WeatherModel> call = retrofit.create(AccuWeather.class).
                getWeather(cityName, "dNCJ5Bk0LjZE75BNfUFBgAdOFj6I7bjI&language=en&details=false");

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
