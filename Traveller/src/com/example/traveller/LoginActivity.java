package com.example.traveller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {

	private Button login;
	private Button register;
	private EditText userName;
	private EditText password;
	private Context myContext;
	
	private static final String TAG = "LoginConnectDataBase";
	public static final int LONGINDATA = 4;
	protected static final int REGISTERDATA = 0;
	
	Handler handler = new Handler(){

		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			
				// 下载第一个界面的内容
				case LONGINDATA:
					loginHanderFunction(msg);
					break;
					
				case REGISTERDATA:
				registerHandlerFunc(msg);
					break;
			}
			
		}

		private void registerHandlerFunc(Message msg) {
			try {
				JSONObject scenryJs = new JSONObject(msg.obj.toString());
				String answer="";
				try {
					answer = scenryJs.getString("answer");
					// 连接数据库失败时，不进行判断
					if(answer.equals("")){
						return;
					}
						
						
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				if(answer.equals("loadsuccess"))
				{
					Toast.makeText(myContext, "注册成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(myContext, "用户名已被注册", Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				Log.e("LoginActivity", "SCENJson");
				e.printStackTrace();
			}
		}

		private void loginHanderFunction(Message msg) {
			try {
				JSONObject scenryJs = new JSONObject(msg.obj.toString());
				String answer="";
				try {
					answer = scenryJs.getString("answer");
					// 连接数据库失败时，不进行判断
					if(answer.equals("")){
						
						return;
					}
						
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(answer.equals("login_ok"))
				{
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(myContext, "密码错误或没有该账户名", Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				Log.e("LoginActivity", "SCENJson");
				e.printStackTrace();
			}
		}

		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		myContext = this;
		login = (Button)findViewById(R.id.login_btnLogin);
		register = (Button)findViewById(R.id.register_btnLogin);
		
		userName = (EditText)findViewById(R.id.login_edtId);
		password = (EditText)findViewById(R.id.login_edtPwd);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();	
				/*
				JSONObject userInfo = new JSONObject();
				try {
					userInfo.put("username", userName.getText().toString());
					userInfo.put("password", password.getText().toString());
					userInfo.put("loginType","3");
				} catch (JSONException e) {
					Log.e("Login","JSONFAIL");
					e.printStackTrace();
				}
				
				connectServer(userInfo);
				*/
			
			}
		});
		
		register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				JSONObject userInfo = new JSONObject();
				try {
					userInfo.put("username", userName.getText().toString());
					userInfo.put("password", password.getText().toString());
					userInfo.put("loginType","1");
				} catch (JSONException e) {
					Log.e("Login","JSONFAIL");
					e.printStackTrace();
				}
				
				connectServer(userInfo);
				
			}
		});
	}
	
	private void connectServer(final JSONObject js)
	{

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Socket socket = null;
				try 
				{	
					 // Log.e("First", "11331");
					socket = new Socket("42.96.208.106",11331);
					
					// 请求类型
					JSONObject userInfo = new JSONObject();
					userInfo.put("id", js.getString("loginType"));
					userInfo.put("username", js.getString("username"));
					userInfo.put("password",js.getString("password"));
					//Log.e(TAG, "success1");
					
					//由系统标准输入
					PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);      
					out.println(userInfo.toString());
					out.flush();

					//Log.e(TAG, "success2");
					
					//接收来自服务器的消息
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
					Log.e(TAG, "success3");
					String jsonString = in.readLine(); 
					Log.e(TAG, jsonString+"kkkkkk");
				
					
					//关闭流
					out.close();
					in.close();
					//关闭Socket
					socket.close();
			
					Log.e("sss", jsonString+"kkkkkk");
					// 发送Message
					Message message = new Message();
					if(js.getString("loginType").equals("3"))
						message.what = LONGINDATA;
					else
						message.what = REGISTERDATA;
					message.obj = jsonString;
			
					handler.sendMessage(message);
					
					
				}
				catch (Exception e) 
				{
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


