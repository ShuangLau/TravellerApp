package myActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.traveller.R;
import com.example.traveller.R.id;
import com.example.traveller.R.layout;
import com.example.traveller.R.menu;

import localdatabase.AppealDatabaseHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import dataType.Tour;

public class MyTour extends Activity implements OnItemClickListener{

	private ListView listview;
	List<Tour> mDataList = new ArrayList<Tour>();
	private ImageView backgroundImage;
	private Intent extIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_tour);
		
		extIntent = getIntent();
		
		listview = (ListView)findViewById(R.id.listView1);
		getData();		
		initList();
		setImage();
		//MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(getApplication(), data, resource, from, to)
		
	}
	private void setImage()
	{
		backgroundImage = (ImageView)findViewById(R.id.imageView1);
	
	}
	private void getData() {
		mDataList.clear();
		AppealDatabaseHelper appeal = new AppealDatabaseHelper(getApplication());
		Cursor c = appeal.query();
		while(c.moveToNext())
		{
			String user_name = c.getString(c.getColumnIndex("user_name"));
			String acname = c.getString(c.getColumnIndex("acname"));
			String destination = c.getString(c.getColumnIndex("destination"));
			String type = c.getString(c.getColumnIndex("type"));
			String date = c.getString(c.getColumnIndex("date"));
			String budget = c.getString(c.getColumnIndex("budget"));
			String description = c.getString(c.getColumnIndex("description"));
			mDataList.add(new Tour(acname,destination,type,budget,date,description));
		
		}
		c.close();
	}


	void initList()
	{
		MySimpleAdapter adapter = new MySimpleAdapter(getApplication(), R.layout.tour_listview,
				mDataList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					int position, long id) {
				
					
					final int pos = position;
					AlertDialog.Builder builder = new AlertDialog.Builder(MyTour.this);
					
					
					
					builder.setTitle("提示")
						   .setMessage("请确认是否删除")
					   	   .setPositiveButton("确认", new DialogInterface.OnClickListener() {
						
						   		@Override
						   		public void onClick(DialogInterface dialog, int which) 
						   		{
						   			String acname = ((TextView) listview.getChildAt(pos)
						   					.findViewById(R.id.tourDetail)).getText().toString();
						   			
						   			Tour tour = new Tour(acname,"","","","","");
						   			
						   			//删除数据库的内容
						   			AppealDatabaseHelper dbHelper = new AppealDatabaseHelper(getApplication());
						   			int delteRow = dbHelper.delete(tour);
						   			
						   			mDataList.remove(pos);
						   			((MySimpleAdapter)listview.getAdapter()).notifyDataSetChanged();
								
						   		}
					   	   })
					   	   .setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {}})
					   	   .show();
				return false;
			}
		});
	}
	
	class MySimpleAdapter extends ArrayAdapter<Tour>{

		private int resourceId;
		
		public MySimpleAdapter(Context context, int resource, List<Tour> objects) {
			super(context, resource, objects);
			resourceId = resource;
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Tour tour = getItem(position);
			View view = LayoutInflater.from(this.getContext())
					.inflate(resourceId, null);
			TextView text = (TextView)view.findViewById(R.id.tourDetail);
			text.setText(tour.getAcname());
			return view;
		}



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_tour, menu);
		return true;
	}

	void update()
	{
		getData();	
		((MySimpleAdapter)listview.getAdapter()).notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		update();
		
	}

	@Override
	public void onItemClick(AdapterView<?> adpter, View view, int position, long id) {
		final View mView = LayoutInflater.from(getApplication()).inflate(R.layout.tour_listview, null);
		TextView text = (TextView)mView.findViewById(R.id.tourDetail);
		
		Intent intent = new Intent(this,Appeal.class);
		
		
		
	}


	
	
}
