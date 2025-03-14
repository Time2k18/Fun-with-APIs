package edu.georgiasouthern.csci5332;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 5332);
		PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
		printWriter.println("Hello World!");
		printWriter.flush();
		BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line = buffer.readLine();
		System.out.println(line);
		printWriter.close();
		buffer.close();
		socket.close();
	}

}
