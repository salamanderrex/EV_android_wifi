package com.rex.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class socket_write extends Thread{

	Socket server;
	String str;

	public socket_write(Socket given_server,String givenstr) {
		server = given_server;
		str=givenstr;
	}

	public void run() {
		
		try {
			// TODO Auto-generated method stub
			
			PrintWriter out;
			out = new PrintWriter(server.getOutputStream());
			OutputStream os=server.getOutputStream();
			System.out.println("I am going to write");
			if (str != "end") {
				out.println(str);
				out.flush();
				//os.write("1".getBytes("utf-8"));
				

			} else {
				server.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}