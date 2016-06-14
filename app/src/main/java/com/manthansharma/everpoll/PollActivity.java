package com.manthansharma.everpoll;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.manthansharma.everpoll.api.MyApiEndpointInterface;
import com.manthansharma.everpoll.api.ServiceGenerator;
import com.manthansharma.everpoll.api.model.Question;
import com.manthansharma.everpoll.api.model.Room;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class PollActivity extends AppCompatActivity implements PollStartFragment.OnFragmentInteractionListener, PollQuestionFragment.OnFragmentInteractionListener {
	private Integer room_id;
	private Room room;
	private ListIterator<Question> questionListIterator;
	private Boolean started = false;
	private Integer question_no = 1;
	private ArrayList<Integer> choice_vote = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poll);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			room_id = extras.getInt("room_uuid");
		}
		new RoomDetailActivity().execute();

		ActionBar actionBar = getSupportActionBar();
		assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private class RoomDetailActivity extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(PollActivity.this);
		Response<Room> response;

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Getting Data...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			SharedPreferences sharedPreferences = getSharedPreferences("user_detail", MODE_PRIVATE);
			String auth_token = sharedPreferences.getString("auth_token", null);
			MyApiEndpointInterface apiService = ServiceGenerator.createService(MyApiEndpointInterface.class, auth_token);
			try {
				response = apiService.get_room(room_id).execute();
				if (response.isSuccessful()) {
					room = response.body();
					questionListIterator = room.getQuestionSetDetail().getQuestion().listIterator();
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				PollActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(PollActivity.this, "Network Failure. Try again after some time", Toast.LENGTH_LONG).show();
						finish();
					}
				});

				return null;
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
			start_fragment();
		}

		private void onFailed() {
			try {
				String json = response.errorBody().string().replace('"', '\'');
				JSONObject jsonObject = new JSONObject(json);
				if (jsonObject.has("detail")) {
					Toast.makeText(PollActivity.this, "Poll not found. Check your Unique ID.", Toast.LENGTH_LONG).show();
					finish();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class ChoiceVoteTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(PollActivity.this);
		Response<ResponseBody> response;

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Submitting Vote...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			SharedPreferences sharedPreferences = getSharedPreferences("user_detail", MODE_PRIVATE);
			String auth_token = sharedPreferences.getString("auth_token", null);
			MyApiEndpointInterface apiService = ServiceGenerator.createService(MyApiEndpointInterface.class, auth_token);
			try {
				response = apiService.join_room(room_id).execute();
				if (!response.isSuccessful()) {
					String message = "";
					try {
						String json = response.errorBody().string().replace('"', '\'');
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.has("non_field_errors")) {
							message = jsonObject.getJSONArray("non_field_errors").get(0).toString();
						} else if (jsonObject.has("user")) {
							message = "User already exists.";
						}

						final String finalMessage = message;
						PollActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(PollActivity.this, finalMessage, Toast.LENGTH_LONG).show();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}

					finish();
					return null;

				}
				response = apiService.vote(choice_vote).execute();

				return response.isSuccessful();
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
			setResult(RESULT_OK, null);
			finish();
		}

		private void onFailed() {
			Toast.makeText(PollActivity.this, "Network Failure. Try again after some time", Toast.LENGTH_LONG).show();
		}
	}

	private void start_fragment() {
		Fragment pollStartFragment = PollStartFragment.newInstance(room);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.setCustomAnimations(R.anim.no_change, R.anim.pull_in_right)
				.replace(R.id.poll_fragment_container, pollStartFragment)
				.commit();
	}

	private void next_question() {
		Fragment pollQuestionFragment = PollQuestionFragment.newInstance(questionListIterator.next(), questionListIterator.hasNext(), question_no);
		question_no++;
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.setCustomAnimations(R.anim.push_out_left, R.anim.pull_in_right)
				.replace(R.id.poll_fragment_container, pollQuestionFragment)
				.commit();
	}

	@Override
	public void onStartButtonPressed() {
		started = true;
		next_question();
	}

	@Override
	public void onQuitButtonPressed() {
		finish_dialog();
	}

	@Override
	public void onSaveButtonPressed(Integer choice_id, Integer question_id) {
		choice_vote.add(choice_id);
		new ChoiceVoteTask().execute();
	}

	@Override
	public void onNextButtonPressed(Integer choice_id, Integer question_id) {
		choice_vote.add(choice_id);
		next_question();
	}


	public void finish_dialog() {
		if (started) {
			AlertDialog.Builder alert = new AlertDialog.Builder(PollActivity.this);
			alert.setTitle("Do you want to stop?")
					.setMessage("Response not yet saved.")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							finish();
						}
					})
					.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.dismiss();
						}
					})
					.show();
		} else {
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case android.R.id.home:
				finish_dialog();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		finish_dialog();
	}
}
