/*
 * This file is part of the MLDA.
 *
 * (c)  Jose Maria Moyano Murillo
 *      Eva Lucrecia Gibaja Galindo
 *      Sebastian Ventura Soto <sventura@uco.es>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package utils;

/**
 * Heap sorting
 * 
 * @author Jose Maria Moyano Murillo
 */
public class HeapSort 
{ 
    private static double[] a; 
    private static int n; 
    private static int left; 
    private static int right; 
    private static int largest;
    
    /**
     * Build heap
     * 
     * @param a Array of values
     */
    public static void buildheap(double []a){ 
        n=a.length-1; 
        for(int i=n/2;i>=0;i--){ 
            maxheap(a,i); 
        } 
    } 
    
    /**
     * Obtain sorted array
     * 
     * @return Sorted array
     */
    public static double[] getSortedArray(){
        return a;
    }
    
    /**
     * Max heap
     * 
     * @param a Array of values
     * @param i i 
     */
    public static void maxheap(double[] a, int i){ 
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
    
    /**
     * Swaps values of two indices
     * 
     * @param i index1
     * @param j index2
     */
    public static void exchange(int i, int j){ 
        double t=a[i]; 
        a[i]=a[j]; 
        a[j]=t; 
    } 
    
    /**
     * Sort array
     * 
     * @param a0 array
     */
    public static void sort(double []a0){ 
        a = a0; 
        buildheap(a); 
        
        for(int i=n;i>0;i--)
        { 
            exchange(0,i); 
            n=n-1; 
            maxheap(a, 0); 
        } 
    } 

}

