package myActivity;

import java.util.ArrayList;
import java.util.List;

import localdatabase.AppealDatabaseHelper;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveller.R;

import dataType.Tour;

public class CheerDetail extends Activity {

	private ListView listview;
	private List<Tour> tour= new ArrayList<Tour>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cheer_detail);
		getData();
		init_view();
		
	}
	
	private void init_view()
	{
		listview = (ListView)findViewById(R.id.listViewCheer);
		HotListViewAdapter adapter = new HotListViewAdapter(getBaseContext(), R.layout.hot_listview, tour);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
		//list
	}
	
	private void getData()
	{
		AppealDatabaseHelper db = new AppealDatabaseHelper(getApplicationContext());
		Cursor cursor = db.query();
		while(cursor.moveToNext())
		{
			Tour t = new Tour();
			t.setAcname(cursor.getString(cursor.getColumnIndex("acname")));
			t.setBudget(cursor.getString(cursor.getColumnIndex("budget")));
			t.setDate(cursor.getString(cursor.getColumnIndex("date")));
			t.setDescription(cursor.getString(cursor.getColumnIndex("description")));
			t.setType(cursor.getString(cursor.getColumnIndex("type")));
			t.setDestination(cursor.getString(cursor.getColumnIndex("destination")));
			tour.add(t);
		}
		cursor.close();
		db.close();
	}

	class HotListViewAdapter extends ArrayAdapter<Tour>
	{
		private int id;
		private Context myContent;
		public HotListViewAdapter(Context context, int resource,
				List<Tour> objects) {
			super(context, resource, objects);
			myContent = context;
			id = resource;
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(myContent).inflate(id, null);
			TextView acname = (TextView)view.findViewById(R.id.acname);
			acname.setTextSize(18);
			TextView acDetail = (TextView)view.findViewById(R.id.acDetail);
			TextView hot_degree = (TextView)view.findViewById(R.id.hot_degree);
			TextView in_num = (TextView)view.findViewById(R.id.in_num);
			Button join  = (Button)view.findViewById(R.id.join);
			
			acDetail.setTextSize(18);
			
			hot_degree.setTextSize(12);
			in_num.setTextSize(12);
			hot_degree.setGravity(Gravity.RIGHT);
			in_num.setGravity(Gravity.RIGHT);
			
			final Tour tour = getItem(position);
			acname.setText(tour.getAcname());
			acDetail.setText(tour.getDescription());
			hot_degree.setText("热度："+(999+position)+"C");
			in_num.setText("加入人数：" + (30-position)+"人");
			
			join.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String d = "已加入"+tour.getAcname();
					Toast.makeText(CheerDetail.this, d, Toast.LENGTH_SHORT).show();
				}
			});
			
			return view;
		}
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cheer_detail, menu);
		return true;
	}

}
