package harry.thread.test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Harry
 *
 */
public class Bank {
	private final static int COUNT = 100;
	private final static Semaphore semaphore = new Semaphore(2,true);
	
	public static void main(String[] args) {
		for (int i = 0; i < COUNT; i++) {
			final int count = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if(semaphore.tryAcquire(10, TimeUnit.MILLISECONDS)){
							try {
								Teller.getService(count);
							} finally {
								semaphore.release();
							}
						}
					} catch (InterruptedException e) {
					}
				}
			}).start();
		}
	}
	
	static class Teller{
		public static void getService(int i){
			try {
				System.out.println("serving: " + i);
				Thread.sleep((long)(10 * Math.random()));
			} catch (InterruptedException e) {
			}
		}
	}
}
