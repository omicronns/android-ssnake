package com.github.omicronns.ssnake;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SnakeActivity extends Activity {
	SSnakeView sv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snake);
		sv = (SSnakeView)findViewById(R.id.snake_view);
//		sv.snakeStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		sv.snakeStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.snake, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.snake_restart) {
			sv.snakeRestart();
			return true;
		}
		else if (id == R.id.snake_start) {
			sv.snakeStart();
			return true;
		}
		else if (id == R.id.snake_stop) {
			sv.snakeStop();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}