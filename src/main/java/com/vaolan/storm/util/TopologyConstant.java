package com.vaolan.storm.util;

public class TopologyConstant {

	public static final String FIELDS_SEPARATOR = "_";
	/**
	 * 统计独立uv时，在key前边增加的前缀
	 */
	public static final String STAT_UV_KEY_PRE = "stat_uv_";
	/**
	 * 统计独立IP时，在key前边增加的前缀
	 */
	public static final String STAT_IP_KEY_PRE = "stat_ip_";
	/**
	 * 以cpm为单位的消耗。key的前缀
	 */
	public static final String STAT_CPM_KEY_PRE = "stat_cpm_";
	/**
	 * PV统计。key的前缀
	 */
	public static final String STAT_PV_KEY_PRE = "stat_pv_";
	/**
	 * click统计，key前缀
	 */
	public static final String STAT_CLICK_KEY_PRE = "stat_click_";
	/**
	 * 一天有多少秒
	 */
	public static final int SECONDS_OF_DAY = 86400;
	/**
	 * 一个小时有多少秒
	 */
	public static final int SECONDS_OF_HOUR = 3600;
	/**
	 * 分网站统计
	 */
	public static final String STATIC_TYPE_MEDIA = "0";
	/**
	 * 分地域统计
	 */
	public static final String STATIC_TYPE_REGION = "1";
	/**
	 * 时段统计
	 */
	public static final String STATIC_TYPE_PERIOD = "2";
}
