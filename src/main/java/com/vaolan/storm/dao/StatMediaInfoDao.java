package com.vaolan.storm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vaolan.common.jdbc.ConnectionManager;
import com.vaolan.common.jdbc.JDBCTemplate;
import com.vaolan.common.util.StringUtil;
import com.vaolan.storm.model.StatMediaInfo;

public class StatMediaInfoDao {

	private static Logger log = Logger.getLogger(StatMediaInfoDao.class);
	private JDBCTemplate jdbc = JDBCTemplate.getInstance();
	
	public int insertOrUpdate(Connection conn,StatMediaInfo statMediaInfo){
		int value = 0 ;
		try {
			String findSql = "select 1 from stat_media_info ";
			StringBuilder condition = new StringBuilder(" where ");
			condition.append(" ad_id=").append(statMediaInfo.getAdId())
			.append(" and str_id=").append(statMediaInfo.getStrId())
			.append(" and ts='").append(statMediaInfo.getTs()).append("'")
			.append(" and site_host='").append(statMediaInfo.getSiteHost()).append("'");
			
			StringBuilder updateSql = new StringBuilder("update stat_media_info set ");
			updateSql.append(" pv_num=").append(statMediaInfo.getPvNum())
//			.append(",uv_num=").append(statMediaInfo.getUvNum())
			.append(",click_num=").append(statMediaInfo.getClickNum())
//			.append(",ip_num=").append(statMediaInfo.getIpNum())
			.append(",cost=").append(statMediaInfo.getCost())
			.append(condition.toString());
			
			if(jdbc.exist(conn, findSql+condition.toString())){
				jdbc.update(conn, updateSql.toString());
			}else{
				jdbc.insertObject(conn, statMediaInfo);
			}
		} catch (Exception e) {
			log.error("向数据库同步失败", e);
		}
		return value;
	}
	/**
	 * 查询所有网站中文名和host的映射关系
	 * @param conn
	 * @return
	 */
	public Map<String,String> findAllSite(){
		PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        Map<String,String> hostAndName = new HashMap<String, String>();
        try {
        	String sql = "select name,host from media_site_info";
        	conn = ConnectionManager.getMysqlConection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				hostAndName.put(StringUtil.getHostUrl(rs.getString("host")), rs.getString("name"));
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
        return hostAndName;
	}
}
