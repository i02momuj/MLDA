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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import mulan.data.LabelSet;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * This class implements some utils for data information
 * 
 * @author Jose Maria Moyano Murillo
 */
public class DataInfoUtils {
    
    /**
     * Obtain label names from a labelset
     * 
     * @param dataset Dataset
     * @param labelset Labelset
     * @return List with label names
     */
    public static ArrayList<String> getLabelNamesByLabelset(MultiLabelInstances
            dataset, String labelset)
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
    
    /**
     * Obtain label by index
     * 
     * @param dataset Dataset
     * @param id Label id
     * @return Label
     */
    public static Attribute getLabelByIndex(MultiLabelInstances dataset, int id)
    {
        int[] labelIndices = dataset.getLabelIndices();
        
        Attribute result = dataset.getDataSet().instance(1).attribute(labelIndices[id]);
        
        return result;
    }
    
    /**
     * Obtain number of appearances of a label
     * 
     * @param imbalancedData Labels as ImbalancedFeature object
     * @return Array with appearances of each label
     */
    public static double[] getLabelAppearances(ImbalancedFeature[] 
            imbalancedData)
    {
        double[] labelFrequency = new double[imbalancedData.length];
        
        for(int i=0; i<imbalancedData.length; i++)
        {
            labelFrequency[i]=(double)imbalancedData[i].getAppearances();
        }
        
        return labelFrequency;
    }
    
    /**
     * Get labelset with appearances
     * 
     * @param stat Statistics
     * @return Combinations of labelset-appearances
     */
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
    
    /**
     * Get label frequency given the index
     * 
     * @param dataset Dataset
     * @param labelIndex Label index
     * @return 
     */
    public static double getLabelFrequency(MultiLabelInstances dataset, 
            int labelIndex)
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
    
    /**
     * Check if the attribute exists
     * 
     * @param visited Attribute names
     * @param attribute Attribute
     * @return True if it exists and false otherwise
     */
    public static boolean existsAttribute (ArrayList<String> visited, 
            ImbalancedFeature attribute)
    {
        for(String current : visited)
        {
            if(current.equals(attribute.getName())) {
                return true;
            }
        }
         
        return false;
    }
    
    /**
     * Obtain label by label name
     * 
     * @param labelName Label name
     * @param list List of labels
     * @return Label
     */
    public static ImbalancedFeature getLabelByLabelname(String labelName, 
            ImbalancedFeature[] list)
    {
        for(int i=0;i<list.length; i++)
        {
            if(labelName.equals(list[i].getName())) {
                return list[i];
            }
        }
        return null;
    }
    
    /**
     * Obtain label index
     * 
     * @param labels Labels
     * @param labelName Label name
     * @return Label index
     */
    public static int getLabelIndex (String[] labels, String labelName)
    {
        for(int i=0; i<labels.length;i++)
        {
            if(labelName.equals(labels[i])){
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Obtain label names
     * 
     * @param labelIndices Label indices
     * @param instances Dataset
     * @return Label names
     */
    public static String[] getLabelNames(int[] labelIndices, 
            Instances instances)
    {
        String[] labelName = new String[labelIndices.length];
                
        for(int i=0;i<labelIndices.length;i++)
        {
            labelName[i] = instances.attribute(labelIndices[i]).name();
        }
        
        return labelName;
    }
    
    /**
     * Obtain label appearances by name
     * 
     * @param imbalancedData Labels
     * @param labelName Label name
     * @return Appearances
     */
    public static int getLabelAppearancesByName(ImbalancedFeature[] 
            imbalancedData, String labelName)
    {
        for(int i=0 ; i<imbalancedData.length; i++)
        {
            if(imbalancedData[i].getName().equals(labelName)) {
                return imbalancedData[i].getAppearances();
            }
        }
        
        return -1;
    }
    
    /**
     * Obtain current label values
     * 
     * @param instances Dataset
     * @param position Position
     * @param labelIndices Label indices
     * @return Array of values
     */
    public static int[] getCurrentValueLabels(Instances instances, int position, 
            int[] labelIndices)
    {
        int[] labelsValue = new int[labelIndices.length];
        int value;
        
        for(int i=0; i<labelsValue.length; i++)
        {
           value = (int)instances.instance(position).value(labelIndices[i]);
           labelsValue[i] = value;
        }
        
        return labelsValue;
    }
}
