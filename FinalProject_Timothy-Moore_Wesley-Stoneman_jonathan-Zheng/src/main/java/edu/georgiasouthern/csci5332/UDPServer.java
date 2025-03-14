package edu.georgiasouthern.csci5332;



import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPServer {
	public static void main(String[] args) throws Exception {
		DatagramSocket socket = new DatagramSocket(5332);
		byte[] buffer = new byte[256];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while(true) {
			socket.receive(packet);
			InetAddress ip = packet.getAddress();
			int port = packet.getPort();
			System.out.println("ip Address: " + ip);
			System.out.println("Port: " + port);
			String msg = new String(buffer, StandardCharsets.UTF_8);
			System.out.println("msg: " + msg);
			byte[] senderBuffer = ("Server: " + msg).getBytes();
			DatagramPacket senderPacket = new DatagramPacket(senderBuffer, senderBuffer.length, ip, port);
			socket.send(senderPacket);
			
		}
			
	}

}
