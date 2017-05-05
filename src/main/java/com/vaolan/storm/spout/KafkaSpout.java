package com.vaolan.storm.spout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import com.vaolan.storm.spout.base.BasicSchemeSpout;

import backtype.storm.spout.Scheme;
import backtype.storm.utils.Utils;

public class KafkaSpout extends BasicSchemeSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(KafkaSpout.class);
	private final Properties kafkaProperties;
	private final String topic;
    private ConsumerConnector consumerConnector;
    private ConsumerIterator<byte[], byte[]> streamIterator;
    
    public KafkaSpout(final Properties kafkaProperties, final String topic, final Scheme scheme) {
        super(scheme);
        this.kafkaProperties = kafkaProperties;
        this.topic = topic;
    }
    
	@Override
	public void open() {
		
		ConsumerConfig consumerConfig = new ConsumerConfig(this.kafkaProperties);
        consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        KafkaStream<byte[],byte[]> stream = consumerMap.get(topic).get(0);
        streamIterator = stream.iterator();
	}

	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		MessageAndMetadata<byte[], byte[]> msg = streamIterator.next();
		if(null != msg){
			emit(msg.message());
		}else{
			if (log.isInfoEnabled()) {
				log.info("Message pulled from consumer is null");
            }
			Utils.sleep(10);
		}
	}

	@Override
	public void close() {
		if (consumerConnector != null) {
            consumerConnector.shutdown();
        }
		super.close();
	}

}
