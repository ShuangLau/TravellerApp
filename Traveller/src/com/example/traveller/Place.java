package com.example.traveller;

import android.graphics.Bitmap;

public class Place {

	private double latitude;   // 经度  
	private double longitude;  // 纬度
	
	private String path="";      // 定义图片的ID
	private String name="";      // 商家的名称
	private String distance="";  // 距离
	private int zan=0;          // 点赞数
	private String address="";   // 地址
	
	private int type = 13352;
	private Bitmap bit = null;

	public Place(double latitude, double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public int getType()
	{
		return type;
	}
	
	public Place(double latitude, double longitude, String path, String name,
			String distance, int zan, String address)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.path = path;
		this.name = name;
		this.distance = distance;
		this.zan = zan;
		this.address = address;
	}

	public void setBitmap(Bitmap bitmap)
	{
		bit = bitmap;
	}
	public Bitmap getBitmap(){
		return bit;
	}
	
	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public String getImgPath()
	{
		return path;
	}

	public void setImgPath(String path)
	{
		this.path = path;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDistance()
	{
		return distance;
	}

	public void setDistance(String distance)
	{
		this.distance = distance;
	}

	public int getZan()
	{
		return zan;
	}
	
	public String getAddress()
	{
		return address;
	}

	public void setZan(int zan)
	{
		this.zan = zan;
	}

}