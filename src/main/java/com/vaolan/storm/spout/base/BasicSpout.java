package com.vaolan.storm.spout.base;

import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;

/**
 * An default implementation if {@link IRichSpout} leaving basic methods empty. This will also save the collector that
 * is set on {@link #open(Map, TopologyContext, SpoutOutputCollector)} and call a simpler {@link #open()} method on
 * sub-classes as a convenience/simplification.
 * 
 * @author Josh Devins
 */
public abstract class BasicSpout implements IRichSpout {

    private static final long serialVersionUID = -9082020138402819214L;

    private SpoutOutputCollector collector;

    /*
     * (non-Javadoc)
     * 
     * @see backtype.storm.spout.ISpout#ack(java.lang.Object)
     */
    @Override
    public void ack(final Object msgId) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see backtype.storm.spout.ISpout#close()
     */
    @Override
    public void close() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see backtype.storm.spout.ISpout#fail(java.lang.Object)
     */
    @Override
    public void fail(final Object msgId) {
    }


    protected List<Integer> emit(final List<Object> values) {
        return collector.emit(values);
    }

    protected SpoutOutputCollector getOutputCollector() {
        return collector;
    }

}
