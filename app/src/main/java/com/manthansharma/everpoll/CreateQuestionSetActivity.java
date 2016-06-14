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
import com.manthansharma.everpoll.api.model.QuestionSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class CreateQuestionSetActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener {
	public static final String TAG = CreateQuestionSetActivity.class.getSimpleName();
	private List<Question> questionArrayList = new ArrayList<>();
	private String question_set_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_question_set);

		ActionBar actionBar = getSupportActionBar();
		assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Add Question");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			question_set_name = extras.getString("set_name");
		}

		Fragment questionFragment = QuestionFragment.newInstance(question_set_name, questionArrayList.size());
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.replace(R.id.create_question_set_fragment_container, questionFragment);
		fragmentTransaction.commit();
	}

	private void add_more_question(Boolean back_stack) {
		Fragment questionFragment = QuestionFragment.newInstance(question_set_name, questionArrayList.size());
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.setCustomAnimations(R.anim.push_out_left, R.anim.pull_in_right);
		fragmentTransaction.replace(R.id.create_question_set_fragment_container, questionFragment);

		if (back_stack)
			fragmentTransaction.addToBackStack(null);

		fragmentTransaction.commit();
	}

	private class CreateQuestionSetTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(CreateQuestionSetActivity.this);
		Response<QuestionSet> response;

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Creating Set...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			SharedPreferences sharedPreferences = getSharedPreferences("user_detail", MODE_PRIVATE);
			String auth_token = sharedPreferences.getString("auth_token", null);
			MyApiEndpointInterface apiService = ServiceGenerator.createService(MyApiEndpointInterface.class, auth_token);
			try {
				QuestionSet questionSet = new QuestionSet();
				questionSet.setName(question_set_name);
				questionSet.setQuestion(questionArrayList);
				response = apiService.create_question_set(questionSet).execute();

				if (response.isSuccessful()) {
					questionSet = response.body();
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
			setResult(RESULT_OK, null);
			finish();
		}

		private void onFailed() {
			Toast.makeText(CreateQuestionSetActivity.this, "Network Failure. Try again after some time", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	public void onFragmentInteraction(Question question, Boolean save) {
		questionArrayList.add(question);
		if (!save)
			add_more_question(true);
		else
			new CreateQuestionSetTask().execute();
	}

	public void finish_dialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(CreateQuestionSetActivity.this);
		alert.setTitle("Do you want to stop?")
				.setMessage("Question Set not yet saved.")
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
