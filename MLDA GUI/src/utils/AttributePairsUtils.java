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
import mulan.data.MultiLabelInstances;
import static utils.Utils.getPossibleCombinations;
import weka.core.Instances;

/**
 *
 * @author Jose Maria Moyano Murillo
 */
public class AttributePairsUtils {
    
    /**
     * Generates attribute pairs
     * 
     * @param labelPairs Label pairs
     * @param labelIndices Label indices
     * @param dataset Multi-label dataset
     * @return List of pairs of attributes
     */
    private static ArrayList<AttributesPair> makeAttributePairs(int [] 
            labelPairs, int[] labelIndices, MultiLabelInstances dataset)
    {
        ArrayList<AttributesPair> list = new ArrayList<>();
        Instances instances = dataset.getDataSet();

        String[] labelNames = DataInfoUtils.getLabelNames(labelIndices, instances);
        AttributesPair current;

        ImbalancedFeature[] imbalancedData = MetricUtils.getImbalancedDataByAppearances(dataset);
        int app_i, app_j;

        int labelPairsIndex = 0;
        int value;

        for(int i = 0; i<labelIndices.length; i++)
        {
            for(int j=i+1; j<labelIndices.length; j++)
            {
                value = labelPairs[labelPairsIndex];

                labelPairsIndex++; 

                if(value ==0) {
                    continue;
                }

                app_i = DataInfoUtils.getLabelAppearancesByName(imbalancedData, labelNames[i]);
                app_j = DataInfoUtils.getLabelAppearancesByName(imbalancedData, labelNames[j]);

                current = new AttributesPair(labelNames[i], labelNames[j],value,i,j,app_i,app_j);
                list.add(current);   
            }
        }
        
        return list;
    }
    
    /**
     * Get index of a label pair
     * 
     * @param index1 Index label 1
     * @param index2 Index label 2
     * @param labels Number of labels
     * @return Label pair index
     */
    public static int getLabelPairsIndex(int index1, int index2, int labels)
    {
        int count = index1;
        int result = 0;
        
        for(int i=1; count>0; i++, count--)
        {
            result += labels-i;
        }
        int distance = index2 - index1;
        
        return result += distance-1;
    }
    
    /**
     * Update attribute pairs
     * 
     * @param labelPairs Pair
     * @param labelValues Values
     * @return New pairs
     */
    public static int[] updateAttributePairs(int[] labelPairs, int[] 
            labelValues)
    {
        int index;
        
        for(int i=0; i<labelValues.length;i++)
        {
            if(labelValues[i]== 0) continue;
            
            for(int j=i+1; j<labelValues.length; j++)
            {
               if(labelValues[i] == labelValues[j])
               {
                   index = getLabelPairsIndex(i, j, labelValues.length);
                   labelPairs[index] = labelPairs[index]+1;
               }
            }
        }
        
        return labelPairs;
    }
    
    /**
     * Search a pair of attributes in the list
     * @param att1 Attribute 1
     * @param att2 Attribute 2
     * @param list List of pairs
     * @return Pair of attributes or null if it does not exist
     */
    public static AttributesPair searchAndGet(String att1, String att2, 
            ArrayList<AttributesPair> list)
    {
        for(AttributesPair current : list)
        {
            if(att1.equals(current.getAttributeName1()) && att2.equals(current.getAttributeName2())) {
                return current;
            }
            else if (att2.equals(current.getAttributeName1()) && att1.equals(current.getAttributeName2())) {
                return current;
            }
        }

        return null;
    }
   
    /**
     * Get a pair
     * 
     * @param att1 Attribute 1
     * @param att2 Attribute 2
     * @param list List of pairs
     * @return Pair of attributes
     */
    private static AttributesPair getPair(String att1, String att2, 
            ArrayList<AttributesPair> list)
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
    
    /**
     * Find pairs of selected attributes
     * 
     * @param pair Pair
     * @param labels Labels
     * @return List of pairs
     */
    public static ArrayList<AttributesPair> findSelectedAttributesPair (
            ArrayList<AttributesPair> pair , ArrayList<String> labels)
    {
       ArrayList<AttributesPair> result = new ArrayList();
       
       AttributesPair current;
       
       for(int i=0; i<labels.size()-1; i++)
       {
           for(int j=i+1; j<labels.size(); j++)
           {
               current = getPair(labels.get(i), labels.get(j), pair);
               if(current!=null){
                   result.add(current);
               }
           }
       }    
       
       return result;
    }
    
    /**
     * Get pairs of attributes
     * 
     * @param dataset Dataset
     * @return List of pairs
     */
    public static ArrayList<AttributesPair> getAttributePairs(
            MultiLabelInstances dataset)
    {       
        Instances instances = dataset.getDataSet();
       
        //Return possible combinations among labels
        int possibleCombinations = getPossibleCombinations(dataset.getNumLabels());
        
        int [] labelPairAppearances = new int[possibleCombinations];
        int [] currentLabelValues;
        int[] labelIndices = dataset.getLabelIndices();
                
        for(int i=0; i<instances.size(); i++)
        {
            currentLabelValues = DataInfoUtils.getCurrentValueLabels(instances, i, labelIndices);
            labelPairAppearances = updateAttributePairs(labelPairAppearances, currentLabelValues);
        }            
        
        return makeAttributePairs(labelPairAppearances, labelIndices, dataset);
    }
}
