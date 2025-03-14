package edu.georgiasouthern.csci5332;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TCPServer {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(5332);
		while(true) {
			Socket socket = server.accept();
			Handler handler = new Handler(socket);
			handler.start();
			
		}

	}
}
class Handler extends Thread {
	Socket socket;
	public Handler(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		
		InputStream is;
		try {
			is = socket.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
			String line = buffer.readLine();
			System.out.println(line);
			OutputStream output = socket.getOutputStream();
			PrintWriter printWriter = new PrintWriter(output);
			Date date = new Date();
			printWriter.println(date);
			printWriter.println("Server recieved: " + line);
			printWriter.flush();
			printWriter.close();
			is.close();
			buffer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
