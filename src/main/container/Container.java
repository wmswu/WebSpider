package main.container;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import main.core.Lifecycle;
import main.spider.Spider;
import main.util.Constant;

public class Container implements Lifecycle{
	/*
	 * the blocking queue for store the urls which not be processed
	 */
	private BlockingQueue<URL> URLQueue=new ArrayBlockingQueue<URL>(Constant.DEFAULT_URL_BLOCKINGQUEUE_SIZE,false);
	
	/*
	 * the blocking queue for store the text which not be seperatored
	 */
	private BlockingQueue<StringBuffer> TextDataQueue=new ArrayBlockingQueue<StringBuffer>(Constant.DEFAULT_TEXTDATA_BLOCKINGQUEUE_SIZE,false);
	
	/*
	 * a thread pool
	 */
	private Executor spiderWorkers=Executors.newFixedThreadPool(Constant.DEFAULT_THREADPOOL_SIZE);
	
	/*
	 * 
	 */
	private Stack<Spider> spiders=new Stack<Spider>();
	/*
	 * singleton design pattern
	 */
	private static class ContainerHolder{
		private static final Container instance=new Container();
	}
	
	public static Container getInstance(){
		return ContainerHolder.instance;
	}
	
	/*
	 * start the container
	 */
	@Override
	public void start(){
		Container container=Container.getInstance();
		for(int i=0;i<Constant.DEFAULT_SPIDERS_NUMBERS;i++){
			Spider spider=new Spider();
			spider.start();
			spiders.push(spider);
			container.spiderWorkers.execute(spider);
		}
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * return the URLs queue
	 */
	public BlockingQueue<URL> getURLQueue(){
		return this.URLQueue;
	}
	
	/*
	 * return the text queue
	 */
	public BlockingQueue<StringBuffer> getTextQueue(){
		return this.TextDataQueue;
	}
	
	public static void main(String args[]){
		Container container=Container.getInstance();
		try {
			container.getURLQueue().add(new URL("http://politics.people.com.cn/n1/2016/0512/c1001-28345037.html"));
			container.start();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
