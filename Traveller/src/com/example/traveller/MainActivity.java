package com.example.traveller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myFragment.Appointment;
import myFragment.MYMapFragment;
import myFragment.PersonalInformation;
import myFragment.Recommend;

import org.json.JSONException;
import org.json.JSONObject;

import adapterHdy.ScenryAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;


@SuppressLint("NewApi") 
public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener{

	public static final int DOWNLOAD = 3;
	public static final int SCRENCYID = 13351;
	public static int scenryNumDownloaded = 0;
	
	public static final int PLACEID = 13352;
	public static int placeNumDownloaded = 0;
	
	private static final int PHOTO_REQUEST_CAMERA = 13;
	
	private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;
    
    private TextView textTop;
    
    //Fragment Object
    private static Recommend fg1=null;
	private static Appointment fg2=null;
	private static MYMapFragment fg3=null;
	private static PersonalInformation fg4=null;
    private static FragmentManager fManager;
    static Context myContext;
    
    /*------------Head image-------------------*/
    private  ImageView head_image;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
	private static final int PHOTO_REQUEST_GALLERY = 2;
	private static final int PHOTO_REQUEST_CUT = 3;
	File tempFile = new File(Environment.getExternalStorageDirectory()+ "/Postcard",getPhotoFileName());
	private String getPhotoFileName() {
	Date date = new Date(System.currentTimeMillis());
	SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
	return dateFormat.format(date) + ".jpg";
	}
	private void showDialog() {
	new AlertDialog.Builder(this)
	.setTitle("请选择头像")
	.setPositiveButton("启动相机", new DialogInterface.OnClickListener() {

	@Override
	public void onClick(DialogInterface dialog, int which) {
	dialog.dismiss();
	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	intent.putExtra(MediaStore.EXTRA_OUTPUT,
	Uri.fromFile(tempFile));
	startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
	}
	})
	.setNegativeButton("从相册选取", new DialogInterface.OnClickListener() {

	@Override
	public void onClick(DialogInterface dialog, int which) {
	dialog.dismiss();
	Intent intent = new Intent(Intent.ACTION_PICK, null);
	intent.setDataAndType(
	MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	"image/*");
	startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}
	}).show();
	}
    /*-------------Handler----------------*/
    public static List<Place> neighbour = new ArrayList<Place>();
    public static List<Scenery> scenry = new ArrayList<Scenery>();
    static Bitmap bit=null;
    private final static String TAG = "MainAcitityDownload";
    private static int scenry_size=3;
    private static final int OUTACTIVITY = 65656;
    
	public static Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			
				// 下载第一个界面的内容
				case ScenryDownload.SCENRYDOWN:
					try {
						JSONObject scenryJs = new JSONObject(msg.obj.toString());
						Log.e("MainActivityScenDown", scenryJs.toString());
						getScenry(scenryJs);
						
					} catch (JSONException e) {
						Log.e("MainActivity", "SCENJson");
						e.printStackTrace();
					}
					break;
					
				// 处理相邻点的距离
				case NeighnbourRunnable.NEIGHBOUR:
					try {
						JSONObject neighJs = new JSONObject(msg.obj.toString());
						getNeighbour(neighJs);
					} catch (JSONException e) {
						Log.e("MainActivity", "SCENJson");
						e.printStackTrace();
					}
					break;
				
				case SCRENCYID:
					
					scenry.get(scenryNumDownloaded).setBitmap( scalingBitmapSize(bit));
					scenryNumDownloaded++;
					// 对bitmap进行放缩，来适应屏幕大小
					if(bit!=null && scenry_size == scenryNumDownloaded){
						FragmentTransaction fTransaction = fManager.beginTransaction();
						Recommend myFragment = fg1;
						
						View firstFrgmentView = fg1.getView();
						ListView listView = (ListView) firstFrgmentView.findViewById(android.R.id.list);
						final ScenryAdapter adapter = new ScenryAdapter(myContext, R.layout.list_item_sample,scenry);
			            listView.setAdapter(adapter);
										
					}

					break;
				case PLACEID:
					placeNumDownloaded++;
					
					if(placeNumDownloaded==scenry.size() && scenry.size()!=0)
					{
			            
					}
					break;
				case OUTACTIVITY:
					
					break;
			}
			
			super.handleMessage(msg);
		}
		
	};
	private static Bitmap scalingBitmapSize(Bitmap bitmap) {
		
		int width = bitmap.getWidth(); 
		int height = bitmap.getHeight(); 
		
		// 通过matrix来扩增bitmap
		Matrix matrix = new Matrix(); 
		// x轴方向放大 10倍， y轴放大8倍
		matrix.preScale(5.0f, 4.0f); 
		
		// 创建新的bitmap
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}
	
    
    /*------------------------------------*/
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);	
		textTop = (TextView)findViewById(R.id.txt_topbar);
		myContext = this;
		
		// 创建四个Fragment
		fManager = getFragmentManager();
		FragmentTransaction fTransaction = fManager.beginTransaction();
		fg1 = new Recommend();
        fTransaction.add(R.id.ly_content,fg1);
        fg2 = new Appointment();
        fTransaction.add(R.id.ly_content,fg2);
        fg3 = new MYMapFragment();
        fTransaction.add(R.id.ly_content,fg3);
        fg4 = new PersonalInformation();
        fTransaction.add(R.id.ly_content,fg4);
        fTransaction.commit();
        
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        //获取第一个单选按钮，并设置其为选中状态
        rb_channel = (RadioButton) findViewById(R.id.rb_channel);
        rb_channel.setChecked(true);
	    
        
	}
	
	void get_head_image()
	{	
		View view = fg4.getView();
		
		head_image = (ImageView)view.findViewById(R.id.user_avatar);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		get_head_image();
		switch (requestCode) 
		{
			case PHOTO_REQUEST_TAKEPHOTO:
				startPhotoZoom(Uri.fromFile(tempFile), 150);
				Log.e("MainActivity","获取图片");
				break;
		
			case PHOTO_REQUEST_GALLERY:
				if (data != null){
					startPhotoZoom(data.getData(), 150);
					Log.e("MainActivity","获取图库图片成功");
				}else{
					Log.e("MainActivity","获取图库图片失败");
				}
				break;
	
			case PHOTO_REQUEST_CUT:
				if (data != null){
					setPicToView(data);
					Log.e("MainActivity","裁减图片成功");
				}else{
					Log.e("MainActivity","裁减图库图片失败");
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void startPhotoZoom(Uri uri, int size) {
	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, "image/*");
	intent.putExtra("crop", "true");

	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);

	intent.putExtra("outputX", size);
	intent.putExtra("outputY", size);
	intent.putExtra("return-data", true);

	startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	@SuppressWarnings("deprecation")
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) 
		{
			Bitmap photo = bundle.getParcelable("data");
			head_image.setImageBitmap(photo);
		}
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId){
            case R.id.rb_channel:
            	textTop.setText("旅游推荐");
                fTransaction.show(fg1);
                Log.e("Tag fg1", "show");
                
                break;
            case R.id.rb_message:
            	textTop.setText("活动");
                    fTransaction.show(fg2);   
                    Log.e("Tag fg2", "show");
                break;
            case R.id.rb_better:
            	textTop.setText("地图");
                    fTransaction.show(fg3);
                    Log.e("Tag fg3", "show");
                break;
            case R.id.rb_setting:
            	textTop.setText("个人主页");
                fTransaction.show(fg4);
                Log.e("Tag fg4", "show");
                break;
        }
        fTransaction.commit();
    }
	
	
	
	
    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
        if(fg3 != null)fragmentTransaction.hide(fg3);
        if(fg4 != null)fragmentTransaction.hide(fg4);
    }

	public static void getNeighbour(JSONObject js)
	{
		int order = 0;
		double lati, longi;
		String name, path, address, distance;
		int zan;
		for(int i=0;i<4;i++)
		{
			try {
				lati = js.getDouble("point"+order+"lati");
				longi = js.getDouble("point"+order+"longi");
				name = js.getString("point"+order+"name");
				path = "http://42.96.208.106/" + js.getString("point"+order+"path");
				zan = js.getInt("point"+order+"zan");
				address = js.getString("point"+order+"address");
				distance = js.getString("point"+order+"distance");
				neighbour.add(new Place(lati, longi, path, name, distance, zan, address));
				downloadImage(path, 13352);
				order++;
			} catch (JSONException e) {
				Log.e("Neighbour", "jsonChange");
				e.printStackTrace();
			}
		}
	}
	
	public static void getScenry(JSONObject js)
	{
		int order = 0;      // 计数器
		String scenName;    // 景物图片名称
		String path;        // 图片路径 
		String comment;     // 景点评论
		for(int i=0;i<3;i++)
		{
			try {
				scenName = js.getString("point"+order+"scenName");
				path = "http://139.129.30.100/" + js.getString("point"+order+"path_scen");
				comment = js.getString("point"+order+"comment_scen");
				Scenery sc = new Scenery(scenName,path,comment);
				scenry.add(sc);
				downloadImage(path, 13351);
			} catch (JSONException e) {
				Log.e("Neighbour", "jsonChange");
				e.printStackTrace();
			}
			order++;
		}
		
	}
    
	static void downloadImage(final String url, final int type){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				URL url_download;
				
				try {
					Log.e("down", url);
					url_download = new URL(url);
					bit = BitmapFactory.decodeStream(url_download.openStream()); 
					Log.e("download", url);
				} catch (MalformedURLException e1) {
					Log.e(TAG, "连接失败");
					e1.printStackTrace();
				} catch (IOException e) {
					Log.e(TAG, "sssss");
					e.printStackTrace();
				}
				
				Message message = new Message();
				message.what = type;
				message.obj = bit;
				handler.sendMessage(message);
				
			}
		}).start();
	}

	

	
}
