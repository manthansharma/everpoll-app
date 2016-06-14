package com.manthansharma.everpoll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manthansharma.everpoll.api.model.Room;

import java.util.List;

public class PublicListAdapter extends BaseAdapter {
	public static final String TAG = PublicListAdapter.class.getSimpleName();
	private Activity activity;
	private LayoutInflater inflater;
	private List<Room> rooms;

	public PublicListAdapter(Activity activity, List<Room> rooms) {
		this.activity = activity;
		this.rooms = rooms;
	}

	@Override
	public int getCount() {
		return rooms.size();
	}

	@Override
	public Object getItem(int i) {
		return rooms.get(i);
	}

	@Override
	public long getItemId(int i) {
		return rooms.get(i).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.public_rooms_list, null);

		TextView name = (TextView) convertView.findViewById(R.id.public_rooms_item_name);
		TextView days_ago = (TextView) convertView.findViewById(R.id.public_rooms_item_days_ago);
		TextView desc = (TextView) convertView.findViewById(R.id.public_rooms_item_description);
		LinearLayout wrapper = (LinearLayout) convertView.findViewById(R.id.public_rooms_item_wrapper);
		
		Room room = rooms.get(position);

		name.setText(room.getName());
		days_ago.setText(room.getDaysAgo());
		desc.setText(room.getDescription());

		return convertView;
	}

}