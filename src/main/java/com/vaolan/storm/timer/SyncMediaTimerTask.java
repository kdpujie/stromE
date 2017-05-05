package com.vaolan.storm.timer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisCluster;

import com.vaolan.common.cache.pool.JedisClusterFactory;
import com.vaolan.common.jdbc.ConnectionManager;
import com.vaolan.common.util.RotatingMap;
import com.vaolan.common.util.StringUtil;
import com.vaolan.storm.dao.StatMediaInfoDao;
import com.vaolan.storm.model.StatMediaInfo;
import com.vaolan.storm.util.TopologyConstant;
/**
 * 每执行一次，先同步最新数据到mysql<br>
 * 然后删除rotatingMap不活跃数据
 * @author pj
 *
 */
public class SyncMediaTimerTask extends TimerTask {

	private Logger logger = Logger.getLogger(SyncMediaTimerTask.class);
	private RotatingMap<String,String> rotatingMap;
	JedisCluster jc= JedisClusterFactory.getcluster();
	public static Map<String,String> HOST_NAME_MAPPING = Collections.emptyMap();
	StatMediaInfoDao dao = new StatMediaInfoDao();
	
	public SyncMediaTimerTask(RotatingMap<String, String> rotatingMap){
		this.rotatingMap = rotatingMap;
		HOST_NAME_MAPPING = dao.findAllSite();
	}
	
	@Override
	public void run() {
		Map<String,String> latest = rotatingMap.cloneFirst();
		rotatingMap.rotate();//去掉不活跃数据
		logger.info("数据同步开始,数据大小为:"+latest.size());
		Connection conn = null;
		StatMediaInfo mediaInfo = null;
		try {
			conn = ConnectionManager.getMysqlConection();
			for(Entry<String, String> enty:latest.entrySet()){
				mediaInfo = new StatMediaInfo();
				String[] colums = enty.getValue().split(TopologyConstant.FIELDS_SEPARATOR);
				if(colums.length == 4 && StringUtil.isNotBlank(colums[0]) && StringUtil.isNotBlank(colums[1]) && 
						StringUtil.isNotBlank(colums[2])){
					mediaInfo.setTs(colums[0]); //ts
					mediaInfo.setAdId(colums[1]);//ad_id
					mediaInfo.setStrId(colums[2]);//str_id
					mediaInfo.setSiteHost(colums[3]);//host
					mediaInfo.setSiteName(HOST_NAME_MAPPING.get(colums[3]));
					mediaInfo.setPvNum(jc.get(TopologyConstant.STAT_PV_KEY_PRE+enty.getKey()));
					mediaInfo.setCost(jc.get(TopologyConstant.STAT_CPM_KEY_PRE+enty.getKey()));
					mediaInfo.setClickNum(jc.get(TopologyConstant.STAT_CLICK_KEY_PRE + enty.getKey()));
				}
				dao.insertOrUpdate(conn, mediaInfo);
				logger.info("to Mysql："+mediaInfo.toString());
			}
		} catch (SQLException e) {
			logger.error("sync Mysql failed", e);
		}finally{
				try {
					if(conn != null) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

}
