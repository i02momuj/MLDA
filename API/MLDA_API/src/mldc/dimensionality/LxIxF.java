package mldc.dimensionality;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the LxIxF metric
*
* @author Jose Maria Moyano Murillo
*/
public class LxIxF extends MLDataMetric {

	public LxIxF() {
		super("LxIxF");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		this.value = mlData.getNumLabels() * mlData.getFeatureIndices().length * mlData.getNumInstances();
		return value;
	}

}
