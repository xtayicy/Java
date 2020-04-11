package harry.thread.test;

import java.text.DateFormatSymbols;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * @author Harry
 *
 */
public class AnnualSalesCalc {
	private static final int NUMBER_OF_CUSTOMERS = 100;
	private static final int NUMBER_OF_MONTHS = 12;
	private static int[][] salesMatrix;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		generateMatrix();
		printMatrix();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		HashSet<Future<Integer>> set = new HashSet<Future<Integer>>();
		for (int row = 0; row < NUMBER_OF_CUSTOMERS; row++) {
			Callable<Integer> task = new Summer(row);
			Future<Integer> future = executor.submit(task);
			set.add(future);
		}
		
		int sum = 0;
		for (Future<Integer> future : set) {
			sum += future.get();
		}
		
		System.out.printf("%nThe annual turnover (bags):%s%n%n",sum);
		executor.shutdown();
	}
	
	private static class Summer implements Callable<Integer>{
		private int companyID;
		
		public Summer(int companyID) {
			this.companyID = companyID;
		}

		@Override
		public Integer call() throws Exception {
			int sum = 0;
			for (int col = 0; col < NUMBER_OF_MONTHS; col++) {
				sum += salesMatrix[companyID][col];
			}
			
			System.out.printf("Totaling for clientID 1%02d complete%n",companyID);
			return sum;
		}
		
	}

	private static void printMatrix() {
		System.out.print("\t\t");
		String[] monthDisplayNames = new DateFormatSymbols().getShortMonths();
		for (String name : monthDisplayNames) {
			System.out.printf("%8s",name);
		}
		
		System.out.printf("%n%n");
		for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
			System.out.printf("Client ID: 1%02d",i);
			for (int j = 0; j < NUMBER_OF_MONTHS; j++) {
				System.out.printf("%10d",salesMatrix[i][j]);
			}
			
			System.out.println();
		}
		
		System.out.printf("%n%n");
	}

	private static void generateMatrix() {
		salesMatrix = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_MONTHS];
		for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
			for (int j = 0; j < NUMBER_OF_MONTHS; j++) {
				salesMatrix[i][j] = (int) (Math.random() * 100);
			}
		}
	}

}
