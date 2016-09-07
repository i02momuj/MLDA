package mldc.labelsDistribution;

import mldc.base.MLDataMetric;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;

/**
* Class implementing the Density
*
* @author Jose Maria Moyano Murillo
*/
public class Density extends MLDataMetric{

	/**
	 * Constructor
	 */
	public Density() {
		super("Density");
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
		
		this.value = stat.density();
		return value;
	}

}
