package com.example.traveller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AccessServer {
	private final String		DEBUG_TAG	= "Activity01";
	
	private TextView	mTextView=null;
	private EditText	mEditText=null;
	private Button		mButton=null;
	private WebView webview = null;
	private String path = "http://42.96.208.106/test.jpg";
	private ImageView imageview = null;
	/** Called when the activity is first created. */

	public static Bitmap getHttpBitmap(String url){  
        URL myFileURL;  
        Bitmap bitmap=null;  
        try{  
            myFileURL = new URL(url);  
            //获得连接  
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();  
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制  
            conn.setConnectTimeout(6000);  
            //连接设置获得数据流  
            conn.setDoInput(true);  
            //不使用缓存  
            conn.setUseCaches(false);  
            //这句可有可无，没有影响  
            //conn.connect();  
            //得到数据流  
            InputStream is = conn.getInputStream();  
            //解析得到图片  
            bitmap = BitmapFactory.decodeStream(is);  
            //关闭数据流  
            is.close();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
        return bitmap;  
          
    }  
}
