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
public class MLDataEvaluator_old {
    
    /*
        Attributes
    */
    
    /* Dataset */
    private MultiLabelInstances mlData;
    
    /* List of metrics */
    private ArrayList<MLDataMetric_old> metrics;
    
    /* Imbalance data */
    private atributo[] imbalanced_data;
    
    /* Statistics */
    Statistics stat = new Statistics();
    
    /* Instances */
    Instances instances = null;
    
    
    /*
        Constructor
    */
    
    public MLDataEvaluator_old(MultiLabelInstances dataset){
        this.mlData = dataset;
        this.metrics = new ArrayList<MLDataMetric_old>();
        imbalanced_data =  util.Get_data_imbalanced_x_label_inter_class(mlData, util.Ordenar_freq_x_attr(util.Get_Frequency_x_label(mlData)));
        stat.calculateStats(mlData);
    }
    
    
    
    /*
        Public methods
    */
    
    public MultiLabelInstances getDataset(){
        return mlData;
    }
    
    public ArrayList<MLDataMetric_old> getMetrics(){
        return metrics;
    }
    
    public boolean addMetric(String name){
        if(getMetricsAvailable().contains(name)){
            metrics.add(new MLDataMetric_old(name));
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
            metrics.add(new MLDataMetric_old(name));
        }
        
        Collections.sort(metrics);
        
        return true;
    }
    
