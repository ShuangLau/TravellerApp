package com.example.traveller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

public class ScenryDownload implements Runnable{
	private final String TAG = "ScenryDown";
	public static final int SCENRYDOWN = 1;
	public List<Place> neighbour = new ArrayList<Place>();
	private JSONObject js=null;
	public ScenryDownload(){}
	public void run()
	{
	
		Socket socket = null;
		try 
		{	
			socket = new Socket("139.129.30.100",11331);
			// �趨�����������
			JSONObject nowLocation = new JSONObject();
			nowLocation.put("id", "6");
			
			// �����������������Ϣ
			PrintWriter out = new PrintWriter( new BufferedWriter( 
					new OutputStreamWriter(socket.getOutputStream())),true);      
			out.println(nowLocation.toString());
			out.flush();
			
			// �������Է���������Ϣ
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			String jsonString = in.readLine(); 
			js = new JSONObject(jsonString);
		
			// ֪ͨ����UI
			Message message = new Message();
			message.what = SCENRYDOWN;
			message.obj = jsonString;
			MainActivity.handler.sendMessage(message);		
			
			//�ر���
			out.close();
			in.close();
			//�ر�Socket
			socket.close();
			

			
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	
	}



	
}
