/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Double.NaN;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import mldc.attributes.AttributesMetrics;
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
import mulan.data.InvalidDataFormatException;
import mulan.data.LabelSet;
import mulan.data.LabelsPair;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.data.UnconditionalChiSquareIdentifier;
import mulan.data.generation.DataSetBuilder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import mldc.size.*;


/**
 *
 * @author osc
 */
public class Util { 
    
    
    public static void writeMulanXML(PrintWriter wr , String[] labels )
    {
        String line = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
        
        line = "<labels xmlns=\"http://mulan.sourceforge.net/labels\">";
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
        
        for(int i=0; i<labels.length;i++)
        {
            line = "<label name=\""+labels[i]+"\"></label>";
            wr.write(line);
            wr.write(System.getProperty("line.separator"));
        }
        
        line = "</labels>";
        wr.write(line);
        wr.write(System.getProperty("line.separator"));
    }
    
    public static boolean isMeka(String relationName)
    {        
        return relationName.contains("-C");
    }
    
   
    public static int getNumLabelsFromArff(String relationName)
    {
        int labels;
        
        String [] words = relationName.split("-C");
        String c = words[1].trim();
        Matcher matcher = Pattern.compile("\\d+").matcher(c);
        matcher.find();
        labels = Integer.valueOf(matcher.group());
        if(c.charAt(0) == '-'){
            labels = labels * -1;
        }
        
        return labels;
    }
    
    public static String getLabelName(String s)
    {
        String result = null;
        int spaces=0;
        int initIndex=0, endIndex=0;
        
        if(s.contains("@attribute")) 
        {
            for(int i=0; i<s.length(); i++)
            {
                if((s.charAt(i) == ' ') && (spaces == 0)){
                    spaces++;initIndex = i;
                }
                else if(s.charAt(i) == ' '){
                    endIndex = i; 
                    break;
                }                
            }
            
            result = s.substring(initIndex+1, endIndex);
        }
        
        return result;
    }
    
    public static String truncateValueAproxZero (String value, int digits)
    {
        String symbol;
        if(hasE(value))
        {
            symbol = value.substring(value.length()-3, value.length());
        }
        
        return Truncate_value(Double.parseDouble(value), digits);//+simbol;
    }
    
