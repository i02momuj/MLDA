package mldc.dimensionality;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Distinct labelsets metric
*
* @author Jose Maria Moyano Murillo
*/
public class DistinctLabelsets extends MLDataMetric {

	public DistinctLabelsets() {
		super("Distinct labelsets");
	}
	
	/**
	 * Calculate metric value
	 * 
	 * @param mlData Multi-label dataset to which calculate the metric
	 * @return Value of the metric
	 */
	public double calculate(MultiLabelInstances mlData){
		Statistics stat = new Statistics();
		stat.calculateStats(mlData);
		
		this.value = stat.labelCombCount().size();
		return value;
	}

}
