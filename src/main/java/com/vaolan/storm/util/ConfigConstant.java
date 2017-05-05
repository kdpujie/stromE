package com.vaolan.storm.util;

import com.vaolan.common.util.NumberUtil;
import com.vaolan.common.util.file.ConfigProperties;

public class ConfigConstant {

	/**
	 *	数据同步到mysql的周期<br>
	 *	默认5分钟
	 */
	public static int STORM_DATA_TO_MYSLQ_PERIOD = 300*1000;
	
	static{
		STORM_DATA_TO_MYSLQ_PERIOD = NumberUtil.toInt(ConfigProperties.getProperty("storm.data.to.mysql.period"), STORM_DATA_TO_MYSLQ_PERIOD) ;
	}
}
