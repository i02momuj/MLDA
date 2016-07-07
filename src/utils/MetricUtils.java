package utils;

import java.util.ArrayList;
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
    
    
}
