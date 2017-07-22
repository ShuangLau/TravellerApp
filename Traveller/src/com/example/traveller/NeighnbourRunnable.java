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


public class NeighnbourRunnable implements Runnable{
	
	public static final int NEIGHBOUR = 2;
	public Place place=null;
	public List<Place> neighbour = new ArrayList<Place>();
	private JSONObject js=null;
	public NeighnbourRunnable(Place place)
	{
		this.place = place;
	}
	
	public void run()
	{
	
		Socket socket = null;
		try 
		{	
			
			// �򱾻���4700�˿��������
			socket = new Socket("42.96.208.106",11331);
			
			// ��������
			JSONObject nowLocation = new JSONObject();
			nowLocation.put("id", 5);
			
			//��ϵͳ��׼����
			PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);      
			out.println(nowLocation.toString());
			out.flush();

			System.out.println("success1");
			
			//�������Է���������Ϣ
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			System.out.println("success2");
			String jsonString = in.readLine(); 
			
			//�ر���
			out.close();
			in.close();
			//�ر�Socket
			socket.close();
			
			// ����Message
			Message message = new Message();
			message.what = NEIGHBOUR;
			message.obj = jsonString;
			MainActivity.handler.sendMessage(message);
			
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	
	}


	
}