    public boolean removeMetric(String name){
        for(MLDataMetric_old metric : metrics){
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
                metrics.add(new MLDataMetric_old(metricName));
            }
        }
    }
    
    public void removeAllMetrics(){
        metrics.clear();
    }
    
    public boolean hasMetric(String name){
        for(MLDataMetric_old metric : metrics){
            if(metric.getName().equals(name)){
                return true;
            }
        }
        
        return false;
    }
    
    public MLDataMetric_old getMetric(String name){
        if(hasMetric(name)){
            for(MLDataMetric_old metric : metrics){
                if(metric.getName().equals(name)){
                    return(metric);
                }
            }
        }
        
        return null;
    }
    
    public String getMetricValueFormatted(String name){
        String value = new String();
        NumberFormat formatter;
        
        double numericValue = getMetric(name).getValue();
        
        //Scientific notation
        if( ((Math.abs(numericValue*1000) < 1.0) && 
                (Math.abs(numericValue*1000) > 0.0)) 
             || (Math.abs(numericValue/1000) > 10))
        {
            formatter = new DecimalFormat("0.###E0");
            value = formatter.format(numericValue);
        }
        //Integer values
        else if((name.toLowerCase().equals("attributes"))
                    || (name.toLowerCase().equals("bound"))
                    || (name.toLowerCase().equals("distinct labelsets"))
                    || (name.toLowerCase().equals("instances"))
                    || (name.toLowerCase().equals("LIF"))
                    || (name.toLowerCase().equals("labels"))
                    || (name.toLowerCase().equals("number of binary attributes"))
                    || (name.toLowerCase().equals("number of labelsets up to 2 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 5 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 10 examples"))
                    || (name.toLowerCase().equals("number of labelsets up to 50 examples"))
                    || (name.toLowerCase().equals("number of nominal attributes"))
                    || (name.toLowerCase().equals("number of unique labelsets"))
                    || (name.toLowerCase().equals("number of unconditionally dependent label pairs by chi-square test")))
        {
            formatter = new DecimalFormat("#0");
            value = formatter.format(numericValue);
        }
        //Decimal values
        else{
            formatter = new DecimalFormat("#0.000");
            value = formatter.format(numericValue);
        }
        
        return(value.replace(",", "."));
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
    
    public void calculate(){
        
        //mlData.getDataset() is done  in a lot of functions
        //Do only once if it is needed
        if((hasMetric("Average absolute correlation between numeric attributes")) || 
                (hasMetric("Minimal entropy of labels")) ||
                (hasMetric("Maximal entropy of labels")) ||
                (hasMetric("Maximal entropy of labels")) ||
                (hasMetric("Mean of entropies of nominal attributes")) ||
                (hasMetric("Mean of mean of numeric attributes")) ||
                (hasMetric("Mean of standard deviation of numeric attributes")) ||
                (hasMetric("Mean of skewness of numeric attributes")) ||
                (hasMetric("Mean of kurtosis")) ||
                (hasMetric("Number of binary attributes")) ||
                (hasMetric("Number of nominal attributes")) ||
                (hasMetric("Proportion of numeric attributes with outliers")) ||
                (hasMetric("Proportion of binary attributes")) ||
                (hasMetric("Proportion of nominal attributes"))
        ){
            instances = mlData.getDataSet();
        }
        
        
        for(MLDataMetric_old metric : metrics){
            switch(metric.getName()){
                case "Attributes":
                    metric.setValue(attributes());
                    break;
                    
               case "Instances":
                    metric.setValue(instances());
                    break;

                case "Labels":
                    metric.setValue(labels());
                    break;

                case "Distinct labelsets":
                    metric.setValue(distinctLabelset());
                    break;

                case "LIF":
                    metric.setValue(LIF());
                    break;

                case "Ratio of number of instances to the number of attributes":
                    metric.setValue(ratioOfNumberOfInstancesToTheNumberOfAttributes());
                    break;

                case "Cardinality":
                    metric.setValue(cardinality());
                    break;

                case "Density":
                    metric.setValue(density());
                    break;

                case "Standard deviation of label cardinality":
                    metric.setValue(standardDeviationOfTheLabelCardinality());
                    break;

                case "Minimal entropy of labels":
                    metric.setValue(minimalEntropyOfLabels());
                    break;

                case "Maximal entropy of labels":
                    metric.setValue(maximalEntropyOfLabels());
                    break;

                case "Mean of entropies of labels":
                    metric.setValue(meanEntropyOfLabels());
                    break;

                case "Diversity":
                    metric.setValue(diversity());
                    break;

                case "Bound":
                    metric.setValue(bound());
                    break;

                case "SCUMBLE":
                    metric.setValue(SCUMBLE());
                    break;

                case "Proportion of distinct labelsets":
                    metric.setValue(proportionOfDistinctLabelset());
                    break;

                case "Number of labelsets up to 2 examples":
                    metric.setValue(numberOfLabelsetsUpTo2Examples());
                    break;

                case "Number of labelsets up to 5 examples":
                    metric.setValue(numberOfLabelsetsUpTo5Examples());
                    break;

                case "Number of labelsets up to 10 examples":
                    metric.setValue(numberOfLabelsetsUpTo10Examples());
                    break;

                case "Number of labelsets up to 50 examples":
                    metric.setValue(numberOfLabelsetsUpTo50Examples());
                    break;

                case "Ratio of number of labelsets up to 2 examples":
                    metric.setValue(ratioOfNumberOfLabelsetsUpTo2Examples());
                    break;

                case "Ratio of number of labelsets up to 5 examples":
                    metric.setValue(ratioOfNumberOfLabelsetsUpTo5Examples());
                    break;

                case "Ratio of number of labelsets up to 10 examples":
                    metric.setValue(ratioOfNumberOfLabelsetsUpTo10Examples());
                    break;

                case "Ratio of number of labelsets up to 50 examples":
                    metric.setValue(ratioOfNumberOfLabelsetsUpTo50Examples());
                    break;

                case "Average examples per labelset":
                    metric.setValue(averageExamplesPerLabelset());
                    break;

                case "Standard deviation of examples per labelset":
                    metric.setValue(standardDeviationOfExamplesPerLabelset());
                    break;

                case "Number of unique labelsets":
                    metric.setValue(numberOfUniqueLabelsets());
                    break;

                case "Ratio of labelsets with number of examples < half of the attributes":
                    metric.setValue(ratioOfLabelsetsWithNumberOfExamplesLessThanHalfOfTheAttributes());
                    break;

                case "Number of unconditionally dependent label pairs by chi-square test":
                    metric.setValue(numberOfUnconditionallyDependentLabelPairsByChiSquareTest());
                    break;

                case "Average of unconditionally dependent label pairs by chi-square test":
                    metric.setValue(averageOfUnconditionallyDependentLabelPairsByChiSquareTest());
                    break;

                case "Ratio of unconditionally dependent label pairs by chi-square test":
                    metric.setValue(ratioOfUnconditionallyDependentLabelPairsByChiSquareTest());
                    break;

                case "Mean of IR per label intra class":
                    metric.setValue(meanOfIRIntraClass());
                    break;

                case "Mean of IR per label inter class":
                    metric.setValue(meanOfIRInterClass());
                    break;

                case "Mean of IR per labelset":
                    metric.setValue(meanIRPerLabelset());
                    break;

                case "Max IR per label inter class":
                    metric.setValue(maxIRInterClass());
                    break;

                case "Max IR per label intra class":
                    metric.setValue(maxIRInterClass());
                    break;

                case "Max IR per labelset":
                    metric.setValue(maxOfIRPerLabelset());
                    break;

                case "Mean of standard deviation of IR per label intra class":
                    metric.setValue(meanOfStandardDeviationIRIntraClass());
                    break;

                case "Kurtosis cardinality":
                    metric.setValue(kurtosisCardinality());
                    break;

                case "Skewness cardinality":
                    metric.setValue(skewnessCardinality());
                    break;

                case "CVIR inter class":
                    metric.setValue(CVIRInterClass());
                    break;

                case "Proportion of maxim label combination (PMax)":
                    metric.setValue(PMax());
                    break;

                case "Proportion of unique label combination (PUniq)":
                    metric.setValue(PUniq());
                    break;

                case "Mean of kurtosis":
                    metric.setValue(meanOfKurtosis());
                    break;

                case "Mean of skewness of numeric attributes":
                    metric.setValue(meanOfSkewnessOfNumericAttributes());
                    break;

                case "Number of binary attributes":
                    metric.setValue(numberOfBinaryAttributes());
                    break;

                case "Number of nominal attributes":
                    metric.setValue(numberOfNominalAttributes());
                    break;

                case "Proportion of binary attributes":
                    metric.setValue(proportionOfBinaryAttributes());
                    break;

                case "Proportion of nominal attributes":
                    metric.setValue(proportionOfNominalAttributes());
                    break;

                case "Proportion of numeric attributes with outliers":
                    metric.setValue(proportionOfNumericAttributesWithOutliers());
                    break;

                case "Mean of entropies of nominal attributes":
                    metric.setValue(meanOfEntropyOfNominalAttributes());
                    break;

                case "Mean of mean of numeric attributes":
                    metric.setValue(meanOfMeanOfNumericAttributes());
                    break;

                case "Mean of standard deviation of numeric attributes":
                    metric.setValue(meanOfStandarDeviationOfNumericAttributes());
                    break;

                case "Average gain ratio":
                    metric.setValue(averageGainRatio());
                    break;

                case "Average absolute correlation between numeric attributes":
                    metric.setValue(averageAbsoluteCorrelationBetweenNumericAttributes());
                    break;
            }
        }
    }
    
    
    /*
        Private methods
    */
    
    private double attributes(){
        return mlData.getFeatureIndices().length;
    }
    
    private double averageAbsoluteCorrelationBetweenNumericAttributes(){
        
        if(instances == null){
            instances = mlData.getDataSet();
        }
        
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
                res += Utils.correlation(attributesToDoubleArray[fIndex1], attributesToDoubleArray[fIndex2], numInstances);
            }
        }
        
        if(count > 0){
            return (res/count);
        }
        else{
            return Double.NaN;
        }
    }
    
    private double averageExamplesPerLabelset(){
        //numInstances / numLabelsets
        return(mlData.getNumInstances() / distinctLabelset());
    }
    
    private double averageGainRatio(){
        
        double res = 0.0;
        
        try{
            ASEvaluation ase = new InfoGainAttributeEval();
        
            BinaryRelevanceAttributeEvaluator eval = new BinaryRelevanceAttributeEvaluator(ase, mlData, "avg", "none", "eval");

            int [] featureIndices = mlData.getFeatureIndices();

            for(int i : featureIndices){
                res += eval.evaluateAttribute(i);
            }

            return (res / featureIndices.length);
        }
        catch(Exception e){
            Logger.getLogger(MLDataEvaluator_old.class.getName()).log(Level.SEVERE, null, e);
            return(Double.NaN);
        }
    }
    
    private double averageOfUnconditionallyDependentLabelPairsByChiSquareTest(){
        
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
    
    private double bound(){
        return (Math.pow(2, mlData.getNumLabels()));
    }
    
    private double cardinality(){
        
        return(stat.cardinality());
    }
    
    private double CVIRInterClass(){
        double value = 0;
        double meanIR = meanOfIRInterClass();
        int nValues = 0;
        
        for(int i=0; i<imbalanced_data.length; i++){
            if(imbalanced_data[i].get_ir_inter_class() >= 0){
                value += Math.pow(imbalanced_data[i].get_ir_inter_class() - meanIR, 2);
                nValues++;
            }
        }
        
        value = value/(nValues-1);
        value = Math.sqrt(value);
        value = value/meanIR;
        
        return(value);
    }
    
    private double density(){
        return(stat.density());
    }
    
    private double distinctLabelset(){
        return (stat.labelSets().size());
    }
    
    private double diversity(){
        return(distinctLabelset()/ bound());
    }
    
    private double instances(){
        return(mlData.getNumInstances());
    }
    
    private double kurtosisCardinality(){

        double cardinality = cardinality();
        
        int [] labelsForInstance = labelsForInstance();
        
        int nLabels = mlData.getNumLabels();
        
        double v;
        double sum2 = 0;
        double sum4 = 0;
        
        for(int i=0; i<nLabels; i++){
            v = labelsForInstance[i] - cardinality;
            sum2 += Math.pow(v, 2);
            sum4 += Math.pow(v, 4);
        }
        
        double kurtosis = (nLabels*sum4/Math.pow(sum2,2))-3;
        double sampleKurtosis = (kurtosis*(nLabels+1) + 6) * (nLabels-1)/((nLabels-2)*(nLabels-3));
        
        return(sampleKurtosis);
    }
    
    private double labels(){
        return mlData.getNumLabels();
    }
    
    private double LIF(){
        return(mlData.getNumLabels() * mlData.getNumInstances() * mlData.getFeatureIndices().length);
    }
    
    private double minimalEntropyOfLabels(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        
        int nLabels = mlData.getNumLabels();
        int [] labels = mlData.getLabelIndices();
        
        double [] entropies = new double[nLabels];
        
        for(int i=0; i<nLabels; i++){
            AttributeStats attStats = instances.attributeStats(labels[i]);
            
            if(attStats.nominalCounts != null){
                entropies[i] = entropy(attStats.nominalCounts);
            }
        }
        
        double minEntropy = Double.MAX_VALUE;
        for(double e : entropies){
            if(e < minEntropy){
                minEntropy = e;
            }
        }
        
        return(minEntropy);        
    }
    
    private double maximalEntropyOfLabels(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        
        int nLabels = mlData.getNumLabels();
        int [] labels = mlData.getLabelIndices();
        
        double [] entropies = new double[nLabels];
        
        for(int i=0; i<nLabels; i++){
            AttributeStats attStats = instances.attributeStats(labels[i]);
            
            if(attStats.nominalCounts != null){
                entropies[i] = entropy(attStats.nominalCounts);
            }
        }
        
        double maxEntropy = Double.MAX_VALUE;
        for(double e : entropies){
            if(e > maxEntropy){
                maxEntropy = e;
            }
        }
        
        return(maxEntropy);        
    }
    
    private double meanEntropyOfLabels(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        
        int nLabels = mlData.getNumLabels();
        int [] labels = mlData.getLabelIndices();
        
        double [] entropies = new double[nLabels];
        
        for(int i=0; i<nLabels; i++){
            AttributeStats attStats = instances.attributeStats(labels[i]);
            
            if(attStats.nominalCounts != null){
                entropies[i] = entropy(attStats.nominalCounts);
            }
        }

        double meanEntropy = 0;
        for(double e : entropies){
            meanEntropy += e;
        }
        meanEntropy /= entropies.length;

        return(meanEntropy);        
    }
    
    private double meanOfEntropyOfNominalAttributes(){
        double value = 0.0;
        
        if(instances == null){
            instances = mlData.getDataSet();
        }
        
        int countNominal = 0;
        int [] featureIndices = mlData.getFeatureIndices();
        
        for(int fIndex : featureIndices){
            AttributeStats attStats = instances.attributeStats(fIndex);
            if(attStats.nominalCounts != null){
                countNominal++;
                value += entropy(attStats.nominalCounts);
            }
        }
        
        value = value/countNominal;
        
        return(value);
    }
    
    private double meanOfStandardDeviationIRIntraClass(){
        double value = 0;
        
        for(int i=0; i<imbalanced_data.length; i++){
            value += Math.sqrt(imbalanced_data[i].get_variance());
        }
        
        return(value/imbalanced_data.length);
    }
    
    private double meanOfIRIntraClass(){
        double value = 0;
        int nValues = 0;
        
        for(int i=0; i<imbalanced_data.length; i++){
            if(imbalanced_data[i].get_ir() != Double.NaN){
                value += imbalanced_data[i].get_ir();
                nValues++;
            }
        }
        
        value = value/nValues;
        
        return(value);
    }
    
    private double meanOfIRInterClass(){
        double value = 0;
        int nValues = 0;
        
        for(int i=0; i<imbalanced_data.length; i++){
            if(imbalanced_data[i].get_ir_inter_class()!= Double.NaN && 
                    imbalanced_data[i].get_ir_inter_class() >= 0){
                value += imbalanced_data[i].get_ir_inter_class();
                nValues++;
            }
        }
        
        value = value/nValues;
        
        return(value);
    }
    
    private double meanIRPerLabelset(){
        HashMap<LabelSet,Integer> result = stat.labelCombCount();
        Set<LabelSet> keysets = result.keySet();

        double value = 0;
        
        int maxCountLabelset = 0;
        
        for(LabelSet labelset : keysets){
            if(result.get(labelset) > maxCountLabelset){
                maxCountLabelset = result.get(labelset);
            }
        }
        
        int i = 0;
        for(LabelSet labelset : keysets){
            value += maxCountLabelset / (result.get(labelset)*1.0);
        }
        value = value/keysets.size();
        
        return(value);
    }
    
    private double maxOfIRPerLabelset(){
        HashMap<LabelSet,Integer> result = stat.labelCombCount();
        Set<LabelSet> keysets = result.keySet();
        
        double value;
        
        int maxCountLabelset = 0;
        double maxIR = 0;
        
        for(LabelSet labelset : keysets){
            if(result.get(labelset) > maxCountLabelset){
                maxCountLabelset = result.get(labelset);
            }
        }
        
        int i = 0;
        for(LabelSet labelset : keysets){
            value = maxCountLabelset / (result.get(labelset)*1.0);
            if(value > maxIR){
                maxIR = value;
            }
            i++;
        }
        
        return(maxIR);
    }
    
    private double maxIRIntraClass(){
        double max = 0;
        
        for(int i=0; i<imbalanced_data.length; i++){
            if(imbalanced_data[i].get_ir() != Double.NaN){
                if(imbalanced_data[i].get_ir() > max){
                    max = imbalanced_data[i].get_ir();
                }
            }
        }
        
        return(max);
    }
    
    private double maxIRInterClass(){
        double max = 0;
        
        for(int i=0; i<imbalanced_data.length; i++){
            if(imbalanced_data[i].get_ir_inter_class()!= Double.NaN && 
                    imbalanced_data[i].get_ir_inter_class() >= 0){
                if(imbalanced_data[i].get_ir_inter_class() > max){
                    max = imbalanced_data[i].get_ir_inter_class();
                }
            }
        }
        
        return(max);
    }

    private double meanOfMeanOfNumericAttributes(){
        double value = 0;
        int nNumeric = 0;
        
        if(instances == null){
            instances = mlData.getDataSet();
        }
        
        Set<Attribute> attributeSet = mlData.getFeatureAttributes();
        for(Attribute att : attributeSet){
            if(att.isNumeric()){
                nNumeric++;
                value += instances.meanOrMode(att);
            }
        }
        
        value = value/nNumeric;
        
        return(value);        
    }
    
    private double meanOfStandarDeviationOfNumericAttributes(){
        double value = 0;
        int nNumeric = 0;
        double dev = 0;
        
        if(instances == null){
            instances = mlData.getDataSet();
        }
        
        Set<Attribute> attributeSet = mlData.getFeatureAttributes();
        for(Attribute att : attributeSet){
            if(att.isNumeric()){
                nNumeric++;
                value += Math.sqrt(instances.variance(att));
            }
        }
        
        if(nNumeric > 0){
            value = value / nNumeric;
            return(value);
        }
        else{
            return(Double.NaN);
        }

    }
    
    private double meanOfSkewnessOfNumericAttributes(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        int nInstances = mlData.getNumInstances();
        
        Set<Attribute> attributesSet = mlData.getFeatureAttributes();
        
        int nNumeric = 0;
        double value = 0;
        double avg;
        double var;
        double stdev;
        double val;
        
        for(Attribute att : attributesSet){
            if(att.isNumeric()){
                nNumeric++;
                avg = instances.meanOrMode(att);
                var = 0;
                for(Instance inst : instances){
                    var += Math.pow(inst.value(att) - avg, 3);
                }
                stdev = Math.sqrt(instances.variance(att));
                value += nInstances*var / ((nInstances-1)*(nInstances-2)*Math.pow(stdev, 3));
            }
        }
        
        if(nNumeric > 0){
            return(value);
        }
        else{
            return(Double.NaN);
        }
    }
    
    private double meanOfKurtosis(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        int nInstances = mlData.getNumInstances();
        
        double avg;
        double var2;
        double var4;
        double val;
        int nNumeric = 0;
        double value = 0;
        
        Set<Attribute> attributesSet = mlData.getFeatureAttributes();
        
        for(Attribute att : attributesSet){
            if(att.isNumeric()){
                nNumeric++;
                avg = instances.meanOrMode(att);
                var2 = 0;
                var4 = 0;
                
                for(Instance inst : instances){
                    val = inst.value(att);
                    var2 += Math.pow(val-avg, 2);
                    var4 += Math.pow(val-avg, 4);
                }
                
                double kurtosis = (nInstances*var4/Math.pow(var2,2))-3;
                double sampleKurtosis = (kurtosis*(nInstances+1) + 6) * (nInstances-1)/((nInstances-2)*(nInstances-3));
                value += sampleKurtosis;
            }
        }
        if(nNumeric > 0){
            value = value/nNumeric;
            return(value);
        }
        else{
            return(Double.NaN);
        }
    }
        
    private double numberOfBinaryAttributes(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        Set<Attribute> attributeSet = mlData.getFeatureAttributes();
        
        int count = 0;
        
        for(Attribute att : attributeSet){
            if(att.numValues() == 2){
                count++;
            }
        }
        
        return count;
    }
    
    private double numberOfLabelsetsUpToNExamples(int n){
        Collection<Integer> counts = stat.labelCombCount().values();
        Integer[] combCounts = new Integer[counts.size()];
        counts.toArray(combCounts);
        Arrays.sort(combCounts);
        
        int count = 0;
        
        for(int i=0; i<combCounts.length; i++){
            if(combCounts[i] <= n){
                count++;
            }
            else{
                return(count);
            }
        }
        
        return(count);
    }
    
    private double numberOfLabelsetsUpTo2Examples(){
        return(numberOfLabelsetsUpToNExamples(2));
    }
    
    private double numberOfLabelsetsUpTo5Examples(){
        return(numberOfLabelsetsUpToNExamples(5));
    }
    
    private double numberOfLabelsetsUpTo10Examples(){
        return(numberOfLabelsetsUpToNExamples(10));
    }
    
    private double numberOfLabelsetsUpTo50Examples(){
        return(numberOfLabelsetsUpToNExamples(50));
    }
    
    private double numberOfNominalAttributes(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        Set<Attribute> attributeSet = mlData.getFeatureAttributes();
        
        int count = 0;
        
        for(Attribute att : attributeSet){
            if(att.isNominal()){
                count++;
            }
        }
        
        return count;
    }
    
    private double numberOfUnconditionallyDependentLabelPairsByChiSquareTest(){
        
        UnconditionalChiSquareIdentifier chi2Identifier = new UnconditionalChiSquareIdentifier();
        LabelsPair[] pairs = chi2Identifier.calculateDependence(mlData);
        
        int dep = 0;
        double score;
        
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
    
    private double proportionOfDistinctLabelset(){
        return(stat.labelCombCount().size() / mlData.getNumInstances());
    }
    
    private double PMax(){
        HashMap<LabelSet, Integer> combCount = stat.labelCombCount();
        
        int max = 0;
        
        for(LabelSet key : combCount.keySet()){
            if(combCount.get(key) > max){
                max = combCount.get(key);
            }
        }
        
        double value = ((double) max)/mlData.getNumInstances();
        
        return(value);
    }
    
    private double proportionOfNumericAttributesWithOutliers(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        int nInstances = mlData.getNumInstances();
        
        double alpha = 0.05;
        int numToTrimAtSide = (int)(nInstances*alpha / 2);
        int nNumeric = 0;
        int nOutliers = 0;
        Set<Attribute> attributeSet = mlData.getFeatureAttributes();
        
        double variance, varianceTrimmed;
        double [] values;
        double [] trimmed = new double[nInstances - (numToTrimAtSide * 2)];
        double ratio;
        
        for(Attribute att : attributeSet){
            if(att.isNumeric()){
                nNumeric++;
                variance = instances.variance(att);
                values = instances.attributeToDoubleArray(att.index());
                Arrays.sort(values);
                
                for(int i=0; i<trimmed.length; i++){
                    trimmed[i] = values[i + numToTrimAtSide];
                }
                varianceTrimmed = Utils.variance(trimmed);
                ratio = varianceTrimmed / variance;
                
                if(ratio < 0.7){
                    nOutliers++;
                }
            }
        }
        
        if(nNumeric > 0){
            return((double) nOutliers / nNumeric);
        }
        else{
            return(Double.NaN);
        }
    }
    
    private double proportionOfBinaryAttributes(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        
        int nBinary = 0;
        
        Set<Attribute> attributesSet = mlData.getFeatureAttributes();
        for(Attribute att : attributesSet){
            if(att.numValues() == 2){
                nBinary++;
            }
        }
        
        return((double) nBinary/attributesSet.size());
    }
    
    private double proportionOfNominalAttributes(){
        if(instances == null){
            instances = mlData.getDataSet();
        }
        
        int nNominal = 0;
        
        Set<Attribute> attributesSet = mlData.getFeatureAttributes();
        for(Attribute att : attributesSet){
            if(att.isNominal()){
                nNominal++;
            }
        }
        
        return((double) nNominal/attributesSet.size());
    }
    
    private double PUniq(){
        double uniq = numberOfUniqueLabelsets();
        
        return(uniq / mlData.getNumInstances());
    }
    
    private double ratioOfLabelsetsWithNumberOfExamplesLessThanHalfOfTheAttributes(){
        double n = numberOfLabelsetsUpToNExamples(mlData.getFeatureIndices().length);
        return(n / stat.labelCombCount().values().size());
    }
    
    private double ratioOfNumberOfInstancesToTheNumberOfAttributes(){
        return(mlData.getNumInstances() / mlData.getFeatureIndices().length);
    }
    
    private double ratioOfNumberOfLabelsetsUpTo2Examples(){
        double n = numberOfLabelsetsUpToNExamples(2);
        return(n / stat.labelCombCount().values().size());
    }
    
    private double ratioOfNumberOfLabelsetsUpTo5Examples(){
        double n = numberOfLabelsetsUpToNExamples(5);
        return(n / stat.labelCombCount().values().size());
    }
    
    private double ratioOfNumberOfLabelsetsUpTo10Examples(){
        double n = numberOfLabelsetsUpToNExamples(10);
        return(n / stat.labelCombCount().values().size());
    }
    
    private double ratioOfNumberOfLabelsetsUpTo50Examples(){
        double n = numberOfLabelsetsUpToNExamples(50);
        return(n / stat.labelCombCount().values().size());
    }
    
    private double ratioOfUnconditionallyDependentLabelPairsByChiSquareTest(){
        
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
    
    private double skewnessCardinality(){
        double value = 0;
        
        try{
            int [] labelsForInstance = labelsForInstance();
            double avg = stat.cardinality();
            double sum = 0;
            int n = labelsForInstance.length;
            double cardStdev;
            
            for(int i=0; i<n; i++){
                sum += Math.pow(labelsForInstance[i] - avg, 3);
            }
            
            cardStdev = Math.sqrt(sum / (n-1));
            value =  n * sum / ((n - 1)*(n-2) * Math.pow(cardStdev, 3));
            
        }
        catch(Exception e){
            value = 0;
            e.printStackTrace();
        }
        
        return(value);
    }
    
    private double standardDeviationOfTheLabelCardinality(){
        double value = 0;
        
        try{
            int [] labelsForInstance = labelsForInstance();
            double avg = stat.cardinality();
            double sum = 0;
            
            for(int i=0; i<labelsForInstance.length; i++){
                sum += Math.pow(labelsForInstance[i] - avg, 2);
            }
            
            value = Math.sqrt(sum / (labelsForInstance.length - 1));
        }
        catch(Exception e){
            value = 0;
            e.printStackTrace();
        }
        
        return(value);
    }
    
    private double standardDeviationOfExamplesPerLabelset(){
        double value = 0;
        
        HashMap<LabelSet,Integer> labelsets = stat.labelCombCount();
        int nValues = labelsets.values().size();
        double media = 0;
        
        for(int n : labelsets.values()){
            media += n;
        }
        media = media/nValues;
        
        double varianza = 0;
        
        for(int n : labelsets.values()){
            varianza += Math.pow(n-media, 2);
        }
        varianza = varianza/nValues;
        
        return(Math.sqrt(varianza));
    }
    
    private double numberOfUniqueLabelsets(){
        HashMap<LabelSet,Integer> labelsets = stat.labelCombCount();
        
        int uniq = 0;
        
        for(int n : labelsets.values()){
            if(n == 1){
                uniq++;
            }
        }
        
        return(uniq);
    }
    
    private double SCUMBLE(){
        return -1;
    }
    
    
    /*Other functions */
    
    private int[] labelsForInstance(){

        int nInstances = mlData.getNumInstances();
        int nLabels = mlData.getNumLabels();
        
        int [] labelsForInstance = new int[nInstances];
        
        int[] labelIndices = mlData.getLabelIndices();
        
        Instances instances = mlData.getDataSet();
        
        for(Instance inst : instances){
            for(int j=0; j<nLabels; j++){
                if(inst.value(labelIndices[j]) == 1){
                    labelsForInstance[j]++;
                }
            }
        }
        
        return(labelsForInstance);
    }
    
    private double entropy(int [] array){
        double entropy = 0;
        int sum = 0;
        
        for(int i=0; i<array.length; i++){
            entropy -= lnFunc(array[i]);
            sum += array[i];
        }
        
        if(sum == 0){
            return 0;
        }
        else{
            return( (entropy+lnFunc(sum)) / (sum*Math.log(array.length)) );
        }
    }
    
    private double lnFunc(double num){
        if(num < 1e-6){
            return 0;
        }
        else{
            return(num*Math.log(num));
        }
    }
    
    private double average(double [] array){
        double value = 0;
        
        for(double num : array){
            value += num;
        }
        
        value = value / array.length;
        
        return(value);
    }
    
}
