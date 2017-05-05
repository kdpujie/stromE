package com.vaolan.storm;

import java.util.Arrays;

import com.vaolan.storm.bolt.StaticMedia2MysqlBolt;
import com.vaolan.storm.bolt.InitMonitorMessageBolt;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class StaticMediaBakTopology {
//
//	private BrokerHosts brokerHosts;
//	
//    public StaticMediaTopology(String kafkaZookeeper) {
//        brokerHosts = new ZkHosts(kafkaZookeeper,"/kafka/brokers");
//    }
//
//    public StormTopology buildTopology() {
//        SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, "monitor", "/kafka", "monitor");
//        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
//        TopologyBuilder builder = new TopologyBuilder();
//        builder.setSpout("kafka-monitor", new KafkaSpout(kafkaConfig), 1);
//        builder.setBolt("static-media-init", new StaticMediaInitBolt(),4).shuffleGrouping("kafka-monitor");
//        builder.setBolt("static-media-to-mysql", new StaticMedia2MysqlBolt(), 1).fieldsGrouping("static-media-init", new Fields("key"));
//        return builder.createTopology();
//    }
//    
//	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
//        String kafkaZk = args[0];
//        StaticMediaTopology mediaTopology = new StaticMediaTopology(kafkaZk);
//        Config config = new Config();
//        config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);
//
//        StormTopology stormTopology = mediaTopology.buildTopology();
//        if (args != null && args.length > 1) {
//            String dockerIp = args[1];
//            config.setNumWorkers(4);
//            config.setMaxTaskParallelism(8);
////            config.put(Config.NIMBUS_HOST, dockerIp);
////            config.put(Config.NIMBUS_THRIFT_PORT, 6627);
//            config.put(Config.STORM_ZOOKEEPER_PORT, 2181);
//            config.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(dockerIp));
//            StormSubmitter.submitTopology("static-media", config, stormTopology);
//        } else {
//            config.setNumWorkers(2);
//            config.setMaxTaskParallelism(2);
//            LocalCluster cluster = new LocalCluster();
//            cluster.submitTopology("kafka", config, stormTopology);
//        }
//    
//	}
//
}
