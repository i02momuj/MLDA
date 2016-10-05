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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class Utils {          
    
    /**
     * Get maximum key value
     * 
     * @param hm HashMap
     * @return Maximum key value
     */
    public  static int maxKey (HashMap<Integer,Integer> hm)
    {
        Set<Integer> keys= hm.keySet();

        int max = 0;

        for(int current : keys)
        {
            if(max<current) {
                max = current;
            }
        }

        return max;
    }
    
    /**
     * Cheks if exists a value into a double array
     * 
     * @param array Array of values
     * @param current Current value
     * @return True if it exists and false otherwise
     */
    public static boolean existsValue (double[] array , double current)
    {
        for(int i=0; i<array.length;i++)
        {
            if(array[i]==current){
                return true;
            }
        }
        
        return false;
    }

    /**
     * Get Q1 of an array
     * 
     * @param orderedArray Array with ordered values
     * @return Q1 value
     */
    public static double getQ1(double[] orderedArray)
    {
        int q = orderedArray.length/4;
       
        if(orderedArray.length %4 ==0){
            return orderedArray[q-1];
        }
        else{
            return orderedArray[q];
        }
    }

    /**
     * Get Q3 of an array
     * 
     * @param orderedArray Array with ordered values
     * @return Q3 value
     */
    public static double getQ3(double[] orderedArray)
    {
        int q = 3*(orderedArray.length/4);
        
        if(orderedArray.length %4 ==0){
            return orderedArray[q-1];
        }
        else{
            return orderedArray[q];
        }
    }
    
    /**
     * Obtain the median of an array
     * 
     * @param sortedArray Array with ordered values
     * @return Median
     */
    public static double getMedian(double[] sortedArray)
    {
        int mean = sortedArray.length/2;
           
        if( sortedArray.length %2 !=0 ) {
            return sortedArray[mean];
        }
        else
        {
            double value1 = sortedArray[mean-1];
            double value2 = sortedArray[mean];
            return (value1+value2)/2;
        }
    }
        
    /**
     * Obtain ImbalancedFeature with most number of appearances from a list of ImbalancedFeature objects
     * 
     * @param list List of ImbalancedFeature objects
     * @return ImbalancedFeature with max number of appearances
     */
    public static ImbalancedFeature getMax(ArrayList<ImbalancedFeature> list)
    {
        ImbalancedFeature max = list.get(0);
               
        for(ImbalancedFeature current : list)
        {
            if(current.getAppearances()>max.getAppearances()) {
                max = current;
            }
        }
            
        return max;       
    }
     
    /**
     * Obtain ImbalancedFeature with minimum number of appearances from a list of ImbalancedFeature objects
     * 
     * @param list List of ImbalancedFeature objects
     * @return ImbalancedFeature with min number of appearances
     */
    public static ImbalancedFeature getMin(ArrayList<ImbalancedFeature> list)
    {
        ImbalancedFeature min = list.get(0);
               
        for(ImbalancedFeature current : list)
        {
            if(current.getAppearances()<min.getAppearances()) {
                min = current;
            }
        }
            
        return min;       
    }
    
    /**
     * Get values of a matrix by row
     * 
     * @param rowNumber Row number
     * @param coefficients Matrix of coefficients
     * @param rowName Row name
     * @return Array of objects
     */
    public static Object[] getValuesByRow(int rowNumber, double[][] 
            coefficients, String rowName)
    {
        Object[] row = new Object[coefficients.length+1];
        String truncate;
        
        for(int i=0; i<row.length;i++)
        {       
            if(i==0)
            {              
                row[i]= rowName;
                continue;
            }
          
            if(coefficients[i-1][rowNumber]==-1.0)
            {
                row[i]= " ";
                continue;
            }
            
            if(i-1 != rowNumber)
            {
                truncate = Double.toString(coefficients[i-1][rowNumber]);
                row[i] = MetricUtils.truncateValue(truncate, 4);
            }                    
            else if (i-1==rowNumber) {
                row[i]= "---"; 
            }
        }
        
        return row;
    }
    
    /**
     * Obtain maximum value of an int array
     * 
     * @param v Array
     * @return Max value
     */
    public static int getMax(int [] v)
    {        
        int max = Integer.MIN_VALUE;
        
        for(int i=0; i<v.length; i++){
            if(v[i] > max){
                max = v[i];
            }
        }
        
        return max;
    }
    
    /**
     * Obtain minimum value of an int array
     * 
     * @param v Array
     * @return Min value
     */
    public static int getMin(int [] v)
    {        
        int min = Integer.MAX_VALUE;
        
        for(int i=0; i<v.length; i++){
            if(v[i] < min){
                min = v[i];
            }
        }
        
        return min;
    }
    
    /**
     * Initialize a matrix to negative values
     * 
     * @param length Length of the square matrix
     * @return Initialized matrix
     */
    public static double[][] initializeNegativeValuesMatrix(int length)
    {
        double[][] data = new double[length][length];
         
        for(int i=0;i<length;i++){
            for(int j=0;j<length;j++){
                data[i][j] = -1.0; 
           }     
        }
            
        return data;
    }
    
    /**
     * Convert a list of String into an array
     * 
     * @param list List of String objects
     * @return 
     */
    public static String[] listToArray(ArrayList<String> list)
    {
        String[] result = new String[list.size()];
        
        for( int i=0; i<result.length ;i++)
        {
            result[i] = list.get(i);
        }
            
        return result;
    }
    
    /**
     * Obtain all possible combinations of pairs of n labels
     * 
     * @param n Number of labels
     * @return 
     */
    public static int getPossibleCombinations(int n)
    {
        int result = 0;
         
        for(int i=n-1; i>0; i--)
        {
            result +=i;
        }
         
        return result;
    }
    
    /**
     * Check if the number has more than n digits
     * 
     * @param d Double number
     * @param digits Number of digits
     * @return True if has more than the specified number of digits and false otherwise
     */
    public static boolean hasMoreNDigits(double d, int digits)
    {
    
        String text = Double.toString(Math.abs(d));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        
        return (decimalPlaces > digits);
    }
    
    /**
     * Check if the String contains a number
     * 
     * @param s String
     * @return True if it is a number and false otherwise
     */
    public static boolean isNumber(String s)
    {
        try  
        {  
            double d = Double.parseDouble(s);  
        }  
        catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
        
        return true;  
    }
    
    /**
     * Check if an array of int contains a value n
     *  
     * @param A Array
     * @param n Number
     * @return True if A contains n and false otherwise
     */
    public static boolean contains(int [] A, int n){
        for(int a : A){
            if(a == n){
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Convert an array of Integer into an array of int
     * 
     * @param IntegerArray Array of Integer
     * @return Array of int
     */
    public static int[] toPrimitive(Integer[] IntegerArray) {
        int[] result = new int[IntegerArray.length];
	for (int i = 0; i < IntegerArray.length; i++) {
            result[i] = IntegerArray[i].intValue();
	}
	return result;	
    }
    
    

}
