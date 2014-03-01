package com.rex;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rex.MyListFragment.OnArticleSelectedListener;
import com.rex.constant.CONSTANT;
import com.rex.domain.CarInfo;
import com.rex.net.socket_connect;
import com.rex.panel.PanelCamera;
import com.rex.panel.PanelCarInfo;
import com.rex.utilities.Rose;

public class IPP_projectActivity extends Activity implements
		OnArticleSelectedListener, SensorListener {

	// net
	int i;
	static Socket server;
	static socket_connect clientsocket;
	TimerTask task;
	// Button changePanelButton;
	// Button writebutton;
	// Button disconnetbutton;
	// Button exitbutton;
	SensorManager sensorManager;
	static final int sensor = SensorManager.SENSOR_ORIENTATION;

	ImageButton IB_connect;
	ImageButton IB_lock;
	TextView title_speed;
	// TextView title_charge;
	Timer timer;
	Timer connect_status_timer;
	PanelCarInfo panelCarInfo;
	PanelCamera panelCamera;
	FragmentTransaction transaction;

	public interface MyCallInterface {
		public void method();

	}

	public class Caller {
		public MyCallInterface mc;

		public void setCallfuc(MyCallInterface mc) {
			this.mc = mc;
		}

		public void call() {
			this.mc.method();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// delete the title
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置标题栏样式
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏

		setContentView(R.layout.main);

		// changePanelButton = (Button) findViewById(R.id.change_button);
		// writebutton = (Button) findViewById(R.id.write_button);
		// disconnetbutton = (Button) findViewById(R.id.disconnect_button);
		// exitbutton = (Button) findViewById(R.id.exit_button);

		IB_connect = (ImageButton) findViewById(R.id.IB_connect_status);
		IB_lock = (ImageButton) findViewById(R.id.IB_lock_status);

		// title_charge = (TextView) findViewById(R.id.title_charge);
		title_speed = (TextView) findViewById(R.id.title_speed);

		FragmentManager fragmentManager = getFragmentManager();
		// Create new fragment and transaction

		transaction = getFragmentManager().beginTransaction();
		MyListFragment myListFragment = new MyListFragment();
		// myListFragment.getListView().setBackgroundColor(Color.BLACK);
		PanelFragment panelFragment = new PanelFragment();
		transaction.add(R.id.list_container, myListFragment);
		transaction.add(R.id.panel_container, panelFragment);
		transaction.commit();

		panelCarInfo = new PanelCarInfo();
		panelCamera = new PanelCamera();

		final Handler myHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == CONSTANT.NET.UPDATE_CARINFO) {
					System.out.println("updating the panel");
					// System.out.println(msg.getData().getString("charge").toString());
					// title_charge.setText();

					title_speed.setText("speed:"
							+ msg.getData().getString("speed").toString()
							+ "  charge:"
							+ msg.getData().getString("charge").toString());
					// panelCarInfo.setSeed();

					// update car panel
					try {
						FragmentManager fragmentManager = getFragmentManager();
						((PanelCarInfo) fragmentManager
								.findFragmentByTag("tag_panelcarinfo"))
								.setSpeed(msg.getData().getString("speed")
										.toString());
						((PanelCarInfo) fragmentManager
								.findFragmentByTag("tag_panelcarinfo"))
								.setCharge(msg.getData().getString("charge")
										.toString());
					} catch (Exception e) {
					}
					// end update car
					if (msg.getData().getString("lock").toString().equals("0"))// lock
					{
						IB_lock.setImageDrawable(getResources().getDrawable(
								R.drawable.small_lock));
					} else {
						IB_lock.setImageDrawable(getResources().getDrawable(
								R.drawable.small_unlock));
					}
				}

				else if (msg.what == CONSTANT.NET.UPDATE_CONNECT_STATUS) {
					if (msg.arg1 == CONSTANT.NET.CONNECTED) {
						IB_connect.setImageDrawable(getResources().getDrawable(
								R.drawable.small_connect));
					} else if (msg.arg1 == CONSTANT.NET.UNCONNECTED) {
						IB_connect.setImageDrawable(getResources().getDrawable(
								R.drawable.small_disconnect));
					}
				}
			}

		};
		// net
		clientsocket = new socket_connect(server, myHandler);
		clientsocket.start();

		// update the connect status
		TimerTask task_update_connect_status = new TimerTask() {
			public void run() {
				System.out.println("checcking the connecting status");
				if (clientsocket.isSuccess()) {
					Message msg = new Message();
					msg.what = CONSTANT.NET.UPDATE_CONNECT_STATUS;
					msg.arg1 = CONSTANT.NET.CONNECTED;
					myHandler.sendMessage(msg);

				} else // unconect
				{
					Message msg = new Message();
					msg.what = CONSTANT.NET.UPDATE_CONNECT_STATUS;
					msg.arg1 = CONSTANT.NET.UNCONNECTED;
					myHandler.sendMessage(msg);
					// clientsocket.connect();
				}
			}
		};

		connect_status_timer = new Timer(true);
		connect_status_timer.schedule(task_update_connect_status, 1000, 1000); // 延时1000ms后执行，1000ms执行一次
		// timer.cancel(); //退出计时器

		// net connect

		task = new TimerTask() {
			public void run() {

				if (clientsocket.isSuccess()) {
					clientsocket.write(CONSTANT.NET.GET_INFO);
				}
			}
		};

		timer = new Timer(true);
		timer.schedule(task, 1000, 1000); // 延时1000ms后执行，1000ms执行一次
		/*
		 * 
		 * changePanelButton.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { // TODO Auto-generated method stub
		 * System.out.println("click");
		 * 
		 * 
		 * 
		 * clientsocket.start(); } });
		 * 
		 * writebutton.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { // TODO Auto-generated method stub //
		 * socket_write socketWrite=new socket_write(server, "1"); //
		 * socketWrite.start(); //clientsocket.write(CONSTANT.NET.GET_INFO);
		 * 
		 * // timer robot
		 * 
		 * TimerTask task = new TimerTask() { public void run() {
		 * clientsocket.write(CONSTANT.NET.GET_INFO); } };
		 * 
		 * timer = new Timer(true); timer.schedule(task, 1000, 1000); //
		 * 延时1000ms后执行，1000ms执行一次 // timer.cancel(); //退出计时器
		 * 
		 * } });
		 */
		/*
		 * disconnetbutton.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { // TODO Auto-generated method stub //
		 * timer.cancel(); clientsocket.disconnet();
		 * 
		 * } }); exitbutton.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { // TODO Auto-generated method stub
		 * 
		 * finish();
		 * 
		 * } });
		 */
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	public void OnArticleSelected(int position) {
		System.out
				.println("in activity receive from fragment list " + position);
		FragmentManager fragmentManager = getFragmentManager();
		// fragmentManager.findFragmentById(R.id.panel_container);
		// Create new fragment and transaction
		// Fragment newFragment = new PanelCarInfo();
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		switch (position) {
		case CONSTANT.PANEL_CAR_INFO: {
			// timer.schedule(task, 1000, 1000); // 延时1000ms后执行，1000ms执行一次
			transaction.replace(R.id.panel_container, panelCarInfo,
					"tag_panelcarinfo");

			transaction.addToBackStack(null);
			// Commit the transaction
			transaction.commit();

			break;
		}
		case CONSTANT.PANEL_CAMERA: {
			// timer.cancel();
			transaction
					.replace(R.id.panel_container, panelCamera, "tag_camera");
			transaction.addToBackStack(null);
			// Commit the transaction
			transaction.commit();
			break;
		}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor);
	}

	// unregister
	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	// Ignore for now
	public void onAccuracyChanged(int sensor, int accuracy) {
	}

	// Listen to sensor and provide output
	public void onSensorChanged(int sensor, float[] values) {
		if (sensor != IPP_projectActivity.sensor)
			return;
		int orientation = (int) values[0];
		// panelCarInfo.getRose().setDirection(orientation);
		FragmentManager fragmentManager = getFragmentManager();
		try {
			((PanelCarInfo) fragmentManager
					.findFragmentByTag("tag_panelcarinfo"))
					.updatecompass(orientation);
		} catch (Exception e) {

		}

	}
}