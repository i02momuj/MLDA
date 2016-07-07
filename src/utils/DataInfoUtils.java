package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import mulan.data.LabelSet;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import weka.core.Attribute;
import weka.core.Instances;

/**
 *
 * @author Jose Maria Moyano Murillo
 */
public class DataInfoUtils {
    
    public static ArrayList<String> getLabelNamesByLabelset(MultiLabelInstances dataset, String labelset)
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
    
    
    public static double[] getLabelAppearances(ImbalancedFeature[] imbalancedData)
    {
        double[] labelFrequency = new double[imbalancedData.length];
        
        for(int i=0;i<imbalancedData.length; i++)
        {
            labelFrequency[i]=(double)imbalancedData[i].getAppearances();
        }
        
        return labelFrequency;
    }
    
    
    //KEY: num of labels, VALUE: appearances of the labelset
    public static HashMap<Integer,Integer> getLabelsetByValues(Statistics stat)
    {
        HashMap<LabelSet,Integer> result = stat.labelCombCount();
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
    
    
    public static double getLabelFrequency(MultiLabelInstances dataset, int labelIndex)
    {
        double value = 0.0;
        
        Instances instances = dataset.getDataSet();
        
        double  isLabel;
        
        for(int i=0; i<instances.size();i++)
        {
            isLabel=instances.instance(i).value(labelIndex);                
            if(isLabel==1.0) {
                value++;
            }
        }         
                
        return value/dataset.getNumInstances();
    }
    
    
    public static boolean existsAttribute (ArrayList<String> visited, ImbalancedFeature attribute)
    {
        for(String current : visited)
        {
            if(current.equals(attribute.getName())) return true;
        }
         
        return false;
    }
    
    
}
