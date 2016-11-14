package mldc.attributes;

import mldc.base.MLDataMetric;
import mldc.util.Utils;
import mulan.data.MultiLabelInstances;
import weka.core.AttributeStats;
import weka.core.Instances;

/**
* Class implementing the Mean of entropies of nominal attributes
*
* @author Jose Maria Moyano Murillo
*/
public class MeanEntropiesNominalAttributes extends MLDataMetric{

	/**
	 * Constructor
	 */
	public MeanEntropiesNominalAttributes() {
		super("Mean of entropies of nominal attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		double mean = 0.0;
        
		Instances instances = mlData.getDataSet();
        
        int countNominal = 0;
        int [] featureIndices = mlData.getFeatureIndices();
        
        for(int fIndex : featureIndices){
            AttributeStats attStats = instances.attributeStats(fIndex);
            if(attStats.nominalCounts != null){
                countNominal++;
                mean += Utils.entropy(attStats.nominalCounts);
            }
        }
        
        mean = mean/countNominal;
		
		this.value = mean;
		return value;
	}

}
