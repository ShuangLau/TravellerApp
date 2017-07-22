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

	
	/*----------�ٶȵ�ͼ------------*/
	// ��λ���
    LocationClient mLocationClient;
   // MyLocationListener mLocationListener;
    boolean isFirstIn = true; // �Ƿ��״ζ�λ
    
    // ��¼��ǰλ��
    private double mLatitude;
    private double mLongtitude;
    
    //�Զ���ͼ��
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    
    //������
    private double mCurrentX; //���ڼ�¼������X�����ֵ
    MapView mMapView;
    BaiduMap mBaiduMap; 

    Context myContext;
    
    // ������
	private BitmapDescriptor mMarker;
	private RelativeLayout mMarkerLy;
	private View view;
	
	// ��ʾͼƬ����
	private ImageView zanImage;

    /*-----------END �ٶȵ�ͼ---------------*/
	
	private Spinner mapStyle;
	private String[] spinnerContent = {"��λ","��������","����"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 view = inflater.inflate(R.layout.baidumaplayout,container,false);
         myContext = getActivity();           

         
         // ��ʼ��view��λ����
         initView(view);
         initLocation();
         //initMarker(view);
         
     	/**----------------��ʼ��λ-----------*/
         // ��λ�Ĺ��ܿ���
     	mBaiduMap.setMyLocationEnabled(true);
     	if( !mLocationClient.isStarted())
     		mLocationClient.start();

     	//�������򴫸���
     	myOrientationListener.start();
     	
     	// ��λ����ǰλ��
     	centerToMyLocation();
         
         /*--------------�����ٽ���-------------*/
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
			// ���޹���
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
			tv.setPadding(30, 20, 30, 50);// ��С
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
	    
	    // �����ͼ����λ�ã�ʹ��ͼ��������ʧ
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
			
			// ���õ��ͼƬ�ļ��������������λ����ͼ����ʧ
			mBaiduMap.setOnMapClickListener(this);
			// ���õ���������jiantingqi
			mBaiduMap.setOnMarkerClickListener(this);
			
	    	// �������ͼ��
	    	mBaiduMap.clear();
			
			
		}
		
		/**
		 * ��Ӹ�����
		 * 
		 * @param infos
		 */
		private void addOverlays(List<Info> infos)
		{
			mBaiduMap.clear();
			LatLng latLng = null;
			Marker marker = null;
			OverlayOptions options;  // ͼ��
			for (Info info : infos)
			{
				// ��γ��
				latLng = new LatLng(info.getLatitude(), info.getLongitude());
				// ͼ��
				options = new MarkerOptions().position(latLng).icon(mMarker)
						.zIndex(5);
				marker = (Marker) mBaiduMap.addOverlay(options);
				
				// Ҫ��info�������
				Bundle arg0 = new Bundle();
				arg0.putSerializable("info", info);
				marker.setExtraInfo(arg0);
			}
			// ��ͼ����ӵ���ǰͼ��
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.setMapStatus(msu);
			


		}
	    
	    //��ʼ����λ
	    private void initLocation()
		{
	    	// ����Ҫ����콣����
			mLocationClient = new LocationClient(myContext);
			//mLocationListener = new MyLocationListener();
			mLocationClient.registerLocationListener(this);

			LocationClientOption option = new LocationClientOption();
			option.setCoorType("bd09ll");
			option.setIsNeedAddress(true);  // �ܹ��õ���ַ
			option.setOpenGps(true);   
			option.setScanSpan(1000);       // 1�����һ��
			
			//����option!!!!!
			mLocationClient.setLocOption(option);
			
			//��ʼ��ͼ��
			mIconLocation = BitmapDescriptorFactory
					.fromResource(R.drawable.navi_map_gps_locked);
			
			// ���ô������ļ�����
			myOrientationListener = new MyOrientationListener(myContext);
			
			myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
				
				@Override
				public void onOrientationChanged(float x) {
					mCurrentX = x;
				}
			});

		}

		//��ʼ���ٶȵ�ͼ
	    private void initView(View view) {
	        mMapView = (MapView) view.findViewById(R.id.bmapView); 
	        mBaiduMap = mMapView.getMap();
	        
	        //���ñ��,500��
	        MapStatusUpdate msu2 = MapStatusUpdateFactory.zoomBy(500);
	        mBaiduMap.setMapStatus(msu2);
	        Log.e("��ʼ����ͼ", "ini"+mLatitude+mLongtitude);
		}

		/**
		 * ��λ���ҵĵ�ǰλ��
		 */
		private void centerToMyLocation() {
			LatLng latLng = new LatLng(mLatitude,mLongtitude);
			
			Log.e("�ҵĵ�ǰλ��", "Location"+mLatitude+mLongtitude);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.animateMapStatus(msu);
		}
	    
	    
	    /**��λSDK��������
	     * �ٶȵ�ͼ
	     */

		@Override
		public void onReceiveLocation(BDLocation location)
		{
			// ���õ�ǰ��ͼ��״̬
			MyLocationData data = new MyLocationData.Builder()//
					.direction((float) mCurrentX)      //���õ�ǰ��X�ķ���
		    		.accuracy(location.getRadius())    //��ʾ�뾶
					.latitude(location.getLatitude())  //�����ȴ�MyLocation�����еõ���
					.longitude(location.getLongitude()) //
					.build();
			mBaiduMap.setMyLocationData(data);

			//��¼��ǰλ��
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			
			// ����ͼ��
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
				
				// ��ʾ��ǰλ��
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

	     	//�������򴫸���
	     	myOrientationListener.start();
	     	
	     	// ��λ����ǰλ��
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
							Toast.makeText(getActivity(), "��������", Toast.LENGTH_SHORT).show();
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
