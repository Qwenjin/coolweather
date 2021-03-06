package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	public static void sendHttpRequest(final String address, final HttpCallbacklistener listener)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try{
					
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuffer response = new StringBuffer();
					String line;
					while((line = reader.readLine()) != null)
					{
						response.append(line);
					}
					
					if(listener != null)
					{
						//回调onFinish方法
						listener.onFinish(response.toString());
					}
					
				}catch(Exception e)
				{
					e.printStackTrace();
					if(listener != null)
					{//回调onError方法
						listener.onError(e);
					}
				}finally{
					if(connection != null)
					{
						connection.disconnect();
					}
				}
					
			}
		}).start();
		
	}

}
