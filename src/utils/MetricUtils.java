package utils;

import java.util.ArrayList;
import mldc.attributes.AvgAbsoluteCorrelationBetweenNumericAttributes;
import mldc.attributes.AvgGainRatio;
import mldc.attributes.BinaryAttributes;
import mldc.attributes.MeanEntropiesNominalAttributes;
import mldc.attributes.MeanOfMeanOfNumericAttributes;
import mldc.attributes.MeanStdvNumericAttributes;
import mldc.attributes.NominalAttributes;
import mldc.attributes.NumericAttributes;
import mldc.attributes.ProportionBinaryAttributes;
import mldc.attributes.ProportionNominalAttributes;
import mldc.attributes.ProportionNumericAttributes;
import mldc.attributes.ProportionNumericAttributesWithOutliers;
import mldc.base.MLDataMetric;
import mldc.imbalance.CVIRInterClass;
import mldc.imbalance.KurtosisCardinality;
import mldc.imbalance.MaxIRInterClass;
import mldc.imbalance.MaxIRIntraClass;
import mldc.imbalance.MaxIRLabelset;
import mldc.imbalance.MeanIRInterClass;
import mldc.imbalance.MeanIRIntraClass;
import mldc.imbalance.MeanIRLabelset;
import mldc.imbalance.MeanKurtosis;
import mldc.imbalance.MeanSkewnessNumericAttributes;
import mldc.imbalance.MeanStdvIRIntraClass;
import mldc.imbalance.PMax;
import mldc.imbalance.PUniq;
import mldc.imbalance.SkewnessCardinality;
import mldc.labelsDistribution.Cardinality;
import mldc.labelsDistribution.Density;
import mldc.labelsDistribution.MaxEntropy;
import mldc.labelsDistribution.MeanEntropy;
import mldc.labelsDistribution.MinEntropy;
import mldc.labelsDistribution.StdvCardinality;
import mldc.labelsRelation.AvgExamplesPerLabelset;
import mldc.labelsRelation.AvgUnconditionalDependentLabelPairsByChiSquare;
import mldc.labelsRelation.Bound;
import mldc.labelsRelation.Diversity;
import mldc.labelsRelation.LabelsetsUpTo10Examples;
import mldc.labelsRelation.LabelsetsUpTo2Examples;
import mldc.labelsRelation.LabelsetsUpTo50Examples;
import mldc.labelsRelation.LabelsetsUpTo5Examples;
import mldc.labelsRelation.NumUnconditionalDependentLabelPairsByChiSquare;
import mldc.labelsRelation.ProportionDistinctLabelsets;
import mldc.labelsRelation.RatioLabelsetsUpTo10Examples;
import mldc.labelsRelation.RatioLabelsetsUpTo2Examples;
import mldc.labelsRelation.RatioLabelsetsUpTo50Examples;
import mldc.labelsRelation.RatioLabelsetsUpTo5Examples;
import mldc.labelsRelation.RatioLabelsetsWithExamplesLessThanHalfAttributes;
import mldc.labelsRelation.RatioUnconditionalDependentLabelPairsByChiSquare;
import mldc.labelsRelation.SCUMBLE;
import mldc.labelsRelation.StdvExamplesPerLabelset;
import mldc.labelsRelation.UniqueLabelsets;
import mldc.size.DistinctLabelsets;
import mldc.size.Labels;
import mldc.size.LxIxF;
import mldc.size.RatioInstancesToAttributes;
import mulan.data.MultiLabelInstances;
import static utils.DataInfoUtils.getLabelByLabelname;
import static utils.util.existsValue;
import static utils.util.getMax;
import static utils.util.hasMoreNDigits;
import weka.core.Attribute;
import weka.core.Instances;

/**
 *
 * @author Jose Maria Moyano Murillo
 */
public class MetricUtils {
    
