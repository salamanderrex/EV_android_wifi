package com.rex.panel;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.rex.R;

public class PanelCamera extends Fragment {
	// ����һ��������صķ���

	WebView show;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("cameraFragment--onCreate");
		
		
	

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		System.out.println("cameraFragment--onCreateView");
		
		
		View view =inflater.inflate(R.layout.panel_camera,null);
		show= (WebView) view.findViewById(R.id.camera_show);

		WebSettings setting = show.getSettings();
		setting.setUseWideViewPort(true);
		setting.setJavaScriptEnabled(true); // Support JavaScript
		setting.setPluginsEnabled(true); // Support Plugins, for example just
											// like flash plugin.
		setting.setPluginState(PluginState.ON);
		setting.setSupportZoom(true); // Zoom Control on web (You don't need
										// this if ROM supports Multi-Touch
		setting.setBuiltInZoomControls(true); // Enable Multitouch if supported
												// by ROM
		setting.setJavaScriptEnabled(true);

		show.setWebViewClient(new WebViewClient() {

			private ProgressDialog loadingBar;

			public boolean shouldOverrideUrlLoading(WebView view, String url) { // ��д�˷������������ҳ��������ӻ����ڵ�ǰ��webview����ת��������������Ǳ�
				view.loadUrl(url);
				return true;
			}
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				loadingBar = ProgressDialog.show(getActivity(), null,
						"���ڼ��ء�");
				super.onPageStarted(view, url, favicon);
			}

			public void onPageFinished(WebView view, String url) {
				if (loadingBar.isShowing()) {
					loadingBar.dismiss();
				}
			}
			
			  public void onReceivedError(WebView view, int errorCode,
		               String description, String failingUrl) {
		           Toast.makeText(getActivity(), "���س���", Toast.LENGTH_LONG).show();
					if (loadingBar.isShowing()) {
						loadingBar.dismiss();
					}
			  }
		});
		show.loadUrl("http://192.168.1.146:8080/javascript_simple.html");
		//show.loadUrl("http://192.168.1.146:8080/?action=snapshot");
		//show.loadUrl("http://192.168.1.1");
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
}
