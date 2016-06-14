package com.manthansharma.everpoll;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.manthansharma.everpoll.api.MyApiEndpointInterface;
import com.manthansharma.everpoll.api.ServiceGenerator;
import com.manthansharma.everpoll.api.model.Room;
import com.manthansharma.everpoll.api.model.RoomList;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Response;

public class PublicRoomActivity extends BaseNavigationActivity {
	public static final String TAG = PublicRoomActivity.class.getSimpleName();
	private PublicRoomActivity _this = PublicRoomActivity.this;

	private static final int REQUEST_CREATE_ROOM = 0;
	private static final int REQUEST_CREATE_QUESTION_SET = 1;
	private static final int REQUEST_POLL = 2;

	@InjectView(R.id.public_rooms_list)
	ListView _roomList;
	@InjectView(R.id.nav_view)
	NavigationView _navigationView;
	@InjectView(R.id.public_rooms_refresh_list)
	SwipeRefreshLayout _roomListRefresh;
	@InjectView(R.id.fab_room)
	FloatingActionButton _fabRoom;
	@InjectView(R.id.fab_set)
	FloatingActionButton _fabSet;
	@InjectView(R.id.fab_poll)
	FloatingActionButton _fabPoll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_room);
		setTitle("Public Poll");
		ButterKnife.inject(this);

		new LoginTask().execute();

		_navigationView.getMenu().getItem(0).setChecked(true);
		_roomListRefresh.setColorSchemeResources(R.color.refresh_progress);

		_fabSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				question_set_dialog();
			}
		});

		_fabPoll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				room_dialog();
			}
		});

		_fabRoom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), CreateRoomActivity.class);
				startActivityForResult(intent, REQUEST_CREATE_ROOM);
			}
		});

		_roomListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new LoginTask().execute();
			}
		});

	}


	private class LoginTask extends AsyncTask<Void, Void, Boolean> {
		RoomList rooms;

		@Override
		protected void onPreExecute() {
			if (_roomListRefresh.isRefreshing())
				_roomListRefresh.setRefreshing(true);
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			SharedPreferences sharedPreferences = getSharedPreferences("user_detail", MODE_PRIVATE);
			String auth_token = sharedPreferences.getString("auth_token", null);
			MyApiEndpointInterface apiService = ServiceGenerator.createService(MyApiEndpointInterface.class, auth_token);
			Response<RoomList> response;
			try {
				response = apiService.get_rooms(Boolean.TRUE).execute();

				if (response.isSuccessful()) {
					rooms = response.body();
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result != null) {
				if (result) {
					onSuccess();
				} else {
					onFailed();
				}
			}
			if (_roomListRefresh.isRefreshing())
				_roomListRefresh.setRefreshing(false);
			super.onPostExecute(result);
		}

		private void onFailed() {
			Toast.makeText(_this, "Network Failure. Try again after some time", Toast.LENGTH_LONG).show();
		}

		private void onSuccess() {
			final PublicListAdapter adapterPublicRoom = new PublicListAdapter(_this, rooms.getResults());
			_roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					Animation anim = new AlphaAnimation(0.8f, 1.0f);

					Intent intent = new Intent(getApplicationContext(), PollActivity.class);
					intent.putExtra("room_uuid", ((Room) adapterPublicRoom.getItem(i)).getId());
					startActivityForResult(intent, REQUEST_POLL);

					anim.setDuration(1000);
					view.startAnimation(anim);
				}
			});

			_roomList.setAdapter(adapterPublicRoom);
		}
	}

	private void question_set_dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText name_input = new EditText(this);
		name_input.setInputType(InputType.TYPE_CLASS_TEXT);

		builder.setTitle("Question Set")
				.setView(name_input)
				.setMessage("Enter question set name: ")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String question_set_name = name_input.getText().toString();
						Intent intent = new Intent(getApplicationContext(), CreateQuestionSetActivity.class);
						intent.putExtra("set_name", question_set_name);
						startActivityForResult(intent, REQUEST_CREATE_QUESTION_SET);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.show();
	}

	private void room_dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText uuid_input = new EditText(this);
		uuid_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);

		builder.setTitle("Join Room")
				.setView(uuid_input)
				.setMessage("Enter unique id: ")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Integer room_uuid = Integer.valueOf(uuid_input.getText().toString());
							Intent intent = new Intent(getApplicationContext(), PollActivity.class);
							intent.putExtra("room_uuid", room_uuid);
							startActivityForResult(intent, REQUEST_POLL);
						} catch (NumberFormatException ignored) {

						}
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CREATE_QUESTION_SET) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(PublicRoomActivity.this, "Question Set created successfully.", Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == REQUEST_POLL){
			if (resultCode == RESULT_OK) {
				Toast.makeText(PublicRoomActivity.this, "Thanks for voting.", Toast.LENGTH_LONG).show();
			}
		}
	}
}
