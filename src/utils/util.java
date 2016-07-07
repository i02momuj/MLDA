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
    


   
   
   
  
  
   
   
    public static void Save_text_file(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset, ImbalancedFeature[] imbalanced_data, boolean  es_de_tipo_meka, Hashtable<String, String> tableMetrics)
    {
         Instances i1= dataset.getDataSet();
        
        //String maxString = "Average of unconditionally dependent label pairs by chi-square test";
        String maxString = new String();
        for(String s : metric_list){
            if(s.length() > maxString.length()){
                maxString = s;
            }
        }
        double maxLength = maxString.length();
        
        wr.write("Relation name:"+ "\t"+i1.relationName());
        wr.write(System.getProperty("line.separator"));  

        for(String metric : metric_list)
        {
            //wr.write(metric + ":" + get_tabs(metric) + tableMetrics.get(metric).replace(",", "."));
            wr.write(metric + ": ");
            for(int i=0; i<(maxLength-metric.length()); i++){
                wr.write(" ");
            }

            wr.write(getValueFormatted(metric, tableMetrics.get(metric)));

            wr.write(System.getProperty("line.separator"));  
        }
 
     }
   
   
   
   public static void Save_text_file_multi(PrintWriter wr,ArrayList<String> metric_list, ArrayList<String> dataNames, Hashtable<String, Hashtable<String, String>> tableMetrics)
     {
        String maxString = "Average of unconditionally dependent label pairs by chi-square test:";
        double maxLength = maxString.length();
        
        
        String value = new String();
        
        String maxName = new String();
        for(int i=0; i<dataNames.size(); i++){
            if(dataNames.get(i).length() > maxName.length()){
                maxName = dataNames.get(i);
            }
        }
        
        String line = new String();
        
        for(int i=0; i<maxLength; i++){ 
            line += " ";
        }
        
        line += " ";
        
        for(int i=0; i<dataNames.size(); i++){
            line += "   ";
            
            for(int j=0; j<(maxName.length()-dataNames.get(i).length()); j++){ 
                line += " ";
            }
            
            line += dataNames.get(i);
        }
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
        
        
        for(String metric : metric_list)
        {
            line = "";
            //wr.write(metric + ":" + get_tabs(metric) + tableMetrics.get(metric).replace(",", "."));
            line = line + metric + ":";
            
            for(int j=0; j<(maxLength-metric.length()); j++){
                line += " ";
            }
            
            for(int i=0; i<dataNames.size(); i++){
                line += "   ";
                
                for(int j=0; j<maxName.length()-dataNames.get(i).length(); j++){ 
                    line += " ";
                }
                
                value = getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
                
                line += value;
                
                for(int j=0; j<dataNames.get(i).length()-value.length(); j++){ 
                    line += " ";
                }
            }
            
            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
        }
 
     }
   
   
    public static void Save_tex_file(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset, ImbalancedFeature[] imbalanced_data, boolean  es_de_tipo_meka, Hashtable<String, String> tableMetrics)
    {
        Instances i1= dataset.getDataSet();
        
        //LaTeX article header
        wr.write("\\documentclass[a4paper,11pt]{article}");
        wr.write(System.getProperty("line.separator"));
        wr.write("\\usepackage[T1]{fontenc}");
        wr.write(System.getProperty("line.separator"));
        wr.write("\\usepackage[utf8]{inputenc}");
        wr.write(System.getProperty("line.separator"));
        wr.write("\\usepackage{lmodern}");
        wr.write(System.getProperty("line.separator"));
        wr.write("\\usepackage[spanish]{babel}");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\begin{document}");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\begin{tabular}{|l|r|}");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\hline");
        wr.write(System.getProperty("line.separator"));
        
        //Metrics
        String value = new String();
        for(String metric : metric_list)
        {
            value = getValueFormatted(metric, tableMetrics.get(metric));
            if(value.equals("---")){
               wr.write(metric + " & " + "NaN" + " \\\\"); 
            }
            else{
                wr.write(metric + " & " + value + " \\\\"); 
            }
            
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write("\\hline");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\end{tabular}");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\end{document}");
        wr.write(System.getProperty("line.separator"));
 
     }
    
    
    public static void Save_tex_file_multi(PrintWriter wr,ArrayList<String> metric_list, ArrayList<String> dataNames, Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
        String s = new String();
        
        //LaTeX article header
        wr.write("\\documentclass[a4paper,11pt]{article}");
        wr.write(System.getProperty("line.separator"));
        wr.write("\\usepackage[T1]{fontenc}");
        wr.write(System.getProperty("line.separator"));
        wr.write("\\usepackage[utf8]{inputenc}");
        wr.write(System.getProperty("line.separator"));
        wr.write("\\usepackage{lmodern}");
        wr.write(System.getProperty("line.separator"));
        wr.write("\\usepackage[spanish]{babel}");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\begin{document}");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("");
        wr.write(System.getProperty("line.separator"));
        
        s = "\\begin{tabular}{l|";
        for(int i=0; i<dataNames.size(); i++){
            s += "r|";
        }
        s += "}";
        wr.write(s);
        wr.write(System.getProperty("line.separator"));
        
        s = " ";
        for(String name : dataNames){
            s = s + "& " + name.replaceAll("_", "\\\\\\_") + " ";
        }
        s += " \\\\";
        
        wr.write(s);       
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\hline");
        wr.write(System.getProperty("line.separator"));
        
        //Metrics
        String value = new String();
        for(String metric : metric_list)
        {
            s = metric;
            
            for(int i=0; i<dataNames.size(); i++){
                value = getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
            
                if(value.equals("---")){
                    s = s + " & " + "NaN";
                }
                else{
                    s = s + " & " + value;
                }                
            }
            
            s += " \\\\";
            
            wr.write(s); 
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write("\\hline");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\end{tabular}");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\end{document}");
        wr.write(System.getProperty("line.separator"));
 
     }
    
   
    public static void Save_csv_file(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset, ImbalancedFeature[] imbalanced_data, boolean  es_de_tipo_meka, Hashtable<String, String> tableMetrics)
    {
         
        Instances i1= dataset.getDataSet();
                 
        wr.write("Relation Name"+ ";" + i1.relationName());
        wr.write(System.getProperty("line.separator"));  

        wr.write(System.getProperty("line.separator"));   
            
        //atributo[] imbalanced_data =  MetricUtils.getImbalancedData(dataset);
        
        String value = new String();
        for(String metric : metric_list)
        {
            value = getValueFormatted(metric, tableMetrics.get(metric));
            if(value.equals("---")){
               wr.write(metric + ";" + "NaN"); 
            }
            else{
                wr.write(metric + ";" + value);
            }
            
            wr.write(System.getProperty("line.separator"));  
        }
 
     }
    
    public static void Save_table_csv(PrintWriter wr, JTable table)
    {                 
        //wr.write("Label frequency");
        //wr.write(System.getProperty("line.separator"));  
    
        String line = new String();
        
        line = "";
        for(int j=0; j<table.getColumnCount(); j++){
            line += table.getColumnName(j) + "; ";
        }
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
                
        for(int i=0; i<table.getRowCount(); i++){
            line = "";
            for(int j=0; j<table.getColumnCount(); j++){
                line += table.getValueAt(i, j) + "; ";
            }
            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
        }
     }
    
    
    public static void Save_mv_table_csv(PrintWriter wr, JTable table, JTable table2)
    {                 
        String line = new String();
        
        line = "";
        for(int j=0; j<table.getColumnCount(); j++){
            line += table.getColumnName(j) + "; ";
        }
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
                
        for(int i=0; i<table.getRowCount(); i++){
            line = "";
            for(int j=0; j<table.getColumnCount(); j++){
                line += table.getValueAt(i, j) + "; ";
            }
            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
        }
     }

    
    public static void Save_table_labelsets_frequency_csv(PrintWriter wr, JTable table, ArrayList<String> labelsetStrings)
    {                 
        String line = new String();
        
        line = "";

        line += "Labelset id" + "; " +
                "Labelset string" + "; " +
                "#Examples" + "; " + 
                "Frequency" + "; ";
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
                
        for(int i=0; i<table.getRowCount(); i++){
            line = "";
            
            line += table.getValueAt(i, 0) + "; " + 
                    "\"" + labelsetStrings.get(i) + "\"" + "; " +
                    table.getValueAt(i, 1) + "; " + 
                    table.getValueAt(i, 2) + "; ";

            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
        }
     }
    
    public static void Save_table_labelsets_IR_csv(PrintWriter wr, JTable table, ArrayList<String> labelsetStrings)
    {                 
        String line = new String();
        
        line = "";

        line += "Labelset id" + "; " +
                "Labelset string" + "; " +
                "IR" + "; ";
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
                
        for(int i=0; i<table.getRowCount(); i++){
            line = "";
            
            line += table.getValueAt(i, 0) + "; " + 
                    "\"" + labelsetStrings.get(i) + "\"" + "; " +
                    table.getValueAt(i, 1) + "; ";

            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
        }
     }
    
    
    public static void save_mv_csv_file(PrintWriter wr, JTable table, Hashtable<String, Integer[]> views, MultiLabelInstances mlData)
    {                 
        String line = new String();
        
        line = "";

        line += "Name" + "; " +
                "#Attributes" + "; " +
                "LxIxF" + "; " + 
                "Ratio Inst/Att" + "; " + 
                "Average gain ratio" + "; ";
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
                
        for(int i=0; i<table.getRowCount(); i++){
            line = "";
            
            line += table.getValueAt(i, 0) + "; " + 
                    table.getValueAt(i, 1) + "; " +
                    table.getValueAt(i, 2) + "; " + 
                    table.getValueAt(i, 3) + "; " + 
                    table.getValueAt(i, 4) + "; ";

            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write(System.getProperty("line.separator"));  
        wr.write(System.getProperty("line.separator")); 
        
        line = "View" + "; " + "Attribute name" + "; ";        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
        
        for(int i=0; i<views.size(); i++){
            for(int j=0; j<views.get("View " + (i+1)).length; j++){
                Integer [] attributes = views.get("View " + (i+1));
                line = "View " + (i+1) + "; " + mlData.getDataSet().attribute(j).name();
                wr.write(line);
                wr.write(System.getProperty("line.separator"));  
            }
        }
     }
    
    public static void Save_csv_file_multi(PrintWriter wr,ArrayList<String> metric_list, ArrayList<String> dataNames, Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
       //System.out.println("Data_names: " + dataNames.toString());
        
        String value = new String();
         
        value = ";";
        for(int i=0; i<tableMetrics.size(); i++){
            value += dataNames.get(i) + ";";
        }
        wr.write(value);
        wr.write(System.getProperty("line.separator"));  
        
        
        String temp;
        //for(int i=0; i<tableMetrics.size(); i++){
        for(String metric : metric_list)
        {   value = metric + ";";
        
            for(int i=0; i<tableMetrics.size(); i++){
                temp = getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
                
                if(temp.equals("---")){
                    value = value + "NaN" + ";";
                }
                else{
                    value = value + temp + ";";
                }
            }
            
            wr.write(value);
            wr.write(System.getProperty("line.separator"));
        } 
        
 
     }
    
    
    public static void Save_chi_phi_csv_file(PrintWriter wr, double [][] coefficients, String[] labelNames)
    {
        for(int i=0; i<coefficients.length; i++){
           //System.out.println(Arrays.toString(coefficients[i]));
        }
        
        //Save label names row
        String line = new String();
        
        line = "Chi;";
        
        for (String labelName : labelNames) {
            line += labelName + ";";
        }
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
       ////System.out.println("line: " + line);
        
        for(int i=0; i<labelNames.length; i++){
            line = "";
            line += labelNames[i] + ";";
            
            for(int j=0; j<coefficients[i].length; j++){
                
                if(i >= j){
                    if(coefficients[j][i] == 0.0){
                        line += "" + ";";
                    }
                    else{
                        line += coefficients[j][i] + ";";
                    } 
                }
                else{
                    if(coefficients[i][j] == 0.0){
                        line += "" + ";";
                    }
                    else{
                        line += coefficients[i][j] + ";";
                    } 
                }
                
                
            }
            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
            //System.out.println("line: " + line);
        }
        
        wr.write(System.getProperty("line.separator"));  
        wr.write(System.getProperty("line.separator"));  
        line = "Phi;";
        
        for (String labelName : labelNames) {
            line += labelName + ";";
        }
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
        //System.out.println("line: " + line);
        
        for(int i=0; i<labelNames.length; i++){
            line = "";
            line += labelNames[i] + ";";
            
            for(int j=0; j<coefficients[i].length; j++){
                if(j >= i){
                    if(coefficients[j][i] == 0.0){
                        line += "" + ";";
                    }
                    else{
                        line += coefficients[j][i] + ";";
                    } 
                }
                else{
                    if(coefficients[i][j] == 0.0){
                        line += "" + ";";
                    }
                    else{
                        line += coefficients[i][j] + ";";
                    } 
                }
                
                
            }
            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
            //System.out.println("line: " + line);
        }
    
     }
    
    public static void Save_coocurrence_csv_file(PrintWriter wr, double [][] coefficients, String[] labelNames)
    {
        //Save label names row
        String line = new String();
        line += " ;";
        
        for (String labelName : labelNames) {
            line += labelName + ";";
        }
        
                 
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
       //System.out.println("line: " + line);
        
        for(int i=0; i<labelNames.length; i++){
            line = "";
            line += labelNames[i] + ";";
            
            for(int j=0; j<coefficients[i].length; j++){
                if(i == j){
                    line += "" + ";";
                }
                else if(i < j){
                    line += (int) coefficients[i][j] + ";";
                }
                else{
                    line += (int) coefficients[j][i] + ";";
                } 
            }
            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
           //System.out.println("line: " + line);
        }
    
     }
    
    public static void Save_heatmap_csv_file(PrintWriter wr, double [][] coefficients, String[] labelNames)
    {
        //Save label names row
        String line = new String();
        line += " ;";
        
        for (String labelName : labelNames) {
            line += labelName + ";";
        }

        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
       //System.out.println("line: " + line);
        
        for(int i=0; i<labelNames.length; i++){
            line = "";
            line += labelNames[i] + ";";
            
            for(int j=0; j<coefficients[i].length; j++){
                if(coefficients[j][i]==-1){
                    line += "" + ";";
                }
                else{
                    line += coefficients[j][i] + ";";
                } 
            }
            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
           //System.out.println("line: " + line);
        }
    
     }
    
    
   
    public static void Save_meka_file(PrintWriter wr,ArrayList<String> metric_list, MultiLabelInstances dataset, ImbalancedFeature[] imbalanced_data, boolean  es_de_tipo_meka, Hashtable<String, String> tableMetrics)
    {
         
         Instances i1= dataset.getDataSet();
                 
          wr.write("@relation" + " \'" + i1.relationName() + "\'");
          wr.write(System.getProperty("line.separator"));  
          
          wr.write(System.getProperty("line.separator")); 
    
        
        for(String metric : metric_list)
        {
            wr.write("@attribute " + metric.replace(" ", "_") + " numeric");
            //wr.write(metric + ":" + get_tabs(metric) + tableMetrics.get(metric).replace(",", "."));
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write(System.getProperty("line.separator")); 
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator")); 
        
        String line = new String();
        
        String value = new String();
        for(String metric : metric_list)
        {         
            value=getValueFormatted(metric, tableMetrics.get(metric));
            if(value.equals("---")){
                line += "?";
            }
            else{
                line += getValueFormatted(metric, tableMetrics.get(metric));
            }
            line += ", ";
        }
        //Delete last ", "
        line = line.substring(0, line.length()-2);
        wr.write(line);
        wr.write(System.getProperty("line.separator")); 
 
     }
    
    
    public static void Save_meka_file_multi(PrintWriter wr,ArrayList<String> metric_list, ArrayList<String> dataNames, Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
        wr.write("@relation" + " \'" + "relationMLDA" + "\'");
        wr.write(System.getProperty("line.separator"));  
          
        wr.write(System.getProperty("line.separator")); 
        
        wr.write("@attribute relation_name String");
        wr.write(System.getProperty("line.separator")); 

        for(String metric : metric_list)
        {
            wr.write("@attribute " + metric.replace(" ", "_") + " numeric");
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write(System.getProperty("line.separator")); 
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator")); 
        
        
        String value = new String();
        String line = new String();
        
        for(int i=0; i<dataNames.size(); i++){
            line = dataNames.get(i) + ", ";
            
            for(String metric : metric_list)
            {         
                value=getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
                if(value.equals("---")){
                    line += "?";
                }
                else{
                    line += getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
                }
                line += ", ";
            }
            
            //Delete last ", "
            line = line.substring(0, line.length()-2);
            wr.write(line);
            wr.write(System.getProperty("line.separator")); 
        }           
        
        
        
 
     }
    
    public static String get_value_metric_imbalanced(String metric,MultiLabelInstances dataset,ImbalancedFeature[] imbalanced_data)
    {
        double value = -1.0;
        MLDataMetric mldm = null;
        
        try{           
            switch (metric) 
            {
                case "Mean of IR per label intra class":
                    mldm = new MeanIRIntraClass();
                    break;     

                case "Max IR per label intra class":
                    mldm = new MaxIRIntraClass();
                    break;  

                case "Mean of IR per label inter class":
                    mldm = new MeanIRInterClass();
                    break;  

                case "Max IR per label inter class":
                    mldm = new MaxIRInterClass();
                    break;  

                case "Mean of IR per labelset":
                    mldm = new MeanIRLabelset();
                    break;  

                case "Max IR per labelset":
                    mldm = new MaxIRLabelset();
                    break; 

                case "Mean of standard deviation of IR per label intra class":
                    mldm = new MeanStdvIRIntraClass();
                    break;         
                    
                case "CVIR inter class":
                    mldm = new CVIRInterClass();     
                    break;       

                case "SCUMBLE":
                    mldm = new SCUMBLE();
                    break;    
                    
                default:  value = -1.0;
                         break;   
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            value = -1.0;
        }
        
        if(mldm != null){
            value = mldm.calculate(dataset);
        }
        else{
            value = -1.0;
        }
        
        if(Double.isNaN(value) || value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY){
            return("NaN");
        }
        else{
            NumberFormat formatter = new DecimalFormat("#0.000"); 
           return(formatter.format(value)); 
        }
    }
    
    
    public static MultiLabelInstances[] Get_dataset_train_test(int n, MultiLabelInstances[] lista) throws InvalidDataFormatException
    {
        MultiLabelInstances[] result = new MultiLabelInstances[2];
        
        Instances data_train, data_test;
        boolean flag=false;
        
        if(n!=0) data_train = new Instances(lista[0].getDataSet()); //inicialiar el objeto data_train sin que n no sea el primer dataset.
        else{
            data_train= new Instances(lista[1].getDataSet());
            data_test = new Instances(lista[0].getDataSet());//entonces n=0
            flag=true;
        }
        
        for(int i =1; i<lista.length; i++)
        {
            if(flag) 
            {
                flag=false;
                continue;                
            }
            if(i==n) {continue;}
            else
            {
                data_train.addAll(lista[i].getDataSet());
            }
        }
        data_test = new Instances(lista[n].getDataSet());
        
        MultiLabelInstances train = new MultiLabelInstances(data_train,lista[0].getLabelsMetaData());
        MultiLabelInstances test = new MultiLabelInstances(data_test,lista[0].getLabelsMetaData());
        
        result[0]=train;
        result[1]= test;
        
        return result;
    }
    
    public static String get_value_metric(String metric, MultiLabelInstances dataset , boolean es_de_tipo_meka)
    {       
        double value =-1.0;
        
        MLDataMetric mldm = null;
        
        try{           
            
        switch (metric) 
        {
            case "Labels x instances x features":  
                mldm = new LxIxF();
                break;     

            case "Instances":
                mldm = new mldc.size.Instances();
                break;
            
            case "Attributes":
                mldm = new mldc.size.Attributes();
                break;
                
            case "Labels":
                mldm = new Labels();
                break;
            
            case "Label density":
                mldm = new Density();
                break;                

            case "Label Cardinality":
                mldm = new Cardinality();
                break;

            case "Distinct labelsets":
                mldm = new DistinctLabelsets();
                break;
            
            case "Number of unique labelsets":
                mldm = new UniqueLabelsets();
                break;
            
            case "Proportion of distinct labelsets":
                mldm = new ProportionDistinctLabelsets();
                break;
            
            case "Density":
                mldm = new Density();
                break;
            
            case "Cardinality":
                mldm = new Cardinality();
                break;
            
            case "Bound":
                mldm = new Bound();
                break;
            
            case "Diversity":
                mldm = new Diversity();
                break;
            
            case "Proportion of unique label combination (PUniq)":
                mldm = new PUniq();
                break;
            
            case "Proportion of maxim label combination (PMax)":
                mldm = new PMax();
                break;
            
            case "Ratio of number of instances to the number of attributes":
                mldm = new RatioInstancesToAttributes();
                break;
            
            case "Number of binary attributes":
                mldm = new BinaryAttributes();
                break;
            
            case "Proportion of binary attributes":
                mldm = new ProportionBinaryAttributes();
                break;
            
            case "Proportion of nominal attributes":
                mldm = new ProportionNominalAttributes();
                break;
            
            case "Proportion of numeric attributes":
                mldm = new ProportionNumericAttributes();
                break;
            
            case "Number of nominal attributes":
                mldm = new NominalAttributes();
                break;
            
            case "Number of numeric attributes":
                mldm = new NumericAttributes();
                break;

            case "Mean of mean of numeric attributes":
                mldm = new MeanOfMeanOfNumericAttributes();
                break;
            
            case "Mean of standard deviation of numeric attributes":
                mldm = new MeanStdvNumericAttributes();
                break;
            
            case "Mean of skewness of numeric attributes":
                mldm = new MeanSkewnessNumericAttributes();
                break;
            
            case "Mean of kurtosis":
                mldm = new MeanKurtosis();
                break;
            
            case "Mean of entropies of nominal attributes":
                mldm = new MeanEntropiesNominalAttributes();
                break;
            
            case "Average absolute correlation between numeric attributes":
                mldm = new AvgAbsoluteCorrelationBetweenNumericAttributes();
                break;
            
            case "Proportion of numeric attributes with outliers":
                mldm = new ProportionNumericAttributesWithOutliers();
                break;
            
            case "Average gain ratio":
                mldm = new AvgGainRatio();
                break;
            
            case "Standard deviation of label cardinality":
                mldm = new StdvCardinality();
                break;
            
            case "Skewness cardinality":
                mldm = new SkewnessCardinality();
                break;
            
            case "Kurtosis cardinality":
                mldm = new KurtosisCardinality();
                break;
            
            case "Number of unconditionally dependent label pairs by chi-square test":
                mldm = new NumUnconditionalDependentLabelPairsByChiSquare();
                break;
            
            case "Ratio of unconditionally dependent label pairs by chi-square test":
                mldm = new RatioUnconditionalDependentLabelPairsByChiSquare();
                break;
            
            case "Average of unconditionally dependent label pairs by chi-square test":
                mldm = new AvgUnconditionalDependentLabelPairsByChiSquare();
                break;
            
            case "Number of labelsets up to 2 examples":
                mldm = new LabelsetsUpTo2Examples();
                break;
            
            case "Number of labelsets up to 5 examples":
                mldm = new LabelsetsUpTo5Examples();
                break;
            
            case "Number of labelsets up to 10 examples":
                mldm = new LabelsetsUpTo10Examples();
                break;
            
            case "Number of labelsets up to 50 examples":
                mldm = new LabelsetsUpTo50Examples();
                break;
            
            case "Ratio of labelsets with number of examples < half of the attributes":
                mldm = new RatioLabelsetsWithExamplesLessThanHalfAttributes();
                break;
            
            case "Ratio of number of labelsets up to 2 examples":
                mldm = new RatioLabelsetsUpTo2Examples();
                break;
            
            case "Ratio of number of labelsets up to 5 examples":
                mldm = new RatioLabelsetsUpTo5Examples();
                break;
            
            case "Ratio of number of labelsets up to 10 examples":
                mldm = new RatioLabelsetsUpTo10Examples();
                break;
            
            case "Ratio of number of labelsets up to 50 examples":
                mldm = new RatioLabelsetsUpTo50Examples();
                break;
  
            case "Average examples per labelset":
                mldm = new AvgExamplesPerLabelset();
                break;
            
            case "Minimal entropy of labels":
                mldm = new MinEntropy();
                break;
            
            case "Maximal entropy of labels":
                mldm = new MaxEntropy();
                break;
            
            case "Mean of entropies of labels":
                mldm = new MeanEntropy();
                break;
            
            case "Standard deviation of examples per labelset":
                mldm = new StdvExamplesPerLabelset();
                break;
            
            default:  
                value = -1.0;
                break;    
            }
        
            if(mldm != null){
                value = mldm.calculate(dataset);
            }
            else{
                value = -1.0;
            }
        
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        if(Double.isNaN(value) || value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY){
           //System.out.println("isNaN");
            return("NaN");
        }
        else{
            //NumberFormat formatter = new DecimalFormat("#0.000"); 
            //return(formatter.format(value));
            //System.out.println("formatter: " + formatter.format(value) + "  value: " + value);
            
            return(Double.toString(value));
        }
        
        
        //return truncateValue(Double.toString(value),4);
    }
    
    
    
    public static double Get_media(ArrayList<Double> lista)
    {
        double value=0.0;
        for(double current : lista)
            value+=current;
        
        return value/lista.size();
    }
    
    
    
    public static ArrayList<String> Get_test_metrics()
     {
       ArrayList<String> result= new ArrayList();
       
        result.add("Average absolute correlation between numeric attributes");
        result.add("Average examples per labelset");
        result.add("Average gain ratio");
        result.add("Average of unconditionally dependent label pairs by chi-square test");
        result.add("Cardinality");
        result.add("CVIR inter class");
        result.add("Density");
        result.add("Distinct labelsets");
        result.add("Diversity");
        result.add("Instances");
        result.add("Kurtosis cardinality");
        result.add("Labels x instances x features");
        result.add("Max IR per label inter class");
        result.add("Max IR per label intra class");
        result.add("Max IR per labelset");
        result.add("Maximal entropy of labels");
        result.add("Mean of entropies of nominal attributes");
        result.add("Mean entropy of labels");
        result.add("Mean of kurtosis");
        result.add("Mean of mean of numeric attributes");
        result.add("Mean of skewness of numeric attributes");
        result.add("Mean of standard deviation of numeric attributes");
        result.add("Mean of standard deviation of IR per label intra class");
        result.add("Mean of IR per label intra class");
        result.add("Mean of IR per label inter class");
        result.add("Mean of IR per labelset");
        result.add("Minimal entropy of labels");
        result.add("Number of labelsets up to 2 examples");
        result.add("Number of labelsets up to 5 examples");
        result.add("Number of labelsets up to 10 examples");
        result.add("Number of labelsets up to 50 examples");
        result.add("Number of unconditionally dependent label pairs by chi-square test");
        result.add("Number of unique labelsets");
        result.add("Number of unseen labelsets");
        result.add("Proportion of numeric attributes with outliers");
        result.add("Ratio of labelsets with number of examples < half of the attributes");
        result.add("Ratio of number of labelsets up to 2 examples");
        result.add("Ratio of number of labelsets up to 5 examples");
        result.add("Ratio of number of labelsets up to 10 examples");
        result.add("Ratio of number of labelsets up to 50 examples");
        result.add("Ratio of number of instances to the number of attributes");
        result.add("Ratio of unseen labelsets");
        result.add("Ratio of unconditionally dependent label pairs by chi-square test");
        result.add("Skewness cardinality");
        result.add("Standard deviation of label cardinality");
        result.add("Standard deviation of examples per labelset");

       return result;
     }
    
    
    public static Object[][] Get_row_data_test_data ()
    {
        ArrayList metrics = Get_test_metrics();
        
        Object rowData[][] = new Object[metrics.size()][4];
        
        for(int i=0; i<metrics.size(); i++){
            rowData[i][0] = metrics.get(i);
            rowData[i][1] = "-";
            rowData[i][2] = "-";
            rowData[i][3]= Boolean.FALSE;
        }
        
        return rowData;
    }
    
    
     public static Object[][] Get_row_data_train_data ()
    {
             Object rowData[][] = { 
                 
                 { "Average examples per labelset", "-", Boolean.FALSE },
                 { "Average of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },
                 { "Cardinality", "-", Boolean.FALSE },
                 { "CVIR inter class", "-", Boolean.FALSE }, 
                 { "Density", "-", Boolean.FALSE },
                 //{ "Default accuracy", Boolean.FALSE }, HACE LO MISMO QUE EL PMAX 
                 { "Distinct labelsets", "-", Boolean.FALSE }, 
                 { "Diversity", "-", Boolean.FALSE }, 
                 { "Instances", "-", Boolean.FALSE},
                   
                 
                 { "Kurtosis cardinality", "-", Boolean.FALSE },//MEDIA OF ALL NUMERIC ATTR    
                 { "Labels x instances x features", "-", Boolean.FALSE }, 
                 { "Mean of standard deviation of IR per label intra class", "-", Boolean.FALSE }, 
                 
                 { "Mean of IR per label intra class", "-", Boolean.FALSE }, 
                 { "Mean of IR per label inter class", "-", Boolean.FALSE }, 
                 { "Mean of IR per labelset", "-", Boolean.FALSE }, 
                  
                 { "Number of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },                  
                 { "Number of labelsets up to 2 examples", "-", Boolean.FALSE },
                 { "Number of labelsets up to 5 examples", "-", Boolean.FALSE },  
                 { "Number of labelsets up to 10 examples", "-", Boolean.FALSE }, 
                 { "Number of labelsets up to 50 examples", "-", Boolean.FALSE },                 
                 { "Ratio of labelsets with number of examples < half of the attributes", "-", Boolean.FALSE },   
               // { "Ratio of distinct classes to the total number label combinations", Boolean.FALSE },                                                   
                 
                  { "Ratio of number of labelsets up to 2 examples", "-", Boolean.FALSE },
                  { "Ratio of number of labelsets up to 5 examples", "-", Boolean.FALSE },
                  { "Ratio of number of labelsets up to 10 examples", "-", Boolean.FALSE },
                  { "Ratio of number of labelsets up to 50 examples", "-", Boolean.FALSE },
           
                  { "Ratio of number of instances to the number of attributes", "-", Boolean.FALSE }, 
                  { "Ratio of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE }, 
                  { "Skewness cardinality", "-", Boolean.FALSE },   
                
                  { "Standard deviation of label cardinality", "-", Boolean.FALSE },
                  { "Standard deviation of examples per labelset", "-", Boolean.FALSE },        
                 // { "Variance of examples per labelset", Boolean.FALSE },  
              };
             
             return rowData;
    }
    
     public static ArrayList<String> Get_common_metrics()
     {
       ArrayList<String> result= new ArrayList();
       
       result.add("Absolute difference between label cardinality of train and test sets");
       result.add("Labels");
       result.add("Number distinct labelset found in train and test sets");
       result.add("Number of binary attributes");
       result.add("Number of nominal attributes");
       result.add("Proportion of binary attributes");
       result.add("Proportion of nominal attributes");
       result.add("Ratio of test instances over training instances");
       result.add("Ratio of distinct labelset found in both datasets over number of labelset");

       return result;
     }
    
     public static Object[][] Get_row_data_commun_data ()
    {
        ArrayList metrics = Get_common_metrics();
        
        Object rowData[][] = new Object[metrics.size()][3];
        
        for(int i=0; i<metrics.size(); i++){
            rowData[i][0] = metrics.get(i);
            rowData[i][1] = "-";
            rowData[i][2]= Boolean.FALSE;
        }
        
        return rowData;
    }
   
     public static Object[][] Get_row_data_imbalanced ()
    {
         Object rowData[][] = { 
          
           { "CVIR inter class", "-", Boolean.FALSE }, 
           
           { "Kurtosis cardinality", "-", Boolean.FALSE },      
           { "Mean of standard deviation of IR per label intra class", "-", Boolean.FALSE }, 
           
           { "Mean of IR per label intra class", "-", Boolean.FALSE },      
           { "Mean of IR per label inter class", "-", Boolean.FALSE }, 
           { "Mean of IR per labelset", "-", Boolean.FALSE }, 
           
           { "Mean of skewness of numeric attributes", "-", Boolean.FALSE },
           { "Mean of kurtosis", "-", Boolean.FALSE },
           { "Proportion of numeric attributes with outliers", "-", Boolean.FALSE }, 
           { "Proportion of maxim label combination (PMax)", "-", Boolean.FALSE},
           { "Proportion of unique label combination (PUniq)", "-", Boolean.FALSE},
           { "Skewness cardinality", "-", Boolean.FALSE },
           { "Standard deviation of examples per labelset", "-", Boolean.FALSE }, 
         //  { "Variance of examples per labelset", Boolean.FALSE }, 
                 
                 
             };
             
             return rowData;
    }
     
        
    public static Object[][] Get_row_data ()
    {
             Object rowData[][] = { 
                 { "Average absolute correlation between numeric attributes", Boolean.FALSE },
                 { "Average examples per labelset", Boolean.FALSE },
                 { "Average gain ratio", Boolean.FALSE }, 
                 { "Average of unconditionally dependent label pairs by chi-square test", Boolean.FALSE },
                 { "Bound", Boolean.FALSE},
                 { "Cardinality", Boolean.FALSE },
                 { "CVIR inter class", Boolean.FALSE }, 
                 
                 
                 //{ "Default accuracy", Boolean.FALSE }, HACE LO MISMO QUE EL PMAX 
                 { "Density", Boolean.FALSE }, 
                 { "Distinct labelsets", Boolean.FALSE}, 
                 { "Diversity", Boolean.FALSE },
                 { "Instances", Boolean.FALSE },
                 
                 { "Kurtosis cardinality", Boolean.FALSE },
                 
                 { "Labels x instances x features", Boolean.FALSE }, 
                 
                 { "Maximal entropy of labels", Boolean.FALSE },
                 { "Mean of entropies of nominal attributes", Boolean.FALSE },
		 { "Mean of standard deviation of IR per label intra class", Boolean.FALSE }, 
                 
                 { "Mean of IR per label intra class", Boolean.FALSE },     
                 { "Mean of IR per label inter class", Boolean.FALSE },  
                 { "Mean of IR per labelset", Boolean.FALSE }, 
                 
                 { "Mean of mean of numeric attributes", Boolean.FALSE },
                 { "Mean of standard deviation of numeric attributes", Boolean.FALSE },
                 { "Mean of skewness of numeric attributes", Boolean.FALSE },
                 { "Mean of kurtosis", Boolean.FALSE },
                 //{ "Mean of entropies (numeric attr)", Boolean.FALSE },
                 { "Minimal entropy of labels", Boolean.FALSE },
                 
                 { "Number of binary attributes", Boolean.FALSE },
                 { "Number of labelsets up to 2 examples", Boolean.FALSE },
                 { "Number of labelsets up to 5 examples", Boolean.FALSE },               
                 { "Number of labelsets up to 10 examples", Boolean.FALSE },
                 { "Number of labelsets up to 50 examples", Boolean.FALSE },
                 
                 { "Number of nominal attributes", Boolean.FALSE },
                 
                 { "Number of unconditionally dependent label pairs by chi-square test", Boolean.FALSE },
                 
                 { "Proportion of distinct labelsets", Boolean.FALSE },                  
                 { "Proportion of maxim label combination (PMax)", Boolean.FALSE},
                 { "Proportion of numeric attributes with outliers", Boolean.FALSE },
                 { "Proportion of binary attributes", Boolean.FALSE },
                 { "Proportion of nominal attributes", Boolean.FALSE },                 
                 { "Proportion of unique label combination (PUniq)", Boolean.FALSE},
                     
                 { "Ratio of labelsets with number of examples < half of the attributes", Boolean.FALSE },
                 //{ "Ratio of distinct classes to the total number label combinations", Boolean.FALSE }, es el DIVERSTY
                 { "Ratio of number of instances to the number of attributes", Boolean.FALSE },                                                                  
                 { "Ratio of number of labelsets up to 2 examples", Boolean.FALSE }, 
                 { "Ratio of number of labelsets up to 5 examples", Boolean.FALSE },
                 { "Ratio of number of labelsets up to 10 examples", Boolean.FALSE },
                 { "Ratio of number of labelsets up to 50 examples", Boolean.FALSE },                 
                 { "Ratio of unconditionally dependent label pairs by chi-square test", Boolean.FALSE },
                 { "Skewness cardinality", Boolean.FALSE },
                 { "Standard deviation of label cardinality", Boolean.FALSE },
                 { "Standard deviation of examples per labelset", Boolean.FALSE },
                // { "Variance of examples per labelset", Boolean.FALSE }, 
                 
             };
             
             return rowData;
    }
    
    
    public static Object[][] Get_row_data_principal()
    {
        ArrayList<String> metrics = Get_all_metrics();
        
        Object rowData[][] = new Object[metrics.size()][3];
        
        for(int i=0; i<metrics.size(); i++){
            if(metrics.get(i).charAt(0) != '<'){
                rowData[i][0] = metrics.get(i);
                rowData[i][1] = "-";
                rowData[i][2]= Boolean.FALSE;
            }
            else{
                rowData[i][0] = metrics.get(i);
                rowData[i][1] = "";
                rowData[i][2] = Boolean.TRUE;
            }
        }
        
        return rowData;
    }
    
    public static Object[][] Get_row_data_multi()
    {
        ArrayList metrics = Get_metrics_multi();
        
        Object rowData[][] = new Object[metrics.size()][2];
        
        for(int i=0; i<metrics.size(); i++){
            rowData[i][0] = metrics.get(i);
            rowData[i][1]= Boolean.FALSE;
        }
        
        return rowData;
    }
    
    
    public static Object[][] Get_row_data_multi(int nDatasets)
    {
        ArrayList metrics = Get_metrics_multi();
        
        Object rowData[][] = new Object[metrics.size()][2+nDatasets];
        
        for(int i=0; i<metrics.size(); i++){
            rowData[i][0] = metrics.get(i);
            rowData[i][1]= Boolean.FALSE;
            for(int d=0; d<nDatasets; d++){
                rowData[i][d+2] = "";
            }
        }
        
        return rowData;
    }
    
    
    public static ArrayList<String> Get_all_metrics()
    {
        return(Get_all_metrics_alpha_ordered());
    }
    
    public static ArrayList<String> Get_all_metrics_type_ordered()
    {
        ArrayList<String> result= new ArrayList();

        //result.add("<html><b>Size metrics</b></html>");
        result.add("Attributes");
        result.add("Instances");
        result.add("Labels");
        result.add("Distinct labelsets");
        result.add("Labels x instances x features");
        result.add("Ratio of number of instances to the number of attributes");

        //result.add("<html><b>Label distribution</b></html>");
        result.add("Cardinality");
        result.add("Density");
        result.add("Maximal entropy of labels");
        result.add("Mean of entropies of labels");
        result.add("Minimal entropy of labels");
        result.add("Standard deviation of label cardinality");

        //result.add("<html><b>Relationship among labels</b></html>");
        result.add("Average examples per labelset");
        result.add("Average of unconditionally dependent label pairs by chi-square test");
        result.add("Bound");
        result.add("Diversity");
        result.add("Number of labelsets up to 2 examples");
        result.add("Number of labelsets up to 5 examples");
        result.add("Number of labelsets up to 10 examples");
        result.add("Number of labelsets up to 50 examples");
        result.add("Number of unconditionally dependent label pairs by chi-square test");
        result.add("Number of unique labelsets");
        result.add("Proportion of distinct labelsets");
        result.add("Ratio of labelsets with number of examples < half of the attributes");
        result.add("Ratio of unconditionally dependent label pairs by chi-square test");
        result.add("Ratio of number of labelsets up to 2 examples");
        result.add("Ratio of number of labelsets up to 5 examples");
        result.add("Ratio of number of labelsets up to 10 examples");
        result.add("Ratio of number of labelsets up to 50 examples");
        result.add("SCUMBLE");
        result.add("Standard deviation of examples per labelset");

        //result.add("<html><b>Imbalance metrics</b></html>");
        result.add("CVIR inter class");
        result.add("Kurtosis cardinality");
        result.add("Max IR per label inter class");
        result.add("Max IR per label intra class");
        result.add("Max IR per labelset");
        result.add("Mean of IR per label inter class");
        result.add("Mean of IR per label intra class");       
        result.add("Mean of IR per labelset");       
        result.add("Mean of kurtosis");
        result.add("Mean of skewness of numeric attributes");
        result.add("Mean of standard deviation of IR per label intra class");
        result.add("Proportion of maxim label combination (PMax)");
        result.add("Proportion of unique label combination (PUniq)");
        result.add("Skewness cardinality");

        //result.add("<html><b>Attributes metrics</b></html>");
        result.add("Average absolute correlation between numeric attributes");
        result.add("Average gain ratio");
        result.add("Mean of entropies of nominal attributes");
        result.add("Mean of mean of numeric attributes");
        result.add("Mean of standard deviation of numeric attributes");
        result.add("Number of binary attributes");
        result.add("Number of nominal attributes");
        result.add("Number of numeric attributes");
        result.add("Proportion of binary attributes");
        result.add("Proportion of nominal attributes");
        result.add("Proportion of numeric attributes with outliers");

        return result;
    }
    
    public static ArrayList<String> Get_all_metrics_alpha_ordered()
    {
        ArrayList<String> result= new ArrayList();

        result.add("Attributes");
        result.add("Average absolute correlation between numeric attributes");
        result.add("Average examples per labelset");
        result.add("Average gain ratio");
        result.add("Average of unconditionally dependent label pairs by chi-square test");
        result.add("Bound");
        result.add("Cardinality");
        result.add("CVIR inter class");
        result.add("Density");
        result.add("Distinct labelsets");
        result.add("Diversity");
        result.add("Instances");
        result.add("Kurtosis cardinality");
        result.add("Labels");
        result.add("Labels x instances x features");
        result.add("Max IR per label inter class");
        result.add("Max IR per label intra class");
        result.add("Max IR per labelset");
        result.add("Maximal entropy of labels");
        result.add("Mean of entropies of labels");
        result.add("Mean of entropies of nominal attributes");
        result.add("Mean of IR per label inter class");
        result.add("Mean of IR per label intra class");       
        result.add("Mean of IR per labelset");
        result.add("Mean of kurtosis");
        result.add("Mean of mean of numeric attributes");
        result.add("Mean of skewness of numeric attributes");
        result.add("Mean of standard deviation of IR per label intra class");
        result.add("Mean of standard deviation of numeric attributes");
        result.add("Minimal entropy of labels");
        result.add("Number of binary attributes");
        result.add("Number of labelsets up to 2 examples");
        result.add("Number of labelsets up to 5 examples");
        result.add("Number of labelsets up to 10 examples");
        result.add("Number of labelsets up to 50 examples");
        result.add("Number of nominal attributes");
        result.add("Number of numeric attributes");
        result.add("Number of unconditionally dependent label pairs by chi-square test");
        result.add("Number of unique labelsets");
        result.add("Proportion of binary attributes");
        result.add("Proportion of distinct labelsets");
        result.add("Proportion of maxim label combination (PMax)");
        result.add("Proportion of nominal attributes");
        result.add("Proportion of numeric attributes");
        result.add("Proportion of numeric attributes with outliers");
        result.add("Proportion of unique label combination (PUniq)");
        result.add("Ratio of labelsets with number of examples < half of the attributes");
        result.add("Ratio of number of instances to the number of attributes");
        result.add("Ratio of unconditionally dependent label pairs by chi-square test");
        result.add("Ratio of number of labelsets up to 2 examples");
        result.add("Ratio of number of labelsets up to 5 examples");
        result.add("Ratio of number of labelsets up to 10 examples");
        result.add("Ratio of number of labelsets up to 50 examples");
        result.add("SCUMBLE");
        result.add("Skewness cardinality");
        result.add("Standard deviation of examples per labelset");
        result.add("Standard deviation of label cardinality");

        return result;
    }
    
    
    public static String Get_metric_tooltip(String metric)
    {
        String tooltip = new String();
        
        switch (metric) {
            case "Attributes":
                tooltip = "Number of attributes";
                break;
            case "Instances":
                tooltip = "Number of instances";
                break;
            case "Labels":
                tooltip = "Number of labels";
                break;
            case "Distinct labelsets":
                tooltip = "Number of distinct labelsets";
                break;
            case "Labels x instances x features":
                tooltip = "Number of labels * number of instances * number of attributes";
                break;
            case "Ratio of number of instances to the number of attributes":
                tooltip = "Number of instances / number of attributes";
                break;

            case "Cardinality":
                tooltip = "Average number of labels per instance";
                break;
            case "Density":
                tooltip = "Cardinality / number of labels";
                break;
            case "Maximal entropy of labels":
                tooltip = "Maximal uncertainty value among the labels";
                break;
            case "Mean of entropies of labels":
                tooltip = "Average uncertainty among the labels";
                break;
            case "Minimal entropy of labels":
                tooltip = "Minimal uncertainty value among the labels";
                break;
            case "Standard deviation of label cardinality":
                tooltip = "Standard deviation of the number of labels for each instance";
                break;

            case "Average examples per labelset":
                tooltip = "Average number of instances per labelset";
                break;
            case "Average of unconditionally dependent label pairs by chi-square test":
                tooltip = "Average of chi-square test values for each pair of labels";
                break;
            case "Bound":
                tooltip = "Maximum number of possible labelsets";
                break;
            case "Diversity":
                tooltip = "Ratio of labelsets existing in the dataset (distinct labelset / bound)";
                break;
            case "Number of labelsets up to 2 examples":
                tooltip = "Number of labelsets with number of instances less or equal to 2";
                break;
            case "Number of labelsets up to 5 examples":
                tooltip = "Number of labelsets with number of instances less or equal to 5";
                break;
            case "Number of labelsets up to 10 examples":
                tooltip = "Number of labelsets with number of instances less or equal to 10";
                break;
            case "Number of labelsets up to 50 examples":
                tooltip = "Number of labelsets with number of instances less or equal to 50";
                break;
            case "Number of unconditionally dependent label pairs by chi-square test":
                tooltip = "Number of pairs of labels that are unconditionally dependent by chi-square test";
                break;
            case "Number of unique labelsets":
                tooltip = "Number of labelsets with only one instance";
                break;
            case "Proportion of distinct labelsets":
                tooltip = "Number of distinct labelsets / number of instances";
                break;
            case "Ratio of labelsets with number of examples < half of the attributes":
                tooltip = "Ratio of labelsets with number of instances less than half ot the number of attributes";
                break;
            case "Ratio of unconditionally dependent label pairs by chi-square test":
                tooltip = "Ratio of pairs of labels that are unconditionally dependent by chi-square test, indicating the level of interdependencies among labels";
                break;
            case "Ratio of number of labelsets up to 2 examples":
                tooltip = "Ratio of labelsets with number of instances less or equal to 2";
                break;
            case "Ratio of number of labelsets up to 5 examples":
                tooltip = "Ratio of labelsets with number of instances less or equal to 5";
                break;
            case "Ratio of number of labelsets up to 10 examples":
                tooltip = "Ratio of labelsets with number of instances less or equal to 10";
                break;
            case "Ratio of number of labelsets up to 50 examples":
                tooltip = "Ratio of labelsets with number of instances less or equal to 50";
                break;
            case "SCUMBLE":
                tooltip = "Measures the concurrence level among frequent and infrequent labels";
                break;
            case "Standard deviation of examples per labelset":
                tooltip = "Standard deviation of number of instances per labelset";
                break;

            case "CVIR inter class":
                tooltip = "Coefficient of variation of the IR per label inter class";
                break;
            case "Kurtosis cardinality":
                tooltip = "Kurtosis of the label cardinality";
                break;
            case "Max IR per label inter class":
                tooltip = "Maximum value of IR per label inter class";
                break;
            case "Max IR per label intra class":
                tooltip = "Maximum value of IR per label intra class";
                break;
            case "Max IR per labelset":
                tooltip = "Maximum value of IR per labelset";
                break;
            case "Mean of IR per label inter class":
                tooltip = "Average value of IR per label inter class";
                break;
            case "Mean of IR per label intra class":
                tooltip = "Average value of IR per label intra class";
                break;       
            case "Mean of IR per labelset":
                tooltip = "Average value of IR per labelset";
                break;       
            case "Mean of kurtosis":
                tooltip = "Average value of kurtosis of all numeric attributes";
                break;
            case "Mean of skewness of numeric attributes":
                tooltip = "Average value of skewness of all numeric attributes";
                break;
            case "Mean of standard deviation of IR per label intra class":
                tooltip = "Average value of standard deviation of IR per label intra class values";
                break;
            case "Proportion of maxim label combination (PMax)":
                tooltip = "Proportion of instances associated with the most frequent labelset";
                break;
            case "Proportion of unique label combination (PUniq)":
                tooltip = "Proportion of instances associated with labelsets appearing only once";
                break;
            case "Skewness cardinality":
                tooltip = "Skewness of the label cardinality";
                break;

            case "Average absolute correlation between numeric attributes":
                tooltip = "Average of absolute correlation values between numeric attributes, indicating robustness to irrelevant attributes";
                break;
            case "Average gain ratio":
                tooltip = "The average information gain ratio is obtained by splitting the data according to each target attribute";
                break;
            case "Mean of entropies of nominal attributes":
                tooltip = "Average value of entropies of nominal attributes";
                break;
            case "Mean of mean of numeric attributes":
                tooltip = "Average of average values of all numeric attributes";
                break;
            case "Mean of standard deviation of numeric attributes":
                tooltip = "Average value of standard deviation of numeric attributes";
                break;
            case "Number of binary attributes":
                tooltip = "Number of binary attributes";
                break;
            case "Number of nominal attributes":
                tooltip = "Number of nominal attributes";
                break;
            case "Number of numeric attributes":
                tooltip = "Number of numeric attributes";
                break;
            case "Proportion of binary attributes":
                tooltip = "Proportion of attributes that are binary";
                break;
            case "Proportion of nominal attributes":
                tooltip = "Proportion of attributes that are nominal";
                break;
            case "Proportion of numeric attributes":
                tooltip = "Proportion of attributes that are numeric";
                break;
            case "Proportion of numeric attributes with outliers":
                tooltip = "Proportion of numeric attributes having outliers";
                break;

            default:
                tooltip = metric;
                break;
        }

        
        ArrayList<String> result= new ArrayList();

        //result.add("<html><b>Size metrics</b></html>");
        result.add("Attributes");
        result.add("Instances");
        result.add("Labels");
        result.add("Distinct labelsets");
        result.add("Labels x instances x features");
        result.add("Ratio of number of instances to the number of attributes");

        //result.add("<html><b>Label distribution</b></html>");
        result.add("Cardinality");
        result.add("Density");
        result.add("Maximal entropy of labels");
        result.add("Mean of entropies of labels");
        result.add("Minimal entropy of labels");
        result.add("Standard deviation of label cardinality");

        //result.add("<html><b>Relationship among labels</b></html>");
        result.add("Average examples per labelset");
        result.add("Average of unconditionally dependent label pairs by chi-square test");
        result.add("Bound");
        result.add("Diversity");
        result.add("Number of labelsets up to 2 examples");
        result.add("Number of labelsets up to 5 examples");
        result.add("Number of labelsets up to 10 examples");
        result.add("Number of labelsets up to 50 examples");
        result.add("Number of unconditionally dependent label pairs by chi-square test");
        result.add("Number of unique labelsets");
        result.add("Proportion of distinct labelsets");
        result.add("Ratio of labelsets with number of examples < half of the attributes");
        result.add("Ratio of unconditionally dependent label pairs by chi-square test");
        result.add("Ratio of number of labelsets up to 2 examples");
        result.add("Ratio of number of labelsets up to 5 examples");
        result.add("Ratio of number of labelsets up to 10 examples");
        result.add("Ratio of number of labelsets up to 50 examples");
        result.add("SCUMBLE");
        result.add("Standard deviation of examples per labelset");

        //result.add("<html><b>Imbalance metrics</b></html>");
        result.add("CVIR inter class");
        result.add("Kurtosis cardinality");
        result.add("Max IR per label inter class");
        result.add("Max IR per label intra class");
        result.add("Max IR per labelset");
        result.add("Mean of IR per label inter class");
        result.add("Mean of IR per label intra class");       
        result.add("Mean of IR per labelset");       
        result.add("Mean of kurtosis");
        result.add("Mean of skewness of numeric attributes");
        result.add("Mean of standard deviation of IR per label intra class");
        result.add("Proportion of maxim label combination (PMax)");
        result.add("Proportion of unique label combination (PUniq)");
        result.add("Skewness cardinality");

        //result.add("<html><b>Attributes metrics</b></html>");
        result.add("Average absolute correlation between numeric attributes");
        result.add("Average gain ratio");
        result.add("Mean of entropies of nominal attributes");
        result.add("Mean of mean of numeric attributes");
        result.add("Mean of standard deviation of numeric attributes");
        result.add("Number of binary attributes");
        result.add("Number of nominal attributes");
        result.add("Number of numeric attributes");
        result.add("Proportion of binary attributes");
        result.add("Proportion of nominal attributes");
        result.add("Proportion of numeric attributes");
        result.add("Proportion of numeric attributes with outliers");

        
        return(tooltip);
    }
    
    
    public static ArrayList<String> Get_metrics_multi()
    {
        return(Get_all_metrics());
        /*
       ArrayList<String> result= new ArrayList();

       result.add("Attributes");
       result.add("Average absolute correlation between numeric attributes");
       result.add("Average examples per labelset");
       result.add("Average gain ratio");
       result.add("Average of unconditionally dependent label pairs by chi-square test");
       result.add("Bound");
       result.add("Cardinality");
       result.add("CVIR inter class");
       
       result.add("Density");
       result.add("Distinct labelsets");
       result.add("Diversity");
       result.add("Instances");
       
       result.add("Kurtosis cardinality");
       
       result.add("Labels");
       result.add("Labels x instances x features");
       
       result.add("Maximal entropy of labels");
       result.add("Mean of entropies of nominal attributes");
       result.add("Mean of standard deviation of IR per label intra class");
       
       result.add("Mean of IR per label intra class");
       result.add("Mean of IR per label inter class");
       result.add("Mean of IR per labelset");
       
       result.add("Mean of mean of numeric attributes");
       result.add("Mean of standard deviation of numeric attributes");
       result.add("Mean of skewness of numeric attributes");
       result.add("Mean of kurtosis");
       
       result.add("Minimal entropy of labels");
       
       result.add("Number of binary attributes");
       result.add("Number of labelsets up to 2 examples");
       result.add("Number of labelsets up to 5 examples");
       result.add("Number of labelsets up to 10 examples");
       result.add("Number of labelsets up to 50 examples");
       
       result.add("Number of nominal attributes");
       
       result.add("Number of unconditionally dependent label pairs by chi-square test");
       
       result.add("Proportion of distinct labelsets");
       result.add("Proportion of maxim label combination (PMax)");
       result.add("Proportion of numeric attributes with outliers");
       result.add("Proportion of binary attributes");
       result.add("Proportion of nominal attributes");
       result.add("Proportion of unique label combination (PUniq)");
       
       result.add("Ratio of labelsets with number of examples < half of the attributes");
       
       result.add("Ratio of number of instances to the number of attributes");
       result.add("Ratio of number of labelsets up to 2 examples");
       result.add("Ratio of number of labelsets up to 5 examples");
       result.add("Ratio of number of labelsets up to 10 examples");
       result.add("Ratio of number of labelsets up to 50 examples");
       result.add("Ratio of unconditionally dependent label pairs by chi-square test");
       
       result.add("SCUMBLE");
       
       result.add("Skewness cardinality");
       result.add("Standard deviation of label cardinality");
       result.add("Standard deviation of examples per labelset");

       return result;
        */
     }
    

    
     public static Object[][] Get_row_data_multi_datasets ()
    {
             Object rowData[][] = { 
                 
                 { "Attributes", "-", Boolean.FALSE},  
                 { "Average absolute correlation between numeric attributes", "-", Boolean.FALSE },
                 { "Average examples per labelset", "-", Boolean.FALSE },
                 { "Average gain ratio", "-", Boolean.FALSE },                  
                 { "Average of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },        
                 
                 { "Bound", "-", "-", Boolean.FALSE}, 
                 { "Cardinality", "-", Boolean.FALSE}, 
                 { "CVIR inter class", "-", Boolean.FALSE },
                 
                 //{ "Default accuracy", Boolean.FALSE }, HACE LO MISMO QUE EL PMAX 
                 { "Density", "-", Boolean.FALSE}, 
                 { "Distinct labelsets", "-", Boolean.FALSE}, 
                 { "Diversity", "-", Boolean.FALSE },
                 { "Instances", "-", Boolean.FALSE}, 
                           
                 { "Kurtosis cardinality","-",  Boolean.FALSE },
                 { "Labels", "-", Boolean.FALSE}, 
                 
                 { "Labels x instances x features", "-", Boolean.FALSE },
                 
                 { "Maximal entropy of labels", "-", Boolean.FALSE },
                 { "Mean of entropies of labels", "-", Boolean.FALSE },         
                 
                 { "Mean of entropies of nominal attributes", "-", Boolean.FALSE },
                
                 { "Mean of IR per label intra class", "-", Boolean.FALSE }, 
                 { "Mean of IR per label inter class", "-", Boolean.FALSE },    
                 { "Mean of IR per labelset", "-", Boolean.FALSE }, 
                 
                 { "Mean of kurtosis", "-", Boolean.FALSE },
                 { "Mean of mean of numeric attributes", "-", Boolean.FALSE },
                 { "Mean of standard deviation of numeric attributes", "-", Boolean.FALSE },
                 { "Mean of skewness of numeric attributes", "-", Boolean.FALSE },
                 { "Mean of standard deviation of IR per label intra class", "-", Boolean.FALSE }, 
                 
                 { "Minimal entropy of labels", "-", Boolean.FALSE },
                 { "Number of binary attributes", "-", Boolean.FALSE },
                 { "Number of labelsets up to 2 examples", "-", Boolean.FALSE },
                 { "Number of labelsets up to 5 examples", "-", Boolean.FALSE },               
                 { "Number of labelsets up to 10 examples", "-", Boolean.FALSE },
                 { "Number of labelsets up to 50 examples", "-", Boolean.FALSE },
                 { "Number of nominal attributes", "-", Boolean.FALSE },
                 
                 { "Number of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },
                  
                 { "Proportion of distinct labelsets", "-", Boolean.FALSE }, 
                 { "Proportion of unique label combination (PUniq)", "-", Boolean.FALSE},
                 { "Proportion of maxim label combination (PMax)", "-", Boolean.FALSE},
                 { "Proportion of numeric attributes with outliers", "-", Boolean.FALSE },
                 { "Proportion of binary attributes", "-", Boolean.FALSE },
                 { "Proportion of nominal attributes", "-", Boolean.FALSE },
                
                 //{ "Mean of entropies (numeric attr)", Boolean.FALSE },
                 { "Ratio of labelsets with number of examples < half of the attributes", "-", Boolean.FALSE },
                // { "Ratio of distinct classes to the total number label combinations", Boolean.FALSE }, es el DIVERSTY
                 { "Ratio of number of labelsets up to 2 examples", "-", Boolean.FALSE }, 
                 { "Ratio of number of labelsets up to 5 examples", "-", Boolean.FALSE },
                 { "Ratio of number of labelsets up to 10 examples", "-", Boolean.FALSE },
                 { "Ratio of number of labelsets up to 50 examples", "-", Boolean.FALSE },
                 { "Ratio of number of instances to the number of attributes", "-", Boolean.FALSE }, 
                 { "Ratio of unconditionally dependent label pairs by chi-square test", "-", Boolean.FALSE },
                 { "Skewness cardinality", "-", Boolean.FALSE },
                 { "Standard deviation of label cardinality", "-", Boolean.FALSE },
                 { "Standard deviation of examples per labelset", "-", Boolean.FALSE },
               //  { "Variance of examples per labelset", Boolean.FALSE }, 
                 
             };
             
             return rowData;
    }
     
     
     public static double[][] get_heatmap_values (MultiLabelInstances dataset, ArrayList<AttributesPair> lista_pares)
     {
          int[] label_indices= dataset.getLabelIndices();
         
         //double[][] get_pair_label = new double[label_indices.length][label_indices.length];
          double[][] get_pair_label = inicializa_arreglo_val_neg(label_indices.length);
          
         int i,j;
         int cant_instancias = dataset.getNumInstances();
         double probabilidad_att1_att2,probabilidad_att2,probabilidad_att1;//, probabilidad_att1;
         
         for( AttributesPair current : lista_pares)
         {
             i= current.getAttribute1Index();
             j= current.getAttribute2Index();
             
             probabilidad_att1_att2 = current.getAppearances()/(double)cant_instancias;
             probabilidad_att1 = current.getAttribute1Appearances()/(double)cant_instancias;
             probabilidad_att2 = current.getAttribute2Appearances()/(double)cant_instancias;
             
             get_pair_label[i][j]= probabilidad_att1_att2/probabilidad_att2;                               
             get_pair_label[j][i]= probabilidad_att1_att2/probabilidad_att1;
         }
         
         
         
         
         return get_pair_label;
     }
     
     
     public static double[][] get_heatmap_values (ArrayList<AttributesPair> lista_pares, int cant_instancias, String[] labelname)
     {
          double[][] get_pair_label = inicializa_arreglo_val_neg(labelname.length);
          
       
         //int cant_instancias = dataset.getNumInstances();
         double probabilidad_att1_att2,probabilidad_att2,probabilidad_att1;//, probabilidad_att1;
         
         AttributesPair current;
         
         for(int i=0;i<labelname.length;i++)
       {
           for(int j=0;j<labelname.length;j++)
           {
               if(i==j || i>j ) continue;
               
               current = Search_and_get(labelname[i],labelname[j],lista_pares);
               
               if(current !=null) 
               {
                   
                probabilidad_att1_att2 = current.getAppearances()/(double)cant_instancias;
                probabilidad_att1 = current.getAttribute1Appearances()/(double)cant_instancias;
                probabilidad_att2 = current.getAttribute2Appearances()/(double)cant_instancias;
             
                get_pair_label[i][j]= probabilidad_att1_att2/probabilidad_att2;                               
                get_pair_label[j][i]= probabilidad_att1_att2/probabilidad_att1;
                
               }
               //current =null;
           }
           
       }
         
         
         /*
         for( AttributesPair current : lista_pares)
         {
             i= current.getAttribute1Index();
             j= current.getAttribute2Index();
             
             probabilidad_att1_att2 = current.getAppearances()/(double)cant_instancias;
             probabilidad_att1 = current.getAttribute1Appearances()/(double)cant_instancias;
             probabilidad_att2 = current.getAttribute2Appearances()/(double)cant_instancias;
             
             get_pair_label[i][j]= probabilidad_att1_att2/probabilidad_att2;                               
             get_pair_label[j][i]= probabilidad_att1_att2/probabilidad_att1;
         }
         */
         return get_pair_label;
     }
     
     public static double[][] inicializa_arreglo_val_neg(int length)
     {
         double[][] data = new double[length][length];
         
         for(int i=0;i<length;i++)
             for(int j=0;j<length;j++)
                 data[i][j]=-1.0;
         return data;
     }
     
     public static double[][] get_pair_label_values (MultiLabelInstances dataset, ArrayList<AttributesPair> lista_pares)
     {
         //Statistics stat = new Statistics();
        //System.out.println("dataLabels: " + dataset.getNumLabels());
         double [][] coocurrences = new double[dataset.getNumLabels()][dataset.getNumLabels()];
         coocurrences = calculateCoocurrences(dataset);
         return(coocurrences);
     }
     
     
    
     public static double[][] get_chi_fi_coefficient (MultiLabelInstances dataset)
     {
        double[][] chi_fi_coefficient = new double[dataset.getNumLabels()][dataset.getNumLabels()];
        double phi, chi;

        
        try {
                
            UnconditionalChiSquareIdentifier depid = new UnconditionalChiSquareIdentifier();
            LabelsPair[] pairs = depid.calculateDependence(dataset);
            Statistics stat = new Statistics();
            double [][] phiMatrix = stat.calculatePhi(dataset);
                
            for (LabelsPair pair : pairs) {
                chi = pair.getScore();
                phi = phiMatrix[pair.getPair()[0]][pair.getPair()[1]];
                ////System.out.println("chi:"+pairs[i].getPair()[0]+" , "+ pairs[i].getPair()[1]+"= "+ chi);
                ////System.out.println("fi:"+pairs[i].getPair()[1]+" , "+ pairs[i].getPair()[0]+"= "+ fi);
                chi_fi_coefficient[pair.getPair()[0]][pair.getPair()[1]] = chi;
                chi_fi_coefficient[pair.getPair()[1]][pair.getPair()[0]] = phi;
            }
                
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return chi_fi_coefficient;
            
     }
    
    public static ArrayList<String> Get_lista_vertices_del_par(String labelname, ArrayList<AttributesPair> mi_lista)
    {
        ArrayList<String> result = new ArrayList<>();    
        
        for(AttributesPair actual : mi_lista)
        {
            if(actual.getAttributeName1()==labelname) 
            {
                result.add(actual.getAttributeName2());
     
            }
           else if(actual.getAttributeName2()==labelname)
           {
               result.add(actual.getAttributeName1());
     
           }
            
        }        
        
        return result;     
        
    }
    
    public static int devuelve_indice (String[] label_list, String labelname)
    {
        for(int i=0; i<label_list.length;i++)
            if(labelname==label_list[i])return i;
        return -1;
    }
    
    private static String[] get_name_label( int[] label_indices, Instances Instancias )
    {
        String[] name_label = new String[label_indices.length];
                
        for(int i=0;i<label_indices.length;i++)
        {
            name_label[i]=Instancias.attribute(label_indices[i]).name();
        }
        return name_label;
    }
    //---------------------------------------------------------------------------------------------------
    
    
    public static boolean Esta_dataset (ArrayList<MultiLabelInstances> list_dataset, String dataset_name)
    {
        for(MultiLabelInstances current : list_dataset)
              if(current.getDataSet().relationName().equals(dataset_name)) return true;
        
        return false;
    }
    
    public static double[] get_ir_values_intra_class(ImbalancedFeature[] label_imbalanced)
    {
        double[] result = new double[label_imbalanced.length];
        
        for(int i=0; i<label_imbalanced.length; i++)
            result[i]=label_imbalanced[i].getIRIntraClass();
        
        return result;    
    
    }
    
    public static double[] get_ir_values_inter_class(ImbalancedFeature[] label_freq)//se le pasa el arreglo ordenado de mayor a menor
    {
        
        ImbalancedFeature[] label_freq_sorted = MetricUtils.sortByFrequency(label_freq);
        
        double[] ir_inter_class = new double[label_freq.length];
        
        int mayor= label_freq_sorted[0].getAppearances();
        double value;
        
        double media=0;
        
        for(int i=0;i<label_freq_sorted.length; i++)
        {
            if(label_freq_sorted[i].getAppearances() <= 0){
                value = Double.NaN;
            }
            else{
                value = mayor/(label_freq_sorted[i].getAppearances()*1.0);
            }
            
            ir_inter_class[i] = value;
            //System.out.println(" Atributo "+i+" , "+value);
            
            media+= value;
        }
        media = media/label_freq_sorted.length;
                
       //System.out.println("Media es "+ media);
        
        return ir_inter_class;
    }
    
    private static int get_frequency_x_label_name(ImbalancedFeature[] lista_attr_freq ,String label_name)
    {
        for(int i=0 ; i<lista_attr_freq.length; i++)
        {
            if(lista_attr_freq[i].getName().equals(label_name)) return lista_attr_freq[i].getAppearances();
        }
        return -1;//es que no aparece el nombre de la etiqueta
    }
    
    
    private static ArrayList<AttributesPair> conforma_pares_atributos(int [] pares_etiquetas,int[] label_indices,MultiLabelInstances dataset )
    {
      ArrayList<AttributesPair> mi_lista= new ArrayList<>();
      Instances Instancias = dataset.getDataSet();
      
      String[] name_attr =get_name_label(label_indices, Instancias); //NOMBRES DE LAS ETIQUETAS
      AttributesPair actual;
      
      ImbalancedFeature[] lista_attr_freq = MetricUtils.getImbalancedDataByAppearances(dataset);
      int cant_i, cant_j;
      
      int index_pares_etiq=0; //indice del arreglo pares_etiquetas
      int value;
                
      for(int i = 0; i<label_indices.length; i++)
      {
          for(int j=i+1 ; j<label_indices.length ; j++)
          {
            value = pares_etiquetas[index_pares_etiq];
            
            index_pares_etiq++; 
              
            if(value ==0) continue; 

            cant_i = get_frequency_x_label_name(lista_attr_freq, name_attr[i]);
            cant_j = get_frequency_x_label_name(lista_attr_freq, name_attr[j]);
            
            actual = new AttributesPair(name_attr[i], name_attr[j],value,i,j,cant_i,cant_j);
            mi_lista.add(actual);
                      
          }
      }
     return mi_lista;
    }
    
    public static int get_indice_pares_etiquetas(int ind_1, int ind_2, int labels)
    {
        int count = ind_1;
        int result=0;
        
        for(int i=1; count>0 ;i++ , count--)
        {
            result += labels-i;
        }
        int distancia = ind_2 - ind_1;
        
        return result+= distancia-1;
    }
    
    //ACTUALIZA LAS FRECUENCIAS DE LAS TUPLAS DE ETIQUETAS QUE APAREZCAN RELEVANTES EN LA N-ESIMA POSICION
    public static int[] Actualiza_Pares_etiquetas(int[] pares_etiquetas, int[] labels_value)
    {
        int index;
        
        for(int i=0; i<labels_value.length;i++)
        {
            if(labels_value[i]== 0) continue;
            
            for(int j=i+1; j<labels_value.length; j++)
            {
               if(labels_value[i] == labels_value[j])
               {
                   index = get_indice_pares_etiquetas(i, j, labels_value.length);
                   pares_etiquetas[index]=pares_etiquetas[index]+1;
               }
            }
        }
        
        return pares_etiquetas;
    }
    
    //DEVUELVE EN LA n-ESIMA INSTANCIA EL CONJUNTO DE VALORES DE ETIQUETAS
    public static int[] get_current_value_labels(Instances Instancias, int posicion, int[] labels_indice)
    {
        int[] value_labels = new int[labels_indice.length];
        int value;
        
        for(int i=0; i<value_labels.length; i++)
        {
           value = (int)Instancias.instance(posicion).value(labels_indice[i]);
           value_labels[i] = value;
        }
        return value_labels;
    }
    
    
    public static double[][] Get_pares_seleccionados (String[] labelname, ArrayList<AttributesPair> pares_seleccionados, int num_instances)
    {
        //LANZAR LA VENTANA EMERGENTE CON LOS PARE SELECCIONADOS
       
       double[][] pares_freq= inicializa_arreglo_val_neg(labelname.length);
       
       AttributesPair current=null;
       
       for(int i=0; i<labelname.length;i++)
       {
           for(int j=0;j<labelname.length;j++)
           {
               if(i==j || i>j ) continue;
               
               current = Search_and_get(labelname[i],labelname[j],pares_seleccionados);
               
               if(current !=null) 
               {
                 pares_freq[i][j]=current.getAppearances()/(num_instances*1.0);
               }
               //current =null;
           }
           
       }
       
       return pares_freq;
    }
    
    
    public static AttributesPair Search_and_get(String par1, String par2,ArrayList<AttributesPair> lista )
    {
      for(AttributesPair current : lista)
      {
          if(par1.equals(current.getAttributeName1()) && par2.equals(current.getAttributeName2())) return current;
          else if (par2.equals(current.getAttributeName1()) && par1.equals(current.getAttributeName2())) return current;
      }
      return null;
    }
    
    public static int get_valor_max_entre_pares_atrr (ArrayList<AttributesPair> lista)
    {
        int mayor=0;
        
        for(AttributesPair current : lista)
            if(mayor<current.getAppearances()) mayor=(int)current.getAppearances();
        
        return mayor;        
    }
    
    public static int get_velocidad(MultiLabelInstances dataset)
    {            
        int cant_label = dataset.getNumLabels();
        int instancias = dataset.getNumInstances();
        
        if(cant_label<50 && instancias<2500) return 25;
        if(cant_label<200 && instancias<2500) return 10;
        
        
        return 2;
    }
    
    public static int get_valor_min_entre_pares_atrr (ArrayList<AttributesPair> lista)
    {
        int min=(int)lista.get(0).getAppearances();
        
        for(AttributesPair current : lista)
            if(min >current.getAppearances()) min=(int)current.getAppearances();
        
        return min;        
    }
    
    public static String[] pasa_valores_al_arreglo(ArrayList<String> lista)
    {
        String[] result= new String[lista.size()];
        
        for( int i=0; i<result.length ;i++)
        {
            result[i]=lista.get(i);
        }
            
        return result;
    }
    
    public static int get_valor_fortaleza (int minimo, int maximo, int n, double edge_value)
    {
        double intervalo = (maximo-minimo)/(n*1.0);
        
        int valor_fort=0;
        
        for(double i=minimo; i<maximo ;i=i+intervalo)
        {
            if(edge_value < i) break;
            valor_fort++;
        }
        return valor_fort;
    }
    /*
    public static int[] coordenadas_jgraphx(int x, int y, int count)
    {
        int[] posicion=new int[2];
        
        
    }
    */
    
    public static int get_valor_fortaleza (int minimo, int maximo, int n, int edge_value)
    {
        int intervalo = (maximo-minimo)/n;
        
        int valor_fort=0;
        
        for(int i=minimo; i<maximo ;i=i+intervalo+1)
        {
            if(edge_value < i) break;
            valor_fort++;
        }
        return valor_fort;
    }
    
    
    private static AttributesPair Devuelve_el_par(String att1, String att2, ArrayList<AttributesPair> lista)
    {
        for( AttributesPair current : lista)
        {
            if(current.getAttributeName1().equals(att1) && current.getAttributeName2().equals(att2))return current;
            if(current.getAttributeName1().equals(att2) && current.getAttributeName2().equals(att1))return current;
        }
        return null;
    }
    
    
    public static ArrayList<AttributesPair> Encuentra_pares_attr_seleccionados (ArrayList<AttributesPair> pares_label , ArrayList<String> labels)
    {
       ArrayList<AttributesPair> result = new ArrayList();
       
       AttributesPair current;
       
       for(int i=0; i<labels.size()-1; i++)
       {
           for(int j=i+1; j<labels.size(); j++)
           {
               current = Devuelve_el_par(labels.get(i), labels.get(j), pares_label);
               if(current!=null){
                   result.add(current);
               }
           }
       }    
       
        return result;
    }
    
    public static ArrayList<AttributesPair> Get_pares_atributos(MultiLabelInstances dataset)
    {       
        Instances Instancias = dataset.getDataSet();
        
        //CREO EL ARREGLO DONDE SE GUARDA LA CANT VECES QUE SE REPITE UN PAR, CADA POSICION ES UN PAR TAL QUE
        //EL TAMAÑO DEL ARREGLO ES LA CANTIDAD DE COMBINACIONES POSIBLES ENTRE PARES DE ETIQUETAS
       
        int comb_posibles = get_comb_posibles(dataset.getNumLabels());//devuelve las combinaciones posibles entre pares de etiquetas 
        
        int [] pares_etiquetas = new int[comb_posibles]; // se usa para guardar la cantidad de veces que ocurre un par de atributos entre si, 
        int [] current_labels_value;
        int[] label_indices= dataset.getLabelIndices();
                
        for(int i=0; i<Instancias.size(); i++)
        {
            current_labels_value = get_current_value_labels(Instancias, i, label_indices); // obtiene los valores de cada etiqueta
           pares_etiquetas = Actualiza_Pares_etiquetas(pares_etiquetas, current_labels_value); //actualiza los pares de etiquetas
        }            
        
        return conforma_pares_atributos(pares_etiquetas, label_indices, dataset);
    }
    
        public static int get_comb_posibles(int n)
        {
	 int result=0;
         
         for(int i=n-1; i>0; i--)
             result +=i;
         
         return result;
        }
    
    //-----------------------------------------------------------------------------------------------------------
    
  
    public static String Get_file_name_xml1(String path)
    {           
        for(int i=path.length()-1;i>0;i--)
        {
            if (path.charAt(i)=='\\')break;
        }
        return path;
    }
    
    public static String Get_file_name_xml(String path)
    {           
//        for(int i=path.length()-1;i>0;i--)
//        {
//            if(path.charAt(i)=='_')
//                return path.substring(0,i)+".xml";
//            
//            if (path.charAt(i)=='\\')break;
//        }
        
        String [] words;
        words = path.split("/");
       //System.out.println("words1: " + Arrays.toString(words));
        path = words[words.length-1];
        String dir = new String();
        if(words.length > 1){
            for(int i=0; i<words.length-1; i++){
                dir += words[i] + "/";
            }
        }
       //System.out.println("dir1: " + dir);
       //System.out.println("path1: " + path);
        /*words = path.split("-");
        if(words.length > 1){
           //System.out.println("words2: " + Arrays.toString(words));
            path = "";
            for(int i=0; i<words.length-1; i++){
                path += words[i]+"-";
            }
            path = path.substring(0, path.length()-1) + ".xml";
        }
        else{
           //System.out.println("path2: " + path);
            path = path.split("\\.")[0];
           //System.out.println("path3: " + path);
            path+=".xml";
        }
        */
       //System.out.println("path4: " + path);
        path = dir+path;
       //System.out.println("path5: " + path);
        
        return path;
    }
    
    public static boolean hasMoreNDigits(double d, int digits)
    {
    
        String text = Double.toString(Math.abs(d));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        
        if(decimalPlaces<=digits) return false;
        return true;
    }
    
    
        
    
    public static int Label_value_most_frequency(HashMap labels)
    {
       Iterator it = labels.entrySet().iterator();
        int mayor=0;
        int temp;
    
        while (it.hasNext())
        {
           Map.Entry current = (Map.Entry)it.next();
        
            temp=(int)current.getValue();
            
            if(mayor<temp) mayor=temp;
        }
      
        
        return mayor;
    }
    
    
   	public static String Separate_data(String results)
	{
		String resultados="";
		
		 char simbolo = ':';
		 boolean es_numero=false;
		 
		 int pos_inicial=0;
		 int pos_final;
		 char actual;
		 
		 String temp;
		 
		 for(int i=0; i < results.length(); i++)
		 {
			 actual = results.charAt(i);
			 
                        // if(actual==' ')continue;
                         
			 if(actual==simbolo){ es_numero=true; continue;	 }
			 
			 if(es_numero && !is_number(actual) && actual==' ' && is_number(results.charAt(i-1)))
			 {
				 es_numero=false;
				 pos_final=i;
				 temp = results.substring(pos_inicial,pos_final);
				 pos_inicial=pos_final;
				 resultados+="\n"+temp;
			 }
		 }
		 return resultados;
	}
	

	public static boolean is_number(char valor)
	{
		 String numero_aceptado="012,3456789.±";
		 
		 for(int i=0;i<numero_aceptado.length();i++)
		 {
			 if(valor == numero_aceptado.charAt(i))return true;
		 }
		 return false;
	}
        
        public static boolean is_number(String cadena)
        {
            if(cadena.isEmpty()) return false;
            
            cadena = cadena.toLowerCase().trim();
            
            String alfabeto ="abcdefghijklmnopqrstuvwxyz";
            
            char current;
            for(int i=0;i<cadena.length();i++)
            {
                current = alfabeto.charAt(i);
                
                if( cadena.indexOf(current)!= -1) return false;
            }
            return true;
        }
        
        
        
        public static String getValueFormatted(String name, String value){
            String formattedValue = new String();

            value = value.replace(",", ".");

            if(value.equals("-")){
                return value;
            }

            if(value.equals("NaN")){
                return "---";
            }


            //Scientific notation numbers
            if( (((Math.abs(Double.parseDouble(value)*1000) < 1.0)) && 
                        ((Math.abs(Double.parseDouble(value)*1000) > 0.0))) ||
                    (Math.abs(Double.parseDouble(value)/1000.0) > 10)){
                NumberFormat formatter = new DecimalFormat("0.###E0");
                formattedValue = formatter.format(Double.parseDouble(value));
            }
            //Integer numbers
            else if( (name.toLowerCase().equals("attributes")) 
                    || (name.toLowerCase().equals("bound")) 
                    || (name.toLowerCase().equals("distinct labelsets"))
                    || (name.toLowerCase().equals("instances"))
                    || (name.toLowerCase().equals("labels x instances x features"))
                    || (name.toLowerCase().equals("labels")) 
                    || (name.toLowerCase().equals("number of binary attributes"))
                    || (name.toLowerCase().equals("number of labelsets up to 2 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 5 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 10 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 50 examples"))
                    || (name.toLowerCase().equals("number of nominal attributes"))
                    || (name.toLowerCase().equals("number of numeric attributes"))
                    || (name.toLowerCase().equals("number of unqiue labelsets"))
                    || (name.toLowerCase().equals("number of unconditionally dependent label pairs by chi-square test"))){

                NumberFormat formatter = new DecimalFormat("#0"); 
                formattedValue = formatter.format(Double.parseDouble(value));
            }
            //Decimal numbers
            else{
                NumberFormat formatter = new DecimalFormat("#0.000"); 
                formattedValue = formatter.format(Double.parseDouble(value));
            } 

            formattedValue = formattedValue.replace(",", ".");
            return formattedValue;
        }
        
        
        public static String getValueFormatted(String value, int nDecimals){
            String formattedValue = new String();

            value = value.replace(",", ".");

            if(value.equals("-")){
                return value;
            }

            if(value.equals("NaN")){
                return "---";
            }

            //Scientific notation numbers
            if( (((Math.abs(Double.parseDouble(value)*1000) < 1.0)) && 
                        ((Math.abs(Double.parseDouble(value)*1000) > 0.0))) ||
                    (Math.abs(Double.parseDouble(value)/1000.0) > 10)){
                NumberFormat formatter = new DecimalFormat("0.###E0");
                formattedValue = formatter.format(Double.parseDouble(value));
            }
            //Decimal numbers
            else{
                String f = "#0.";
                for(int i=0; i<nDecimals; i++){
                    f += "0";
                }
                
                NumberFormat formatter = new DecimalFormat(f); 
                formattedValue = formatter.format(Double.parseDouble(value));
            } 

            formattedValue = formattedValue.replace(",", ".");
            return formattedValue;
        }
        
        public static double[][] calculateCoocurrences(MultiLabelInstances mldata){
            
            int nLabels = mldata.getNumLabels();
            Instances data = mldata.getDataSet();
            
            double [][] coocurrenceMatrix = new double[nLabels][nLabels];
            
            int nFeatures = data.numAttributes() - nLabels;
            
            int [] labelIndices = mldata.getLabelIndices();
            
            Instance temp = null;
            for(int k=0; k<data.numInstances(); k++){   
                temp = data.instance(k);
                
                for(int i=0; i<nLabels; i++){
                    for(int j=i+1; j<nLabels; j++){
                        //if(i!=j){
                          if((temp.value(labelIndices[i]) == 1.0) && (temp.value(labelIndices[j]) == 1.0)){
                            coocurrenceMatrix[i][j]++;
                          }  
                        //}
                    }
                }
            }
            
            return coocurrenceMatrix;
        }
        
        
        
        
        
}
