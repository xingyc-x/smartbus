package com.pcis.smartbus.project.bean;

import java.io.Serializable;
import java.util.Date;

public class Project implements Serializable{

	private static final long serialVersionUID = 1L;
	int ID;
	private String number;
	private String company;
	
	private String location;
	private String info;
	float j;
	float w;
	Date create_time;
	public Project() {}
	public String getNumber() {
		return number;
	}
	
	public Project(String number, String company, String location, String info) {
		super();
		this.number = number;
		this.company = company;
		this.location = location;
		this.info = info;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "Project [number=" + number + ", company=" + company + ", location=" + location + ", info=" + info + "]";
	}
	
}
