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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JTable;
import mulan.data.MultiLabelInstances;

/**
 *
 * @author Jose Maria Moyano Murillo
 */
public class ResultsIOUtils {
    
    /**
     * Save metrics as .txt file
     * 
     * @param wr PrintWriter
     * @param metricsList List of metric names
     * @param dataset Dataset
     * @param tableMetrics Table with metrics and values
     */
    public static void saveMetricsTxt(PrintWriter wr, ArrayList<String> 
            metricsList, MultiLabelInstances dataset, Hashtable<String, String> 
            tableMetrics)
    {
        String maxString = new String();
        for(String s : metricsList){
            if(s.length() > maxString.length()){
                maxString = s;
            }
        }
        double maxLength = maxString.length();
        
        wr.write("Relation name:"+ "\t"+dataset.getDataSet().relationName());
        wr.write(System.getProperty("line.separator"));  

        for(String metric : metricsList)
        {
            wr.write(metric + ": ");
            for(int i=0; i<(maxLength-metric.length()); i++){
                wr.write(" ");
            }

            wr.write(MetricUtils.getValueFormatted(metric, tableMetrics.get(metric)));
            wr.write(System.getProperty("line.separator"));  
        }
    }   
   
    /**
     * Save multiple datasets metrics as .txt file
     * 
     * @param wr PrintWriter
     * @param metricsList List of metric names
     * @param dataNames List of dataset names
     * @param tableMetrics  Table with metrics and values for all datasets
     */
    public static void saveMultiMetricsTxt(PrintWriter wr, ArrayList<String> 
            metricsList, ArrayList<String> dataNames, 
            Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
        String maxString = new String();
        for(String s : metricsList){
            if(s.length() > maxString.length()){
                maxString = s;
            }
        }
        
        double maxLength = maxString.length();
        
        String value;
        
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
        
        
        for(String metric : metricsList)
        {
            line = "";
            line = line + metric + ":";
            
            for(int j=0; j<(maxLength-metric.length()); j++){
                line += " ";
            }
            
            for(int i=0; i<dataNames.size(); i++){
                line += "   ";
                
                for(int j=0; j<maxName.length()-dataNames.get(i).length(); j++){ 
                    line += " ";
                }
                
                value = MetricUtils.getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
                
                line += value;
                
                for(int j=0; j<dataNames.get(i).length()-value.length(); j++){ 
                    line += " ";
                }
            }
            
            wr.write(line);
            wr.write(System.getProperty("line.separator"));  
        } 
    }
   
