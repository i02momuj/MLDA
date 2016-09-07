package mldc.dimensionality;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the Labels metric
*
* @author Jose Maria Moyano Murillo
*/
public class Labels extends MLDataMetric {

	public Labels() {
		super("Labels");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		this.value = mlData.getNumLabels();
		return value;
	}

}
