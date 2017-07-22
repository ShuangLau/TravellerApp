package com.example.traveller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class Scenery {
	
	private String scenName;  // 旅游景点的名字
	private String path;      // 图片的url 
	private String comment;   // 旅游景点的评论
	public static final int type = 13351; // 当前服务的编号
	private Bitmap bit = null;
	
	public Scenery(String scenName, String path, String comment)
	{
		this.scenName = scenName;
		this.path = path;
		this.comment = comment;
		
	}
	public void setBitmap(Bitmap bitmap)
	{
		bit = bitmap;
	}
	public Bitmap getBitmap(){
		return bit;
	}
	
	public static int getType()
	{
		return type;
	}
	public String getScenName()
	{
		return scenName;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public String getComment()
	{
		return comment;
	}
	
	
}
