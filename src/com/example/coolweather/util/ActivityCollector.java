package com.example.coolweather.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollector {
	public static List<Activity>activities = new ArrayList<Activity>();

	/**
	 * <B>添加新的活动实例</B>
	 * @param activity
	 */
	public static void addActivity(Activity activity)
	{
		if(!activities.contains(activity))
		{
			activities.add(activity);
		}
		
	}
	
	/**
	 * <B>从集合中移除该实例</B>
	 * @param activity
	 */
	public static void removeActivity(Activity activity)
	{
		
		activities.remove(activity);
	}
	
	/**
	 * 退出所有的活动实例！
	 */
	public static void finishAll()
	{
		for (Activity activity:activities)
		{
			if(!activity.isFinishing())
				activity.finish();
		}
		
	}
	
	
}
