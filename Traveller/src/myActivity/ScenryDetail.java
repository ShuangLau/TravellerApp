package myActivity;

import com.example.traveller.R;
import com.example.traveller.R.layout;
import com.example.traveller.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class ScenryDetail extends Activity {

	private Intent getInent = getIntent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scenry_detail);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scenry_detail, menu);
		return true;
	}

}
