package com.vaolan.storm;

import java.util.Properties;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.TopologyBuilder;

import com.vaolan.common.util.StringUtil;
import com.vaolan.common.util.file.ConfigProperties;
import com.vaolan.storm.bolt.StaticMedia2MysqlBolt;
import com.vaolan.storm.bolt.InitMonitorMessageBolt;
import com.vaolan.storm.bolt.StaticPeriod2MysqlBolt;
import com.vaolan.storm.bolt.StaticRegion2MysqlBolt;
import com.vaolan.storm.spout.KafkaSpout;
import com.vaolan.storm.spout.base.StringScheme;
/**
 * TODO
 * <br>create DateTime: 2015年5月5日 上午10:20:34
 * @author pj
 *
 */
public class StaticMediaTopology {
	private static Logger log = Logger.getLogger(StaticMediaTopology.class);
	private static String KAFKA_ZK_CONNECT = "zookeeper.connect";
	public static String KAFKA_ZK_CONNECTION_TIME_OUT = "zookeeper.connectiontimeout.ms";
  
	public static void main(String[] args) {
		try {
			StaticMediaTopology mediaTopology = new StaticMediaTopology();
	        Config config = new Config();
	        config.setNumWorkers(4);
	        config.setMaxTaskParallelism(8);
	        
	        config.put(Config.STORM_ZOOKEEPER_PORT, 2181);
			StormSubmitter.submitTopology("monitor", config, mediaTopology.buildTopology());
		} catch (AlreadyAliveException e) {
			log.error(e.getMessage(), e);
		} catch (InvalidTopologyException e) {
			log.error(e.getMessage(), e);
		}
    
	}
  public StormTopology buildTopology() {
	  TopologyBuilder builder = new TopologyBuilder();
      builder.setSpout("kafka-topic-monitor", createKafkaSpout(),1);
      builder.setBolt("init-monitor-info", new InitMonitorMessageBolt(),4).shuffleGrouping("kafka-topic-monitor");
      
      builder.setBolt("static-media-to-mysql", new StaticMedia2MysqlBolt(), 1).shuffleGrouping("init-monitor-info");
     
      builder.setBolt("static-region-to-mysql", new StaticRegion2MysqlBolt(), 1).shuffleGrouping("init-monitor-info");
      
      builder.setBolt("static-period-to-mysql", new StaticPeriod2MysqlBolt(),1).shuffleGrouping("init-monitor-info");
      
	  return builder.createTopology();
  }
  
  private static IRichSpout createKafkaSpout() {
      // setup Kafka consumer
	  String zkConnect = ConfigProperties.getProperty(KAFKA_ZK_CONNECT);
	  String zkConnectTimeout = ConfigProperties.getProperty(KAFKA_ZK_CONNECTION_TIME_OUT);
	  if(StringUtil.isBlank(zkConnect) || StringUtil.isBlank(zkConnectTimeout)){
		  throw new NullPointerException("kafka config is null");
	  }
	  
      Properties kafkaProps = new Properties();
      kafkaProps.put("zookeeper.connect", zkConnect);
      kafkaProps.put("zookeeper.connectiontimeout.ms", zkConnectTimeout);
      kafkaProps.put("group.id", "monitor");

      return new KafkaSpout(kafkaProps, "monitor", new StringScheme());
  }

}
