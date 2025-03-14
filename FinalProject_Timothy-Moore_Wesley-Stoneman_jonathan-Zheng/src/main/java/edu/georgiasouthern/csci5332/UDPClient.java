package edu.georgiasouthern.csci5332;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPClient {
	public static void main(String[] args) throws Exception {
		DatagramSocket socket = new DatagramSocket();
		byte[] buffer = "Hello World!".getBytes();
		InetAddress ip = InetAddress.getLocalHost();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ip, 5332);
		
			socket.send(packet);
			
			byte[] recieveBuffer = new byte[256];
			DatagramPacket recievePacket = new DatagramPacket(recieveBuffer,recieveBuffer.length);
			socket.receive(recievePacket);
			
			InetAddress recieveIP = packet.getAddress();
			int port = packet.getPort();
			
			System.out.println("ip Address: " + recieveIP);
			System.out.println("Port: " + port);
			String msg = new String(recieveBuffer, StandardCharsets.UTF_8);
			System.out.println("msg: " + msg);
			
	}
}
