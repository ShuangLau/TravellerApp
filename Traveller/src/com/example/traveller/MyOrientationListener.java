package com.example.traveller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**�ֻ����򴫸����ļ�����
 * applied:�ٶȵ�ͼ
 * */
public class MyOrientationListener implements SensorEventListener{

	private Context mContext;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float lastX;  //�������
	
	
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
			//��÷��򴫸���
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		
		if (mSensor != null)
		{
			// ע��Sensor
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
			// �õ�X�����ֵ
			float x = event.values[SensorManager.DATA_X];
			
			// ��ֹ���¹��죬ʹ�õ�X�仯����1.0ʱ���Ž��и���
			if(Math.abs(x-lastX)>1.0)
			{
				if(mOnOrientationListener != null)
				{
					// �����ｫֵ�ӽӿڽ�ֵ����MainActivity?????
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

