package com.manthansharma.everpoll;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.manthansharma.everpoll.api.MyApiEndpointInterface;
import com.manthansharma.everpoll.api.ServiceGenerator;
import com.manthansharma.everpoll.api.model.QuestionSet;
import com.manthansharma.everpoll.api.model.QuestionSetList;
import com.manthansharma.everpoll.api.model.Room;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Response;


public class CreateRoomActivity extends AppCompatActivity {
	CreateRoomActivity _this = CreateRoomActivity.this;

	@InjectView(R.id.input_create_room_name)
	EditText _nameText;
	@InjectView(R.id.wrapper_create_room_name)
	TextInputLayout _nameWrapper;
	@InjectView(R.id.input_create_room_desc)
	EditText _descText;
	@InjectView(R.id.wrapper_create_room_desc)
	TextInputLayout _descWrapper;
	@InjectView(R.id.input_create_room_set)
	Spinner _setSpinner;
	@InjectView(R.id.create_room_btn)
	Button _button;

	SwitchCompat _publicSwitch;

	Room room = new Room();
	QuestionSetAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_room);
		ButterKnife.inject(this);

		new QuestionSetTask().execute();

		ActionBar actionBar = getSupportActionBar();
		assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Create Room");

		_setSpinner.setPrompt("Question Set");

		_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				login();
			}
		});
	}

	public void login() {
		if (!validate()) {
			return;
		}

		_button.setEnabled(false);

		room.setName(_nameText.getText().toString());
		room.setDescription(_descText.getText().toString());
		room.setQuestionSet((int) adapter.getItemId(_setSpinner.getSelectedItemPosition()));
		room.setPublic(_publicSwitch.isChecked());

		new CreateRoomTask().execute();
	}

	private class CreateRoomTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(CreateRoomActivity.this);
		Response<Room> response;

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Creating Room...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			SharedPreferences sharedPreferences = getSharedPreferences("user_detail", MODE_PRIVATE);
			String auth_token = sharedPreferences.getString("auth_token", null);
			MyApiEndpointInterface apiService = ServiceGenerator.createService(MyApiEndpointInterface.class, auth_token);
			try {
				response = apiService.create_rooms(room).execute();

				if (response.isSuccessful()) {
					room = response.body();
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				_this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(_this, "Network Failure. Try again after some time", Toast.LENGTH_LONG).show();
					}
				});
				return null;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			_button.setEnabled(true);
			if (result != null) {
				if (result) {
					onSuccess();
				} else {
					onFailed();
				}
			}
			progressDialog.dismiss();
			super.onPostExecute(result);
		}

		private void onSuccess() {
			AlertDialog.Builder alert = new AlertDialog.Builder(CreateRoomActivity.this);
			alert.setTitle("Room Created Successfully");
			alert.setMessage("UNIQUE ROOM ID: " + room.getId());
			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					setResult(RESULT_OK, null);
					finish();
				}
			});
			alert.show();
		}

		private void onFailed() {
			try {
				String json = response.errorBody().string().replace('"', '\'');
				JSONObject jsonObject = new JSONObject(json);
				if (jsonObject.has("non_field_errors")) {
					_nameWrapper.setError(jsonObject.getJSONArray("non_field_errors").getString(0));
				}
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class QuestionSetTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(CreateRoomActivity.this);
		QuestionSetList setList;

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Getting Data...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... voids) {

			MyApiEndpointInterface apiService = ServiceGenerator.createService(MyApiEndpointInterface.class);
			Response<QuestionSetList> response;
			try {
				response = apiService.get_question_set_list().execute();

				if (response.isSuccessful()) {
					setList = response.body();
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
			progressDialog.dismiss();
			super.onPostExecute(result);
		}

		private void onSuccess() {
			adapter = new QuestionSetAdapter(_this, setList.getResults());
			_setSpinner.setAdapter(adapter);
		}

		private void onFailed() {
			Toast.makeText(_this, "Network Failure. Try again after some time", Toast.LENGTH_LONG).show();
		}
	}

	public class QuestionSetAdapter extends BaseAdapter {
		private Activity activity;
		private LayoutInflater inflater;
		private List<QuestionSet> question_set_list;

		public QuestionSetAdapter(Activity activity, List<QuestionSet> question_set_list) {
			this.activity = activity;
			this.question_set_list = question_set_list;
		}

		@Override
		public int getCount() {
			return question_set_list.size();
		}

		@Override
		public Object getItem(int i) {
			return question_set_list.get(i);
		}

		@Override
		public long getItemId(int i) {
			return question_set_list.get(i).getId();
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			if (inflater == null)
				inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (view == null)
				view = inflater.inflate(R.layout.question_set_spinner_item, null);

			TextView textView = (TextView) view.findViewById(android.R.id.text1);

			QuestionSet question_set = question_set_list.get(i);

			textView.setText(question_set.getName());

			return view;
		}

		@Override
		public View getDropDownView(int i, View view, ViewGroup parent) {
			if (inflater == null)
				inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (view == null)
				view = inflater.inflate(R.layout.question_set_spinner_item, null);

			TextView textview = (TextView) view.findViewById(android.R.id.text1);

			QuestionSet question_set = question_set_list.get(i);

			textview.setText(question_set.getName());

			return view;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case android.R.id.home:
				finish();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_room, menu);
		_publicSwitch = (SwitchCompat) menu.findItem(R.id.menu_bar_create_room_public).getActionView().findViewById(R.id.create_room_public);

		return true;
	}

	public boolean validate() {
		boolean valid = true;
		String name = _nameText.getText().toString();
		String desc = _descText.getText().toString();

		if (name.isEmpty() || name.length() < 4 || name.length() > 20) {
			_nameWrapper.setError("between 4 and 20 alphanumeric characters");
			valid = false;
		} else {
			_nameWrapper.setError(null);
			_nameWrapper.setErrorEnabled(false);
		}

		if (desc.isEmpty() || desc.length() < 4 || desc.length() > 100) {
			_descWrapper.setError("between 4 and 100 alphanumeric characters");
			valid = false;
		} else {
			_descWrapper.setError(null);
			_descWrapper.setErrorEnabled(false);
		}

		return valid;
	}
}
