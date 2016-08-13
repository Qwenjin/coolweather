package com.example.coolweather.util;

public interface HttpCallbacklistener {
	void onFinish(String response);
	void onError(Exception e);

}
