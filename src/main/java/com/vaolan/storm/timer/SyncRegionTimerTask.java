package com.vaolan.storm.timer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisCluster;

import com.vaolan.common.cache.pool.JedisClusterFactory;
import com.vaolan.common.jdbc.ConnectionManager;
import com.vaolan.common.util.RotatingMap;
import com.vaolan.common.util.StringUtil;
import com.vaolan.storm.dao.StatRegionInfoDao;
import com.vaolan.storm.model.StatRegionInfo;
import com.vaolan.storm.util.TopologyConstant;
/**
 * 每执行一次，先同步最新数据到mysql<br>
 * 然后删除rotatingMap不活跃数据
 * @author pj
 *
 */
public class SyncRegionTimerTask extends TimerTask {

	private Logger logger = Logger.getLogger(SyncRegionTimerTask.class);
	private RotatingMap<String,String> rotatingMap;
	JedisCluster jc= JedisClusterFactory.getcluster();
	StatRegionInfoDao dao = null;
	
	public SyncRegionTimerTask(RotatingMap<String, String> rotatingMap){
		dao = StatRegionInfoDao.instance();
		this.rotatingMap = rotatingMap;
	}
	
	@Override
	public void run() {
		Map<String,String> latest = rotatingMap.cloneFirst();
		rotatingMap.rotate();//去掉不活跃数据
		logger.info("数据同步开始,数据大小为:"+latest.size());
		Connection conn = null;
		StatRegionInfo regionInfo = null;
		try {
			conn = ConnectionManager.getMysqlConection();
			for(Entry<String, String> entry:latest.entrySet()){
				regionInfo = new StatRegionInfo();
				String[] colums = entry.getValue().split(TopologyConstant.FIELDS_SEPARATOR);
				if(colums.length == 4 && StringUtil.isNotBlank(colums[0]) && StringUtil.isNotBlank(colums[1]) && 
						StringUtil.isNotBlank(colums[2])){
					regionInfo.setTs(colums[0]); //ts
					regionInfo.setAdId(colums[1]);//ad_id
					regionInfo.setStrId(colums[2]);//str_id
					regionInfo.setRegionName(colums[3]);//regionName
					regionInfo.setPvNum(jc.get(TopologyConstant.STAT_PV_KEY_PRE+entry.getKey()));
					regionInfo.setCost(jc.get(TopologyConstant.STAT_CPM_KEY_PRE+entry.getKey()));
					regionInfo.setClickNum(jc.get(TopologyConstant.STAT_CLICK_KEY_PRE + entry.getKey()));
				}
				dao.insertOrUpdate(conn, regionInfo);
				logger.info("region to Mysql："+regionInfo.toString());
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
