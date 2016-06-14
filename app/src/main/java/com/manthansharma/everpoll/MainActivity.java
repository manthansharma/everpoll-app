package com.manthansharma.everpoll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
	public static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (getIntent().getBooleanExtra("Exit", false)) {
			finish();
			return;
		}

		final ImageView imageView = (ImageView) findViewById(R.id.main_activity_logo);

		SharedPreferences sharedPreferences = this.getSharedPreferences("user_detail", MODE_PRIVATE);
		final String auth_token = sharedPreferences.getString("auth_token", null);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);

					if (auth_token == null) {
						Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					} else {
						Intent intent = new Intent(getApplicationContext(), PublicRoomActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}
}
