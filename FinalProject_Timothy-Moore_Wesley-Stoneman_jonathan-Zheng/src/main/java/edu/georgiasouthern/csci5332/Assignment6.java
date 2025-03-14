package edu.georgiasouthern.csci5332;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class Assignment6 {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(5332);
        while (true) {
            Socket socket = server.accept();
            WeatherHandler handler = new WeatherHandler(socket);
            handler.start();
        }
    }
}

class WeatherHandler extends Thread {
    public static String key = "ddf158eda8d8022e031769b164bc8b9f";
    Socket socket;

    public WeatherHandler(Socket socket) {
        this.socket = socket;
    }

    public void runWeather() {

        InputStream is;
        try {
            is = socket.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
            String line = buffer.readLine();
            System.out.println(line);
            OutputStream output = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(output);
            // printWriter.println("Server recieved: " + line);
            printWriter.println("Server recieved: " + line);
            printWriter.println("Server Output: " + getTemperature(getGeoInfo(socket.toString())));

            printWriter.flush();
            printWriter.close();
            is.close();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public GeoInfo getGeoInfo(String city) {
        try {
            // URL to obtain latitude and longitude with City name
            URL url = new URL("http://api.openweathermap.org/geo/1.0/direct?q=" + city + ",US&limit=1&appid=" + key);
            // Make connection to needed URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                // Pull string from Json page
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                // Use Gson to deserealize needed GeoInformation(latitude and longitude)
                Gson gson = new Gson();
                GeoInfo[] gi = gson.fromJson(reader, GeoInfo[].class);
                // return first element of array which is the whole whole string needed to be
                // deserialized
                return gi[0];
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public double getTemperature(GeoInfo gi) {

        try {
            // URL to obtain tempurature with latitude and longitude
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + gi.lat + "&lon=" + gi.lon
                    + "&appid=" + key);
            // Make connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                // Pull string from Json page
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                // Use Gson to deserealize needed GeoInformation(temperature)
                Gson gson = new Gson();
                Weather giMain = gson.fromJson(reader, Weather.class);
                // return Fahrenheit after converting from Kelvin
                return 1.8 * (giMain.main.temp - 273) + 32;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return 0;
    }

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
