package com.vaolan.strom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.vaolan.common.jdbc.ConnectionManager;
import com.vaolan.storm.dao.StatPeriodInfoDao;
import com.vaolan.storm.dao.StatRegionInfoDao;
import com.vaolan.storm.model.StatMediaInfo;
import com.vaolan.storm.model.StatPeriodInfo;
import com.vaolan.storm.model.StatRegionInfo;

public class Test {

	public static void main(String[] args) throws IOException {
		Connection conn = null;
		try {
			conn = ConnectionManager.getMysqlConection();
			
//			StatMediaInfoDao dao = new StatMediaInfoDao();
//			dao.insertOrUpdate(conn,createMediaInfo());
//			StatRegionInfoDao dao =StatRegionInfoDao.instance();
//			dao.insertOrUpdate(conn, createRegionInfo());
			StatPeriodInfoDao dao = StatPeriodInfoDao.instance();
			dao.insertOrUpdate(conn, createPeriodInfo());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(conn != null)conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static StatMediaInfo createMediaInfo(){
		StatMediaInfo mediaInfo = new StatMediaInfo();
		mediaInfo.setAdId("596");
		mediaInfo.setStrId("888");
		mediaInfo.setTs("2015-04-01");
		mediaInfo.setSiteHost("baidu.com");
		mediaInfo.setPvNum("6789");
		mediaInfo.setClickNum("22");
		mediaInfo.setCost("8888");
		mediaInfo.setSiteName("百度");
		return mediaInfo;
	}
	public static StatRegionInfo createRegionInfo(){
		StatRegionInfo region = new StatRegionInfo();
		region.setAdId("596");
		region.setStrId("888");
		region.setTs("2015-04-09");
		region.setRegionName("江苏省");
		region.setPvNum("200");
		region.setClickNum("10");
		region.setCost("554");
		return region;
	}
	public static StatPeriodInfo createPeriodInfo(){
		StatPeriodInfo period = new StatPeriodInfo();
		period.setAdId("456");
		period.setTs("2015-04-15");
		period.setHourOfDay("01");
		period.setPvNum("1000");
		period.setClickNum("20");
		period.setCost("200");
		return period;
	}
}
