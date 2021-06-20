package Elfor;

import java.util.Random;

public class MyArrays{

	public static void reverseit(int[] A){
		int temp;
		for(int i = 0; i < A.length/2; i++){
			temp = A[i];
			A[i] = A[A.length-1-i];
			A[A.length-1-i] = temp;
		}
	}

	public static void bubleSort(int[] A){
		int temp;
		for(int i = 0; i < A.length-1; i++){
			for(int j = 0; j < A.length-1; j++){
				if(A[j]>A[j+1]){
					temp = A[j];
					A[j] = A[j+1];
					A[j+1] = temp;
				}
			}
		}
	}
	public static void bubleSort(int[][] A){
		for(int i = 0; i < A.length; i++){
			bubleSort(A[i]);
		}
	}

	public static void selectionSort(int[] A){

		for(int i = 0; i < A.length - 1; i++){
			int min_indx = i;
			for(int j = i+1; j<A.length; j++){
				if(A[j]<A[min_indx])
					min_indx = j;
			}
			
			int temp = A[min_indx]; 
			A[min_indx] = A[i]; 
			A[i] = temp;
		}
	}//selectionSort
	public static void selectionSort(int[][] A){
		for(int i = 0; i < A.length; i++){
			selectionSort(A[i]);
		}
	}

	public static void printArray(String[] A){
		System.out.print("[ "+A[0]);
		for(int i = 1; i < A.length; i++){
			if(A[i] == null) continue;
			System.out.printf(" , %s",A[i]);
		}
		System.out.println(" ]");
	}

	public static void printArray(String[][] A){

		for(int i = 0; i < A.length; i++){
			System.out.print("[");
			for(int j = 0; j < A[i].length; j++){
				if(A[i][j] == null) continue;
				if(j != 0) System.out.print(",");
				System.out.printf(" %s ",A[i][j]);
			}//col
			System.out.println("]");
		}//row


	}//printArray

	public static void printArray(int[] A){
		System.out.print("[ "+A[0]);
		for(int i = 1; i < A.length; i++){
			System.out.printf(" , %d",A[i]);
		}
		System.out.println(" ]");
	}
	public static void printArray(int[][] A){
		if(A == null) {
			System.out.println("null array");
			return;
		}
		for(int i = 0; i < A.length; i++){
			if(A[i] == null) continue;
			System.out.print("[");
			for(int j = 0; j < A[i].length; j++){
				if(j != 0) System.out.print(",");
				System.out.printf(" %d ",A[i][j]);
			}//col
			System.out.println("]");
		}//row


	}//printArray

	public static int[] createRandomArray(int r){
		Random random = new Random();
		int[] A = new int[r];
		for(int i = 0; i < r; i++){
			A[i] = random.nextInt(10);
		}
		return A;
	}
	public static int[][] createRandomArray(int r, int c){
		Random random = new Random();
		int[][] A = new int[r][c];
		for(int i = 0; i < r; i++){
			for(int j = 0; j < c; j++){
				A[i][j] = random.nextInt(10);
			}
		}
		return A;
	}


	public static int searchArray(int[] A, int x) {
		for (int i = 0; i < A.length; i++) {
			if(A[i] == x)
				return i;
		}
		return -1;
	}

	public static int[] searchArray(int[][] A, int x) {
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[i].length; j++) {
				if(A[i][j] == x)
					return new int[] {i,j};
			}
		}
		return null;
	}
}
