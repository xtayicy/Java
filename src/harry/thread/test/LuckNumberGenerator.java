package harry.thread.test;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * 
 * @author Harry
 *
 */
public class LuckNumberGenerator {
	public static void main(String[] args) {
		TransferQueue<String> queue = new LinkedTransferQueue<String>();
		Thread producer = new Thread(new Producer(queue));
		producer.setDaemon(true);
		producer.start();
		for (int i = 0; i < 10; i++) {
			Thread consumer = new Thread(new Consumer(queue));
			consumer.setDaemon(true);
			consumer.start();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class Producer implements Runnable{
		private final TransferQueue<String> queue;
		
		public Producer(TransferQueue<String> queue) {
			super();
			this.queue = queue;
		}
		
		private String produce(){
			return " your luck number is " + (int)(Math.random() * 100);
		}

		@Override
		public void run() {
			try {
				while(true){
					if(queue.hasWaitingConsumer()){
						queue.transfer(produce());
					}
					
					TimeUnit.SECONDS.sleep(1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class Consumer implements Runnable{
		private final TransferQueue queue;
		
		public Consumer(TransferQueue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				System.out.println("Consumer " + Thread.currentThread().getName() + queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}