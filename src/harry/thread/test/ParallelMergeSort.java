package harry.thread.test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 
 * @author Harry
 *
 */
public class ParallelMergeSort {
	private static ForkJoinPool threadPool;
	private static final int THRESHOLD = 16;

	public static void main(String[] args) {
		int processors = Runtime.getRuntime().availableProcessors();
		System.out.println("No of processors: " + processors);
		threadPool = new ForkJoinPool(processors);
		Double[] data = createRandomData(1000);
		System.out.println("Original unsorted data:");
		for (Double d : data) {
			System.out.printf("%3.2f ",d);
		}
		
		sort(data);
		System.out.println("\n\nSorted Array:");
		for (Double d : data) {
			System.out.printf("%3.2f ",d);
		}
	}

	private static void sort(Comparable<Double>[] objectArray) {
		Comparable<Double>[] destArray = new Comparable[objectArray.length];
		threadPool.invoke(new SortTask(objectArray, destArray, 0, objectArray.length - 1));
	}
	
	private static class SortTask extends RecursiveAction{
		private Comparable<Double>[] sourceArray;
		private Comparable<Double>[] destArray;
		private int lowerIndex,upperIndex;
		
		public SortTask(Comparable<Double>[] sourceArray, Comparable<Double>[] destArray, int lowerIndex,
				int upperIndex) {
			this.sourceArray = sourceArray;
			this.destArray = destArray;
			this.lowerIndex = lowerIndex;
			this.upperIndex = upperIndex;
		}

		@Override
		protected void compute() {
			if(upperIndex - lowerIndex < THRESHOLD){
				insertionSort(sourceArray,lowerIndex,upperIndex);
				return;
			}
			
			int midIndex = (lowerIndex + upperIndex) >>> 1;
			invokeAll(new SortTask(sourceArray, destArray, lowerIndex, midIndex),new SortTask(sourceArray, destArray, midIndex + 1, upperIndex));
			merge(sourceArray,destArray,lowerIndex,midIndex,upperIndex);
		}
	}
	
	private static void insertionSort(Comparable<Double>[] sourceArray, int lowerIndex, int upperIndex) {
		for (int i = lowerIndex + 1; i < upperIndex; i++) {
			int j = i;
			Comparable<Double> temp = sourceArray[j];
			while(j > lowerIndex && temp.compareTo((Double)sourceArray[j - 1]) < 0){
				sourceArray[j] = sourceArray[j - 1];
				--j;
			}
			
			sourceArray[j] = temp;
		}
	}

	public static void merge(Comparable<Double>[] sourceArray, Comparable<Double>[] destArray, int lowerIndex, int midIndex,
			int upperIndex) {
		if(sourceArray[midIndex].compareTo((Double)sourceArray[midIndex + 1]) <= 0){
			return;
		}
		
		System.arraycopy(sourceArray, lowerIndex, destArray, lowerIndex, midIndex - lowerIndex + 1);
		int i = lowerIndex;
		int j = midIndex + 1;
		int k = lowerIndex;
		while(k < j && j <= upperIndex){
			if(destArray[i].compareTo((Double)sourceArray[j]) <= 0){
				sourceArray[k++] = destArray[i++];
			}else{
				sourceArray[k++] = sourceArray[j++];
			}
		}
		
		System.arraycopy(destArray, i, sourceArray, k, j - k);
	}

	private static Double[] createRandomData(int length) {
		Double[] data = new Double[length];
		for (int i = 0; i < data.length; i++) {
			data[i] = length * Math.random();
		}
		
		return data;
	}
}
