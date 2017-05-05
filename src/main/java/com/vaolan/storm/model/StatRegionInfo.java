package com.vaolan.storm.model;

import com.vaolan.common.jdbc.annotation.Column;
import com.vaolan.common.jdbc.annotation.Table;

@Table("stat_region_info")
public class StatRegionInfo {
	
	@Column("ad_id")
	private String adId;
	@Column("str_id")
	private String strId;
	@Column("ts")
	private String ts;
	@Column("region_name")
	private String regionName;
	
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
		.append(this.getStrId()).append(",")
		.append(this.getTs()).append(",")
		.append(this.getRegionName())
		.append(this.getPvNum())
		.append(this.getClickNum())
		.append(this.getCost());
		return sb.toString();
	}
	
	public String getAdId() {
		return adId;
	}
	public void setAdId(String adId) {
		this.adId = adId;
	}
	public String getStrId() {
		return strId;
	}
	public void setStrId(String strId) {
		this.strId = strId;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
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