    private static boolean hasE(String value)
    {               
        if(value.charAt(value.length()-3) =='E' ||value.charAt(value.length()-2) =='E'){
           return true; 
        }
        else{
           return false; 
        }
    }

    
    public static ArrayList<String> getLabelNamesFromLabelset(MultiLabelInstances dataset, String labelset)
    {
        ArrayList<String> labelNames = new ArrayList();
        
        for(int i=0; i<labelset.length();i++)
        {
            if(labelset.charAt(i)=='1')
            {
                labelNames.add(getLabelByIndex(dataset, i).name());
            }
        }
        return labelNames;
    }
    
    
    public static Attribute getLabelByIndex(MultiLabelInstances dataset, int id)
    {
        int[] labelIndices = dataset.getLabelIndices();
        
        Attribute result = dataset.getDataSet().instance(1).attribute(labelIndices[id]);
        
        return result;
    }
    
    
    public static void updateBarChartValues(ImbalancedFeature[] label_x_frequency, int cant_instancias, CategoryPlot cp)
    {   
        DefaultCategoryDataset my_data = new DefaultCategoryDataset();
      
        double prob;
            
        label_x_frequency = Util.orderImbalancedDataByFrequency(label_x_frequency);
            
        double sum = 0.0;
        for(int i=0; i<label_x_frequency.length;i++)
        {
            prob= label_x_frequency[i].getAppearances()*1.0/cant_instancias;
            sum += prob;
            
            my_data.setValue(prob, label_x_frequency[i].getName()," ");               
        }
          
        cp.setDataset(my_data);
        
        sum = sum/label_x_frequency.length;
        Marker start = new ValueMarker(sum);
        start.setPaint(Color.red);
        start.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        start.setLabel("                        Mean: "+Util.Truncate_value(sum, 3));
        cp.addRangeMarker(start);
    }
    
    
    public static void updateIRChartValues(ImbalancedFeature[] labels, double[] IR, CategoryPlot cp)
    {
        DefaultCategoryDataset my_data = new DefaultCategoryDataset();
      
        double prob;
            
        labels = Util.orderImbalancedDataByFrequency(labels);
            
        double sum = 0.0;
        for(int i=labels.length-1; i>=0; i--)
        {
            prob = IR[i];           
            sum += prob;
            my_data.setValue(prob, labels[i].getName()," ");
        }
          
        cp.setDataset(my_data);
            
        sum = sum/labels.length;
        Marker meanMark = new ValueMarker(sum);
        meanMark.setPaint(Color.red);
        meanMark.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        meanMark.setLabel("                        Mean: "+Util.Truncate_value(sum, 3));
        cp.addRangeMarker(meanMark);
            
        Marker limitMark = new ValueMarker(1.5);
        limitMark.setPaint(Color.black);
        limitMark.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
            
        if((sum < 1.3) || (sum > 1.7)){
            limitMark.setLabel("                                                Imbalance limit (IR=1.5)");
        }
        cp.addRangeMarker(limitMark);
    }     
   
 
    public static void updateXYValues(ChartPanel xyplot1, double[] orderedArray) {

        XYPlot xyplot = xyplot1.getChart().getXYPlot();

        double min = orderedArray[0];
        double max = orderedArray[orderedArray.length-1];

        double median = Util.getMedian(orderedArray);

        double q1 = Util.get_q1(orderedArray);
        double q3 = Util.get_q3(orderedArray);

        XYTextAnnotation annotation;

        //min horizontal
        XYSeries serie15 = new XYSeries("15");
        serie15.add(min, 0.5);

        //max horizontal
        XYSeries serie16 = new XYSeries("16");
        serie16.add(max, 0.5);

        //min vertical
        XYSeries serie1 = new XYSeries("0");
        serie1.add(min, 0.45);
        serie1.add(min, 0.55);

        annotation = new XYTextAnnotation("Min", min, 0.40);
        annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
        xyplot.addAnnotation(annotation);

        //min-q1 horizontal
        XYSeries serie2 = new XYSeries("1");
        serie2.add(min, 0.5);
        serie2.add(q1, 0.5);

        //q1 vertical  
        XYSeries serie3 = new XYSeries("2");
        serie3.add(q1, 0.1);
        serie3.add(q1, 0.9);

        annotation = new XYTextAnnotation("Q1", q1, 0.08);
        annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
        xyplot.addAnnotation(annotation);

        // mediana 
        XYSeries serie_median = new XYSeries("11");
        serie_median.add(median, 0.1);
        serie_median.add(median, 0.9);

        annotation = new XYTextAnnotation("Median", median, 0.04);
        annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
        xyplot.addAnnotation(annotation);

        //q1-q3 horizontal sup
        XYSeries serie4 = new XYSeries("3");
        serie4.add(q1, 0.9);
        serie4.add(q3, 0.9);

        //q1-q3 horizontal inf
        XYSeries serie5 = new XYSeries("4");
        serie5.add(q1, 0.1);
        serie5.add(q3, 0.1);

        //q3 vertical
        XYSeries serie6 = new XYSeries("5");
        serie6.add(q3, 0.1);
        serie6.add(q3, 0.9);

        annotation = new XYTextAnnotation("Q3", q3, 0.08);
        annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
        xyplot.addAnnotation(annotation);

        //q3-max horizontal
        XYSeries serie7 = new XYSeries("6");
        serie7.add(q3, 0.5);
        serie7.add(max, 0.5);

        //max vertical
        XYSeries serie8 = new XYSeries("7");
        serie8.add(max, 0.45);
        serie8.add(max, 0.55);

        annotation = new XYTextAnnotation("Max", max, 0.4);
        annotation.setFont(new Font("SansSerif", Font.PLAIN, 11));
        xyplot.addAnnotation(annotation);

        XYSeriesCollection xyseriescollection = new XYSeriesCollection();

        xyseriescollection.addSeries(serie1);
        xyseriescollection.addSeries(serie2);
        xyseriescollection.addSeries(serie3);
        xyseriescollection.addSeries(serie4);
        xyseriescollection.addSeries(serie5);
        xyseriescollection.addSeries(serie6);
        xyseriescollection.addSeries(serie7);
        xyseriescollection.addSeries(serie8);
        xyseriescollection.addSeries(serie15);
        xyseriescollection.addSeries(serie16);
        xyseriescollection.addSeries(serie_median);

        xyplot.getRenderer().setSeriesPaint(9, Color.black);
        xyplot.getRenderer().setSeriesPaint(10, Color.black); 

        xyplot.getRenderer().setSeriesPaint(0, Color.black);
        xyplot.getRenderer().setSeriesPaint(1, Color.black);
        xyplot.getRenderer().setSeriesPaint(2, Color.black);
        xyplot.getRenderer().setSeriesPaint(3, Color.black);
        xyplot.getRenderer().setSeriesPaint(4, Color.black);
        xyplot.getRenderer().setSeriesPaint(5, Color.black);
        xyplot.getRenderer().setSeriesPaint(6, Color.black);
        xyplot.getRenderer().setSeriesPaint(7, Color.black);
        xyplot.getRenderer().setSeriesPaint(8, Color.black);
        xyplot.getRenderer().setSeriesPaint(9, Color.black);
        xyplot.getRenderer().setSeriesPaint(10, Color.black);
        xyplot.getRenderer().setSeriesPaint(11, Color.black);
        xyplot.getRenderer().setSeriesPaint(12, Color.black);
        xyplot.getRenderer().setSeriesPaint(13, Color.black);

        //add dataset
        xyplot.setDataset(xyseriescollection);
        

        XYSeriesCollection anotherserie = new XYSeriesCollection();

        XYSeries serie_point = new XYSeries("21");

        double[] valor_y = {0.47,0.49,0.51,0.53};

        for(int i=0, j=0; i<orderedArray.length; i++ , j++)
        {
            if(j%4==0) {
                j=0;
            }
            serie_point.add(orderedArray[i],valor_y[j] );
        }

        anotherserie.addSeries(serie_point);

        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true); 
        renderer1.setSeriesPaint(0, Color.lightGray);
        xyplot.setDataset(1, anotherserie);
        xyplot.setRenderer(1, renderer1); 
    }
  

    public static void updateLineChartValues(int cant_instancias, CategoryPlot cp,HashMap<Integer,Integer> labelset_x_frequency )
    {      
        DefaultCategoryDataset my_data = new DefaultCategoryDataset();
      
        double prob;            

        int max = MaxKey(labelset_x_frequency);
                     
        for(int i=0; i<=max ; i++)
        {
            int freq_current=0;
            if(labelset_x_frequency.get(i)!=null) {
                freq_current=labelset_x_frequency.get(i);
            }
               
            prob= freq_current*1.0/cant_instancias;
            
            if(prob==0.0) {
                my_data.setValue(0 , "Label-Combination: ",Integer.toString(i));
            }
            else {
                my_data.setValue(prob , "Label-Combination: ",Integer.toString(i));
            }   
        }    
        
        cp.setDataset(my_data);       
        
        if(max>30) {
            cp.getDomainAxis().setTickLabelsVisible(false);
        }   
        else{
            cp.getDomainAxis().setTickLabelsVisible(true);   
        }    
    }
    
    
    public  static int MaxKey (HashMap<Integer,Integer> hm)
    {
        Set<Integer> keys= hm.keySet();
       
        int max=0;
       
        for(int current : keys)
        {
           if(max<current) {
               max = current;
            }
        }
        
        return max;
    }
    
    
    public static double[] getLabelFrequency(ImbalancedFeature[] label_freq)
    {
        double[] label_frequency = new double[label_freq.length];
        
        for(int i=0;i<label_freq.length; i++){
            label_frequency[i]=(double)label_freq[i].getAppearances();
        }
            
        return label_frequency;
    }
     
    
    public static HashMap<Integer,Integer> getLabelsetsByValues(Statistics stat1)
    {            
        HashMap<LabelSet,Integer> result = stat1.labelCombCount();
        Set<LabelSet> keysets = result.keySet();
        
        HashMap<Integer,Integer> labelsetsByFrequency = new HashMap<Integer,Integer>();
        
        int oldValue;
        
        for(LabelSet current : keysets)
        {
            int value = result.get(current);
            int key = current.size();
            
            if(labelsetsByFrequency.get(key)==null)
            {
                labelsetsByFrequency.put(key, value);
            }
            else
            {
                oldValue = labelsetsByFrequency.get(key);
                labelsetsByFrequency.remove(key);
                labelsetsByFrequency.put(key, value+oldValue);     
            }
        }
        
        return labelsetsByFrequency;
    }
    
    
    public static String getXMLString(String arffText)
    {
        String result = "";

        String [] words = arffText.split("-t");
        if(words.length > 1){
            for(int i=0; i<words.length-1; i++){
                if(i == (words.length-2)){
                    result = result + words[i];
                }
                else{
                    result = result + words[i]+"-t";
                } 
            }
            result = result.substring(0, result.length()) + ".xml";
        }
        else{
            result = arffText.substring(0,arffText.length()-5)+".xml";
        }
        
        return result;
    }
     
    
     
    public static boolean contains (double[] visited , double current)
    {
        for(int i=0; i<visited.length;i++){
            if(visited[i]==current){
                return true;
            }  
        }
             
        return false;
    }
    
    
    public static boolean containsAttribute (ArrayList<String> visitados , ImbalancedFeature currentIF)
    {
        for(String current : visitados)
        {
            if(current.equals(currentIF.getName())) {
                return true;
            }
        }
         
        return false;
    }
    
    
    public static ImbalancedFeature getMaxIRIntraClass(ImbalancedFeature[] imbalancedData, ArrayList<String> visited)
    {
        ImbalancedFeature max = null ;
         
        for(ImbalancedFeature current : imbalancedData)
        {
            if(containsAttribute(visited, current)) {
                continue;
            }
            else
            {
                if(max == null) {
                    max = current;
                }
                else
                {
                    if(max.getIRIntraClass() <= current.getIRIntraClass() && max.getVariance() < current.getVariance()){
                        max = current;
                    }
                }
            }
        }
        
        return max;
    }
     
    
    public static ImbalancedFeature getMaxIRInterClass(ImbalancedFeature[] imbalancedData, ArrayList<String> visited)
    {
        ImbalancedFeature max = null ;
         
        for( ImbalancedFeature current : imbalancedData )
        {
            if( containsAttribute(visited, current)) {
                continue;
            }
            else
            {
                if(max == null) {
                    max = current;
                }
                else
                {
                    if(max.getIRInterClass()<= current.getIRInterClass()&& max.getVariance() < current.getVariance()) {
                        max = current;
                    }
                }
            }
        }
        
        return max;
    }
     
    
    public static ImbalancedFeature getMinIRIntraClass(ImbalancedFeature[] imbalancedData, ArrayList<String> visited)
     {
        ImbalancedFeature min = null ;
         
        for(ImbalancedFeature current : imbalancedData)
        {
            if(containsAttribute(visited, current)) {
                continue;
            }
            else
            {
                if(min == null) {
                    min = current;
                }
                else
                {
                    if(min.getIRIntraClass() >= current.getIRIntraClass() && min.getVariance() > current.getVariance()) {
                        min = current;
                    }
                }
            }
        }
        
        return min;
    }
     
     
    public static double get_q1(double[] orderedArray)
    {
        int quart = orderedArray.length/4;
          
        if(orderedArray.length %4 ==0){
            return orderedArray[quart-1];
        }
          
        return orderedArray[quart];                 
    }
    
        
    public static double get_q3(double[] orderedArray)
    {
        int quart = 3*(orderedArray.length/4);
        
        if(orderedArray.length %4 ==0){
            return orderedArray[quart-1];
        }
        
        return orderedArray[quart];                 
    }
    
    
    public static double getMedian(double[] orderedArray)
    {
        int mean = orderedArray.length/2;
           
        if( orderedArray.length %2 !=0 ) {
            return orderedArray[mean];
        }
        else
        {
            double value1 = orderedArray[mean-1];
            double value2 = orderedArray[mean];
            return (value1+value2)/2;
        }
    }
     

    public static ImbalancedFeature[] sortByIRIntraClass(ImbalancedFeature[] imbalancedData)
    {
        ImbalancedFeature[] ordered = new ImbalancedFeature[imbalancedData.length];
        
        ArrayList<String> visited = new ArrayList();
        ImbalancedFeature current;
        
        for(int i=0; i<imbalancedData.length; i++)
        {
            current = getMaxIRIntraClass(imbalancedData, visited);
            if(current == null) {
                break;
            }
            
            ordered[i] = current;
            visited.add(current.getName());
        }
        
        return ordered;     
    }
    
    
    public static ImbalancedFeature[] getImbalancedByIRInterClass(MultiLabelInstances dataset, ImbalancedFeature[] imbalancedData)
    {
        int[] label_indices= dataset.getLabelIndices();
        
        ImbalancedFeature[] labelsImbalanced = new ImbalancedFeature[label_indices.length];
         
        Instances Instancias = dataset.getDataSet();
         
        int app1=0, app0=0, maxAppearance;
        double is, IR, variance, IRInterClass;         
        double mean = dataset.getNumInstances()/2;
         
        Attribute current;
        ImbalancedFeature currentLabel;
                 
        for(int i=0; i<label_indices.length;i++)
        {
            current = Instancias.attribute(label_indices[i]);
           
            for(int j=0; j<Instancias.size();j++)
            {
                is=Instancias.instance(j).value(current);
                if(is ==1.0) {
                    app1++;
                }
                else {
                    app0++;
                }
            }
             
            try { 
                if(app0 ==0 || app1 ==0) {
                    IR=0;
                }
                else if(app0>app1) {
                    IR= app0/(app1*1.0);
                }
                else {
                    IR=app1/(app0*1.0);
                }
            } catch(Exception e1)
            {
                IR=0;            
            }
                    
            variance = (Math.pow((app0-mean), 2) + Math.pow((app1-mean), 2))/2;
             
            currentLabel = getLabelByLabelname(current.name(),imbalancedData);
             
            maxAppearance = imbalancedData[0].getAppearances();
             
            if(currentLabel.getAppearances() <= 0){
                IRInterClass = Double.NaN;
            }
            else{
                IRInterClass = maxAppearance/(currentLabel.getAppearances()*1.0);
            }
             
            labelsImbalanced[i]= new ImbalancedFeature(current.name(),currentLabel.getAppearances(),IR, variance, IRInterClass);
             
            app0=0;
            app1=0;
        }
         
        return labelsImbalanced;
    }
    
    

    public static ImbalancedFeature[] getImbalancedByLabel( MultiLabelInstances dataset)
    {
        int[] labelIndices = dataset.getLabelIndices();
        
        ImbalancedFeature[] labelsImbalanced = new ImbalancedFeature[labelIndices.length];
         
        Instances Instancias = dataset.getDataSet();
         
        int app1=0, app0=0;
        double is, IR, variance;         
        double mean = dataset.getNumInstances()/2;
         
        Attribute current;
        
        for(int i=0; i<labelIndices.length;i++)
        {
            current = Instancias.attribute(labelIndices[i]);
           
            for(int j=0; j<Instancias.size();j++)
            {
                is=Instancias.instance(j).value(current);
                if(is ==1.0) {
                    app1++;
                }
                else {
                    app0++;
                }
            }
             
            try { 
                if(app0 ==0 || app1 ==0) {
                    IR=0;
                }
                else if(app0>app1) {
                    IR= app0/(app1*1.0);
                }
                else {
                    IR=app1/(app0*1.0);
                }
            } catch(Exception e1)
            {
                IR=0;            
            }
                    
            variance = (Math.pow((app0-mean), 2) + Math.pow((app1-mean), 2))/2;
            
            labelsImbalanced[i] = new ImbalancedFeature(current.name(), IR, variance);
             
            app0=0;
            app1=0;
        }
         
        return labelsImbalanced;
    }
    
    
    public static ImbalancedFeature[] getFrequencyByLabel(MultiLabelInstances dataset)
    {
        int[] label_indices = dataset.getLabelIndices();
        
        ImbalancedFeature[] imbalancedData = new ImbalancedFeature[label_indices.length];
         
        Instances Instancias = dataset.getDataSet();
         
        int frequency = 0;
        double is;
        Attribute current;
         
        for(int i=0; i<label_indices.length;i++)
        {
            current= Instancias.attribute(label_indices[i]);
             
            for(int j=0; j<Instancias.size();j++){
                is=Instancias.instance(j).value(current);
                if(is == 1.0) {
                    frequency++;
                }
            }
            imbalancedData[i] = new ImbalancedFeature(current.name(), frequency);
            frequency=0;
        }
         
        return imbalancedData;
    }
    
    
    public static ImbalancedFeature[] orderImbalancedDataByFrequency (ImbalancedFeature[] imbalancedData)
    {
        ArrayList<ImbalancedFeature> lista = new ArrayList();
        
        for(int i=0; i<imbalancedData.length; i++){
            lista.add(imbalancedData[i]);
        }
        
        ImbalancedFeature[] ordered = new ImbalancedFeature [imbalancedData.length];
        
        for(int i=0 ; i<imbalancedData.length; i++)
        {
            ordered[i]= Util.getMax(lista);
            lista.remove(ordered[i]);
        }
        
        return ordered;
    }
    
    
    public static ImbalancedFeature getMax( ArrayList<ImbalancedFeature> list)
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
     
    
    public static ImbalancedFeature getMin(ArrayList<ImbalancedFeature> list)
    {
        ImbalancedFeature menor = list.get(0);
               
        for(ImbalancedFeature current : list)
        {
            if(current.getAppearances()<menor.getAppearances()) {
                menor = current;
            }
        }
            
        return menor;       
    }
    

    public static int getNumLabelsByIR(ImbalancedFeature[] imbalancedData, double[] visited ,double current)
    {
        if (contains(visited,current)) {
            return -1;
        }
        
        int apperances=0;
        
        for(int i=0; i<imbalancedData.length;i++)
        {
            if(current > imbalancedData[i].getIRIntraClass()) {
                return apperances;
            }
            if(current == imbalancedData[i].getIRIntraClass()) {
                apperances++;
            }
        }
        
        return apperances;
    }
    
    
    public static int getNumLabelsByIR(double[] IRInterClass, double[] visited, double current)
    {
        if (contains(visited, current)) {
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
    
    
    public static Object[] getValuesByRow(int nRow, double[][] coefficient, String rowName)
    {
        Object[] fila = new Object[coefficient.length+1];
        String truncate;
        
        for(int i=0; i<fila.length;i++)
        {       
            if(i==0) {              
                fila[i]= rowName;
                continue;
            }
          
            if(coefficient[i-1][nRow]==-1.0) {
                fila[i]= " ";
                continue;
            }
           
            if(i-1 != nRow) {
                truncate = Double.toString(coefficient[i-1][nRow]);
                fila[i]= Util.truncateValueAproxZero(truncate, 4);
            }
                    
            else if (i-1==nRow) {
                fila[i]= "---"; 
            }
        }
        
        return fila;
    }
    
    
    public static void saveXML(PrintWriter wr, MultiLabelInstances dataset) throws IOException
    {       
        wr.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        wr.write(System.getProperty("line.separator")); 
        wr.write("<labels xmlns=\"http://mulan.sourceforge.net/labels\">");
        wr.write(System.getProperty("line.separator")); 

        String [] labelNames = dataset.getLabelNames();

        for(int i=0; i<labelNames.length; i++){
            wr.write("<label name=\"" + labelNames[i] + "\"></label>");
            wr.write(System.getProperty("line.separator")); 
        }

        wr.write("</labels>");
        wr.write(System.getProperty("line.separator")); 
    }
    
    
    public static void saveDataset(ArrayList<MultiLabelInstances> dataset, String path, 
            String datasetName, String type) throws IOException
    {
        BufferedWriter  bw_current;
        PrintWriter wr;        

        int index = 1;
        String current_path;
      
        for(MultiLabelInstances dataset_current : dataset)
        {
            current_path = path + "/"+datasetName+type+index+".arff";
          
            bw_current = new BufferedWriter(new FileWriter(current_path));
            wr = new PrintWriter(bw_current);

            Util.saveDataset(wr,dataset_current);

            wr.close();
            bw_current.close();

            index++;
        } 
    }
    
    
    public static void saveDatasetNoViews(ArrayList<MultiLabelInstances> dataset, String path, 
            String datasetName, String type) throws IOException
    {
        BufferedWriter  bw_current;
        PrintWriter wr;        

        int index = 1;
        String current_path;

        for(MultiLabelInstances dataset_current : dataset)
        {
            current_path = path + "/"+datasetName+type+index+".arff";

            bw_current = new BufferedWriter(new FileWriter(current_path));
            wr = new PrintWriter(bw_current);

            saveDataset(wr,dataset_current, datasetName);

            wr.close();
            bw_current.close();

            index++;
        }
        
    }
    
    
    public static void saveMekaDataset(ArrayList<MultiLabelInstances> dataset, String path, 
            String datasetName, String type, String relationName) throws IOException
    {
        BufferedWriter  bw_current;
        PrintWriter wr;        

        int index = 1;
        String current_path;

        for(MultiLabelInstances dataset_current : dataset)
        {
            current_path = path + "/"+datasetName+"-"+type+index+".arff";

            bw_current = new BufferedWriter(new FileWriter(current_path));
            wr = new PrintWriter(bw_current);

            Util.saveMekaDataset(wr,dataset_current);

            wr.close();
            bw_current.close();

            index++;
        }
    }
    
    
    public static void saveMekaDatasetNoViews(ArrayList<MultiLabelInstances> dataset, 
            String path, String datasetName, String type, String relationName) throws IOException
    {
        BufferedWriter  bw_current;
        PrintWriter wr;        

        int index = 1;
        String current_path;

        for(MultiLabelInstances dataset_current : dataset)
        {
            current_path = path + "/"+datasetName+"-"+type+index+".arff";

            bw_current = new BufferedWriter(new FileWriter(current_path));
            wr = new PrintWriter(bw_current);

            saveMekaDataset(wr,dataset_current, datasetName);

            wr.close();
            bw_current.close();

            index++;
        }
        
    }
    
    
    public static void saveMekaDataset(ArrayList<MultiLabelInstances> dataset, 
            String path, String datasetName, String type) throws IOException
    {
        Util.saveMekaDataset(dataset, path,datasetName, type, datasetName);        
    }
    
    
    public static void saveMekaDatasetNoViewes(ArrayList<MultiLabelInstances> dataset, 
            String path,String datasetName, String type) throws IOException
    {
        saveMekaDatasetNoViews(dataset, path,datasetName, type, datasetName);        
    }
    
    public static void saveDataset(PrintWriter wr, MultiLabelInstances dataset)
    {
        saveDataset(wr, dataset, dataset.getDataSet().relationName());   
    }
    
    public static void saveMekaDataset(PrintWriter wr, MultiLabelInstances dataset)
    {
        saveMekaDataset(wr, dataset, dataset.getDataSet().relationName());   
    }
    
    
    public static void saveDataset(PrintWriter wr, MultiLabelInstances dataset, String relationName)
    {
        relationName = relationName.replaceAll(" ", "_");
        if(relationName.contains("-")){
            wr.write("@relation " + "\'" + relationName + "\'");
        }
        else if(relationName.contains(":")){
            wr.write("@relation " + "\'" + relationName + "\'");
        }
        else{
            wr.write("@relation " + relationName);
        }

       wr.write(System.getProperty("line.separator"));  

       Instances instances = dataset.getDataSet();
        
       Attribute att;
       for (int i=0; i< instances.numAttributes();i++) {
            att = instances.attribute(i);
            wr.write(att.toString());
            wr.write(System.getProperty("line.separator")); 
        }   

        String current ;
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));  
        for(int i=0; i<dataset.getNumInstances();i++)
        {
            current = dataset.getDataSet().get(i).toString();
            wr.write(current);
            wr.write(System.getProperty("line.separator"));  
        }
    }
    
    
    public static void saveDatasetMV(PrintWriter wr, MultiLabelInstances dataset, String relationName, String views)
    {
        relationName.replaceAll(" ", "_");

        wr.write("@relation " + "\'" + relationName + " " + views + "\'");
        wr.write(System.getProperty("line.separator"));  

        Instances instancias = dataset.getDataSet();
       
        Attribute att;
        for (int i=0; i< instancias.numAttributes();i++)
        {
            att = instancias.attribute(i);
            wr.write(att.toString());
            wr.write(System.getProperty("line.separator")); 
        }   

        String current ;
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));  
        for(int i=0; i<dataset.getNumInstances();i++)
        {
            current = dataset.getDataSet().get(i).toString();
            wr.write(current);
            wr.write(System.getProperty("line.separator"));  
        } 
    }
    
    
    public static int getMax(int [] v){
        
        int max = Integer.MIN_VALUE;
        
        for(int i=0; i<v.length; i++){
            if(v[i] > max){
                max = v[i];
            }
        }
        
        return max;
    }
    
    
    public static int getMin(int [] v){
        
        int min = Integer.MAX_VALUE;
        
        for(int i=0; i<v.length; i++){
            if(v[i] < min){
                min = v[i];
            }
        }
        
        return min;
    }
    
    
    public static void saveMekaDataset(PrintWriter wr, MultiLabelInstances dataset, String relationName)
    {
        int maxAttIndex = Integer.MIN_VALUE;
        int minAttIndex = Integer.MAX_VALUE;
        
        String c = new String();
        c = "-C ";
        
        int [] attIndex = dataset.getFeatureIndices();
        
        maxAttIndex = getMax(attIndex);
        minAttIndex = getMin(attIndex);
        
        int [] labelIndices = dataset.getLabelIndices();
        
        boolean areLabelMaxIndices = true;
        boolean areLabelMinIndices = false;
        
        for(int i=0; i<labelIndices.length && areLabelMaxIndices; i++){
            if(labelIndices[i] < maxAttIndex){
                areLabelMaxIndices = false;
            }
        }
        
        if(!areLabelMaxIndices){
            areLabelMinIndices = true;
            for(int i=0; i<labelIndices.length && areLabelMinIndices; i++){
                if(labelIndices[i] > minAttIndex){
                    areLabelMinIndices = false;
                }
            }
        }
        
        if((!areLabelMaxIndices) && (!areLabelMinIndices)){
            JOptionPane.showMessageDialog(null, "Cannot save as meka.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(areLabelMaxIndices){
            c = c + "-" + labelIndices.length;
        }
        else{
            c = c + labelIndices.length;
        }
        
        if(relationName.contains("-V:")){
            wr.write("@relation " + "\'" + relationName.split("-V:")[0] + ": " + c + " -V:" + relationName.split("-V:")[1] +  "\'");
        }
        else{
            wr.write("@relation " + "\'" + relationName + ": " + c + "\'");
        }
        
        wr.write(System.getProperty("line.separator"));  

        Instances instancias = dataset.getDataSet();
       
        Attribute att;
        for (int i=0; i< instancias.numAttributes();i++)
        {
              att = instancias.attribute(i);
              wr.write(att.toString());
              wr.write(System.getProperty("line.separator")); 
        }   
        
        String current ;
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));  
        for(int i=0; i<dataset.getNumInstances();i++)
        {
            current = dataset.getDataSet().get(i).toString();
            wr.write(current);
            wr.write(System.getProperty("line.separator"));  
        }
        
    }
    
    public static void saveMekaDatasetMV(PrintWriter wr, MultiLabelInstances dataset, 
            String relationName, String views)
    {
        int maxAttIndex;
        int minAttIndex;
        
        String c;
        c = "-C ";
        
        int [] attIndex = dataset.getFeatureIndices();
        
        maxAttIndex = getMax(attIndex);
        minAttIndex = getMin(attIndex);
        
        int [] labelIndices = dataset.getLabelIndices();
        
        boolean areLabelMaxIndices = true;
        boolean areLabelMinIndices = false;
        
        for(int i=0; i<labelIndices.length && areLabelMaxIndices; i++){
            if(labelIndices[i] < maxAttIndex){
                areLabelMaxIndices = false;
            }
        }
        
        if(!areLabelMaxIndices){
            areLabelMinIndices = true;
            for(int i=0; i<labelIndices.length && areLabelMinIndices; i++){
                if(labelIndices[i] > minAttIndex){
                    areLabelMinIndices = false;
                }
            }
        }
        
        if((!areLabelMaxIndices) && (!areLabelMinIndices)){
            JOptionPane.showMessageDialog(null, "Cannot save as meka.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(areLabelMaxIndices){
            c = c + "-" + labelIndices.length;
        }
        else{
            c = c + labelIndices.length;
        }
        
        
        wr.write("@relation " + "\'" + relationName + ": " + c + " " + views + "\'");
        wr.write(System.getProperty("line.separator"));  

        Instances instancias = dataset.getDataSet();
       
        Attribute att;
        for (int i=0; i< instancias.numAttributes();i++)
        {
            att = instancias.attribute(i);
            wr.write(att.toString());
            wr.write(System.getProperty("line.separator")); 
        }   

        String current ;
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator"));  
        for(int i=0; i<dataset.getNumInstances();i++)
        {
            current = dataset.getDataSet().get(i).toString();
            wr.write(current);
            wr.write(System.getProperty("line.separator"));  
        }
        
    }
    
   
    public static void saveMetricsCSV(PrintWriter wr,ArrayList<String> metricList, 
            MultiLabelInstances current, String datasetName, boolean isMeka)
    {
     
        String encabezado = " Relation name" ;
        
        String value,temp;
        
        encabezado+= ";"+datasetName;
         wr.write(encabezado );
         wr.write(System.getProperty("line.separator"));  
        
         ImbalancedFeature[] imbalanced_data =  Util.getImbalancedByLabel(current); 
         
        for(String metric : metricList)
        {
            value= metric;
            temp = getMetricValue(metric, current, isMeka);
            if(temp.equals("-1.0")) temp = getImbalancedMetricValue(metric, current, imbalanced_data);
            
            value+= ";" + temp.replaceAll("\\.", ",");

            wr.write(value );
            wr.write(System.getProperty("line.separator"));  
                  
        }
		
    }
    
    public static void saveMetricsCSV(PrintWriter wr, ArrayList<String> metricList, 
           ArrayList<MultiLabelInstances> datasetList, ArrayList<String> datasetNames, 
           boolean isMeka)
    {
        boolean flag=true;
        String encabezado = " Metrics" ;
        
        String value, temp;
        
        ImbalancedFeature[] imbalanced_data;
        
        for(String metric : metricList)
        {
            value= metric;
            MultiLabelInstances current ;

            for(int i=0; i< datasetList.size();i++)
            {
                current = datasetList.get(i);
                imbalanced_data =  Util.getImbalancedByLabel(current); 
            
                if(flag){ encabezado+= ";"+datasetNames.get(i);}
                
                temp = getMetricValue(metric, current,isMeka);
                if(temp.equals("-1.0")) temp= getImbalancedMetricValue(metric, current, imbalanced_data);
                
                value+= ";" + temp.replaceAll("\\.", ",");
            }
            if(flag)
            {
                flag =false;
                wr.write(encabezado );
                wr.write(System.getProperty("line.separator"));
            }
          
            wr.write(value );
            wr.write(System.getProperty("line.separator"));     
        }	
    }
   
   
   public static ImbalancedFeature getLabelByLabelname(String labelName, ImbalancedFeature[] list)
   {
        for(int i=0;i<list.length; i++)
        {
            if(labelName.equals(list[i].getName())) {
                return list[i];
            }
        }
        return null;
    }
   
   
    public static void saveMetricsTXT(PrintWriter wr,ArrayList<String> metricList, 
            MultiLabelInstances dataset, ImbalancedFeature[] imbalancedData, 
            boolean  isMeka, Hashtable<String, String> tableMetrics)
    {
        Instances instances = dataset.getDataSet();
        
        String maxString = new String();
        for(String s : metricList){
            if(s.length() > maxString.length()){
                maxString = s;
            }
        }
        
        double maxLength = maxString.length();
        
        wr.write("Relation name:"+ "\t"+instances.relationName());
        wr.write(System.getProperty("line.separator"));  

        for(String metric : metricList)
        {
            wr.write(metric + ": ");
            for(int i=0; i<(maxLength-metric.length()); i++){
                wr.write(" ");
            }

            wr.write(getValueFormatted(metric, tableMetrics.get(metric)));

            wr.write(System.getProperty("line.separator"));  
        }
    }
   
   
    public static void saveMultiMetricsTXT(PrintWriter wr,ArrayList<String> metricList, 
            ArrayList<String> dataNames, Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
        String maxString = new String();
        for(String s : metricList){
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
        
        
        for(String metric : metricList)
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
   
   
    public static void saveMetricsTEX(PrintWriter wr,ArrayList<String> metricList, 
            MultiLabelInstances dataset, ImbalancedFeature[] imbalancedData, 
            boolean  isMeka, Hashtable<String, String> tableMetrics)
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
        String value = new String();
        for(String metric : metricList)
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
    
    
    public static void saveMultiMetricsTEX(PrintWriter wr, ArrayList<String> metricList, 
            ArrayList<String> dataNames, Hashtable<String, Hashtable<String, String>> tableMetrics)
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
        for(String metric : metricList)
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
    
   
    public static void saveCSV(PrintWriter wr,ArrayList<String> metricList, 
            MultiLabelInstances dataset, ImbalancedFeature[] imbalancedData, 
            boolean  isMeka, Hashtable<String, String> tableMetrics)
    {
        Instances instances = dataset.getDataSet();
                 
        wr.write("Relation Name"+ ";" + instances.relationName());
        wr.write(System.getProperty("line.separator"));  

        wr.write(System.getProperty("line.separator"));   

        String value = new String();
        for(String metric : metricList)
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
    
    public static void saveTableCSV(PrintWriter wr, JTable table)
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
    
    
    public static void saveMVTableCSV(PrintWriter wr, JTable table, JTable table2)
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

    
    public static void saveLabelsetsFrequencyTableCSV(PrintWriter wr, JTable table, ArrayList<String> labelsetStrings)
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
    
    
    public static void saveTableIRLabelsetsCSV(PrintWriter wr, JTable table, ArrayList<String> labelsetStrings)
    {                 
        String line;
        
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
    
    
    public static void saveMVTableCSV(PrintWriter wr, JTable table, 
            Hashtable<String, Integer[]> views, MultiLabelInstances mlData)
    {                 
        String line;
        
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
    
    
    public static void saveMultiMetricsCSV(PrintWriter wr,ArrayList<String> metricList, 
            ArrayList<String> dataNames, Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
        String value = new String();
         
        value = ";";
        for(int i=0; i<tableMetrics.size(); i++){
            value += dataNames.get(i) + ";";
        }
        wr.write(value);
        wr.write(System.getProperty("line.separator"));  
        
        String temp;
        for(String metric : metricList)
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
    
    
    public static void saveChiPhiTableCSV(PrintWriter wr, double [][] coefficients, String[] labelNames)
    {
        //Save label names row
        String line = new String();
        
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
        
        wr.write(System.getProperty("line.separator"));  
        wr.write(System.getProperty("line.separator"));  
        
        //Save Phi table
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
    
    
    public static void saveCoocurrenceTableCSV(PrintWriter wr, double [][] coefficients, String[] labelNames)
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
    
    public static void saveHeatmapTableCSV(PrintWriter wr, double [][] coefficients, String[] labelNames)
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
    
    
    public static void saveMetricsARFF(PrintWriter wr,ArrayList<String> metricList, 
            MultiLabelInstances dataset, ImbalancedFeature[] imbalancedData, 
            boolean  isMeka, Hashtable<String, String> tableMetrics)
    {
        Instances i1= dataset.getDataSet();
                 
        wr.write("@relation" + " \'" + i1.relationName() + "\'");
        wr.write(System.getProperty("line.separator"));  
          
        wr.write(System.getProperty("line.separator")); 
            
        for(String metric : metricList)
        {
            wr.write("@attribute " + metric.replace(" ", "_") + " numeric");
            wr.write(System.getProperty("line.separator"));  
        }
        
        wr.write(System.getProperty("line.separator")); 
        
        wr.write("@data");
        wr.write(System.getProperty("line.separator")); 
        
        String line = new String();
        
        String value = new String();
        for(String metric : metricList)
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
    
    
    public static void saveMultiMetricsARFF(PrintWriter wr, ArrayList<String> metricList, 
            ArrayList<String> dataNames, Hashtable<String, Hashtable<String, String>> tableMetrics)
    {
        wr.write("@relation" + " \'" + "relationMLDA" + "\'");
        wr.write(System.getProperty("line.separator"));  
          
        wr.write(System.getProperty("line.separator")); 
        
        wr.write("@attribute relation_name String");
        wr.write(System.getProperty("line.separator")); 

        for(String metric : metricList)
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
            
            for(String metric : metricList)
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
    
    
    public static String getImbalancedMetricValue(String metric,MultiLabelInstances dataset,ImbalancedFeature[] imbalanced_data)
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
                    
                default:  
                    value = -1.0;
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
    
    
    public static String getMetricValue(String metric, MultiLabelInstances dataset, boolean isMeka)
    {       
        double value = -1.0;
        
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
        
        } catch (Exception e) {
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
            rowData[i][0] = metrics.get(i);
            rowData[i][1] = "-";
            rowData[i][2]= Boolean.FALSE;
        }
        
        return rowData;
    }
    
    
    public static Object[][] getRowDataMulti()
    {
        ArrayList metrics = getAllMetrics();
        
        Object rowData[][] = new Object[metrics.size()][2];
        
        for(int i=0; i<metrics.size(); i++){
            rowData[i][0] = metrics.get(i);
            rowData[i][1]= Boolean.FALSE;
        }
        
        return rowData;
    }
    
    
    public static ArrayList<String> getAllMetrics()
    {
        return(getAllMetricsAlphaOrdered());
    }
    
    public static ArrayList<String> getAllMetricsTypeOrdered()
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
        result.add("Mean of kurtosis");
        result.add("Mean of skewness of numeric attributes");

        return result;
    }
    
    public static ArrayList<String> getAllMetricsAlphaOrdered()
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
        
        return(tooltip);
    }    
    
     
    public static double[][] getPairLabelValues (MultiLabelInstances dataset, ArrayList<AttributesPair> lista_pares)
    {
        double [][] coocurrences = new double[dataset.getNumLabels()][dataset.getNumLabels()];
        coocurrences = calculateCoocurrences(dataset);
        return(coocurrences);
    }     
    
    
    public static double[][] getChiPhiCoefficients (MultiLabelInstances dataset)
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
                chi_fi_coefficient[pair.getPair()[0]][pair.getPair()[1]] = chi;
                chi_fi_coefficient[pair.getPair()[1]][pair.getPair()[0]] = phi;
            }                
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return chi_fi_coefficient;    
    }
    
    
    public static ArrayList<String> getVertices(String labelname, ArrayList<AttributesPair> list)
    {
        ArrayList<String> result = new ArrayList<>();    
        
        for(AttributesPair actual : list)
        {
            if(actual.getAttributeName1().equals(labelname))  {
                result.add(actual.getAttributeName2());
            }
            else if(actual.getAttributeName2().equals(labelname)) {
               result.add(actual.getAttributeName1());
            }
        }        
        
        return result;     
        
    }
    
    
    public static int getIndex (String[] labels, String labelname)
    {
        for(int i=0; i<labels.length;i++){
            if(labelname.equals(labels[i])){
                return i;
            }
        }
            
        return -1;
    }
    
    
    private static String[] getLabelName( int[] label_indices, Instances instances)
    {
        String[] name = new String[label_indices.length];
                
        for(int i=0;i<label_indices.length;i++)
        {
            name[i] = instances.attribute(label_indices[i]).name();
        }
        
        return name;
    }
    
    
    public static double[] getIRIntraClassValues(ImbalancedFeature[] imbalanced)
    {
        double[] result = new double[imbalanced.length];
        
        for(int i=0; i<imbalanced.length; i++) {
            result[i]=imbalanced[i].getIRIntraClass();
        }
        
        return result;    
    }
    
    
    public static double[] getIRInterClassValues(ImbalancedFeature[] imbalanced)
    {
        ImbalancedFeature[] labelFreqSorted = orderImbalancedDataByFrequency(imbalanced);
        
        double[] IRInterClass = new double[imbalanced.length];
        
        int max = labelFreqSorted[0].getAppearances();
        double value;
        
        for(int i=0;i<labelFreqSorted.length; i++)
        {
            if(labelFreqSorted[i].getAppearances() <= 0){
                value = Double.NaN;
            }
            else{
                value = max/(labelFreqSorted[i].getAppearances()*1.0);
            }
            
            IRInterClass[i] = value;
        }

        return IRInterClass;
    }
    
    
    private static int getFrequencyByLabelName(ImbalancedFeature[] list ,String labelName)
    {
        for(int i=0 ; i<list.length; i++) {
            if(list[i].getName().equals(labelName)) {
                return list[i].getAppearances();
            }
        }
        
        return -1;
    }
    
    
    private static ArrayList<AttributesPair> makeLabelPairs(int [] labelPairs, 
            int[] labelIndices, MultiLabelInstances dataset)
    {
        ArrayList<AttributesPair> list = new ArrayList<>();
        Instances instances = dataset.getDataSet();

        String[] labelNames = getLabelName(labelIndices, instances);
        AttributesPair current;

        ImbalancedFeature[] listByFrequency = getFrequencyByLabel(dataset);
        int app_i, app_j;

        int labelPairsIndex = 0;
        int value;

        for(int i = 0; i<labelIndices.length; i++)
        {
            for(int j=i+1 ; j<labelIndices.length ; j++)
            {
              value = labelPairs[labelPairsIndex];

              labelPairsIndex++; 

              if(value ==0) continue; 

              app_i = getFrequencyByLabelName(listByFrequency, labelNames[i]);
              app_j = getFrequencyByLabelName(listByFrequency, labelNames[j]);

              current = new AttributesPair(labelNames[i], labelNames[j], value, i, j, app_i, app_j);
              list.add(current);
            }
        }
        
        return list;
    }
    
    
    public static int getLabelPairIndex(int index1, int index2, int labels)
    {
        int count = index1;
        int result = 0;
        
        for(int i=1; count>0 ;i++ , count--) {
            result += labels-i;
        }
        int distance = index2 - index1;
        
        result += distance-1;
                
        return result;
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
                   index = getLabelPairIndex(i, j, labels_value.length);
                   pares_etiquetas[index]=pares_etiquetas[index]+1;
               }
            }
        }
        
        return pares_etiquetas;
    }
    
    
    public static int[] getCurrentValueLabels(Instances instances, int pos, int[] labelIndices)
    {
        int[] valueLabels = new int[labelIndices.length];
        int value;
        
        for(int i=0; i<valueLabels.length; i++)
        {
           value = (int)instances.instance(pos).value(labelIndices[i]);
           valueLabels[i] = value;
        }
        return valueLabels;
    }
    
    
    public static AttributesPair getAttributesPair(String name1, String name2, ArrayList<AttributesPair> list)
    {
        for(AttributesPair current : list)
        {
            if(name1.equals(current.getAttributeName1()) && name2.equals(current.getAttributeName2())) {
                return current;
            }
            else if (name2.equals(current.getAttributeName1()) && name1.equals(current.getAttributeName2())) {
                return current;
            }
        }

        return null;
    }
    
        
    public static String[] StringListToArray(ArrayList<String> lista)
    {
        String[] result = new String[lista.size()];
        
        for(int i=0; i<result.length; i++)
        {
            result[i] = lista.get(i);
        }
            
        return result;
    }
    
    
    public static int getBorderStrengthValue (int min, int max, int n, double edgeValue)
    {
        double interval = (max-min)/(n*1.0);
        
        int strength=0;
        
        for(double i=min; i<max; i=i+interval)
        {
            if(edgeValue < i){
                break;
            }
            strength++;
        }
        
        return strength;
    }
    
    
    private static AttributesPair getAttributePair(String att1, String att2, ArrayList<AttributesPair> list)
    {
        for( AttributesPair current : list)
        {
            if(current.getAttributeName1().equals(att1) && current.getAttributeName2().equals(att2)){
                return current;
            }
            if(current.getAttributeName1().equals(att2) && current.getAttributeName2().equals(att1)){
                return current;
            }
        }
        
        return null;
    }
    
    
    public static ArrayList<AttributesPair> getSelectedLabelPairs (ArrayList<AttributesPair> labelPairs, ArrayList<String> labels)
    {
        ArrayList<AttributesPair> result = new ArrayList();
       
        AttributesPair current;
       
        for(int i=0; i<labels.size()-1; i++)
        {
            for(int j=i+1; j<labels.size(); j++)
            {
                current = getAttributePair(labels.get(i), labels.get(j), labelPairs);
                if(current!=null){
                    result.add(current);
                }
            }
        }    
       
        return result;
    }
    
    
    public static ArrayList<AttributesPair> getAttributePairs(MultiLabelInstances dataset)
    {       
        Instances instances = dataset.getDataSet();

        int possibleComb = getPossibleComb(dataset.getNumLabels());
        
        int [] labelPairs = new int[possibleComb]; 
        int [] currentLabelValues;
        int [] labelIndices = dataset.getLabelIndices();
                
        for(int i=0; i<instances.size(); i++)
        {
            currentLabelValues = getCurrentValueLabels(instances, i, labelIndices);
            labelPairs = Actualiza_Pares_etiquetas(labelPairs, currentLabelValues);
        }            
        
        return makeLabelPairs(labelPairs, labelIndices, dataset);
    }
    
    
    public static int getPossibleComb(int n)
    {
        int result=0;
         
        for(int i=n-1; i>0; i--) {
            result +=i;
        }
         
        return result;
    }
    
    
    public static String getXMLFilename(String path)
    {           
        String [] words;
        words = path.split("/");

        path = words[words.length-1];
        String dir = new String();
        if(words.length > 1){
            for(int i=0; i<words.length-1; i++){
                dir += words[i] + "/";
            }
        }
      
        path = dir+path;

        return path;
    }
    
    public static boolean hasMoreThanNDigits(double n, int digits)
    {
        String text = Double.toString(Math.abs(n));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        
        if(decimalPlaces<=digits) {
            return false;
        }
        else{
            return true;
        }
    }
    
    
    public static String Truncate_value(double value, int digits)
    {
        String number = Double.toString(value);
        int count_dig =0;
        String result = "";
        boolean flag =false;
        
        if(!hasMoreThanNDigits(value, digits)) {
            return Double.toString(value);
        }
        
        for(int i=0; i<number.length();i++)
        {
            if(flag && count_dig!=digits){
                count_dig++;
            }
                
            if(number.charAt(i)=='.') {
                flag=true; continue;
            }
            
            if(count_dig == digits) {
                result=number.substring(0, i);
                break;
            }
        }
        return result;
    }
        
    
    public static boolean isNumber(String value)
    {
        if(value.isEmpty()) {
            return false;
        }
            
        value = value.toLowerCase().trim();
            
        String alphabet ="abcdefghijklmnopqrstuvwxyz";
            
        char current;
        for(int i=0;i<value.length();i++)
        {
            current = alphabet.charAt(i);
                
            if(value.indexOf(current)!= -1) {
                return false;
            }
        }
        
        return true;
    }
        
            
    public static String getValueFormatted(String name, String value)
    {
        String formattedValue;

        value = value.replace(",", ".");

        if(value.equals("-")){
            return value;
        }

        if(value.equals("NaN")){
            return "---";
        }

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
    
    
    public static String getValueFormatted(String value, int nDecimals)
    {
        String formattedValue;

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
        
    
    public static double[][] calculateCoocurrences(MultiLabelInstances mldata)
    {
        int nLabels = mldata.getNumLabels();
        Instances data = mldata.getDataSet();
            
        double [][] coocurrenceMatrix = new double[nLabels][nLabels];
            
        int [] labelIndices = mldata.getLabelIndices();
            
        Instance temp = null;
        for(int k=0; k<data.numInstances(); k++){   
            temp = data.instance(k);
                
            for(int i=0; i<nLabels; i++){
                for(int j=i+1; j<nLabels; j++){
                    if((temp.value(labelIndices[i]) == 1.0) && (temp.value(labelIndices[j]) == 1.0)){
                        coocurrenceMatrix[i][j]++;
                    }
                }
            }
        }
            
        return coocurrenceMatrix;
    }
        

}
