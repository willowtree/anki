package com.anki.desk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.anki.desk.R;

public class AtmosphereActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atmosphere);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.atmosphere, menu);
		return true;
	}

}
