package mldc.labelsRelation;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;

/**
* Class implementing the Bound
*
* @author Jose Maria Moyano Murillo
*/
public class Bound extends MLDataMetric{

	/**
	 * Constructor
	 */
	public Bound() {
		super("Bound");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){	
		this.value = Math.pow(2, mlData.getNumLabels());
		return value;
	}

}
