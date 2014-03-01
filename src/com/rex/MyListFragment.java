package com.rex;

import android.app.Activity;
import android.app.ListFragment;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyListFragment extends ListFragment {
	String[] myselectItems = { "car info", "camera", "map" };
	OnArticleSelectedListener mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragement, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, myselectItems));
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		Toast.makeText(getActivity(),
				"You have selected " + myselectItems[position],
				Toast.LENGTH_SHORT).show();

		// Send the event and Uri to the host activity
		mListener.OnArticleSelected(position);
	}

	// Container Activity must implement this interface
	public interface OnArticleSelectedListener {
		public void OnArticleSelected(int position);

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			mListener = (OnArticleSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implementOnArticleSelectedListener");
		}
	}

}