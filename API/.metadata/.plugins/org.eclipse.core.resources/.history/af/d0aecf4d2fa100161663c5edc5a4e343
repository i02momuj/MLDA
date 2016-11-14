package mldc.dimensionality;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the Attributes metric
*
* @author Jose Maria Moyano Murillo
*/
public class Attributes extends MLDataMetric{

	public Attributes() {
		super("Attributes");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		this.value = mlData.getFeatureIndices().length;
		return value;
	}

}
