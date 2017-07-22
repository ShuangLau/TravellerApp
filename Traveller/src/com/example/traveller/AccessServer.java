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
            //�������  
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();  
            //���ó�ʱʱ��Ϊ6000���룬conn.setConnectionTiem(0);��ʾû��ʱ������  
            conn.setConnectTimeout(6000);  
            //�������û��������  
            conn.setDoInput(true);  
            //��ʹ�û���  
            conn.setUseCaches(false);  
            //�����п��ޣ�û��Ӱ��  
            //conn.connect();  
            //�õ�������  
            InputStream is = conn.getInputStream();  
            //�����õ�ͼƬ  
            bitmap = BitmapFactory.decodeStream(is);  
            //�ر�������  
            is.close();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
        return bitmap;  
          
    }  
}
