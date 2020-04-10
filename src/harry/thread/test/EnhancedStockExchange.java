package harry.thread.test;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * 
 * @author Harry
 *
 */
public class EnhancedStockExchange {

	public static void main(String[] args) {
		BlockingDeque<Integer> orderQueue = new LinkedBlockingDeque<Integer>();
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch stopSignal = new CountDownLatch(200);
		Seller seller = new Seller(orderQueue,startSignal,stopSignal);
		Thread[] sellers = new Thread[100];
		for (int i = 0; i < 100; i++) {
			sellers[i] = new Thread(seller);
			sellers[i].start();
		}
		
		Buyer buyer = new Buyer(orderQueue,startSignal,stopSignal);
		Thread[] buyers = new Thread[100];
		for (int i = 0; i < 100; i++) {
			buyers[i] = new Thread(buyer);
			buyers[i].start();
		}
		
		System.out.println("Go");
		startSignal.countDown();
		try {
			while(System.in.read() != '\n');
		} catch (Exception e) {
		}
		
		System.out.println("Terminating...");
		for (Thread t : sellers) {
			t.interrupt();
		}
		
		for (Thread t : buyers) {
			t.interrupt();
		}
		
		try {
			stopSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Closing down...");
	}
	
	private static class Seller implements Runnable{
		private BlockingQueue orderQueue;
		private boolean shutdownRequest = false;
		private CountDownLatch startSignal,stopSignal;
		
		public Seller(BlockingQueue orderQueue,CountDownLatch startSignal,CountDownLatch stopSignal) {
			this.orderQueue = orderQueue;
			this.startSignal = startSignal;
			this.stopSignal = stopSignal;
		}

		@Override
		public void run() {
			try {
				startSignal.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while(!shutdownRequest){
				Integer quantity = (int) (Math.random() * 100);
				try {
					orderQueue.put(quantity);
					Thread.sleep(100);
					System.out.println("Sell order by " + Thread.currentThread().getName() + ": " + quantity);
				} catch (InterruptedException e) {
					shutdownRequest = true;
				}
			}
			
			stopSignal.countDown();
		}
	}
	
	private static class Buyer implements Runnable{
		private BlockingQueue orderQueue;
		private boolean shutdownRequest = false;
		private CountDownLatch startSignal,stopSignal;
		
		public Buyer(BlockingQueue orderQueue,CountDownLatch startSignal,CountDownLatch stopSignal) {
			this.orderQueue = orderQueue;
			this.startSignal = startSignal;
			this.stopSignal = stopSignal;
		}

		@Override
		public void run() {
			try {
				startSignal.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while(!shutdownRequest){
				try {
					Integer quantity = (Integer) orderQueue.take();
					Thread.sleep(100);
					System.out.println("buy order by " + Thread.currentThread().getName() + ": " + quantity);
				} catch (InterruptedException e) {
					shutdownRequest = true;
				}
			}
			
			stopSignal.countDown();
		}
	}
}
