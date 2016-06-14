package com.manthansharma.everpoll;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
	public static final String TAG = LoginActivity.class.getSimpleName();
	private static final int REQUEST_SIGNUP = 0;
	private User user = null;
	private LoginActivity _this = LoginActivity.this;

	@InjectView(R.id.input_login_email)
	EditText _emailText;
	@InjectView(R.id.input_login_password)
	EditText _passwordText;
	@InjectView(R.id.login_button)
	Button _loginButton;
	@InjectView(R.id.link_signup)
	TextView _signupLink;
	@InjectView(R.id.wrapper_login_email)
	TextInputLayout _emailWrapper;
	@InjectView(R.id.wrapper_login_password)
	TextInputLayout _passwordWrapper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.inject(this);


		_loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				login();
			}
		});

		assert _signupLink != null;
		_signupLink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start the Signup activity
				Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
				startActivityForResult(intent, REQUEST_SIGNUP);
				overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
			}
		});
	}

	public void login() {
		if (!validate()) {
			onLoginFailed();
			return;
		}
		_loginButton.setEnabled(false);

		String email = _emailText.getText().toString();
		String password = _passwordText.getText().toString();

		user = new User(email, password);
		new LoginTask().execute();
	}

	private class LoginTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Authenticating...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... voids) {

			MyApiEndpointInterface apiService = ServiceGenerator.createService(MyApiEndpointInterface.class);
			Response<User> response;
			try {
				response = apiService.get_token(user).execute();

				if (response.isSuccessful()) {
					user = response.body();
					SharedPreferences.Editor editor = getSharedPreferences("user_detail", MODE_PRIVATE).edit();
					editor.putString("auth_token", user.getAuthToken());
					editor.putInt("user_id", user.getId());
					editor.putString("user_name", user.getName());
					editor.putString("user_email", user.getEmail());
					editor.apply();
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				_this.runOnUiThread(new Runnable() {
					public void run() {
						onLoginFailed();
					}
				});
				return null;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result != null) {
				if (result) {
					onLoginSuccess();
				} else {
					onVerificationFailed();
				}
			}
			progressDialog.dismiss();
			super.onPostExecute(result);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_SIGNUP) {
			if (resultCode == RESULT_OK) {
				onLoginSuccess();
			}
		}
	}

	public void onLoginSuccess() {
		_loginButton.setEnabled(true);
		Intent intent = new Intent(getApplicationContext(), PublicRoomActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void onLoginFailed() {
		Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
		_loginButton.setEnabled(true);
	}

	public void onVerificationFailed() {
		_emailWrapper.setErrorEnabled(true);
		_emailWrapper.setError("The email you entered don't match.");

		_passwordWrapper.setErrorEnabled(true);
		_passwordWrapper.setError("The password you entered don't match.");

		_loginButton.setEnabled(true);
	}

	public boolean validate() {
		boolean valid = true;
		String email = _emailText.getText().toString();
		String password = _passwordText.getText().toString();

		if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			_emailWrapper.setError("enter a valid email address");
			valid = false;
		} else {
			_emailWrapper.setError(null);
			_emailWrapper.setErrorEnabled(false);
		}

		if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
			_passwordWrapper.setError("between 4 and 20 alphanumeric characters");
			valid = false;
		} else {
			_passwordWrapper.setError(null);
			_passwordWrapper.setErrorEnabled(false);
		}

		return valid;
	}
}
