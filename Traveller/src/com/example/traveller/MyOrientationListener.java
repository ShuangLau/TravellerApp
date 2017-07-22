package com.example.traveller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**手机方向传感器的监听器
 * applied:百度地图
 * */
public class MyOrientationListener implements SensorEventListener{

	private Context mContext;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float lastX;  //横向的轴
	
	
	public MyOrientationListener(Context context)
	{
		this.mContext = context;
	}
	
	@SuppressWarnings("deprecation")
	public void start()
	{
		mSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
		
		if(mSensorManager!=null)
		{
			//获得方向传感器
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		
		if (mSensor != null)
		{
			// 注册Sensor
			mSensorManager.registerListener(this, mSensor,
					SensorManager.SENSOR_DELAY_UI);
		}
	}
	
	public void stop()
	{
		mSensorManager.unregisterListener(this);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType()==Sensor.TYPE_ORIENTATION)
		{
			// 得到X方向的值
			float x = event.values[SensorManager.DATA_X];
			
			// 防止更新过快，使得当X变化大于1.0时，才进行更新
			if(Math.abs(x-lastX)>1.0)
			{
				if(mOnOrientationListener != null)
				{
					// 在这里将值从接口将值传入MainActivity?????
					mOnOrientationListener.onOrientationChanged(x);
				}
				lastX = x;
			}
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	
	private OnOrientationListener mOnOrientationListener;

	public void setOnOrientationListener(
			OnOrientationListener mOnOrientationListener)
	{
		this.mOnOrientationListener = mOnOrientationListener;
	}

	public interface OnOrientationListener
	{
		void onOrientationChanged(float x);
	}
	
}

