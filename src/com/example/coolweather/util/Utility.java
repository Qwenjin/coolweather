package com.example.coolweather.util;

import com.example.coolweather.model.City;
import com.example.coolweather.model.CoolWeatherDB;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;

import android.text.TextUtils;

public class Utility {
	/**
	 * 解析和处理服务器返回的省级数据
	 */
	
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, String resopnse)
	{
		if(!TextUtils.isEmpty(resopnse))
		{
			String[] allProvinces = resopnse.split(",");
			if(allProvinces != null && allProvinces.length > 0)
			{
				for(String p : allProvinces)
				{
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//将解析出来的数据存储到Province表
					coolWeatherDB.saveProvince(province);
					
				}
				return true;
			}
		}
		
		return false;
	} //public synchronized static boolean handleProvinceResponse
	
	
	/**
	 * 解析和处理服务器返回的市数据
	 */
	
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String resopnse, int provinceId)
	{
		if(!TextUtils.isEmpty(resopnse))
		{
			String[] allCities = resopnse.split(",");
			if(allCities != null && allCities.length > 0)
			{
				for(String p : allCities)
				{
					String[] array = p.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//将解析出来的数据存储到City表
					coolWeatherDB.saveCity(city);
					
				}
				return true;
			}
		}
		
		return false;
	} 	//public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String resopnse, int provinceId)

	

	/**
	 * 解析和处理服务器返回的县数据
	 */ 
	
	public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB, String resopnse, int cityId)
	{
		if(!TextUtils.isEmpty(resopnse))
		{
			String[] allCountries = resopnse.split(",");
			if(allCountries != null && allCountries.length > 0)
			{
				for(String p : allCountries)
				{
					String[] array = p.split("\\|");
					Country country = new Country();
					country.setContryCode(array[0]);
					country.setContryName(array[1]);
					country.setCityId(cityId);
					//将解析出来的数据存储到Country表
					coolWeatherDB.saveCountry(country);
					
				}
				return true;
			}
		}
		
		return false;
	} //public synchronized static boolean handleProvinceResponse

	

}
