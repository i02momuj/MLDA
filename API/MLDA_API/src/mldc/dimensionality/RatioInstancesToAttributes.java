package mldc.dimensionality;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the Ratio of number of instances to the number of attributes
*
* @author Jose Maria Moyano Murillo
*/
public class RatioInstancesToAttributes extends MLDataMetric {

	public RatioInstancesToAttributes() {
		super("Ratio of number of instances to the number of attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		this.value = ((double)mlData.getNumInstances()) / mlData.getFeatureIndices().length;
		return value;
	}

}
