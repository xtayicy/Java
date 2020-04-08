package harry.thread.test;

/**
 * 
 * @author Harry
 *
 */
public class ProducerConsumerGame {
	public static void main(String[] args) {
		Bucket bucket = new Bucket();
		new Thread(new ProducerConsumerGame().new Producer(bucket)).start();
		new Thread(new ProducerConsumerGame().new Consumer(bucket)).start();
	}
	
	class Producer implements Runnable{
		private Bucket bucket;
		
		public Producer(Bucket bucket) {
			super();
			this.bucket = bucket;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				bucket.put((int)(Math.random() * 100));
			}
		}
	}
	
	class Consumer implements Runnable{
		private Bucket bucket;
		
		public Consumer(Bucket bucket) {
			this.bucket = bucket;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				bucket.get();
			}
		}
	}
}

class Bucket{
	private int packOfBalls;
	private boolean available = false;
	
	public synchronized int get(){
		if(!available){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Consumer Got " + packOfBalls);
		available = false;
		notify();
		
		return packOfBalls;
	}
	
	public synchronized void put(int packOfBalls){
		if(available){
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		
		this.packOfBalls = packOfBalls;
		available = true;
		System.out.println("Producer Put " + packOfBalls);
		notify();
	}
}