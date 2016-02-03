/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import static metrics.HeapSort.buildheap;
import static metrics.HeapSort.exchange;
import static metrics.HeapSort.maxheap;

/**
 *
 * @author oscglezm
 */
public class HeapSort1 {
    
    private static int[] a; 
    private static int n; 
    private static int left; 
    private static int right; 
    private static int largest;
    
    public static void buildheap(int []a){ 
        n=a.length-1; 
        for(int i=n/2;i>=0;i--){ 
            maxheap(a,i); 
        } 
    } 
    
    public static int[] get_array_sorted(){return a;}
    
    public static void maxheap(int[] a, int i){ 
        left=2*i; 
        right=2*i+1; 
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
            exchange(i,largest); 
            maxheap(a, largest); 
        } 
    } 
    
    public static void exchange(int i, int j){ 
        int t=a[i]; 
        a[i]=a[j]; 
        a[j]=t; 
        } 
    
    public static void sort(int []a0){ 
        a=a0; 
        buildheap(a); 
        
        for(int i=n;i>0;i--)
        { 
            exchange(0,i); 
            n=n-1; 
            maxheap(a, 0); 
        } 
    } 

    
}
