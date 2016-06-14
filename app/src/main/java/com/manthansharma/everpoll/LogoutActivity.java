package com.manthansharma.everpoll;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LogoutActivity extends Activity {
	public static final String TAG = LogoutActivity.class.getSimpleName();


	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences.Editor editor = getSharedPreferences("user_detail", MODE_PRIVATE).edit();
		editor.remove("auth_token");
		editor.remove("user_id");
		editor.remove("user_name");
		editor.remove("user_email");
		editor.apply();
	}
}
