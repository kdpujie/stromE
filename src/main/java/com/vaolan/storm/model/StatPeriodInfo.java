package com.vaolan.storm.model;

import com.vaolan.common.jdbc.annotation.Column;
import com.vaolan.common.jdbc.annotation.Table;

@Table("stat_period_info")
public class StatPeriodInfo {
	
	@Column("ad_id")
	private String adId;
	@Column("ts")
	private String ts;
	@Column("hour_of_day")
	private String hourOfDay;
	@Column("pv_num")
	private String pvNum;
	@Column("click_num")
	private String clickNum;
	@Column("cost")
	private String cost;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getAdId()).append(",")
		.append(this.getTs()).append(",")
		.append(this.getHourOfDay()).append(",")
		.append(this.getPvNum()).append(",")
		.append(this.getClickNum()).append(",")
		.append(this.getCost());
		return sb.toString();
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getHourOfDay() {
		return hourOfDay;
	}

	public void setHourOfDay(String hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public String getPvNum() {
		return pvNum;
	}

	public void setPvNum(String pvNum) {
		this.pvNum = pvNum;
	}

	public String getClickNum() {
		return clickNum;
	}

	public void setClickNum(String clickNum) {
		this.clickNum = clickNum;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	
	
	

	
	
}
