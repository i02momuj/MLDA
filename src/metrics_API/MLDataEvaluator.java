/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics_API;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import metrics.atributo;
import static metrics.metrics.Mean_IR_BR_inter_class;
import metrics.util;
import mulan.data.LabelSet;
import mulan.data.LabelsPair;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.data.UnconditionalChiSquareIdentifier;
import mulan.dimensionalityReduction.BinaryRelevanceAttributeEvaluator;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

/**
 *
 * @author Jose Maria Moyano Murillo
 */
public class MLDataEvaluator {
    
    /*
        Attributes
    */
    
    /* Dataset */
    private MultiLabelInstances mlData;
    
    /* List of metrics */
    private ArrayList<MLDataMetric> metrics;
    

    /*
        Constructor
    */
    
    public MLDataEvaluator(MultiLabelInstances dataset){
        this.mlData = dataset;
        this.metrics = new ArrayList<MLDataMetric>();
    }
    
    
    
    /*
        Public methods
    */
    
    public MultiLabelInstances getDataset(){
        return mlData;
    }
    
    public ArrayList<MLDataMetric> getMetrics(){
        return metrics;
    }
    
    public boolean addMetric(MLDataMetric metric){
        if(getMetricsAvailable().contains(metric.getName())){
            metrics.add(metric);
            Collections.sort(metrics);
            return true;
        }
        else{
            return false;
        }
    }
        
    public boolean removeMetric(String name){
        for(MLDataMetric metric : metrics){
            if(metric.getName().equals(name)){
                metrics.remove(metric);
                return true;
            }
        }
        
        
        return false;
    }
    
    public void addAllMetrics(){
        /*ArrayList<String> allMetrics = getMetricsAvailable();
        
        for(String metricName : allMetrics){
            if(! this.hasMetric(metricName)){
                metrics.add(new MLDataMetric(metricName));
            }
        }*/
        
        System.out.println("Not implemented method. Sorry.");
    }
    
    public void removeAllMetrics(){
        metrics.clear();
    }
    
    public boolean hasMetric(String name){
        for(MLDataMetric metric : metrics){
            if(metric.getName().equals(name)){
                return true;
            }
        }
        
        return false;
    }
    
    public MLDataMetric getMetric(String name){
        if(hasMetric(name)){
            for(MLDataMetric metric : metrics){
                if(metric.getName().equals(name)){
                    return(metric);
                }
            }
        }
        
        return null;
    }
    
    
    public ArrayList<String> getMetricsAvailable(){
        
        ArrayList<String> allMetrics = new ArrayList<String>();
        
        allMetrics.add("Attributes");
        allMetrics.add("Instances");
        allMetrics.add("Labels");
        allMetrics.add("Distinct labelsets");
        allMetrics.add("LIF");
        allMetrics.add("Ratio of number of instances to the number of attributes");

        allMetrics.add("Cardinality");
        allMetrics.add("Density");
        allMetrics.add("Standard deviation of label cardinality");
        allMetrics.add("Minimal entropy of labels");
        allMetrics.add("Maximal entropy of labels");
        allMetrics.add("Maximal entropy of labels");

        allMetrics.add("Diversity");
        allMetrics.add("Bound");
        allMetrics.add("SCUMBLE");
        allMetrics.add("Proportion of distinct labelsets");
        allMetrics.add("Number of labelsets up to 2 examples");
        allMetrics.add("Number of labelsets up to 5 examples");
        allMetrics.add("Number of labelsets up to 10 examples");
        allMetrics.add("Number of labelsets up to 50 examples");
        allMetrics.add("Ratio of number of labelsets up to 2 examples");
        allMetrics.add("Ratio of number of labelsets up to 5 examples");
        allMetrics.add("Ratio of number of labelsets up to 10 examples");
        allMetrics.add("Ratio of number of labelsets up to 50 examples");
        allMetrics.add("Average examples per labelset");
        allMetrics.add("Standard deviation of examples per labelset");
        allMetrics.add("Number of unique labelsets");
        allMetrics.add("Ratio of labelsets with number of examples < half of the attributes");
        allMetrics.add("Number of unconditionally dependent label pairs by chi-square test");
        allMetrics.add("Average of unconditionally dependent label pairs by chi-square test");
        allMetrics.add("Ratio of unconditionally dependent label pairs by chi-square test");

        allMetrics.add("Mean of IR per label intra class");
        allMetrics.add("Mean of IR per label inter class");
        allMetrics.add("Mean of IR per labelset");
        allMetrics.add("Max IR per label inter class");
        allMetrics.add("Max IR per label intra class");
        allMetrics.add("Max IR per labelset");
        allMetrics.add("Mean of standard deviation of IR per label intra class");
        allMetrics.add("Kurtosis cardinality");
        allMetrics.add("Skewness cardinality");
        allMetrics.add("CVIR inter class");
        allMetrics.add("Proportion of maxim label combination (PMax)");
        allMetrics.add("Proportion of unique label combination (PUniq)");
        allMetrics.add("Mean of kurtosis");
        allMetrics.add("Mean of skewness of numeric attributes");

        allMetrics.add("Number of binary attributes");
        allMetrics.add("Number of nominal attributes");
        allMetrics.add("Proportion of binary attributes");
        allMetrics.add("Proportion of nominal attributes");
        allMetrics.add("Proportion of numeric attributes with outliers");
        allMetrics.add("Mean of entropies of nominal attributes");
        allMetrics.add("Mean of mean of numeric attributes");
        allMetrics.add("Mean of standard deviation of numeric attributes");
        allMetrics.add("Average gain ratio");
        allMetrics.add("Average absolute correlation between numeric attributes");
        
        return allMetrics;
    }
    
    public void evaluate(){
        for(MLDataMetric metric : metrics){
            metric.value = metric.calculate();
        }
    }
    
    /*
        Private methods
    */
    
   
    
}
