package com.example.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.R;
import com.example.coolweather.model.City;
import com.example.coolweather.model.CoolWeatherDB;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;
import com.example.coolweather.util.HttpCallbacklistener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.LogUtil;
import com.example.coolweather.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTRY = 2;

	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listVeiw;
	private ArrayAdapter<String>adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	
	/*
	 * 省列表 
	 */
	private List<Province> provinceList;
	
	/*
	 * 市列表 
	 */
	private List<City> cityList;
	
	/*
	 * 县列表 
	 */
	private List<Country> countryList;
	
	/*
	 * 选中的省份
	 */
	private Province selectiveProvince;
	
	/*
	 * 选中的城市
	 */
	private City selectCity;
	
	/*
	 * 当前选中的级别
	 */
	
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean("city_selected", false))
		{
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return ;
		}
		else {
			LogUtil.e(LogUtil.TAG, "sharedPreferences fucked!");
		}
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		listVeiw = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,dataList);
		listVeiw.setAdapter(adapter);
		
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		
		listVeiw.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(currentLevel == LEVEL_PROVINCE)
				{
					LogUtil.v(LogUtil.TAG, "选中了某个省份！");
					selectiveProvince = provinceList.get(position);
					queryCities();
				}else if(currentLevel == LEVEL_CITY)
				{
					selectCity = cityList.get(position);
					querycountries();
				}
				/*
				else {
					Toast.makeText(ChooseAreaActivity.this, countryList.get(position).getContryName(), Toast.LENGTH_SHORT).show();
				}
				*/
				else if(currentLevel == LEVEL_COUNTRY)
				{
					String countryCode = countryList.get(position).getContryCode();
					Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("county_code", countryCode);
					startActivity(intent);
					finish();
				}
				else {
					Toast.makeText(ChooseAreaActivity.this, countryList.get(position).getContryName(), Toast.LENGTH_SHORT).show();
				}
				
			}
		}); //setOnItemClickListener
		
		//默认会自动加载省级数据
		queryProvince();
		
	} //onCreate
	
	/**
	 * 查询全国所有的省，优先从数据库查询， 如果没有查到到再去服务器上查询。
	 */
	private void queryProvince()
	{
		LogUtil.v(LogUtil.TAG, "queryProvince");
		
		provinceList = coolWeatherDB.loadProvinces();
		if(provinceList.size() > 0)
		{//如果数据库有
			dataList.clear();
			for(Province province:provinceList)
			{
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listVeiw.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}else {
			queryFromServer(null, "province");
		}
	} //private void queryProvince()
	
	/**
	 * 查询选中的省内的所有的市，优先从数据库查询，如果没有则到服务器上查询
	 */
	
	private void queryCities()
	{
		LogUtil.v(LogUtil.TAG, "queryCities");
		LogUtil.v(LogUtil.TAG, "选中省份：selectiveProvince ： "+selectiveProvince.getId()  + " code: "+
				selectiveProvince.getProvinceCode() + selectiveProvince.getProvinceName()
				);
		
		cityList = coolWeatherDB.loadCities(selectiveProvince.getId());
		if(cityList.size() > 0)
		{//如果数据库有的话
			dataList.clear();
			for(City city:cityList)
			{
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listVeiw.setSelection(0);
			titleText.setText(selectiveProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else {
			LogUtil.v(LogUtil.TAG, "FUCK server again!");
			queryFromServer(selectiveProvince.getProvinceCode(), "city");
		}
	} //private void queryCities()
	
	/**
	 * 查询选中的市内的所有的县，优先从数据库查询，如果没有则到服务器上查询
	 */
	
	private void querycountries()
	{
		LogUtil.v(LogUtil.TAG, "querycountries");
		LogUtil.v(LogUtil.TAG, "城市selectCity： " + selectCity.getId() + " "+selectCity.getCityCode() + selectCity.getCityName());
		
		countryList = coolWeatherDB.loadCountries(selectCity.getId());
		if(countryList.size() > 0)
		{
			dataList.clear();
			for(Country country:countryList)
			{
				dataList.add(country.getContryName());
			}
			adapter.notifyDataSetChanged();
			listVeiw.setSelection(0);
			titleText.setText(selectCity.getCityName());
			currentLevel = LEVEL_COUNTRY;
			
		}else {
			queryFromServer(selectCity.getCityCode(), "country");
		}
	} //querycountries
	
	/*
	 * 根据传入的代号和类型从服务器伤查询省市县数据
	 */
	
	private void queryFromServer(final String code, final String type)
	{
		LogUtil.v(LogUtil.TAG, "queryFromServer");
		
		String address;
		if(!TextUtils.isEmpty(code))
		{
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
			
		}else{
			
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		//
		showProgressDialog(); //加载中对话框
		HttpUtil.sendHttpRequest(address, new HttpCallbacklistener() {
			
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if("province".equals(type))
				{
					result = Utility.handleProvinceResponse(coolWeatherDB, response);
				}else if("city".equals(type))
				{
					result = Utility.handleCitiesResponse(coolWeatherDB, response, selectiveProvince.getId());
				}else if("country".equals(type))
				{
					result = Utility.handleCountriesResponse(coolWeatherDB, response, selectCity.getId());
				}else {
					LogUtil.v(LogUtil.TAG, "FUCKed ! onFinish");
				}
				
				if(result)
				{//通过runOnUiThread（）方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							LogUtil.v(LogUtil.TAG, "runOnUiThread " + Thread.currentThread().getId());
							closeProgressDialog();
							if("province".equals(type))
							{
								queryProvince();
							}else if("city".equals(type))
							{
								queryCities();
							}else if("country".equals(type))
							{
								querycountries();
							}else {
								LogUtil.v(LogUtil.TAG, "FUCKed ! runOnUiThread");
							}
						}
					}); //runOnUiThread
					
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				//通过runOnUiThread() 回到主线程处理逻辑
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败.", Toast.LENGTH_SHORT).show();
					}
				}); //runOnUiThread
				
			}
		}); //HttpUtil.sendHttpRequest
		
	} //private void queryFromServer
	
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog()
	{
		if(progressDialog == null)
		{
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载中...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		
		progressDialog.show();
	} //showProgressDialog
	
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog(){
		if(progressDialog != null)
		{
			progressDialog.dismiss();
		}
	} //closeProgressDialog
	
	/**
	 * 捕获Back按键， 根据当前的级别来判断，此时应该返回市列表丶省列表丶还是直接退出。
	 */
	
	@Override
	public void onBackPressed() {
		if(currentLevel == LEVEL_COUNTRY)
		{
			queryCities();
		}else if(currentLevel == LEVEL_CITY)
		{
			queryProvince();
		}else {
			finish(); //直接退出程序
		}
	}
}
