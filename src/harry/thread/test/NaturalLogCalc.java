package harry.thread.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 
 * @author Harry
 *
 */
public class NaturalLogCalc {
	private static final int numberOfTerms = 10;
	private static final double[] termArray = new double[numberOfTerms];
	private static final float x = 0.2f;
	
	public static void main(String[] args) {
		CyclicBarrier cylicBarrier = new CyclicBarrier(numberOfTerms, new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Computing series sum");
				double sum = 0;
				for (double term : termArray) {
					sum += term;
				}
				
				System.out.println("ln (1-" + x + ") equals " + -sum);
			}
		});
		for (int i = 0; i < numberOfTerms; i++) {
			new Thread(new TermCalc(i, cylicBarrier)).start();;
		}
		
		System.out.println("Waiting....");
	}
	
	private static class TermCalc implements Runnable{
		private int termIndex;
		private CyclicBarrier cylicBarrier;
		
		public TermCalc(int termIndex, CyclicBarrier cylicBarrier) {
			this.termIndex = termIndex;
			this.cylicBarrier = cylicBarrier;
		}

		@Override
		public void run() {
			double result = Math.pow(x, termIndex + 1) / (termIndex + 1);
			termArray[termIndex] = result;
			System.out.println("Term " + (termIndex + 1) + ":" + result);
			try {
				cylicBarrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
		
	}
}
