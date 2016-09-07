package mldc.dimensionality;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the Instances metric
*
* @author Jose Maria Moyano Murillo
*/
public class Instances extends MLDataMetric {

	public Instances() {
		super("Instances");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		this.value = mlData.getNumInstances();
		return value;
	}

}
