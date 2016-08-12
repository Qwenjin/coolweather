package com.example.coolweather.model;

public class Country {
	
	private int id;
	private String contryName;
	private String contryCode;
	private int cityId;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContryName() {
		return contryName;
	}
	public void setContryName(String contryName) {
		this.contryName = contryName;
	}
	public String getContryCode() {
		return contryCode;
	}
	public void setContryCode(String contryCode) {
		this.contryCode = contryCode;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
	

}
