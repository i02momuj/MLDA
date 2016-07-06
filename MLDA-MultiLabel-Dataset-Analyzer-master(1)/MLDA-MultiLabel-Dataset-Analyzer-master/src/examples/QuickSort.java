/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

/**
 *
 * @author oscglezm
 */
public class QuickSort{

  public static void main(String a[]){
      int i;
      int array[] = {2,5,7,9,11,13,56,56,74,98,98,99,100};
  
      double[] arreglo ={32.0,21.3,1.9,2.1,43.3,5.8,7.2,41.013,86.0123,45.0346,72.078,15.0234,6.143,12.6545,9.325,43.754,98.0234,13.123,15.14};
      
      Recorre_Arreglo(arreglo);
      
        Mergesort sorter = new Mergesort();
        sorter.sort(arreglo);
        
      Recorre_Arreglo(arreglo);
      
      System.out.println("cantidad de elemento es "+array.length);
      System.out.println("la media es "+ array.length/2);
      
      double value =get_mediana(array);
      System.out.println("la mediana es "+value);
      
      double q1 = get_q1(array);
      System.out.println("el 1er cuartil es "+ q1);
      
      double q3 = get_q3(array);
      System.out.println("el 3er cuartil es "+ q3);
      

    }
  
       public static void Recorre_Arreglo(double[] data)
    {
       // System.out.println("RECORRER ARREGLO DATOS");
        
        for(int j=0; j<data.length ; j++)
             System.out.print(" , "+data[j]);
               
        System.out.println();            
    }

       public static double get_mediana(int[] arreglo_ordenado)
     {
       int mean = arreglo_ordenado.length/2;
           
       if( arreglo_ordenado.length %2 !=0 ) return arreglo_ordenado[mean];
       else
       {
           double value1 = arreglo_ordenado[mean-1];
           double value2 = arreglo_ordenado[mean];
           return (value1+value2)/2;
       }
     }
       
       public static double get_q1(int[] arreglo_ordenado)
     {
         int cuarto = arreglo_ordenado.length/4;
         System.out.println("cuarto " + cuarto);
          
          if(arreglo_ordenado.length %4 ==0)return arreglo_ordenado[cuarto-1];
          return arreglo_ordenado[cuarto];
          //return arreglo_ordenado[parte_entera-1];
                 
     }
       
         public static double get_q3(int[] arreglo_ordenado)
     {
         int cuarto = 3*(arreglo_ordenado.length/4);
         System.out.println("cuarto " + cuarto);
          
          if(arreglo_ordenado.length %4 ==0)return arreglo_ordenado[cuarto-1];
          return arreglo_ordenado[cuarto];
          //return arreglo_ordenado[parte_entera-1];
                 
     }

  
  public static void quick_srt(int array[],int low, int n){
      int lo = low;
      int hi = n;
      if (lo >= n) {
          return;
      }
      int mid = array[(lo + hi) / 2];
      while (lo < hi) {
          while (lo<hi && array[lo] < mid) {
              lo++;
          }
          while (lo<hi && array[hi] > mid) {
              hi--;
          }
          if (lo < hi) {
              int T = array[lo];
              array[lo] = array[hi];
              array[hi] = T;
          }
      }
      if (hi < lo) {
          int T = hi;
          hi = lo;
          lo = T;
      }
      quick_srt(array, low, lo);
      quick_srt(array, lo == low ? lo+1 : lo, n);
   }
}