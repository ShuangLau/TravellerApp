package myFragment;

import myActivity.Appeal;
import myActivity.CheerDetail;
import myActivity.MyTour;
import myActivity.My_Personal_Design;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveller.R;


public class Appointment extends Fragment implements OnClickListener{

	private ImageButton appearBnt;  // 发起活动
	private ImageButton cheerBnt;   // 最热的活动
	private ImageButton myTour;   // 个人创建的活动
	private ImageButton myRecommand; //我的请求 
	private View view = null;
	private TextView callText;
	private TextView hotText;
	private TextView myTourText;
	private TextView personalText;
	private Gallery gallery;
	//TODO 需要添加新的类来存储附近的人
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fg2_content, container, false);
        
        setGallery(view);
        
        callText = (TextView)view.findViewById(R.id.textView1);
        hotText  = (TextView)view.findViewById(R.id.textView2);
        myTourText = (TextView)view.findViewById(R.id.textView3);
        personalText = (TextView)view.findViewById(R.id.textView4);
        Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/rabit.ttf");
        callText.setTypeface(fontFace);
        hotText.setTypeface(fontFace);
        myTourText.setTypeface(fontFace);
        personalText.setTypeface(fontFace);
        
        appearBnt = (ImageButton)view.findViewById(R.id.appearBnt);
        cheerBnt = (ImageButton)view.findViewById(R.id.cheerBnt);
        myTour = (ImageButton)view.findViewById(R.id.myTourBnt);
        myRecommand = (ImageButton)view.findViewById(R.id.personnalDemand);
        appearBnt.setOnClickListener(this);
        cheerBnt.setOnClickListener(this);
        myTour.setOnClickListener(this);
        myRecommand.setOnClickListener(this);
        
        return view;
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId())
		{
			case R.id.appearBnt:
				Intent intent = new Intent(getActivity(), Appeal.class);
				startActivity(intent);
				break;
			case R.id.cheerBnt:
				Intent intentCheer = new Intent(getActivity(),CheerDetail.class);
				startActivity(intentCheer);
				break;
			case R.id.myTourBnt:
				Intent intent2 = new Intent(getActivity(), MyTour.class);
				startActivity(intent2);
				break;
			case R.id.personnalDemand:
				Intent personIntent = new Intent(getActivity(),My_Personal_Design.class);
				startActivity(personIntent);
				break;
		}
		
	}
	
	void setGallery(View view)
	{
		 gallery = (Gallery)view.findViewById(R.id.gallery1);
		 gallery.setAdapter(new ImageAdapter(getActivity()));
		 
		 gallery.setOnItemClickListener(new OnItemClickListener()
		 {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,   
                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,   
                                Toast.LENGTH_LONG).show();   
				
			}
			 
			 
		 });

	}
	
	 //定义继承BaseAdapter的匹配器   
    public class ImageAdapter extends BaseAdapter {   

            //Item的修饰背景   
            int mGalleryItemBackground;   

            //上下文对象   
            private Context mContext;   

            //图片数组   
            private Integer[] mImageIds = { R.drawable.wastasenia,   
                            R.drawable.pu, R.drawable.beach};   

            //构造方法   
            public ImageAdapter(Context c){   
                    mContext = c;   
                    //读取styleable资源   
                    
            //TypedArray a = obtainStyledAttributes(R.styleable.HelloGallery);   
            //mGalleryItemBackground = a.getResourceId(   
              //      R.styleable.HelloGallery_android_galleryItemBackground, 0);   
            //a.recycle();   

            }   

            //返回项目数量   
            @Override  
            public int getCount() {   
                    return mImageIds.length;   
            }   

            //返回项目   
            @Override  
            public Object getItem(int position) {   
                    return position;   
            }   

            //返回项目Id   
            @Override  
            public long getItemId(int position) {   
                    return position;   
            }   

            //返回视图   
            @Override  
            public View getView(int position, View convertView, ViewGroup parent) {   

                    ImageView iv = new ImageView(mContext);   
                    iv.setImageResource(mImageIds[position]);   
                    //给生成的ImageView设置Id，不设置的话Id都是-1   
                    iv.setId(mImageIds[position]);   
                    //iv.setLayoutParams(new Gallery.LayoutParams(120, 160));   
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);   
                    iv.setBackgroundResource(mGalleryItemBackground);   
                    return iv;   
            }   

    }   
	

	
	
}
