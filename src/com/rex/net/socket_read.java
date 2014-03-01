package com.rex.net;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.rex.constant.CONSTANT;
import com.rex.domain.CarInfo;

public class socket_read extends Thread {

	String str_read_socket;
	Handler myHandler;
	InputStream ip;

	public socket_read(InputStream givenip, Handler givenhandler) {
		ip=givenip;
		myHandler = givenhandler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
	

		while (true) {
			try {

				int inputstream = 0;
				System.out.println("receiving");
				while ((inputstream = ip.read()) != -1) {
					System.out.print((char) inputstream);
					if ((char) inputstream == '$') {
						break;
					}
					sb.append((char) inputstream);

				}
				str_read_socket = sb.toString();
				System.out.println(sb.toString());

				// recv resp for lock
				// System.out.println("haha");
				if (str_read_socket.equals("LOCKED")) {
					System.out.println("haha");
					Bundle mBundle = new Bundle();
					mBundle.putString("lock_status", "" + 0);
					Message msg = new Message();
					msg.setData(mBundle);
					msg.what = CONSTANT.NET.UPDATE_LOCKSTATUS;

					myHandler.sendMessage(msg);
					System.out.println("haha");

					
				} else if (str_read_socket.equals("UNLOCKED")) {
					System.out.println("haha");
					Bundle mBundle = new Bundle();
					mBundle.putString("lock_status", "" + 1);
					Message msg = new Message();
					msg.setData(mBundle);
					msg.what = CONSTANT.NET.UPDATE_LOCKSTATUS;

					myHandler.sendMessage(msg);
					System.out.println("haha");

				
				} else {
					Gson gson = new Gson();
					if (str_read_socket != null) {
						CarInfo carinfo = gson.fromJson(sb.toString(),
								CarInfo.class);
						if (carinfo != null) {
							System.out
									.println(" json analyze success ,speed is "
											+ carinfo.getSpeed());

							Bundle mBundle = new Bundle();
							mBundle.putString("speed", "" + carinfo.getSpeed());
							mBundle.putString("charge",
									"" + carinfo.getCharge());
							mBundle.putString("current",
									"" + carinfo.getCurrent());
							mBundle.putString("lock", "" + carinfo.getLock());
							Message msg = new Message();
							msg.setData(mBundle);
							msg.what = CONSTANT.NET.UPDATE_CARINFO;

							myHandler.sendMessage(msg);

						} else {
							System.out.println("json analyze fail");
						}
					}
	
				}
				sb.delete(0, sb.length());
			}

			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

}
