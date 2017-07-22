package myFragment;



import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.traveller.Info;
import com.example.traveller.MyOrientationListener;
import com.example.traveller.MyOrientationListener.OnOrientationListener;
import com.example.traveller.R;

public class MYMapFragment extends Fragment 
			implements BDLocationListener, OnMapClickListener, OnMarkerClickListener{

	
	/*----------百度地图------------*/
	// 定位相关
    LocationClient mLocationClient;
   // MyLocationListener mLocationListener;
    boolean isFirstIn = true; // 是否首次定位
    
    // 记录当前位置
    private double mLatitude;
    private double mLongtitude;
    
    //自定义图标
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    
    //传感器
    private double mCurrentX; //用于记录传感器X方向的值
    MapView mMapView;
    BaiduMap mBaiduMap; 

    Context myContext;
    
    // 覆盖物
	private BitmapDescriptor mMarker;
	private RelativeLayout mMarkerLy;
	private View view;
	
	// 显示图片内容
	private ImageView zanImage;

    /*-----------END 百度地图---------------*/
	
	private Spinner mapStyle;
	private String[] spinnerContent = {"定位","附近的人","导航"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 view = inflater.inflate(R.layout.baidumaplayout,container,false);
         myContext = getActivity();           

         
         // 初始化view定位坐标
         initView(view);
         initLocation();
         //initMarker(view);
         
     	/**----------------开始定位-----------*/
         // 定位的功能开启
     	mBaiduMap.setMyLocationEnabled(true);
     	if( !mLocationClient.isStarted())
     		mLocationClient.start();

     	//开启方向传感器
     	myOrientationListener.start();
     	
     	// 定位到当前位置
     	centerToMyLocation();
         
         /*--------------访问临近点-------------*/
         /*
         Place place = new Place(1.0000,2.0000);
         NeighnbourRunnable neigh = new NeighnbourRunnable(place);
         new Thread(neigh).start();
         */
     	//addOverlays(Info.infos);
     	
     	setSpinner(view);
        return view;
	}
	
	 @Override
		public boolean onMarkerClick(Marker marker)
		{

			Bundle extraInfo = marker.getExtraInfo();
			Info info = (Info) extraInfo.getSerializable("info");
			final ImageView iv = (ImageView) mMarkerLy
					.findViewById(R.id.id_info_img);
			// 点赞功能
			final ImageView zanImage = (ImageView)mMarkerLy.findViewById(R.id.zanImage);
			zanImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					zanImage.setImageResource(R.drawable.redrr);
					
				}
			});
			
			TextView distance = (TextView) mMarkerLy
					.findViewById(R.id.id_info_distance);
			TextView name = (TextView) mMarkerLy
					.findViewById(R.id.id_info_name);
			TextView zan = (TextView) mMarkerLy
					.findViewById(R.id.id_info_zan);
			iv.setImageResource(info.getImgId());
			distance.setText(info.getDistance());
			name.setText(info.getName());
			zan.setText(info.getZan() + "");
			
			InfoWindow infoWindow;
			TextView tv = new TextView(myContext);
			tv.setBackgroundResource(R.drawable.location_tips);
			tv.setPadding(30, 20, 30, 50);// 大小
			tv.setText(info.getName());
			tv.setTextColor(Color.parseColor("#ffffff"));

			final LatLng latLng = marker.getPosition();
			android.graphics.Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
			p.y -= 47;

			LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);

			infoWindow = new InfoWindow(tv, ll, 1);

			mBaiduMap.showInfoWindow(infoWindow);
					
			mMarkerLy.setVisibility(View.VISIBLE);
			return true;
		}
	    
	    // 点击地图其他位置，使地图覆盖物消失
		@Override
		public void onMapClick(LatLng arg0) {
			mMarkerLy.setVisibility(View.GONE);
			mBaiduMap.hideInfoWindow();
			
		}
		@Override
		public boolean onMapPoiClick(MapPoi arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	    
	    
		private void initMarker(View view)
		{
			mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
			mMarkerLy = (RelativeLayout) view.findViewById(R.id.id_maker_ly);
			
			// 设置点击图片的监听器，点击其他位置让图标消失
			mBaiduMap.setOnMapClickListener(this);
			// 设置点击覆盖物的jiantingqi
			mBaiduMap.setOnMarkerClickListener(this);
			
	    	// 首先清除图标
	    	mBaiduMap.clear();
			
			
		}
		
		/**
		 * 添加覆盖物
		 * 
		 * @param infos
		 */
		private void addOverlays(List<Info> infos)
		{
			mBaiduMap.clear();
			LatLng latLng = null;
			Marker marker = null;
			OverlayOptions options;  // 图标
			for (Info info : infos)
			{
				// 经纬度
				latLng = new LatLng(info.getLatitude(), info.getLongitude());
				// 图标
				options = new MarkerOptions().position(latLng).icon(mMarker)
						.zIndex(5);
				marker = (Marker) mBaiduMap.addOverlay(options);
				
				// 要将info进行添加
				Bundle arg0 = new Bundle();
				arg0.putSerializable("info", info);
				marker.setExtraInfo(arg0);
			}
			// 将图层添加到当前图层
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.setMapStatus(msu);
			


		}
	    
	    //初始化定位
	    private void initLocation()
		{
	    	// 这里要如何天剑内容
			mLocationClient = new LocationClient(myContext);
			//mLocationListener = new MyLocationListener();
			mLocationClient.registerLocationListener(this);

			LocationClientOption option = new LocationClientOption();
			option.setCoorType("bd09ll");
			option.setIsNeedAddress(true);  // 能够得到地址
			option.setOpenGps(true);   
			option.setScanSpan(1000);       // 1秒更新一次
			
			//设置option!!!!!
			mLocationClient.setLocOption(option);
			
			//初始化图标
			mIconLocation = BitmapDescriptorFactory
					.fromResource(R.drawable.navi_map_gps_locked);
			
			// 设置传感器的监听器
			myOrientationListener = new MyOrientationListener(myContext);
			
			myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
				
				@Override
				public void onOrientationChanged(float x) {
					mCurrentX = x;
				}
			});

		}

		//初始化百度地图
	    private void initView(View view) {
	        mMapView = (MapView) view.findViewById(R.id.bmapView); 
	        mBaiduMap = mMapView.getMap();
	        
	        //设置标尺,500米
	        MapStatusUpdate msu2 = MapStatusUpdateFactory.zoomBy(500);
	        mBaiduMap.setMapStatus(msu2);
	        Log.e("初始化地图", "ini"+mLatitude+mLongtitude);
		}

		/**
		 * 定位到我的当前位置
		 */
		private void centerToMyLocation() {
			LatLng latLng = new LatLng(mLatitude,mLongtitude);
			
			Log.e("我的当前位置", "Location"+mLatitude+mLongtitude);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.animateMapStatus(msu);
		}
	    
	    
	    /**定位SDK监听函数
	     * 百度地图
	     */

		@Override
		public void onReceiveLocation(BDLocation location)
		{
			// 设置当前地图的状态
			MyLocationData data = new MyLocationData.Builder()//
					.direction((float) mCurrentX)      //设置当前的X的方向
		    		.accuracy(location.getRadius())    //显示半径
					.latitude(location.getLatitude())  //将精度从MyLocation的类中得到；
					.longitude(location.getLongitude()) //
					.build();
			mBaiduMap.setMyLocationData(data);

			//记录当前位置
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			
			// 设置图标
			MyLocationConfiguration config = new MyLocationConfiguration
					(com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, 
							true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);
			
			if (isFirstIn)
			{
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;
				
				Log.e("FirstIn", "FirstIN");
				
				// 显示当前位置
				Toast.makeText(myContext,
						location.getAddrStr(),
						Toast.LENGTH_SHORT ).show();
			}

		}

	    void clearMark()
	    {
	    	mBaiduMap.clear();
	    }
	    
	    void locationOption()
	    {
	    	initLocation();
	     	mBaiduMap.setMyLocationEnabled(true);
	     	if( !mLocationClient.isStarted())
	     		mLocationClient.start();

	     	//开启方向传感器
	     	myOrientationListener.start();
	     	
	     	// 定位到当前位置
	     	centerToMyLocation();
	    }
	    
		public void setSpinner(final View view)
		{
	         mapStyle = (Spinner)view.findViewById(R.id.mapspinner);
	         mapStyle.setOnItemSelectedListener(new OnItemSelectedListener() 
	         {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					switch(position)
					{
						case 0:
							clearMark();
							initLocation();
							break;
						case 1:
							Toast.makeText(getActivity(), "附近的人", Toast.LENGTH_SHORT).show();
							clearMark();
							initMarker(view);
							addOverlays(Info.infos);
							break;
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
	         });

	     }
	
}
