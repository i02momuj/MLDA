package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import mldc.attributes.*;
import mldc.base.MLDataMetric;
import mldc.imbalance.*;
import mldc.labelsDistribution.*;
import mldc.labelsRelation.*;
import mldc.size.*;

import mulan.data.InvalidDataFormatException;
import mulan.data.LabelsPair;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.data.UnconditionalChiSquareIdentifier;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;


/**
 * 
 * @author Jose Maria Moyano Murillo
 */
public class util {          
    
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
    
    
    public static boolean existsValue (double[] visited , double current)
    {
        for(int i=0; i<visited.length;i++)
        {
            if(visited[i]==current){
                return true;
            }
        }
        
        return false;
    }

     
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
     

    public static ImbalancedFeature getMin( ArrayList<ImbalancedFeature> list)
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
    

    public static Object[] getValuesByRow(int rowNumber, double[][] coefficients, String rowName )
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
    
    
    public static double[][] initializeNegativeValuesMatrix(int length)
    {
        double[][] data = new double[length][length];
         
        for(int i=0;i<length;i++){
            for(int j=0;j<length;j++){
            data[i][j]=-1.0; 
           }     
        }
            
        return data;
    }
    
    
    public static String[] listToArray(ArrayList<String> lista)
    {
        String[] result = new String[lista.size()];
        
        for( int i=0; i<result.length ;i++)
        {
            result[i]=lista.get(i);
        }
            
        return result;
    }
    
        
    public static int getPossibleCombinations(int n)
    {
        int result=0;
         
        for(int i=n-1; i>0; i--)
        {
            result +=i;
        }
         
        return result;
    }
    
    
    public static boolean hasMoreNDigits(double d, int digits)
    {
    
        String text = Double.toString(Math.abs(d));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        
        if(decimalPlaces<=digits) return false;
        return true;
    }
    
   
    public static boolean isNumber(String s)
    {
        if(s.isEmpty()) {
            return false;
        }
        
        s = s.toLowerCase().trim();
        
        String alphabet ="abcdefghijklmnopqrstuvwxyz";
        
        char current;
        for(int i=0;i<s.length();i++)
        {
            current = alphabet.charAt(i);
                
            if(s.indexOf(current)!= -1) {
                return false;
            }
        }
            
        return true;
    }

}
