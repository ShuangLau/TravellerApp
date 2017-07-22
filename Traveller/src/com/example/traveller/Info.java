package com.example.traveller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Info implements Serializable
{
	private static final long serialVersionUID = -1010711775392052966L;
	private double latitude;  
	private double longitude;
	
	private int imgId;      // 定义图片的ID
	private String name;    // 商家的名称
	private String distance;  // 距离
	private int zan;          // 点赞数

	/*
	 * 
	 * 
	 * */
	// 这里的图片是静态加载的
	public static List<Info> infos = new ArrayList<Info>();

	static
	{
		infos.add(new Info(22.35542, 113.587021, R.drawable.gym, "体育馆",
				"距离952米", 50));
		infos.add(new Info(22.349883, 113.595542, R.drawable.libary, "图书馆",
				"距离250米", 456));
		infos.add(new Info(22.354977, 113.595883, R.drawable.teachingbuilding, "教学楼",
				"距离276米", 19));
		infos.add(new Info(22.358913, 113.588692, R.drawable.dormitory, "榕园宿舍",
				"距离1000米", 0));
	}

	public Info(double latitude, double longitude, int imgId, String name,
			String distance, int zan)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.imgId = imgId;
		this.name = name;
		this.distance = distance;
		this.zan = zan;
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

	public int getImgId()
	{
		return imgId;
	}

	public void setImgId(int imgId)
	{
		this.imgId = imgId;
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

	public void setZan(int zan)
	{
		this.zan = zan;
	}

}