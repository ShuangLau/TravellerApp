package com.example.traveller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class Scenery {
	
	private String scenName;  // ���ξ��������
	private String path;      // ͼƬ��url 
	private String comment;   // ���ξ��������
	public static final int type = 13351; // ��ǰ����ı��
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
