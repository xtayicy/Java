import java.util.Arrays;

/**
 * 
 * @author Harry
 *
 */
public class InsertionAndMergeSort {

	public static void main(String[] args) {
		int[] data = new int[]{4,3,8,7,2,6,1,5};
		//int[] data= new int[]{2,6,1,5,4,3,8,7};
		/*insertionSort(data,0,data.length - 1);
		System.out.println(Arrays.toString(data));*/
		
		int[] destData = new int[data.length];
		new Sort(data,destData,0,data.length - 1).sort();
		System.out.println(Arrays.toString(data));
	}
	
	private static class Sort{
		private int[] srcData;
		private int[] destData;
		private int lowerIndex,upperIndex;
		
		public Sort(int[] srcData, int[] destData, int lowerIndex, int upperIndex) {
			this.srcData = srcData;
			this.destData = destData;
			this.lowerIndex = lowerIndex;
			this.upperIndex = upperIndex;
		}
		
		public void sort(){
			if(upperIndex - lowerIndex < 4){
				insertionSort(srcData,lowerIndex,upperIndex);
				return;
			}
			
			int midIndex = (upperIndex + lowerIndex) >>> 1;
			new Sort(srcData,destData,lowerIndex,midIndex).sort();
			new Sort(srcData, destData, midIndex + 1, upperIndex).sort();
			merge(srcData,destData,lowerIndex,midIndex,upperIndex);
		}
		
		/**
		 *  3 4 7 8 1 2 5 6
			3 4 7 8 0 0 0 0
			
			1 4 7 8 1 2 5 6
			1 2 7 8 1 2 5 6
			1 2 3 8 1 2 5 6
			1 2 3 4 1 2 5 6
			1 2 3 4 5 2 5 6
			1 2 3 4 5 6 5 6
		 */
		public void merge(int[] srcData, int[] destData, int lowerIndex, int midIndex, int upperIndex) {
			if(srcData[midIndex] <= srcData[midIndex + 1]){
				return;
			}
			
			System.arraycopy(srcData, lowerIndex, destData, lowerIndex, midIndex - lowerIndex + 1);
			int i = lowerIndex;
			int j = midIndex + 1;
			int k = lowerIndex;
			while(k < j && j <= upperIndex){
				if(destData[i] <= srcData[j]){
					srcData[k++] = destData[i++];
				}else{
					srcData[k++] = srcData[j++];
				}
			}
			
			System.arraycopy(destData, i, srcData, k, j - k);
		}

		/**
		 *  4 3 8 7 2 6 1 5

			4 4 8 7 2 6 1 5
			3 4 8 7 2 6 1 5
			
			3 4 8 7 2 6 1 5
			
			3 4 8 8 2 6 1 5
			3 4 7 8 2 6 1 5
			
			3 4 7 8 8 6 1 5
			3 4 7 7 8 6 1 5
			3 4 4 7 8 6 1 5
			3 3 4 7 8 6 1 5
			2 3 4 7 8 6 1 5
			
			2 3 4 7 8 8 1 5
			2 3 4 7 7 8 1 5
			2 3 4 6 7 8 1 5
			
			2 3 4 6 7 8 8 5
			2 3 4 6 7 7 8 5
			2 3 4 6 6 7 8 5
			2 3 4 4 6 7 8 5
			2 3 3 4 6 7 8 5
			2 2 3 4 6 7 8 5
			1 2 3 4 6 7 8 5
			
			1 2 3 4 6 7 8 8
			1 2 3 4 6 7 7 8
			1 2 3 4 6 6 7 8
			1 2 3 4 5 6 7 8
		 * */
		private void insertionSort(int[] srcData,int lowerIndex,int upperIndex) {
			for (int i = lowerIndex + 1; i <= upperIndex; i++) {
				int j = i;
				int temp = srcData[j];
				while(j > lowerIndex && temp < srcData[j - 1]){
					srcData[j] = srcData[j - 1];
					--j;
				}
				
				srcData[j] = temp;
			}
		}
	}
}
