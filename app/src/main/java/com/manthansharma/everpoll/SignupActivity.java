package com.manthansharma.everpoll;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.manthansharma.everpoll.api.MyApiEndpointInterface;
import com.manthansharma.everpoll.api.ServiceGenerator;
import com.manthansharma.everpoll.api.model.User;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
	public static final String TAG = SignupActivity.class.getSimpleName();
	private User user = null;

	@InjectView(R.id.input_signup_name)
	EditText _nameText;
	@InjectView(R.id.input_signup_email)
	EditText _emailText;
	@InjectView(R.id.input_signup_password)
	EditText _passwordText;
	@InjectView(R.id.signup_button)
	Button _signupButton;
	@InjectView(R.id.link_login)
	TextView _loginLink;
	@InjectView(R.id.wrapper_signup_name)
	TextInputLayout _nameWrapper;
	@InjectView(R.id.wrapper_signup_email)
	TextInputLayout _emailWrapper;
	@InjectView(R.id.wrapper_signup_password)
	TextInputLayout _passwordWrapper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		ButterKnife.inject(this);

		_signupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signup();
			}
		});

		_loginLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Finish the registration screen and return to the Login activity
				finish();
				overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
			}
		});
	}

	public void signup() {
		if (!validate()) {
			onSignupFailed();
			return;
		}

		_signupButton.setEnabled(false);

		String name = _nameText.getText().toString();
		String email = _emailText.getText().toString();
		String password = _passwordText.getText().toString();

		user = new User(name, email, password);
		new SignupTask().execute();
	}

	private class SignupTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
		Response<User> response;

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Creating...");
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			MyApiEndpointInterface apiService = ServiceGenerator.createService(MyApiEndpointInterface.class, null);
			try {
				response = apiService.create_user(user).execute();

				if (response.isSuccessful()) {
					user = response.body();
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				onSignupSuccess();
			} else {
				try {
					String json = response.errorBody().string().replace('"', '\'');
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject.has("email")) {
						_emailWrapper.setErrorEnabled(true);
						_emailWrapper.setError(jsonObject.getJSONArray("email").getString(0));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				onSignupFailed();
			}
			progressDialog.dismiss();
			super.onPostExecute(result);
		}

	}


	public void onSignupSuccess() {
		SharedPreferences.Editor editor = this.getSharedPreferences("user_detail", MODE_PRIVATE).edit();
		editor.putString("auth_token", user.getAuthToken());
		editor.putInt("user_id", user.getId());
		editor.putString("user_name", user.getName());
		editor.putString("user_email", user.getEmail());
		editor.apply();

		_signupButton.setEnabled(true);
		setResult(RESULT_OK, null);
		finish();
	}

	public void onSignupFailed() {
		Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
		_signupButton.setEnabled(true);
	}

	public boolean validate() {
		boolean valid = true;

		String name = _nameText.getText().toString();
		String email = _emailText.getText().toString();
		String password = _passwordText.getText().toString();

		if (name.isEmpty() || name.length() < 3) {
			_nameWrapper.setError("at least 3 characters");
			valid = false;
		} else {
			_nameWrapper.setError(null);
			_nameWrapper.setErrorEnabled(false);
		}

		if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			_emailWrapper.setError("enter a valid email address");
			valid = false;
		} else {
			_emailWrapper.setError(null);
			_emailWrapper.setErrorEnabled(false);
		}

		if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
			_passwordWrapper.setError("between 4 and 10 alphanumeric characters");
			valid = false;
		} else {
			_passwordWrapper.setError(null);
			_passwordWrapper.setErrorEnabled(false);
		}

		return valid;
	}
}
