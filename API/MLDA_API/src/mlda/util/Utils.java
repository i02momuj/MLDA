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

package mlda.util;

import java.util.ArrayList;

import mulan.data.MultiLabelInstances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
* Class implementing different utils for metrics calculation
*
* @author Jose Maria Moyano Murillo
*/
public class Utils {
	
	/**
	 * Entropy of array values
	 * 
	 * @param array Array with values to calculate entropy
	 * @return Entropy value
	 */
	public static double entropy(int [] array){
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
	
	private static double lnFunc(double num){
        if(num < 1e-6){
            return 0;
        }
        else{
            return(num*Math.log(num));
        }
    }
	

	/**
	 * Get number of labels associated with each instance
	 * 
	 * @param mlData Multi-label dataset
	 * @return Array with the number of labels associated with each instance
	 */
	public static int[] labelsForInstance(MultiLabelInstances mlData){

        int nInstances = mlData.getNumInstances();
        int nLabels = mlData.getNumLabels();
        
        int [] labelsForInstance = new int[nInstances];
        
        int[] labelIndices = mlData.getLabelIndices();
        
        Instances instances = mlData.getDataSet();
        
        Instance inst;
        for(int i=0; i<nInstances; i++){
        	inst = instances.get(i);
   
            for(int j=0; j<nLabels; j++){
                if(inst.value(labelIndices[j]) == 1){
                    labelsForInstance[i]++;
                }
            }
        }
        
        return(labelsForInstance);
    }
	
		
	/**
	 * Get array of ImbalancedFeature with labels frequency
	 * 
	 * @param dataset Multi-label dataset
	 * @return Array of ImbalancedFeature with the labels frequency
	 */
	public static ImbalancedFeature[] getAppearancesPerLabel( MultiLabelInstances dataset)
    {
		int[] labelIndices = dataset.getLabelIndices();
        
        ImbalancedFeature[] labels = new ImbalancedFeature[labelIndices.length];
         
        Instances instances = dataset.getDataSet();
         
        int appearances = 0;
        Attribute currentAtt;
         
        for(int i=0; i<labelIndices.length;i++)
        {
        	currentAtt = instances.attribute(labelIndices[i]);
        	appearances=0;
             
            for(int j=0; j<instances.size();j++)
            {
                if(instances.instance(j).value(currentAtt) == 1.0){
                	appearances++;
                }
            }
            labels[i] = new ImbalancedFeature(currentAtt.name(), appearances);
        }
         
        return labels;
    }
	
	/**
	 * Get array of ImbalancedFeature in desdendent order of frequency
	 * 
	 * @param labels Labels of the dataset as ImbalancedFeature objects
	 * @return Array of ImbalancedFeature in desdendent order of frequency
	 */
	public static ImbalancedFeature[] getSortedByFrequency (ImbalancedFeature[] labels)
    {
        ArrayList<ImbalancedFeature> listIF = new ArrayList<ImbalancedFeature>();
        
        for(int i=0; i<labels.length; i++) {
        	listIF.add(labels[i]);
        }
        
        ImbalancedFeature[] sorted = new ImbalancedFeature [labels.length];
        
        for(int i=0 ; i<labels.length; i++)
        {
            sorted[i]= getMaxAppearance(listIF);
            listIF.remove(sorted[i]);
        }
        
        return sorted;
	}
	
	
	/**
	 * Calculate IRs of the ImbalancedFeatures
	 * 
	 * @param dataset Multi-label dataset
	 * @param labels Labels of the dataset as ImbalancedFeature objects
	 * @return Array of ImbalancedFeature objects with calculated IR
	 */
	public static ImbalancedFeature[] getImbalancedWithIR (MultiLabelInstances dataset, ImbalancedFeature[] labels)
    {
		int[] labelIndices = dataset.getLabelIndices();
        
        ImbalancedFeature[] labels_imbalanced = new ImbalancedFeature[labelIndices.length];
         
        Instances instances = dataset.getDataSet();
         
        int nOnes=0, nZeros=0, maxAppearance=0;
        double IRIntraClass;
        double variance;
        double IRInterClass;         
        double mean = dataset.getNumInstances()/2;
         
        Attribute current;
        ImbalancedFeature currentLabel;
         
        for(int i=0; i<labelIndices.length;i++) //for each label
        {
        	nZeros=0;
            nOnes=0;
        	current = instances.attribute(labelIndices[i]); //current label
           
            for(int j=0; j<instances.size();j++) //for each instance
            {
                if(instances.instance(j).value(current) == 1.0){
                	nOnes++;
                }
                else{
                	nZeros++;
                }
            }
             
            try { 
            	if(nZeros ==0 || nOnes ==0){
            		IRIntraClass = 0;
            	}
                else if(nZeros > nOnes){
                	IRIntraClass = (double)nZeros/nOnes;
                }
                else{
                	IRIntraClass = (double)nOnes/nZeros;
                }
            }           
            catch(Exception e1)
            {
            	IRIntraClass = 0;            
            }
                    
            variance = (Math.pow((nZeros-mean), 2) + Math.pow((nOnes-mean), 2)) / 2;
             
            currentLabel = getLabelByName(current.name(), labels);
             
            maxAppearance = labels[0].getAppearances();
             
            if(currentLabel.getAppearances() <= 0){
        		IRInterClass = Double.NaN;
            }
            else{
        		IRInterClass = (double)maxAppearance/currentLabel.getAppearances();
            }
               
            labels_imbalanced[i] = new ImbalancedFeature(current.name(), currentLabel.getAppearances(), IRInterClass, IRIntraClass, variance);
        }
         
        return labels_imbalanced;
    }
	
	/**
	 * Get an ImbalancedFeature with the label given by name
	 * 
	 * @param labelname Name of the label
	 * @param list Array of ImbalancedFeature
	 * @return ImbalancedFeature according to the label name given
	 */
	public static ImbalancedFeature getLabelByName(String labelname , ImbalancedFeature[] list)
	{
		for(int i=0;i<list.length; i++)
	    {
			if(labelname.equals(list[i].getName())) return list[i];
	    }
	    
		return null;
	}
	
	/**
	 * Get the max appearance in the list
	 * 
	 * @param list List of ImbalancedFeature
	 * @return Max value of appearance in the list
	 */
	public static ImbalancedFeature getMaxAppearance(ArrayList<ImbalancedFeature> list)
    {
        ImbalancedFeature max = list.get(0);
               
        for(ImbalancedFeature current : list){
        	if(current.getAppearances() > max.getAppearances()){
        		max = current;
        	}
        }
        
        return max;       
    }

}
