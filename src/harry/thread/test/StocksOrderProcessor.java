package harry.thread.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Harry
 *
 */
public class StocksOrderProcessor {
	private static final int MAX_NUMBER_OF_ORDERS = 10000;
	private static List<Future<Integer>> orderToProcess = new ArrayList<Future<Integer>>();
	private static ExecutorService executor = Executors.newFixedThreadPool(100);

	public static void main(String[] args) {
		System.out.printf("Submitting %d trades%n",MAX_NUMBER_OF_ORDERS);
		for (int i = 0; i < MAX_NUMBER_OF_ORDERS; i++) {
			submitOrder(i);
		}
		
		new Thread(new EvilThread(orderToProcess)).start();
		System.out.println("Cancelling a few orders at random");
		try {
			executor.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Checking status before shutdown");
		int count = 0;
		for (Future<Integer> future : orderToProcess) {
			if(future.isCancelled()){
				count++;
			}
		}
		
		System.out.printf("%d trades cancelled%n",count);
		executor.shutdown();
	}

	private static void submitOrder(int id) {
		Callable<Integer> task = new OrderExecutor(id);
		orderToProcess.add(executor.submit(task));
	}
	
	private static class EvilThread implements Runnable{
		private List<Future<Integer>> orderToProcess;
		
		public EvilThread(List<Future<Integer>> orderToProcess) {
			this.orderToProcess = orderToProcess;
		}

		@Override
		public void run() {
			Random myNextKill = new Random(System.currentTimeMillis() % 100);
			for (int i = 0; i < 100; i++) {
				int index = myNextKill.nextInt(MAX_NUMBER_OF_ORDERS);
				boolean cancel = orderToProcess.get(index).cancel(true);
				if(cancel){
					System.out.println("Cancel order succeeded: " + index);
				}else{
					System.out.println("Cancel order failed: " + index);
				}
				
				try {
					Thread.sleep(myNextKill.nextInt(100));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static class OrderExecutor implements Callable<Integer>{
		private int id = 0;
		private int count = 50;
		
		public OrderExecutor(Integer id) {
			this.id = id;
		}

		@Override
		public Integer call() throws Exception {
			try {
				while(count < 50){
					count++;
					Thread.sleep(new Random(System.currentTimeMillis() % 100).nextInt(10));
				}
				
				System.out.println("Successfully executed order: " + id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return id;
		}
	}
}
