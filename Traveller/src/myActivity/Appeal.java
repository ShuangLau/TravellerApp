package myActivity;

import java.util.zip.Inflater;

import localdatabase.AppealDatabaseHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveller.LoginActivity;
import com.example.traveller.MainActivity;
import com.example.traveller.R;

import dataType.Tour;

public class Appeal extends Activity {
	
	private static final int Notification_ID = 0;
	private TextView acName;
	private TextView destination;
	private TextView typeActivity;	
	private TextView budget;	
	private TextView date;
	private TextView desciption; 
	private Button create;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_appeal);
		acName = (TextView)findViewById(R.id.acName);
		destination = (TextView)findViewById(R.id.destination);
		typeActivity = (TextView)findViewById(R.id.typeActivity);
		budget = (TextView)findViewById(R.id.budget);
		date = (TextView)findViewById(R.id.date);
		desciption = (TextView)findViewById(R.id.activityDesp);
		create = (Button)findViewById(R.id.createBnt);
		create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String activityName = acName.getText().toString();
				String des = destination.getText().toString();
				String type = typeActivity.getText().toString();
				String budg = budget.getText().toString();
				String _date = date.getText().toString();
				String despt = desciption.getText().toString();
				final Tour tour = new Tour(activityName, des, type, budg, _date, despt);
				
				AppealDatabaseHelper dbHelper = new AppealDatabaseHelper(Appeal.this);
				dbHelper.insert(tour);
				Toast.makeText(getApplication(), "成功创建活动", Toast.LENGTH_SHORT).show();
				
				AlertDialog.Builder builder =  new AlertDialog.Builder(Appeal.this);
				//builder.setTitle(Window.FEATURE_NO_TITLE);
				builder.setPositiveButton("分享", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final View myView = LayoutInflater.from(Appeal.this).inflate(R.layout.add_feeling, null);
						AlertDialog.Builder addFeeling = new AlertDialog.Builder(Appeal.this);
						addFeeling.setView(myView);
						
						addFeeling.setPositiveButton("分享",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								String feeling = ((EditText)myView.findViewById(R.id.editText1)).getText().toString();
								shareData (tour, feeling);
							}
						});
						addFeeling.setNegativeButton("暂不", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {}
						});
						
						addFeeling.show();
					}
				});
				
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						notification(tour);
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
				LayoutParams params = dialog.getWindow().getAttributes();
				params.width  = 500;
				params.height  = 400;
				dialog.getWindow().setAttributes(params);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appeal, menu);
		return true;
	}
	
	// 分享内容
	private void shareData (Tour tour, String feeling) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
		
		// 新建一个Intent，类型为SEND
	    Intent mSharingIntent = new Intent(Intent.ACTION_SEND);
	    String mShareBody = new StringBuilder()
	    		.append(feeling+"\n")
	            .append(tour.getAcname()+"\n")
	            .append("活动详情: ").append(tour.getDescription())
	            .toString();
	    mSharingIntent.setType("text/plain");
	    mSharingIntent.putExtra(
	            android.content.Intent.EXTRA_SUBJECT, "Pedometer");
	    mSharingIntent.putExtra(
	            android.content.Intent.EXTRA_TEXT, mShareBody);
	    // 进行广播，发活动内容
	    startActivity(Intent.createChooser(mSharingIntent, "Share via"));
	}
	
	void notification(Tour tour)
	{
		
		
		// The first step get the manager
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManger = (NotificationManager)getSystemService(ns);
		
		// set the paramemters
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		
		mBuilder.setContentTitle("咕噜推荐");
		mBuilder.setTicker("Cheer")
				.setWhen(System.currentTimeMillis())
				.setContentText(tour.getAcname()+tour.getDescription())
				.setPriority(Notification.PRIORITY_DEFAULT)
				.setSmallIcon(R.drawable.recommand);
		
		//Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:104040444"));
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		
		Notification notify = mBuilder.build();
		notify.defaults = Notification.DEFAULT_SOUND;
		notify.contentIntent = pendingIntent;
		notificationManger.notify(Notification_ID, notify);
		
		
		
	}

	
	

}
