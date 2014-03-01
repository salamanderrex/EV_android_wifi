package com.rex.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.rex.constant.*;
import com.rex.domain.CarInfo;
public class socket_connect extends Thread {
	Socket mysocket;
	boolean success;
	socket_read  mysocket_read;
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	boolean reading;
	PrintWriter out;
	String str_read_socket;
	Handler myHandler;
	InputStream ip;
	OutputStream os;

	public socket_connect(Socket givensocket, Handler givenHandler) {
		mysocket = givensocket;
		success = false;
		myHandler = givenHandler;
		reading = false;
		
	}

	public void disconnet() {
		try {
			System.out.println("staring close^^^^^");
			success = false;
			mysocket_read.stop();
			// success = false;
			// os.close();
			// ip.close();
			mysocket.close();
			System.out.print("socket close");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(" close fail");
		}

	}

	public void connect() {
		System.out.println("staring connetct^^^^^^");
		try {
			mysocket = new Socket(CONSTANT.NET.HOST_IP, CONSTANT.NET.PORT);
			mysocket.setSoTimeout(5000);
			ip = mysocket.getInputStream();

			os = mysocket.getOutputStream();
			success = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("socket connect succeed");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!success) {
			try {
				
				mysocket = new Socket(CONSTANT.NET.HOST_IP, CONSTANT.NET.PORT);

				//mysocket.setSoTimeout(5000);
				ip = mysocket.getInputStream();

				os = mysocket.getOutputStream();
				success=true;
				mysocket_read=new socket_read(ip,myHandler);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (success)
				mysocket_read.start();
		}
	}

	public void write(String str) {
		if (success) {
			try {

				// out = new PrintWriter(mysocket.getOutputStream());

				System.out.println("I am going to write");

				if (str != "end") {
					// out.println(str);
					// out.flush();
					os.write(str.getBytes("utf-8"));

					os.flush();
					System.out.println("write finish ");
				} else {
					// out.close();
					// os.close();
					// mysocket.close();

				}

			} catch (Exception e) {
				// TODO: handle exception

				System.out.println(" write fail");
			}

		}
	}
}
