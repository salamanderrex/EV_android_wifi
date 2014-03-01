package com.rex.panel;

import com.rex.R;
import com.rex.utilities.Rose;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PanelCarInfo extends Fragment {
	// 三个一般必须重载的方法
	Rose rose;
	TextView speed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		//View view = getView();
	//	LinearLayout linearLayout1 = (LinearLayout) view
	//			.findViewById(R.id.compass_position);
	//	rose = new Rose(view.getContext());

	//	rose.setBackgroundResource(R.drawable.ic_launcher);
	//	linearLayout1.addView(rose);
		System.out.println("ExampleFragment--onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.panel_car_info,null);
		speed = (TextView) view.findViewById(R.id.info_speed);
		// speed.setText("22");
		System.out.println("ExampleFragment--onCreateView");
		//view.
		LinearLayout linearLayout1 = (LinearLayout) view.findViewById(R.id.compass_position);
		rose = new Rose(view.getContext());
		linearLayout1.addView(rose);
		//((ViewGroup) view).addView(linearLayout1);

		return view;

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("ExampleFragment--onPause");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("ExampleFragment--onResume");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("ExampleFragment--onStop");
	}

	public void setSpeed(String speed) {
		((TextView) getView().findViewById(R.id.info_speed)).setText(speed);

	}

	public void setCharge(String charge)

	{
		((TextView) getView().findViewById(R.id.info_charge)).setText(charge);
	}
	
	public void updatecompass(int orientation)
	{
		rose.setDirection(orientation);
	}
	
}
