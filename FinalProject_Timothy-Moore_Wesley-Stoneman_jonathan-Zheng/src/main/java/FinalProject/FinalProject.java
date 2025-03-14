package FinalProject;

/**
 *
 * @author Timothy Moore
 */
//package edu.georgiasouthern.csci5332;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class FinalProject {

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(6969);
        while (true) {
            new Thread(new Handler(server.accept())).start();
        }
    }
}

class Handler extends Thread {

    Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;

    }
    public static String weatherKey = "ddf158eda8d8022e031769b164bc8b9f";
    public static String dadJokeKey = "122209a60dmshfad3942c209a66dp15b1f1jsne3f2004ede28";
    public static int boredKey = getRandom();
    public static String setup = " ";
    public static String punchLine = " ";
    public static String solution = " ";

    public static int getRandom() {
        Random random = new Random();
        int key = random.nextInt(9999999) + 1000000;
        return key;
    }

    public static GeoInfo getGeoInfo(String city) {
        try {
            // URL to obtain latitude and longitude with City name
            URL url = new URL("http://api.openweathermap.org/geo/1.0/direct?q=" + city + ",US&limit=1&appid=" + weatherKey);
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

    public static double getTemperature(GeoInfo gi) {

        try {
            // URL to obtain tempurature with latitude and longitude
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + gi.lat + "&lon=" + gi.lon + "&appid=" + weatherKey);
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

    public static void getDadJokeInfo() throws IOException {
        try {
            // URL to obtain tempurature with latitude and longitude
            URL url = new URL("https://dad-jokes.p.rapidapi.com/random/joke?rapidapi-key=" + dadJokeKey);
            // Make connection
            HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();
            if (conn1 != null) {
                // Pull string from Json page
                InputStream in = conn1.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                // Use Gson to deserealize needed variables of dad joke
                Gson gson = new Gson();
                Dad dadJoke = gson.fromJson(reader, Dad.class);
                // return dad joke
                setup = dadJoke.body.get(0).setup;
                punchLine = dadJoke.body.get(0).punchline;
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public static void getBoredActivity() throws IOException {
        try {
            // URL to connect to http
            URL url = new URL("http://www.boredapi.com/api/activity"); //?/key= + Integer.toString(boredKey)

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection != null) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                Gson gson = new Gson();
                BoredActivity activity = gson.fromJson(reader, BoredActivity.class);
                // assign random chuck norris joke
                solution = activity.activity;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getAgifyInfo(String name) throws IOException {
        try {
            // URL to obtain agify prediction based on United States
            URL url = new URL("https://api.agify.io?name="+ name +"&country_id=US");

            // Make connection
            HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();
            if (conn1 != null) {
                // Pull string from Json page
                InputStream in = conn1.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                // Use Gson to deserealize needed variables of agify
                Gson gson = new Gson();
                Age age = gson.fromJson(reader, Age.class);
                // return age prediction
                return age.age;
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return 0;
    } 
    
    public void run() {
        try {
            String sentinal = "quit";
            Date date = new Date();
            String line = "Line";
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);

            while (!line.equals(sentinal)) {
                //Reading the message from the client
                line = br.readLine();

                System.out.println("Recieved '" + line + "' at " + date);
                pw.println("Recieved '" + line + "' at " + date);
                pw.flush();

                switch (line) {
                    case "/weather": {

                        System.out.println("Enter City: ");
                        pw.println("Enter City: ");
                        pw.flush();

                        BufferedReader city = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String cityString = city.readLine();
                        System.out.println(cityString);

                        double temp = getTemperature(getGeoInfo(cityString));
                        String tempurature = String.format("Temperature: %.1f (Fahrenheit)\n", temp);
                        System.out.println(tempurature);
                        System.out.println();
                        pw.println(date + "Server received:" + cityString);
                        pw.flush();
                        System.out.println(" Server received:" + cityString);
                        System.out.println();
                        pw.printf(tempurature);
                        pw.flush();
                        break;
                    }

                    case "/dad": {
                        OutputStream osDad = socket.getOutputStream();
                        PrintWriter pwDad = new PrintWriter(osDad);
                        getDadJokeInfo();
                        pwDad.println(setup + ": " + punchLine);
                        pwDad.flush();
                        pwDad.close();
                        System.out.println(setup + ": " + punchLine);
                        System.out.println();
                        break;
                    }
                    case "/bored": {
                        OutputStream osActivity = socket.getOutputStream();
                        PrintWriter pwActivity = new PrintWriter(osActivity);
                        getBoredActivity();
                        pwActivity.println(solution);
                        pwActivity.flush();
                        System.out.println(solution);
                        System.out.println();
                        break;
                    }
                    case "/name": {
                        System.out.println("Enter City: ");
                        pw.println("Enter Name: ");
                        pw.flush();

                        BufferedReader name = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String nameString = name.readLine();
                        System.out.println(nameString);
                        
                        pw.println(getAgifyInfo(nameString) + " : is your current age prediction based on your name.");
                        pw.flush();
                        break;
                    }
                    default: {
                        OutputStream osDefault = socket.getOutputStream();
                        PrintWriter pwDefault = new PrintWriter(osDefault);
                        pwDefault.println("Please type one of the Commands given( '/weather' , '/dad' , or 'quit' to stop running)");
                        pwDefault.flush();
                        pwDefault.close();
                        System.out.println("Please type one of the Commands given( '/weather' , '/dad' , or 'quit' to stop running)");
                        System.out.println();
                        break;
                    }
                }
            }
            pw.close();
            os.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Something Went Wrong: ");
            System.out.println(ex.getMessage());

        }
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

class Dad {

    ArrayList<Body> body;
}

class Body {

    String setup;
    String punchline;
}

class BoredActivity {

    String activity;
}

/*class Activity {

    String solution;
}*/

class Age {
    int age;
}


