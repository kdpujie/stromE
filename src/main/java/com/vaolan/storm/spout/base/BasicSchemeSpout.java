package com.vaolan.storm.spout.base;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import backtype.storm.spout.Scheme;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;

/**
 * A simple {@link BasicSpout} that delegates to a provided {@link Scheme} where possible.
 * 
 * @author Josh Devins
 */
public abstract class BasicSchemeSpout extends BaseRichSpout {

    private static final long serialVersionUID = -832374927567182090L;

    private final Scheme scheme;
    private SpoutOutputCollector collector;

    public BasicSchemeSpout(final Scheme scheme) {

        Validate.notNull(scheme, "Scheme is required");
        this.scheme = scheme;
    }

    
    @Override
	public void open(Map conf, TopologyContext context,SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
		open();
	}
    
    protected abstract void open();

	/**
     * Delegates to the {@link Scheme}.
     */
    @Override
    public void declareOutputFields(final OutputFieldsDeclarer declarer) {
        declarer.declare(scheme.getOutputFields());
    }

    /**
     * Delegates deserialization to the {@link Scheme}.
     */
    protected List<Integer> emit(final byte[] bytes) {
        return collector.emit(scheme.deserialize(bytes));
    }
}
