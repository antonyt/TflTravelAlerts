package com.tfltravelalerts;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.EditText;

import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		EditText holoEditText = (EditText) findViewById(R.id.holo_edittext);
		holoEditText.setText("Holo test");
	}
}
