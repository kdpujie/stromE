package com.vaolan.storm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vaolan.common.jdbc.ConnectionManager;
import com.vaolan.common.jdbc.JDBCTemplate;
import com.vaolan.storm.model.StatRegionInfo;

public class StatRegionInfoDao {

	private static Logger log = Logger.getLogger(StatMediaInfoDao.class);
	private JDBCTemplate jdbc = JDBCTemplate.getInstance();
	private static StatRegionInfoDao dao = new StatRegionInfoDao();
	
	private StatRegionInfoDao(){}
	
	public static StatRegionInfoDao instance(){
		return dao;
	}
	
	public int insertOrUpdate(Connection conn,StatRegionInfo regionInfo){
		int value = 0;
		try {
			String findSql = "select 1 from stat_region_info ";
			StringBuilder condition = new StringBuilder(" where ");
			condition.append(" ad_id=").append(regionInfo.getAdId())
			.append(" and str_id=").append(regionInfo.getStrId())
			.append(" and ts='").append(regionInfo.getTs()).append("'")
			.append(" and region_name='").append(regionInfo.getRegionName()).append("'");
			
			StringBuilder updateSql = new StringBuilder("update stat_region_info set ");
			updateSql.append(" pv_num=").append(regionInfo.getPvNum())
			.append(",click_num=").append(regionInfo.getClickNum())
			.append(",cost=").append(regionInfo.getCost())
			.append(condition.toString());
			
			if(jdbc.exist(conn, findSql+condition.toString())){
				jdbc.update(conn, updateSql.toString());
			}else{
				jdbc.insertObject(conn, regionInfo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return value;
	}
	
	/**
	 * 查询所有网站中文名和host的映射关系
	 * @param conn
	 * @return
	 */
	public Set<String> findProvince(){
		PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        Set<String> provinces = new HashSet<String>();
        try {
        	String sql = "select name from region parent_code where parent_code=-1;";
        	conn = ConnectionManager.getMysqlConection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				provinces.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
        finally{
			try {
				if(rs != null)rs.close();
				if(ps != null)ps.close();
				if(conn != null)conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        return provinces;
	}
}
