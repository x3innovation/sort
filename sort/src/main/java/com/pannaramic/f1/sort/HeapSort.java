package com.pannaramic.f1.sort;

public class HeapSort 
{
    private static int n;
    private static int left;
    private static int right;
    private static int largest;

    // Build-Heap Function
    public static void buildheap(int []a){
    	n=a.length-1;
    	for(int i=n/2;i>=0;i--){
    		System.out.println(i);
    		maxheap(a,i);
    	}
    }
    
    // Max-Heap Function
    public static void maxheap(int[] a, int i){
    	left=2*i;
    	right=2*i+1;
    	System.out.println(i + " " + left + " " + right);
    	if(left <= n && a[left] > a[i]){
    		largest=left;
    	}
    	else{
    		largest=i;
    	}
    	
    	if(right <= n && a[right] > a[largest]){
    		largest=right;
    	}
    	if(largest!=i){
    		exchange(a, i,largest);
    		maxheap(a, largest);
    	}
    }
    
    // Exchange Function
    public static void exchange(int a[], int i, int j){
    	int t=a[i];
    	a[i]=a[j];
    	a[j]=t; 
    	}
    
    // Sort Function
    public static void sort(int []a0){
    	buildheap(a0);
    	
    	for(int i=n;i>0;i--){
    		exchange(a0, 0, i);
    		n=n-1;
    		maxheap(a0, 0);
    	}
    }
    
    public static void main(String[] args) {
		int []a1={3,5,1,2,4,4,4,8,7,9,3,2,0,1,7,6,5,4};
    	sort(a1);
    	for(int i=0;i<a1.length;i++){
    		System.out.print(a1[i] + " ");
    	}
	}
} 
