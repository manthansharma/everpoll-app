package com.manthansharma.everpoll;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.manthansharma.everpoll.api.MyApiEndpointInterface;
import com.manthansharma.everpoll.api.ServiceGenerator;
import com.manthansharma.everpoll.api.model.Question;
import com.manthansharma.everpoll.api.model.QuestionSet;
import com.manthansharma.everpoll.api.model.QuestionSetList;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Response;

public class QuestionSetActivity extends BaseNavigationActivity {
	@InjectView(R.id.question_set_list)
	ExpandableListView _setList;
	@InjectView(R.id.nav_view)
	NavigationView _navigationView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_set);
		setTitle("Question Set");
		ButterKnife.inject(this);

		_navigationView.getMenu().getItem(2).setChecked(true);

		new QuestionSetTask().execute();
	}

	private class QuestionSetTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(QuestionSetActivity.this);
		Response<QuestionSetList> response;
		QuestionSetList questionSetListResponse;

		List<QuestionSet> _questionSetList;
		HashMap<Integer, List<Question>> _questionMap = new HashMap<>();

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
				response = apiService.get_question_set_list().execute();

				if (response.isSuccessful()) {
					questionSetListResponse = response.body();
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
			if (!result) {
				Toast.makeText(QuestionSetActivity.this, "Network Failure. Try again after some time", Toast.LENGTH_LONG).show();
			} else {
				_questionSetList = questionSetListResponse.getResults();
				for (QuestionSet questionSet : _questionSetList) {
					_questionMap.put(questionSet.getId(), questionSet.getQuestion());
				}

				QuestionSetListAdapter questionSetListAdapter = new QuestionSetListAdapter(QuestionSetActivity.this, _questionSetList, _questionMap);
				_setList.setAdapter(questionSetListAdapter);
			}
			progressDialog.dismiss();
			super.onPostExecute(result);
		}
	}

}
