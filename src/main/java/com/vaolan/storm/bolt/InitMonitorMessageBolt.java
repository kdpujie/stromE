package com.vaolan.storm.bolt;

import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisCluster;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.vaolan.common.cache.pool.JedisClusterFactory;
import com.vaolan.common.util.Constant;
import com.vaolan.common.util.NumberUtil;
import com.vaolan.common.util.StringUtil;
import com.vaolan.common.util.encrype.MD5OperatorUtil;
import com.vaolan.common.util.file.IpSearch;
import com.vaolan.storm.dao.StatRegionInfoDao;
import com.vaolan.storm.util.TopologyConstant;

public class InitMonitorMessageBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;
	private Logger log = null;
	public IpSearch ipSearch;
	JedisCluster jc;
	/**
	 * 广告计划标识
	 */
	String adId;
	/**
	 * 策略标识
	 */
	String sId;
	/**
	 * 投放的域名
	 */
	String host;
	/**
	 * 广告展示的时间。格式为:2015-03-27 17:24:25
	 */
	String ts;
	/**
	 * 广告展示的时间。hour of day
	 */
	String hour;
	/**
	 * adx的cookie ID
	 */
	String tid;
	/**
	 * 用户地址
	 */
	String region;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		Logger.getLogger(InitMonitorMessageBolt.class);
		jc = JedisClusterFactory.getcluster();
		ipSearch = IpSearch.getInstance();
		ipSearch.init(StatRegionInfoDao.instance().findProvince());
		super.prepare(stormConf, context);
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String key;
		String value;
		String impressOrclick = input.getString(0);
		String[] colums = impressOrclick.split(Constant.LOG_SEPARATOR);
		if(Constant.LOG_TYPE_IMPRESS.equals(colums[1])){//曝光日志
			adId = colums[4];
			sId = colums[5];
			host = StringUtil.getHostUrl(colums[9]);
			ts = colums[11].substring(0, 10);//以day为单位
			//分媒体统计
			value = ts + TopologyConstant.FIELDS_SEPARATOR + adId + TopologyConstant.FIELDS_SEPARATOR
					+ sId + TopologyConstant.FIELDS_SEPARATOR + host;
			key = MD5OperatorUtil.MD5_little(value);
			saveImpressInfo(key,NumberUtil.toInt(colums[7]),TopologyConstant.SECONDS_OF_DAY);
			collector.emit(new Values(TopologyConstant.STATIC_TYPE_MEDIA,key,value));  
			//分地域统计
			region = ipSearch.getAddrByIp(colums[10]);
			value = ts + TopologyConstant.FIELDS_SEPARATOR + adId + TopologyConstant.FIELDS_SEPARATOR
					+ sId + TopologyConstant.FIELDS_SEPARATOR + region;
			key = MD5OperatorUtil.MD5_little(value);
			saveImpressInfo(key,NumberUtil.toInt(colums[7]),TopologyConstant.SECONDS_OF_DAY);
			collector.emit(new Values(TopologyConstant.STATIC_TYPE_REGION,key,value));
			//时段统计
			hour = findHourOfDay(colums[11]);
			value = ts + TopologyConstant.FIELDS_SEPARATOR + adId + TopologyConstant.FIELDS_SEPARATOR + hour;
			key = MD5OperatorUtil.MD5_little(value);
			saveImpressInfo(key,NumberUtil.toInt(colums[7]),TopologyConstant.SECONDS_OF_HOUR + 600);
			collector.emit(new Values(TopologyConstant.STATIC_TYPE_PERIOD,key,value));
		}else if(Constant.LOG_TYPE_CLICK.equals(colums[1])){//点击日志
			adId = colums[4];
			sId = colums[5];
			host = StringUtil.getHostUrl(colums[8]);
			ts = colums[10].substring(0, 10);//以day为单位
			//分媒体统计
			value = ts + TopologyConstant.FIELDS_SEPARATOR + adId + TopologyConstant.FIELDS_SEPARATOR
					+ sId + TopologyConstant.FIELDS_SEPARATOR + host;
			key = MD5OperatorUtil.MD5_little(value);
			saveClickInfo(key,TopologyConstant.SECONDS_OF_DAY);
			collector.emit(new Values(TopologyConstant.STATIC_TYPE_MEDIA,key,value));
			//分地域统计
			region = ipSearch.getAddrByIp(colums[9]);
			value = ts + TopologyConstant.FIELDS_SEPARATOR + adId + TopologyConstant.FIELDS_SEPARATOR
					+ sId + TopologyConstant.FIELDS_SEPARATOR + region;
			key = MD5OperatorUtil.MD5_little(value);
			saveClickInfo(key,TopologyConstant.SECONDS_OF_DAY);
			collector.emit(new Values(TopologyConstant.STATIC_TYPE_REGION,key,value));
			//时段统计
			hour = findHourOfDay(colums[10]);
			value = ts + TopologyConstant.FIELDS_SEPARATOR + adId + TopologyConstant.FIELDS_SEPARATOR + hour;
			key = MD5OperatorUtil.MD5_little(value);
			saveClickInfo(key,TopologyConstant.SECONDS_OF_HOUR + 600);
			collector.emit(new Values(TopologyConstant.STATIC_TYPE_PERIOD,key,value));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("type","key","value"));
	}
	/**
	 * 保存曝光日志的统计信息。pv/cost
	 * @param key
	 * @param cpmPrice
	 */
	private void saveImpressInfo(String key,int cpmPrice,int periodSeconds){
		String cpmKey = TopologyConstant.STAT_CPM_KEY_PRE+key;
		if(jc.exists(cpmKey)){
			jc.incrBy(cpmKey, cpmPrice);//cpm消耗
		}else{
			jc.incrBy(cpmKey, cpmPrice);
			jc.expire(cpmKey, periodSeconds);
		}
		String pvKey = TopologyConstant.STAT_PV_KEY_PRE+key;
		if(jc.exists(pvKey)){
			jc.incr(pvKey);//pv 累加
		}else{
			jc.incr(pvKey);
			jc.expire(pvKey, periodSeconds);
		}
	}
	/**
	 * 保存点击日志的统计信息。click
	 * @param key
	 */
	private void saveClickInfo(String key,int periodSeconds){
		String clickKey = TopologyConstant.STAT_CLICK_KEY_PRE + key;
		if(jc.exists(clickKey)){
			jc.incr(clickKey); //点击统计
		}else{
			jc.incr(clickKey);
			jc.expire(clickKey, periodSeconds);
		}
	}
	/**
	 * 从时间字符串中，找出所属的hour
	 * @param date
	 * @return hour
	 */
	private String findHourOfDay(String date){
		String value = "";
		try {
			String[] dates = date.split(" ");
			String[] time = dates[1].split(":");
			value = time[0];
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return value;
	}
}
