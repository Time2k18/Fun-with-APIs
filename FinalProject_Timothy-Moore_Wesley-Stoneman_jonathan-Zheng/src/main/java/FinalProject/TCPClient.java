package FinalProject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package edu.georgiasouthern.csci5332;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import javax.swing.SwingUtilities;

/**
 *
 * @author Timothy Moore
 */
public class TCPClient {

    private static Socket socket;

    public static void main(String[] args) {
        try {
            String input = " ";
            String sentinal = "quit";
            System.out.println("Welcome to fun with API\nCommands: \nFor WeatherAPI: '/weather'"
                    + "\nFor Random Dad jokes: '/dad'"
                    + "\nFor Random Activities: '/bored'"
                    + "\nFor Random Activities: '/name'"
                    + "\nTo stop connection: 'quit'");
            while (!input.equals(sentinal)) {
                Socket socket = new Socket("localhost", 6969);
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextLine();
                
                pw.println(input);
                pw.flush();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String server = br.readLine();
                System.out.println("Sent: " + input);
                System.out.println(server);
                
                if (input.equals("/weather")) {
                    System.out.println(br.readLine());

                    input = scanner.nextLine();
                    pw.println(input);
                    pw.flush();
                    System.out.println(br.readLine());
                } else if (input.equals("/name")){
                    System.out.println(br.readLine());

                    input = scanner.nextLine();
                    pw.println(input);
                    pw.flush();
                    System.out.println(br.readLine());
                }
                
                System.out.println(br.readLine());
                br.close();
                pw.close();
                socket.close();
            }
        } catch (Exception exception) {
            System.out.println("Possible Wrong Port Number");
        }

    }
}