    public static String truncateValue (String value, int digits)
    {
        double valor = Double.parseDouble(value);
        return truncateValue(valor, digits);
    }
    
    
    public static String truncateValue(double value, int digits)
    {
        String number = Double.toString(value);
        int countDigits =0;
        String result = "";
        boolean flag =false;
        
        if(!hasMoreNDigits(value, digits)) {
            return Double.toString(value);
        }
        
        for(int i=0; i<number.length();i++)
        {
            if(flag && countDigits!=digits){
                countDigits++;
            }
                
            if(number.charAt(i)=='.') {
                flag=true; 
                continue;
            }
            
            if(countDigits == digits) 
            {
                result=number.substring(0,i);
                break;
            }
        }
        
        return result;
    }
    
    
    public static ImbalancedFeature getMaxIRIntraClass(ImbalancedFeature[] imbalancedData, ArrayList<String> visited)
    {
        ImbalancedFeature max=null ;
         
        for( ImbalancedFeature current : imbalancedData )
        {
            if(! DataInfoUtils.existsAttribute(visited, current)) {
                if(max == null) {
                    max = current;
                }
                else
                {
                    if(max.getIRIntraClass() <= current.getIRIntraClass() && max.getVariance() < current.getVariance()) max = current;
                }
            }
        }
        
        return max;
    }
    
    
    public static ImbalancedFeature getMaxIRInterClass(ImbalancedFeature[] imbalancedData, ArrayList<String> visited)
    {
        ImbalancedFeature mayor = null;
         
        for(ImbalancedFeature current : imbalancedData)
        {
            if(! DataInfoUtils.existsAttribute(visited, current)) {
                if(mayor == null) {
                    mayor = current;
                }
                else
                {
                    if(mayor.getIRInterClass()<= current.getIRInterClass()&& mayor.getVariance() < current.getVariance()) mayor = current;
                }
            }
        }
        
        return mayor;
    }
    
    
    public static ImbalancedFeature getMinIR(ImbalancedFeature[] imbalancedData, ArrayList<String> visited)
    {
        ImbalancedFeature menor=null ;
         
        for( ImbalancedFeature current : imbalancedData )
        {
            if(! DataInfoUtils.existsAttribute(visited, current)) {
                if(menor == null) {
                    menor = current;
                }
                else
                {
                    if(menor.getIRIntraClass() >= current.getIRIntraClass() && menor.getVariance() > current.getVariance()) menor = current;
                }
            }
        }
        
        return menor;
    }
    
    
    public static ImbalancedFeature[] sortImbalancedDataByIRIntraClass(ImbalancedFeature[] imbalancedData)
    {
        ImbalancedFeature[] sorted = new ImbalancedFeature[imbalancedData.length];
        
        ArrayList<String> visited = new ArrayList();
        ImbalancedFeature current;
        
        for(int i=0; i<imbalancedData.length; i++)
        {
            current = MetricUtils.getMaxIRIntraClass(imbalancedData,visited);
            if(current == null) {
                break;
            }
            
            sorted[i]=current;
            visited.add(current.getName());
        }
        
        return sorted;                
    }

    
    public static ImbalancedFeature[] getImbalancedDataByIRInterClass( 
            MultiLabelInstances dataset, ImbalancedFeature[] labelsByFrequency)
    {
        int[] labelIndices= dataset.getLabelIndices();
        
        ImbalancedFeature[] imbalancedData = new ImbalancedFeature[labelIndices.length];
         
        Instances instances = dataset.getDataSet();
         
        int n1=0, n0=0, maxAppearance;
        double is, IRIntraClass, variance, IRInterClass;         
        double mean = dataset.getNumInstances()/2;
         
        Attribute currentAttribute;
        ImbalancedFeature currentLabel;        
         
        for(int i=0; i<labelIndices.length;i++)
        {
            currentAttribute= instances.attribute(labelIndices[i]);
           
            for(int j=0; j<instances.size();j++)
            {
                is=instances.instance(j).value(currentAttribute);
                if(is ==1.0) {
                    n1++;
                }
                else {
                    n0++;
                }
            } try { 
                if(n0 ==0 || n1 ==0) {
                    IRIntraClass=0;
                }
                else if(n0>n1) {
                    IRIntraClass= n0/(n1*1.0);
                }
                else {
                    IRIntraClass=n1/(n0*1.0);
                }
            } catch(Exception e1)
            {
                e1.printStackTrace();
                IRIntraClass=0;            
            }
                    
            variance = (Math.pow((n0-mean), 2) + Math.pow((n1-mean), 2))/2;
             
            currentLabel = getLabelByLabelname(currentAttribute.name(), labelsByFrequency);
             
            maxAppearance = labelsByFrequency[0].getAppearances();
             
            if(currentLabel.getAppearances() <= 0){
                IRInterClass = Double.NaN;
            }
            else{
                IRInterClass = maxAppearance/(currentLabel.getAppearances()*1.0);
            }

            imbalancedData[i] = new ImbalancedFeature(currentAttribute.name(),currentLabel.getAppearances(),IRIntraClass, variance, IRInterClass);
             
            n0=0;
            n1=0;
        }
         
        return imbalancedData;
    }
    
    
    public static ImbalancedFeature[] getImbalancedData( MultiLabelInstances dataset)
    {
        int[] labelIndices = dataset.getLabelIndices();
        
        ImbalancedFeature[] imbalancedData = new ImbalancedFeature[labelIndices.length];
         
        Instances Instancias = dataset.getDataSet();
         
        int n1=0, n0=0;
        double is, IR, variance;         
        double mean = dataset.getNumInstances()/2;
         
        Attribute current;
         
        for(int i=0; i<labelIndices.length;i++)
        {
            current= Instancias.attribute(labelIndices[i]);
           
            for(int j=0; j<Instancias.size();j++)
            {
                is=Instancias.instance(j).value(current);
                if(is ==1.0) {
                    n1++;
                }
                else {
                    n0++;
                }
            } try { 
                if(n0 ==0 || n1 ==0) IR=0;
                else if(n0>n1) IR= n0/(n1*1.0);
                else IR=n1/(n0*1.0);  
            } catch(Exception e1)
            {
                e1.printStackTrace();
                IR=0;            
            }
                    
            variance = (Math.pow((n0-mean), 2) + Math.pow((n1-mean), 2))/2;

            imbalancedData[i]= new ImbalancedFeature(current.name(), IR, variance);
             
            n0=0;
            n1=0;
        }
         
        return imbalancedData;
    }
    
    
    public static ImbalancedFeature[] getImbalancedDataByAppearances(MultiLabelInstances dataset)
    {
        int[] labelIndices = dataset.getLabelIndices();
        
        ImbalancedFeature[] imbalancedData = new ImbalancedFeature[labelIndices.length];
         
        Instances instances = dataset.getDataSet();
         
        int appearances = 0;
        double is;
        Attribute current;
         
        for(int i=0; i<labelIndices.length;i++)
        {
            current = instances.attribute(labelIndices[i]);
             
            for(int j=0; j<instances.size();j++)
            {
                is=instances.instance(j).value(current);
                if(is ==1.0) {
                    appearances++;
                }
            }
            imbalancedData[i]= new ImbalancedFeature(current.name(), appearances);
            appearances=0;
        }
         
        return imbalancedData;
    }
    
    
    public static ImbalancedFeature[] sortByFrequency (ImbalancedFeature[] labelFrequency)
    {
        ArrayList<ImbalancedFeature> list = new ArrayList();
        
        for(int i=0; i<labelFrequency.length; i++)
        {
            list.add(labelFrequency[i]);
        }
        
        ImbalancedFeature[] sorted = new ImbalancedFeature [labelFrequency.length];
        
        for(int i=0 ; i<labelFrequency.length; i++)
        {
            sorted[i]= getMax(list);
            list.remove(sorted[i]);
        }
        
        return sorted;
    }
    
    
    public static int getNumLabelsByIR(ImbalancedFeature[] imbalancedData, 
            double[] visited , double current)
    {
        if (existsValue(visited,current)) {
            return -1;
        }
        
        int appearances=0;
        
        for(int i=0; i<imbalancedData.length;i++)
        {
            if(current > imbalancedData[i].getIRIntraClass()) {
                return appearances;
            }
            if(current == imbalancedData[i].getIRIntraClass()) {
                appearances++;
            }
        }
        
        return appearances;
    }
    
    
    public static int getNumLabelsByIR(double[] IRInterClass, double[] visited, double current)
    {
        if (existsValue(visited,current)) {
            return -1;
        }
        
        int appearances = 0;
        
        for(int i=0; i<IRInterClass.length;i++)
        {
            if(current == IRInterClass[i]) {
                appearances++;
            }
        }
        
        return appearances;
    }
    
    
    public static String getMetricValue(String metric, MultiLabelInstances dataset)
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
            return("NaN");
        }
        else{
            return(Double.toString(value));
        }
    }
    
    
    public static Object[][] getRowData()
    {
        ArrayList<String> metrics = getAllMetrics();
        
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
    
    
    public static Object[][] getRowDataMulti()
    {
        ArrayList metrics = Get_metrics_multi();
        
        Object rowData[][] = new Object[metrics.size()][2];
        
        for(int i=0; i<metrics.size(); i++){
            rowData[i][0] = metrics.get(i);
            rowData[i][1]= Boolean.FALSE;
        }
        
        return rowData;
    }
    
    
    public static ArrayList<String> getAllMetrics()
    {
        return(getAllMetricsAlphaSorted());
    }
    
    public static ArrayList<String> getAllMetricsTypeSorted()
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
    
    public static ArrayList<String> getAllMetricsAlphaSorted()
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
    
    
    public static String getMetricTooltip(String metric)
    {
        String tooltip;
        
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
        return(getAllMetrics());
    }
}
