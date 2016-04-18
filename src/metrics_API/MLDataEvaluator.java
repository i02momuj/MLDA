/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics_API;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import mulan.data.LabelsPair;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.data.UnconditionalChiSquareIdentifier;
import mulan.dimensionalityReduction.BinaryRelevanceAttributeEvaluator;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.InfoGainAttributeEval;
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
    ArrayList<MLDataMetric> metrics;
    
    
    
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
    
    public boolean addMetric(String name){
        if(getMetricsAvailable().contains(name)){
            metrics.add(new MLDataMetric(name));
            Collections.sort(metrics);
            return true;
        }
        else{
            return false;
        }
    }
        
    public boolean addMetrics(ArrayList<String> names){
        ArrayList<String> available = getMetricsAvailable();
        
        for(String name : names){
            if(! available.contains(name)){
                return false;
            }
        }
        
        for(String name : names){
            metrics.add(new MLDataMetric(name));
        }
        
        Collections.sort(metrics);
        
        return true;
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
        ArrayList<String> allMetrics = getMetricsAvailable();
        
        for(String metricName : allMetrics){
            if(! this.hasMetric(metricName)){
                metrics.add(new MLDataMetric(metricName));
            }
        }
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
    
    
    public ArrayList<String> getMetricsAvailable(){
        
        ArrayList<String> allMetrics = new ArrayList<String>();
        
        allMetrics.add("Attributes");
        allMetrics.add("Average absolute correlation between numeric attributes");
        allMetrics.add("Average examples per labelset");
        allMetrics.add("Average gain ratio");
        allMetrics.add("Average of unconditionally dependent label pairs by chi-square test");
        allMetrics.add("Bound");
        allMetrics.add("Cardinality");
        allMetrics.add("CVIR inter class");
        allMetrics.add("Density");
        allMetrics.add("Distinct Labelset");
        allMetrics.add("Diversity");
        allMetrics.add("Instances");
        allMetrics.add("Kurtosis cardinality");
        allMetrics.add("Labels");
        allMetrics.add("Labels x instances x features");
        allMetrics.add("Maximal entropy of labels");
        allMetrics.add("Mean of entropy of nominal attributes");
        allMetrics.add("Mean of standard deviation IR per label intra class");
        allMetrics.add("Mean of IR per label intra class");
        allMetrics.add("Mean of IR per label inter class");
        allMetrics.add("Mean of IR per labelset");
        allMetrics.add("Mean of mean of numeric attributes");
        allMetrics.add("Mean of standar deviation of numeric attributes");
        allMetrics.add("Mean of skewness of numeric attributes");
        allMetrics.add("Mean of kurtosis");
        allMetrics.add("Minimal entropy of labels");
        allMetrics.add("Number of binary attributes");
        allMetrics.add("Number of labelsets up to 2 examples");
        allMetrics.add("Number of labelsets up to 5 examples");
        allMetrics.add("Number of labelsets up to 10 examples");
        allMetrics.add("Number of labelsets up to 50 examples");
        allMetrics.add("Number of nominal attributes");
        allMetrics.add("Number of unconditionally dependent label pairs by chi-square test");
        allMetrics.add("Proportion of Distinct Labelset");
        allMetrics.add("Proportion of maxim label combination (PMax)");
        allMetrics.add("Proportion of numeric attributes with outliers");
        allMetrics.add("Proportion of binary attributes");
        allMetrics.add("Proportion of nominal attributes");
        allMetrics.add("Proportion of unique label combination (PUniq)");
        allMetrics.add("Ratio of labelsets with number of examples < half of the attributes");
        allMetrics.add("Ratio of number of instances to the number of attributes");
        allMetrics.add("Ratio of number of labelsets up to 2 examples");
        allMetrics.add("Ratio of number of labelsets up to 5 examples");
        allMetrics.add("Ratio of number of labelsets up to 10 examples");
        allMetrics.add("Ratio of number of labelsets up to 50 examples");
        allMetrics.add("Ratio of unconditionally dependent label pairs by chi-square test");
        allMetrics.add("Skewness cardinality");
        allMetrics.add("Standard desviation of the label cardinality");
        allMetrics.add("Standard desviation of examples per labelset");
        
        return allMetrics;
    }
    
    
    /*
        Private methods
    */
    
    private double calculateAttributes(){
        return mlData.getFeatureIndices().length;
    }
    
    private double calculateAverageAbsoluteCorrelationBetweenNumericAttributes(){
        
        Instances instances = mlData.getDataSet();
        
        int numInstances = mlData.getNumInstances();
        
        double res = 0.0;
        int count = 0;
        
        int [] featureIndices = mlData.getFeatureIndices();
        
        Vector<Integer> numericFeatureIndices = new Vector<>();
        for(int fIndex : featureIndices){
            if(instances.attribute(fIndex).isNumeric()){
                numericFeatureIndices.add(fIndex);
            }
        }
        
        if(numericFeatureIndices.size() <= 0){
            return Double.NaN;
        }
        
        double [][] attributesToDoubleArray = new double[numericFeatureIndices.size()][numInstances];
        for(int fIndex : numericFeatureIndices){
            attributesToDoubleArray[fIndex] = instances.attributeToDoubleArray(fIndex);
        }
        
        for(int fIndex1 : numericFeatureIndices){
            for(int fIndex2 = fIndex1+1; fIndex2 < numericFeatureIndices.size(); fIndex2++){
                count++;
                Utils.correlation(attributesToDoubleArray[fIndex1], attributesToDoubleArray[fIndex1], numInstances);
            }
        }

        
        return (res/count);
    }
    
    private double calculateAverageExamplesPerLabelset(){
        //numInstances / numLabelsets
        return(mlData.getNumInstances() / calculateDistinctLabelset());
    }
    
    private double calculateAverageGainRatio(){
        
        double res = 0.0;
        
        ASEvaluation ase = new InfoGainAttributeEval();
        
        BinaryRelevanceAttributeEvaluator eval = new BinaryRelevanceAttributeEvaluator(ase, mlData, "avg", "none", "eval");
         
        int [] featureIndices = mlData.getFeatureIndices();
        
        for(int i : featureIndices){
            try {
                res += eval.evaluateAttribute(i);
            } catch (Exception ex) {
                Logger.getLogger(MLDataEvaluator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return (res / featureIndices.length);
    }
    
    private double calculateAverageOfUnconditionallyDependentLabelPairsByChiSquareTest(){
        
        UnconditionalChiSquareIdentifier chi2Identifier = new UnconditionalChiSquareIdentifier();
        LabelsPair[] pairs = chi2Identifier.calculateDependence(mlData);
        
        int dep = 0;
        double sum = 0.0;
        double score = 0.0;
        
        for (LabelsPair pair : pairs) {
            score = pair.getScore();
            if(score > 6.635){
                dep++;
                sum += score;
            }
            else{
                break;
            }
        }
        
        return(sum/dep);
    }
    
    private double calculateBound(){
        return (Math.pow(2, mlData.getNumLabels()));
    }
    
    private double calculateCardinality(){
        Statistics stat = new Statistics();
        return(stat.cardinality());
    }
    
    private double calculateCVIRInterClass(){
        return -1;
    }
    
    private double calculateDensity(){
        return -1;
    }
    
    private double calculateDistinctLabelset(){
        
        Statistics stat = new Statistics();
        
        return (stat.labelSets().size());
    }
    
    private double calculateDiversity(){
        return -1;
    }
    
    private double calculateInstances(){
        return -1;
    }
    
    private double calculateKurtosisCardinality(){
        return -1;
    }
    
    private double calculateLabels(){
        return -1;
    }
    
    private double calculateLIF(){
        return -1;
    }
    
    private double calculateMaximalEntropyOfLabels(){
        return -1;
    }
    
    private double calculateMeanOfEntropyOfNominalAttributes(){
        return -1;
    }
    
    private double calculateMeanOfStandardDeviationIrPerLabelIntraClass(){
        return -1;
    }
    
    private double calculateMeanOfIrPerLabelIntraClass(){
        return -1;
    }
    
    private double calculateMeanOfIrPerLabelInterClass(){
        return -1;
    }
    
    private double calculateMeanOfIrPerLabelset(){
        return -1;
    }
    
    private double calculateMeanOfMeanOfNumericAttributes(){
        return -1;
    }
    
    private double calculateMeanOfStandarDeviationOfNumericAttributes(){
        return -1;
    }
    
    private double calculateMeanOfSkewnessOfNumericAttributes(){
        return -1;
    }
    
    private double calculateMeanOfKurtosis(){
        return -1;
    }
    
    private double calculateMinimalEntropyOfLabels(){
        return -1;
    }
    
    private double calculateNumberOfBinaryAttributes(){
        return -1;
    }
    
    private double calculateNumberOfLabelsetsUpTo2Examples(){
        return -1;
    }
    
    private double calculateNumberOfLabelsetsUpTo5Examples(){
        return -1;
    }
    
    private double calculateNumberOfLabelsetsUpTo10Examples(){
        return -1;
    }
    
    private double calculateNumberOfLabelsetsUpTo50Examples(){
        return -1;
    }
    
    private double calculateNumberOfNominalAttributes(){
        return -1;
    }
    
    private double calculateNumberOfUnconditionallyDependentLabelPairsByChiSquareTest(){
        
        UnconditionalChiSquareIdentifier chi2Identifier = new UnconditionalChiSquareIdentifier();
        LabelsPair[] pairs = chi2Identifier.calculateDependence(mlData);
        
        int dep = 0;
        double score = 0.0;
        
        for (LabelsPair pair : pairs) {
            score = pair.getScore();
            if(score > 6.635){
                dep++;
            }
            else{
                break;
            }
        }
        
        return(dep);
    }
    
    private double calculateProportionOfDistinctLabelset(){
        return -1;
    }
    
    private double calculatePMax(){
        return -1;
    }
    
    private double calculateProportionOfNumericAttributesWithOutliers(){
        return -1;
    }
    
    private double calculateProportionOfBinaryAttributes(){
        return -1;
    }
    
    private double calculateProportionOfNominalAttributes(){
        return -1;
    }
    
    private double calculatePUniq(){
        return -1;
    }
    
    private double calculateRatioOfLabelsetsWithNumberOfExamplesLessThanHalfOfTheAttributes(){
        return -1;
    }
    
    private double calculateRatioOfNumberOfInstancesToTheNumberOfAttributes(){
        return -1;
    }
    
    private double calculateRatioOfNumberOfLabelsetsUpTo2Examples(){
        return -1;
    }
    
    private double calculateRatioOfNumberOfLabelsetsUpTo5Examples(){
        return -1;
    }
    
    private double calculateRatioOfNumberOfLabelsetsUpTo10Examples(){
        return -1;
    }
    
    private double calculateRatioOfNumberOfLabelsetsUpTo50Examples(){
        return -1;
    }
    
    private double calculateRatioOfUnconditionallyDependentLabelPairsByChiSquareTest(){
        
        UnconditionalChiSquareIdentifier chi2Identifier = new UnconditionalChiSquareIdentifier();
        LabelsPair[] pairs = chi2Identifier.calculateDependence(mlData);
        
        int dep = 0;
        double score = 0.0;
        
        for (LabelsPair pair : pairs) {
            score = pair.getScore();
            if(score > 6.635){
                dep++;
            }
            else{
                break;
            }
        }
        
        return((double) dep/pairs.length);
    }
    
    private double calculateSkewnessCardinality(){
        return -1;
    }
    
    private double calculateStandardDesviationOfTheLabelCardinality(){
        return -1;
    }
    
    private double calculateStandardDesviationOfExamplesPerLabelset(){
        return -1;
    }
    
}
