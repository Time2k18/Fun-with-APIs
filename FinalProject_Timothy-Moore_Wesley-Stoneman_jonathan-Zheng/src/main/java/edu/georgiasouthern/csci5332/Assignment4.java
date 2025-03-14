package edu.georgiasouthern.csci5332;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Timothy Moore and hzhang
 */
public class Assignment4 {

    static String key = "ddf158eda8d8022e031769b164bc8b9f";

    public static void main(String[] args) {
        double temp = getTemperature(getGeoInfo("Savannah"));
        System.out.printf("Temperature: %3.1f (Fahrenheit)\n", temp);
    }

    public static GeoInfo getGeoInfo(String city) {
        try {
            //URL to obtain latitude and longitude with City name
            URL url = new URL("http://api.openweathermap.org/geo/1.0/direct?q=" + city + ",US&limit=1&appid=" + key);
            //Make connection to needed URL 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                //Pull string from Json page
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                //Use Gson to deserealize needed GeoInformation(latitude and longitude)
                Gson gson = new Gson();
                GeoInfo[] gi = gson.fromJson(reader, GeoInfo[].class);
                //return first element of array which is the whole whole string needed to be deserialized
                return gi[0];
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static double getTemperature(GeoInfo gi) {

        try {
            //URL to obtain tempurature with latitude and longitude
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + gi.lat + "&lon=" + gi.lon + "&appid=" + key);
            //Make connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                //Pull string from Json page
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                //Use Gson to deserealize needed GeoInformation(temperature)
                Gson gson = new Gson();
                Weather giMain = gson.fromJson(reader, Weather.class);
                //return Fahrenheit after converting from Kelvin
                return 1.8 * (giMain.main.temp - 273) + 32;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return 0;
    }

    class GeoInfo {

        double lat;
        double lon;
    }

    class Weather {

        Main main;
    }

    class Main {

        double temp;
    }
}
