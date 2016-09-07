package mldc.attributes;

import java.util.Arrays;
import java.util.Set;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Utils;

/**
* Class implementing the Proportion of numeric attributes with outliers
*
* @author Jose Maria Moyano Murillo
*/
public class ProportionNumericAttributesWithOutliers extends MLDataMetric{

	/**
	 * Constructor
	 */
	public ProportionNumericAttributesWithOutliers() {
		super("Proportion of numeric attributes with outliers");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		Instances instances = mlData.getDataSet();
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
        	this.value = ((double) nOutliers) / nNumeric;
        }
        else{
        	this.value = Double.NaN;
        }
		
		return value;
	}

}
