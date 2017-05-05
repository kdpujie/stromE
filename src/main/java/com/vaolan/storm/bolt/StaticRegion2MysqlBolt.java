package com.vaolan.storm.bolt;

import java.util.Map;
import java.util.Timer;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.vaolan.common.util.RotatingMap;
import com.vaolan.storm.timer.SyncRegionTimerTask;
import com.vaolan.storm.util.ConfigConstant;
import com.vaolan.storm.util.TopologyConstant;
/**
 * 统计独立UV
 * @author pj
 *
 */
public class StaticRegion2MysqlBolt extends BaseBasicBolt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RotatingMap<String, String> activeKey;
	private SyncRegionTimerTask syncRegionTask;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		activeKey = new RotatingMap<String,String>(2);
		Timer timer = new Timer();
		syncRegionTask = new SyncRegionTimerTask(activeKey);
		timer.schedule(syncRegionTask, 90000, ConfigConstant.STORM_DATA_TO_MYSLQ_PERIOD);
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String value = input.getStringByField("value");
		String key = input.getStringByField("key");
		String type = input.getStringByField("type");
		if( TopologyConstant.STATIC_TYPE_REGION.equals(type) && !activeKey.containsKey(key)){
			activeKey.put(key, value);
		}
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanup() {
		syncRegionTask.run();
		super.cleanup();
	}

}
