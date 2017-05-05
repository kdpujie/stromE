package com.vaolan.strom;

public class TestThread {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

//		TestRunnable tr = new TestRunnable();
//		Thread th =new Thread(tr);
////		th.setDaemon(true);
//		th.start();
////		Thread.sleep(10000);
//		System.out.println("主线程退出");
		String str = "2015-03-27 17:24:25";
		String[] str1 = str.split(" ");
		String[] str2 = str1[1].split(":");
		System.out.println(str2[0]);
	}

}

class TestRunnable implements Runnable{     
    public void run(){       
    	try {
			Thread.sleep(1000);//守护线程阻塞1秒后运行    
			System.out.println("线程执行:"+TestRunnable.class);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
    }     
} 