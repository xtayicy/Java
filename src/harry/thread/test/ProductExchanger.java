package harry.thread.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

import org.omg.Messaging.SyncScopeHelper;

/**
 * 
 * @author Harry
 *
 */
public class ProductExchanger {
	private static Exchanger<List<Integer>> exchanger = new Exchanger<List<Integer>>();
	
	public static void main(String[] args) {
		Thread producer = new Thread(new Producer());
		Thread consumer = new Thread(new Consumer());
		producer.start();
		consumer.start();
		
		try {
			while(System.in.read() != '\n'){}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		producer.interrupt();
		consumer.interrupt();
	}
	
	private static class Consumer implements Runnable{
		private boolean ready = true;
		private static List<Integer> buffer = new ArrayList<Integer>();
		
		@Override
		public void run() {
			while(ready){
				try {
					if(buffer.isEmpty()){
						buffer  = exchanger.exchange(buffer);
						System.out.println("Consumer Buffer: ");
						for (Integer i : buffer) {
							System.out.print(i + ", ");
						}
						
						System.out.println("\n");
						Thread.sleep((int)(Math.random() * 1000));
						buffer.clear();
					}
				} catch (InterruptedException e) {
					ready = false;
				}
			}
		}
	}
	
	private static class Producer implements Runnable{
		private static List<Integer> buffer = new ArrayList<Integer>();
		private boolean ready = true;
		private static final int BUFFER_SIZE = 10;
		
		@Override
		public void run() {
			while(ready){
				try {
					if(buffer.isEmpty()){
						for (int i = 0; i < BUFFER_SIZE; i++) {
							buffer.add((int)(Math.random() * 10));
						}
						
						Thread.sleep((int)(Math.random() * 1000));
						System.out.println("Producer Buffer: ");
						for (Integer i : buffer) {
							System.out.print(i + ", ");
						}
						
						System.out.println();
						System.out.println("Exchanging....");
						buffer = exchanger.exchange(buffer);
					}
				} catch (InterruptedException e) {
					ready = false;
				}
			}
			
			
		}
	}
}
