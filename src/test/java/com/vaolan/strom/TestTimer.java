package com.vaolan.strom;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.vaolan.common.util.file.IpSearch;

public class TestTimer {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
//		Timer t= new Timer(true);
//		t.schedule(new MyTask(),1000,3000);
//		Thread.sleep(10000);
		Set<String> names = new HashSet<String>();
		names.add("北京市");
		names.add("江苏省");
		names.add("广西");
		IpSearch is = IpSearch.getInstance();
		is.init(names);
		String addr = is.getAddrByIp("222.218.183.251");
		System.out.println(addr);
		addr = is.getAddrByIp("180.96.26.130");
		System.out.println(addr);
	}

}
class MyTask extends TimerTask{

	@Override
	public void run() {
		System.out.println("task执行："+MyTask.class);
	}
	
}