package harry.thread.test;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author Harry
 *
 */
public class MultipleServices {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		ArrayList<Callable<Double>> tasks = new ArrayList<Callable<Double>>();
		for (int i = 0; i < 10; i++) {
			double m = Math.random() * 10;
			int n = (int) (Math.random() * 1000);
			System.out.printf("Created task for computing: " + "%.02f raised to %d\n",m,n);
			tasks.add(new Exp(m,n));
		}
		
		ExecutorCompletionService<Double> service = new ExecutorCompletionService<Double>(executor);
		for (Callable<Double> callable : tasks) {
			service.submit(callable);
		}
		
		ReentrantLock lock = new ReentrantLock();
		for (int i = 0; i < tasks.size(); i++) {
			try {
				lock.lock();
				Double d = service.take().get();
				System.out.printf("Result: %E%n",d);
				lock.unlock();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
	}
	
	private static class Exp implements Callable<Double>{
		private double m;
		private int n;
		
		public Exp(double m, int n) {
			this.m = m;
			this.n = n;
		}

		@Override
		public Double call() throws Exception {
			double result = 1;
			for (int i = 0; i < n; i++) {
				result *= m;
				Thread.sleep(10);
			}
			
			System.out.printf("%n computed %.02f raised to %d%n",m,n);
			return result;
		}
		
	}
}
