package myFragment;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveller.MainActivity;
import com.example.traveller.PullScrollView;
import com.example.traveller.R;

public class PersonalInformation extends Fragment 
				implements OnItemClickListener,PullScrollView.OnTurnListener{

	private static final int PHOTO_REQUEST_CAMERA = 13;
    private PullScrollView mScrollView;
    private ImageView mHeadImg;
    private LinearLayout mMainLayout;
    private ImageView head_image;
	private View view = null;
	private static int is_click = 0;
	
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
	new AlertDialog.Builder(getActivity())
	.setTitle("请选择头像")
	.setPositiveButton("启动相机", new DialogInterface.OnClickListener() {

	@Override
	public void onClick(DialogInterface dialog, int which) {
	// TODO Auto-generated method stub
	dialog.dismiss();
	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	intent.putExtra(MediaStore.EXTRA_OUTPUT,
	Uri.fromFile(tempFile));
	getActivity().startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
	}
	})
	.setNegativeButton("从相册选取", new DialogInterface.OnClickListener() {

	@Override
	public void onClick(DialogInterface dialog, int which) {
	// TODO Auto-generated method stub
	dialog.dismiss();
	Intent intent = new Intent(Intent.ACTION_PICK, null);
	intent.setDataAndType(
	MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	"image/*");
	getActivity().startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}
	}).show();
	}
	void get_head_image(View view)
	{
		//View view = LayoutInflater.from(getActivity()).inflate(R.layout.act_pull_down, null);
		
		head_image = (ImageView)view.findViewById(R.id.user_avatar);
		head_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDialog();
				
			}
		});
		
		
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

	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) 
		{
			Bitmap photo = bundle.getParcelable("data");
			head_image.setImageBitmap(photo);
			Toast.makeText(getActivity(), "成功设置图片", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// *** 在实现功能
    	view = inflater.inflate(R.layout.act_pull_down,container,false);
    	//setHeadImage(view);
    	get_head_image(view);
        mScrollView = (PullScrollView) view.findViewById(R.id.scroll_view);
        mHeadImg = (ImageView) view.findViewById(R.id.background_img);

        mMainLayout = (LinearLayout) view.findViewById(R.id.base_info);

        mScrollView.setHeader(mHeadImg);
        mScrollView.setOnTurnListener(this);
        
        
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.leftMargin = 30;
        layoutParams.bottomMargin = 10;
        layoutParams.topMargin = 10;

        add_item(view, layoutParams);
        
        return view;
	}
	
	void setHeadImage(View view)
	{
		head_image = (ImageView)view.findViewById(R.id.user_avatar);
		head_image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				// 新修Dialog来显示选择
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				View alter = LayoutInflater.from(getActivity()).inflate(R.layout.choose_image, null);
				Button pick = (Button)alter.findViewById(R.id.button1);
				pick.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						try{
							// 激活系统图库，选择一张图片
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setType("image/*");
							// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
							getActivity().startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
							Log.e("pick","open");
						}catch(Exception e)
						{
							Log.e("Pick_picture","fail");
							e.printStackTrace();
						}
						
					}
				});
				Button take_photo = (Button)alter.findViewById(R.id.button2);
				take_photo.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try{
							
				             //拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，  
							//有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个  
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
							intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);  
							getActivity().startActivityForResult(intent, PHOTO_REQUEST_CAMERA); 
							Log.e("take","open");
							} catch (Exception e) {  
								Log.e("Take_picture","failed");
							    e.printStackTrace();  
				            } 
						
					}
				});
				Button cancel = (Button)alter.findViewById(R.id.button3);
				cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				builder.setView(alter);

				builder.show();
			}
		});
	}
	

	class MySimpleAdapter extends SimpleAdapter{

		private static final int OUTACTIVITY = 65656;

		public MySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Map<String,String> item = (Map<String, String>) getItem(position);
			if(item.values().contains("退出"))
				MainActivity.handler.sendEmptyMessage(OUTACTIVITY);
			return super.getView(position, convertView, parent);
		}
		
	};
	
	void add_item(View view,  TableRow.LayoutParams layoutParams)
	{
		
        
        TableRow row2 = new TableRow(getActivity());
        TextView save = new TextView(getActivity());
        save.setText("收藏");
        save.setTextSize(15);
        save.setPadding(15, 15, 15, 15);
        row2.addView(save,layoutParams);
        row2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity(), "收藏", Toast.LENGTH_SHORT).show();
				
			}
		});
        mMainLayout.addView(row2);
        
        
        TableRow row3 = new TableRow(getActivity());
        TextView achievement = new TextView(getActivity());
        achievement.setText("成就");
        achievement.setTextSize(15);
        achievement.setPadding(15, 15, 15, 15);
        row3.addView(achievement,layoutParams);
        row3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
			}
		});
        
        mMainLayout.addView(row3);
		
        TableRow tableRow = new TableRow(getActivity());
        TextView text = new TextView(getActivity());
        text.setText("设置");
        text.setTextSize(15);
        text.setPadding(15, 15, 15, 15);
        tableRow.addView(text,layoutParams);
        
        final ListView listView = new ListView(getActivity());
        listView.setVisibility(View.GONE);
        
        String[] array = {"退出"};
        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
        for(int i=0;i<array.length;i++)
        {
        	Map<String,String>  out = new HashMap<String,String>();
        	out.put("set", array[i]);
        	data.add(out);
        }
        
        // 使用自定义的SimpleAdapter
        MySimpleAdapter adapter = new MySimpleAdapter(getActivity(), data,
        		R.layout.list_text, 
        		new String[]{"set"},
        		new int []{R.id.list_text});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
			}
		});

        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
            	if(is_click%2==0)
            		listView.setVisibility(View.VISIBLE);
            	else
            		listView.setVisibility(View.GONE);
            	is_click++;
            }
        });
        mMainLayout.addView(tableRow);
        mMainLayout.addView(listView);
        
        TableRow row4 = new TableRow(getActivity());
        TextView help = new TextView(getActivity());
        help.setText("帮助");
        help.setTextSize(15);
        help.setPadding(15, 15, 15, 15);
        row4.addView(help,layoutParams);
        row4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity(), "帮助", Toast.LENGTH_SHORT).show();
				
			}
		});
        mMainLayout.addView(row4);

        
           
	}
	

	
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        
    	Toast.makeText(getActivity(), "Item Clicked: " + position, Toast.LENGTH_SHORT)
    			.show();
    	
    }

	@Override
	public void onTurn() {
		// TODO Auto-generated method stub
		
	}
	
}
