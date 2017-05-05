package com.vaolan.storm.model;

import com.vaolan.common.jdbc.annotation.Column;
import com.vaolan.common.jdbc.annotation.Table;


@Table("stat_media_info")
public class StatMediaInfo {
	
//	private Lock lock = new ReentrantLock(false);
	@Column("ad_id")
	private String adId;
	@Column("str_id")
	private String strId;
	@Column("ts")
	private String ts;
	@Column("site_host")
	private String siteHost;
	@Column("pv_num")
	private String pvNum;
	@Column("click_num")
	private String clickNum;
	@Column("cost")
	private String cost;
	@Column("site_name")
	private String siteName;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getAdId()).append(",")
		.append(this.getStrId()).append(",")
		.append(this.getTs()).append(",")
		.append(this.getSiteHost()).append(",")
		.append(this.getPvNum()).append(",")
		.append(this.clickNum).append(",")
		.append(this.getCost()).append(",")
		.append(this.getSiteName());
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
	public String getSiteHost() {
		return siteHost;
	}
	public void setSiteHost(String siteHost) {
		this.siteHost = siteHost;
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
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	
}