    /**
     * Save metrics as .tex file
     * 
     * @param wr PrintWriter
     * @param metricsList List of metric names
     * @param tableMetrics Table with metrics and values
     */
    public static void saveMetricsTex(PrintWriter wr, ArrayList<String> metricsList, 
           Hashtable<String, String> tableMetrics)
    {        
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
        String value;
        for(String metric : metricsList)
        {
            value = MetricUtils.getValueFormatted(metric, tableMetrics.get(metric));
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
    
    /**
     * Save multiple datasets metric values as .tex file
     * 
     * @param wr PrintWriter
     * @param metricsList List of metric names
     * @param dataNames List of dataset names
     * @param tableMetrics Table with metrics and values for all datasets
     */
    public static void saveMultiMetricsTex(PrintWriter wr, ArrayList<String> 
            metricsList, ArrayList<String> dataNames, 
            Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
        String line;
        
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
        
        line = "\\begin{tabular}{l|";
        for(int i=0; i<dataNames.size(); i++){
            line += "r|";
        }
        line += "}";
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
        
        line = " ";
        for(String name : dataNames){
            line = line + "& " + name.replaceAll("_", "\\\\\\_") + " ";
        }
        line += " \\\\";
        
        wr.write(line);       
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\hline");
        wr.write(System.getProperty("line.separator"));
        
        //Metrics
        String value = new String();
        for(String metric : metricsList)
        {
            line = metric;
            
            for(int i=0; i<dataNames.size(); i++){
                value = MetricUtils.getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
            
                if(value.equals("---")){
                    line = line + " & " + "NaN";
                }
                else{
                    line = line + " & " + value;
                }                
            }
            
            line += " \\\\";
            
            wr.write(line); 
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write("\\hline");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\end{tabular}");
        wr.write(System.getProperty("line.separator"));
        
        wr.write("\\end{document}");
        wr.write(System.getProperty("line.separator"));
    }
    
    /**
     * Save metrics as .csv file
     * 
     * @param wr PrintWriter
     * @param metricsList List with metric names
     * @param dataset Dataset
     * @param tableMetrics Table with metrics and values
     */
    public static void saveMetricsCsv(PrintWriter wr, ArrayList<String> 
            metricsList, MultiLabelInstances dataset, Hashtable<String, String> 
            tableMetrics)
    {
        wr.write("Relation Name"+ ";" + dataset.getDataSet().relationName());
        wr.write(System.getProperty("line.separator"));  

        wr.write(System.getProperty("line.separator"));   
        
        String value;
        for(String metric : metricsList)
        {
            value = MetricUtils.getValueFormatted(metric, tableMetrics.get(metric));
            if(value.equals("---")){
               wr.write(metric + ";" + "NaN"); 
            }
            else{
                wr.write(metric + ";" + value);
            }
            
            wr.write(System.getProperty("line.separator"));  
        }
    }
    
    /**
     * Save table as .csv format
     * 
     * @param wr PrintWriter
     * @param table Table
     */
    public static void saveTableCsv(PrintWriter wr, JTable table)
    {                 
        String line = new String();
        
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
    
    /**
     * Save table of labelsets frequency as .csv file
     * 
     * @param wr PrintWriter
     * @param table Table
     * @param labelsetStrings Strings defining the labelsets
     */
    public static void saveTableLabelsetsFrequencyCsv(PrintWriter wr, JTable 
            table, ArrayList<String> labelsetStrings)
    {                 
        String line = new String();

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
    
    /**
     * Save table of labelsets IR as .csv file
     * 
     * @param wr PrintWriter
     * @param table Table
     * @param labelsetStrings Strings defining the labelsets
     */
    public static void saveTableLabelsetsIRCsv(PrintWriter wr, JTable table, 
            ArrayList<String> labelsetStrings)
    {                 
        String line = new String();
        
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
    
    /**
     * Save multi-view table as .csv file
     * 
     * @param wr PrintWriter
     * @param table Table
     * @param views List of views
     * @param mlData Dataset
     */
    public static void saveMVTableCsv(PrintWriter wr, JTable table, 
            Hashtable<String, Integer[]> views, MultiLabelInstances mlData)
    {                 
        String line = new String();

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
                line = "View " + (i+1) + "; " + mlData.getDataSet().attribute(j).name();
                wr.write(line);
                wr.write(System.getProperty("line.separator"));  
            }
        }
    }
    
    /**
     * Save multiple datasets metrics as .csv file
     * 
     * @param wr PrintWriter
     * @param metricsList List of metric names
     * @param dataNames List of dataset names
     * @param tableMetrics Table with metrics and values for all datasets
     */
    public static void saveMultiMetricsCsv(PrintWriter wr, ArrayList<String> 
            metricsList, ArrayList<String> dataNames, 
            Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
        String value;
         
        value = ";";
        for(int i=0; i<tableMetrics.size(); i++){
            value += dataNames.get(i) + ";";
        }
        wr.write(value);
        wr.write(System.getProperty("line.separator"));  
        
        
        String temp;
        for(String metric : metricsList)
        {   value = metric + ";";
        
            for(int i=0; i<tableMetrics.size(); i++){
                temp = MetricUtils.getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
                
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
    
    /**
     * Save Chi and Phi coefficients as .csv file. They are stored in two different tables instead only one
     * 
     * @param wr PrintWriter
     * @param coefficients Coefficients
     * @param labelNames Label names
     */
    public static void saveChiPhiTableCsv(PrintWriter wr, double [][] 
            coefficients, String[] labelNames)
    {
        //Save label names row
        String line;        
        
        //Save Chi table
        line = "Chi;";
        
        for (String labelName : labelNames) {
            line += labelName + ";";
        }
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
        
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
        }
        
        //Save Phi table
        wr.write(System.getProperty("line.separator"));  
        wr.write(System.getProperty("line.separator"));  
        line = "Phi;";
        
        for (String labelName : labelNames) {
            line += labelName + ";";
        }
        
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
        
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
        }
    }
    
    /**
     * Save co-ocurrences table as .csv file
     * 
     * @param wr PrintWriter
     * @param coefficients Coefficients
     * @param labelNames Label names
     */
    public static void saveCoocurrenceTableCsv(PrintWriter wr, double [][] 
            coefficients, String[] labelNames)
    {
        //Save label names row
        String line = new String();
        line += " ;";
        
        for (String labelName : labelNames) {
            line += labelName + ";";
        }
                         
        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
 
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
        }
    
    }
    
    /**
     * Save heatmap table as .csv file
     * 
     * @param wr PrintWriter
     * @param coefficients Coefficients
     * @param labelNames Label names
     */
    public static void saveHeatmapTableCsv(PrintWriter wr, double [][] 
            coefficients, String[] labelNames)
    {
        //Save label names row
        String line = new String();
        line += " ;";
        
        for (String labelName : labelNames) {
            line += labelName + ";";
        }

        wr.write(line);
        wr.write(System.getProperty("line.separator"));  
        
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
        }
    
    }
        
    /**
     * Save metrics as .arff file
     * 
     * @param wr PrintWriter
     * @param metricsList List of metric names
     * @param dataset Dataset
     * @param tableMetrics Table with metrics and values
     */
    public static void saveMetricsArff(PrintWriter wr,ArrayList<String> 
            metricsList, MultiLabelInstances dataset, Hashtable<String, String>
            tableMetrics)
    {
        wr.write("@relation" + " \'" + dataset.getDataSet().relationName() + "\'");
        wr.write(System.getProperty("line.separator"));  
          
        wr.write(System.getProperty("line.separator")); 
    
        
        for(String metric : metricsList)
        {
            wr.write("@attribute " + metric.replace(" ", "_") + " numeric");
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write(System.getProperty("line.separator")); 
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator")); 
        
        String line = new String();
        
        String value;
        for(String metric : metricsList)
        {         
            value = MetricUtils.getValueFormatted(metric, tableMetrics.get(metric));
            if(value.equals("---")){
                line += "?";
            }
            else{
                line += MetricUtils.getValueFormatted(metric, tableMetrics.get(metric));
            }
            line += ", ";
        }
        //Delete last ", "
        line = line.substring(0, line.length()-2);
        wr.write(line);
        wr.write(System.getProperty("line.separator")); 
    }
    
    /**
     * Save metrics for multiple datasets as .arff format
     * 
     * @param wr PrintWriter
     * @param metricsList List of metric names
     * @param dataNames List of dataset names
     * @param tableMetrics Table with metrics and values for all datasets
     */
    public static void saveMultiMetricsArff(PrintWriter wr,ArrayList<String> 
            metricsList, ArrayList<String> dataNames, 
            Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
        wr.write("@relation" + " \'" + "relationMLDA" + "\'");
        wr.write(System.getProperty("line.separator"));  
          
        wr.write(System.getProperty("line.separator")); 
        
        wr.write("@attribute relation_name String");
        wr.write(System.getProperty("line.separator")); 

        for(String metric : metricsList)
        {
            wr.write("@attribute " + metric.replace(" ", "_") + " numeric");
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write(System.getProperty("line.separator")); 
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));
        
        String value;
        String line;
        
        for(int i=0; i<dataNames.size(); i++){
            line = dataNames.get(i) + ", ";
            
            for(String metric : metricsList)
            {         
                value = MetricUtils.getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
                if(value.equals("---")){
                    line += "?";
                }
                else{
                    line += MetricUtils.getValueFormatted(metric, tableMetrics.get(dataNames.get(i)).get(metric));
                }
                line += ", ";
            }
            
            //Delete last ", "
            line = line.substring(0, line.length()-2);
            wr.write(line);
            wr.write(System.getProperty("line.separator")); 
        }           
    }
}
