package com.vaolan.storm.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.vaolan.common.jdbc.JDBCTemplate;
import com.vaolan.storm.model.StatPeriodInfo;

public class StatPeriodInfoDao {

	private static Logger log = Logger.getLogger(StatPeriodInfoDao.class);
	private JDBCTemplate jdbc = JDBCTemplate.getInstance();
	private static StatPeriodInfoDao dao = new StatPeriodInfoDao();
	
	private StatPeriodInfoDao(){}
	
	public static StatPeriodInfoDao instance(){
		return dao;
	}
	
	public int insertOrUpdate(Connection conn,StatPeriodInfo periodInfo){
		int value = 0;
		try {
			String findSql = "select 1 from stat_period_info ";
			StringBuilder condition = new StringBuilder(" where ");
			condition.append(" ad_id=").append(periodInfo.getAdId())
			.append(" and ts='").append(periodInfo.getTs()).append("'")
			.append(" and hour_of_day=").append(periodInfo.getHourOfDay());
			
			StringBuilder updateSql = new StringBuilder("update stat_period_info set ");
			updateSql.append(" pv_num=").append(periodInfo.getPvNum())
			.append(",click_num=").append(periodInfo.getClickNum())
			.append(",cost=").append(periodInfo.getCost())
			.append(condition.toString());
			
			if(jdbc.exist(conn, findSql+condition.toString())){
				jdbc.update(conn, updateSql.toString());
			}else{
				jdbc.insertObject(conn, periodInfo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return value;
	}
}
