package harry.thread.test;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 
 * @author Harry
 *
 */
public class StockExchange {
	public static void main(String[] args) {
		System.out.println("Hit Enter to terminate\n\n");
		BlockingDeque<Integer> orderQueue = new LinkedBlockingDeque<Integer>();
		Seller seller = new Seller(orderQueue);
		Thread[] sellers = new Thread[100];
		for (int i = 0; i < 100; i++) {
			sellers[i] = new Thread(seller);
			sellers[i].start();
		}
		
		Buyer buyer = new Buyer(orderQueue);
		Thread[] buyers = new Thread[100];
		for (int i = 0; i < 100; i++) {
			buyers[i] = new Thread(buyer);
			buyers[i].start();
		}
		
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
	}
}

class Seller implements Runnable{
	private BlockingQueue orderQueue;
	private boolean shutdownRequest = false;
	
	public Seller(BlockingQueue orderQueue) {
		this.orderQueue = orderQueue;
	}

	@Override
	public void run() {
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
	}
}

class Buyer implements Runnable{
	private BlockingQueue orderQueue;
	private boolean shutdownRequest = false;
	
	public Buyer(BlockingQueue orderQueue) {
		this.orderQueue = orderQueue;
	}

	@Override
	public void run() {
		while(!shutdownRequest){
			try {
				Integer quantity = (Integer) orderQueue.take();
				Thread.sleep(100);
				System.out.println("buy order by " + Thread.currentThread().getName() + ": " + quantity);
			} catch (InterruptedException e) {
				shutdownRequest = true;
			}
		}
	}
}
