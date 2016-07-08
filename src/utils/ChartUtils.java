package utils;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import mulan.data.LabelsPair;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.data.UnconditionalChiSquareIdentifier;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import static utils.util.maxKey;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Jose Maria Moyano Murillo
 */
public class ChartUtils {
    
    public static void updateValuesBarChart(ImbalancedFeature[] labelsByFreq, 
            int nInstances, CategoryPlot cp)
    {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        
        double prob;
            
        labelsByFreq = MetricUtils.sortByFrequency(labelsByFreq);
        
        double sum = 0.0;
        for(int i=0; i<labelsByFreq.length;i++)
        {
            prob = labelsByFreq[i].getAppearances()*1.0/nInstances;
            sum += prob;
            
            data.setValue(prob, labelsByFreq[i].getName()," ");
        }
          
        cp.setDataset(data);
            
        // add mean mark
        sum = sum/labelsByFreq.length;
        Marker start = new ValueMarker(sum);
        start.setPaint(Color.red);
        start.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        start.setLabel("                        Mean: "+MetricUtils.truncateValue(sum, 3));
        cp.addRangeMarker(start);
    }
    
    
    public static void updateIRBarChart(ImbalancedFeature[] labelsByFrequency, 
            double[] IR, CategoryPlot cp)
    {
        DefaultCategoryDataset my_data = new DefaultCategoryDataset();
      
        double prob = 0;
            
        labelsByFrequency = MetricUtils.sortByFrequency(labelsByFrequency);
            
        double sum = 0.0;
        for(int i=labelsByFrequency.length-1; i>=0; i--)
        {
            prob= IR[i];
            sum += prob;
            my_data.setValue(prob, labelsByFrequency[i].getName()," ");
        }
          
        cp.setDataset(my_data);
            
        // add mean mark
        sum = sum/labelsByFrequency.length;
        Marker meanMark = new ValueMarker(sum);
        meanMark.setPaint(Color.red);
        meanMark.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
        meanMark.setLabel("                        Mean: "+MetricUtils.truncateValue(sum, 3));
        cp.addRangeMarker(meanMark);
            
        //Add Imbalance limit mark
        Marker limitMark = new ValueMarker(1.5);
        limitMark.setPaint(Color.black);
        limitMark.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
            
        if((sum < 1.3) || (sum > 1.7)){
            limitMark.setLabel("                                                Imbalance limit (IR=1.5)");
        }            
        cp.addRangeMarker(limitMark);
    }
    
    
    public static void updateXYChart(ChartPanel plot, double[] sortedArray) {

        XYPlot xyplot = plot.getChart().getXYPlot();

        double min = sortedArray[0];
        double max = sortedArray[sortedArray.length-1];

        double median = util.getMedian(sortedArray);

        double q1 = util.getQ1(sortedArray);
        double q3 = util.getQ3(sortedArray);

        XYTextAnnotation annotation ;

        //min-lowlimit horizontal
        XYSeries serie15 = new XYSeries("15");
        serie15.add(min, 0.5);

        //max-toplimit horizontal
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

        // median 
        XYSeries serie_mediana = new XYSeries("11");
        serie_mediana.add(median, 0.1);
        serie_mediana.add(median, 0.9);

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
        xyseriescollection.addSeries(serie_mediana);

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

        // add a second dataset and renderer... 
        XYSeriesCollection anotherserie = new XYSeriesCollection();

        XYSeries serie_point = new XYSeries("21");

        double[] yValue = {0.47,0.49,0.51,0.53};

        for(int i=0, j=0; i<sortedArray.length; i++ , j++)
        {
            if(j%4==0) {
                j=0;
            }
            serie_point.add(sortedArray[i], yValue[j]);
        }

        anotherserie.addSeries(serie_point);

        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(false, true); 
        renderer1.setSeriesPaint(0, Color.lightGray);

        xyplot.setDataset(1, anotherserie);
        xyplot.setRenderer(1, renderer1);
    }
    
    
    public static void updateLineChart(int nInstances, CategoryPlot cp, 
            HashMap<Integer,Integer> labelsetsByFrequency )
    {      
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        
        double prob;            

        int max = maxKey(labelsetsByFrequency);
                     
        for(int i=0; i<=max ; i++)
        {
            int freq_current=0;
            if(labelsetsByFrequency.get(i)!=null) {
                freq_current=labelsetsByFrequency.get(i);
            }
            
            prob= freq_current*1.0/nInstances;
            
            if(prob==0.0) {
                data.setValue(0 , "Label-Combination: ",Integer.toString(i));
            }
            else {
                data.setValue(prob , "Label-Combination: ",Integer.toString(i));
            }
               
        }         
        cp.setDataset(data);       
        
        if(max>30) {
            cp.getDomainAxis().setTickLabelsVisible(false);
        }   
        else{
            cp.getDomainAxis().setTickLabelsVisible(true);   
        }
    }
    
    
    public static double[][] getCoocurrences (MultiLabelInstances dataset)
    {
        double [][] coocurrences;
        coocurrences = calculateCoocurrences(dataset);
        return(coocurrences);
    }
    
    
    public static double[][] getChiPhiCoefficients (MultiLabelInstances dataset)
    {
        double[][] coefficients = new double[dataset.getNumLabels()][dataset.getNumLabels()];
        double phi, chi;
        
        try {                
            UnconditionalChiSquareIdentifier depid = new UnconditionalChiSquareIdentifier();
            LabelsPair[] pairs = depid.calculateDependence(dataset);
            Statistics stat = new Statistics();
            double [][] phiMatrix = stat.calculatePhi(dataset);
                
            for (LabelsPair pair : pairs) {
                chi = pair.getScore();
                phi = phiMatrix[pair.getPair()[0]][pair.getPair()[1]];
                coefficients[pair.getPair()[0]][pair.getPair()[1]] = chi;
                coefficients[pair.getPair()[1]][pair.getPair()[0]] = phi;
            }   
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return coefficients;  
    }
    
    
    public static ArrayList<String> getVertices(String labelName, ArrayList<AttributesPair> list)
    {
        ArrayList<String> result = new ArrayList<>();    
        
        for(AttributesPair actual : list)
        {
            if(actual.getAttributeName1().equals(labelName)) 
            {
                result.add(actual.getAttributeName2());
            }
            else if(actual.getAttributeName2().equals(labelName))
            {
                result.add(actual.getAttributeName1());
            }
        }        
        
        return result;     
    }
    
    
    public static int getBorderStrength (int min, int max, int n, double edgeValue)
    {
        double interval = (max-min)/(n*1.0);
        
        int strength = 0;
        
        for(double i=min; i<max ;i=i+interval)
        {
            if(edgeValue < i) break;
            {
                strength++;
            }
        }
        return strength;
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
