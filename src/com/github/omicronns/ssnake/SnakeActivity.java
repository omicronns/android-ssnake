package com.github.omicronns.ssnake;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

public class SnakeActivity extends Activity {
	
	private static SSnakeView sv;
	
	public static void snakeStep() {
		sv.snakeStep();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snake);
		sv = (SSnakeView)findViewById(R.id.snake_view);
		sv.snakeStart();
		SnakeTimer.start();
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
		if (id == R.id.snake_options) {
			SSnakeView sv = (SSnakeView)findViewById(R.id.snake_view);
			sv.restartGame();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

class SnakeTimer {
	private static boolean isStarted = false;
	private static Handler handler = new Handler();

	private static Runnable stepTimer = new Runnable() { 
		@Override
		public void run() {
			SnakeActivity.snakeStep();
			handler.postDelayed(this, 100);
		}
	};
	
	public static void start() {
		if(!isStarted) {
			handler.postDelayed(stepTimer, 0);
			isStarted = true;
		}
	}
}