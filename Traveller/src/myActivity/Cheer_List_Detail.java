package myActivity;

import com.example.traveller.R;
import com.example.traveller.R.layout;
import com.example.traveller.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Cheer_List_Detail extends Activity {

	private TextView tour_name;
	private TextView destination;
	private TextView description;
	private TextView date;
	private Button tour_num;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheer__list__detail);
		init_view();
	}
	
	void init_view()
	{
		tour_name = (TextView)findViewById(R.id.tour_name);
		destination = (TextView)findViewById(R.id.destination);
		description = (TextView)findViewById(R.id.description);
		date = (TextView)findViewById(R.id.date);
		tour_num = (Button)findViewById(R.id.takenNum);
		tour_num.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.cheer__list__detail, menu);
		return true;
	}

}
